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
import android.widget.Toast;
import edu.lastcow.hids.db.HidsDbHelper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lastcow
 * Date: 3/19/13
 * Time: 11:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppsSystemCallReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {

        Log.i(this.getClass().getName(), "Start scanning application: " + intent.getStringExtra("processName"));
        Toast.makeText(context, "Start scan application: " + intent.getStringExtra("processName"), Toast.LENGTH_LONG).show();

        String processName = intent.getStringExtra("processName");
        boolean isRunning = false;

        // Check if application is running.
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo processInfo : procInfos){
            if(processInfo.processName.equals(processName)) {
                isRunning = true;
                break;
            }
        }

        // Running, scan.
        if(isRunning){
            // Scan
        }
        // Not running, save to db for furture scan, setup alarm every hour (default) for 24 hours.
        else{

            // Init db
            HidsDbHelper dbHelper = new HidsDbHelper(context);
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
                    Intent cancelIntent = new Intent(context, AppsSystemCallReceiver.class);
                    PendingIntent sender = PendingIntent.getBroadcast(context, 0, cancelIntent, 0);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(sender);

                    // Close cursor
                    cursor.close();

                    // Delete from db.
                    sqLiteDatabase.delete("app", "appName = ?", new String[]{processName});

                }else{
                    // count ++
                    ContentValues updateValues = new ContentValues();
                    updateValues.put("count", ++ count);

                    sqLiteDatabase.update("app", updateValues, "appName = ?", new String[]{processName});
                }

                // Close DB
                sqLiteDatabase.close();


            }else{
                // Insert to db.
                ContentValues values = new ContentValues();
                values.put("appName", processName);
                values.put("actionType", "scan");
                values.put("count", 0);

                sqLiteDatabase.insert("app", null, values);

                // Set alarm.
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(context, AppsSystemCallReceiver.class);
                alarmIntent.putExtra("onetime", Boolean.FALSE);
                alarmIntent.putExtra("processName", processName);
                PendingIntent pi = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

                // Repeat every 1 hour.
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000*60*60, pi);

            }
        }
    }
}
