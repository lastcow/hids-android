package edu.lastcow.hids.receiver;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import edu.lastcow.hids.db.HidsDbHelper;
import edu.lastcow.hids.task.SystemCallMonitor;
import edu.lastcow.hids.util.CommonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lastcow
 * Date: 3/19/13
 * Time: 11:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppsSystemCallReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {

        SystemCallMonitor systemCallMonitor = null;
        String pid = null;

        HidsDbHelper dbHelper = new HidsDbHelper(context);

        Log.i(this.getClass().getName(), "Start scanning application: " + intent.getStringExtra("processName"));

        String processName = intent.getStringExtra("processName");
        boolean isRunning = false;

        // Check if application is running.
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo processInfo : procInfos){
            if(processInfo.processName.equals(processName)) {
                isRunning = true;
                pid = String.valueOf( processInfo.pid );
                break;
            }
        }

        // Running, scan.
        if(isRunning){
            // Scan
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("pid", pid);
            params.put("pname", processName);
            params.put("context", context);
            params.put("intent", intent);
//
            // Start scan
            systemCallMonitor = new SystemCallMonitor();
            systemCallMonitor.execute(params);

            // Update status to running.
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
//            sqLiteDatabase.delete("app", "actionType = 'scan' and appName = ?", new String[]{processName});
            ContentValues updateValues = new ContentValues();
            updateValues.put("status", CommonUtil.STATUS_SCANNING);

            sqLiteDatabase.update("app", updateValues, "actionType = 'scan' and appName = ?", new String[]{processName});

            // Close db connection
            sqLiteDatabase.close();

            // Cancel alarm
            cancelAlarm(context);

            // Start alarm for sending file
            // Set alarm.
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent fileSendIntent = new Intent(context, FileSendAlarmReceiver.class);
            fileSendIntent.putExtra("processName", processName);
            PendingIntent fileSendPendingIntent = PendingIntent.getBroadcast(context, 0, fileSendIntent, 0);

            // Repeat every 1 hour.
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000*60*1, fileSendPendingIntent);

        }
        // Not running, save to db for furture scan, setup alarm every hour (default) for 24 hours.
        else{

            // Init db
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

            // if Alarm is ran for certain time. (24 hours). Cancel it and delete db record.

            // Check whether this request already in DB?
            Cursor cursor = sqLiteDatabase.query("app", new String[]{"id", "appName", "actionType", "count"}, "actionType = 'scan' AND appName = '"+processName +"'", null, null, null, null);

            if(cursor.getCount() > 0 ){

                // if Alarm is ran for certain time. (24 hours). Cancel it and delete db record.
                cursor.moveToFirst();
                int count = cursor.getInt(3);
                if(count == 24){
                    // Cancel alarm service.
                    cancelAlarm(context);

                    // Close cursor
                    cursor.close();

                    // Delete from db.
                    sqLiteDatabase.delete("app", "appName = ?", new String[]{processName});

                }else{
                    // count ++
                    ContentValues updateValues = new ContentValues();
                    updateValues.put("count", ++ count);

                    sqLiteDatabase.update("app", updateValues, "actionType = 'scan' and appName = ?", new String[]{processName});
                }

                // Close DB
                sqLiteDatabase.close();


            }else{
                // Insert to db.
                ContentValues values = new ContentValues();
                values.put("appName", processName);
                values.put("actionType", "scan");
                values.put("count", 0);
                values.put("status", CommonUtil.STATUS_SCAN_PENDING);

                sqLiteDatabase.insert("app", null, values);

                // Set alarm.
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(context, AppsSystemCallReceiver.class);
                alarmIntent.putExtra("processName", processName);
                PendingIntent pi = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

                // Repeat every 1 hour.
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000*60*1, pi);

            }
        }
    }


    /**
     * Cancel alarm
     * @param context
     */
    private void cancelAlarm(Context context){
        // Cancel alarm service.
        Intent cancelIntent = new Intent(context, AppsSystemCallReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, cancelIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
