package edu.lastcow.hids.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import edu.lastcow.hids.db.HidsDbHelper;
import edu.lastcow.hids.task.SystemCallMonitor;
import edu.lastcow.hids.task.SystemCallSendToServer;
import edu.lastcow.hids.util.CommonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lastcow
 * Date: 3/2/13
 * Time: 1:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class SystemCallReceiver extends BroadcastReceiver {

    Map<String, Object> params = null;
    SystemCallMonitor systemCallMonitor = null;
    SystemCallSendToServer systemCallSendToServer = null;

    @Override
    public void onReceive(Context context, Intent intent) {

        // Gather logs.
        if(intent.getAction().equals(CommonUtil.SYSTEM_CALL_TASK_ACTION)){

            // Get the white list name
            HidsDbHelper dbHelper = new HidsDbHelper(context);
            SQLiteDatabase database = dbHelper.getReadableDatabase();

            // Insert test data.
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("whitename", "com.tt.yy");
//            database.insert("white_list", null, contentValues);

            List<String> whiteNameList = new ArrayList<String>();

            Cursor cursor = database.query("white_list", new String[]{"_id", "whitename"}, null, null, null, null, null);
            cursor.moveToFirst();

            while(!cursor.isAfterLast()){
                whiteNameList.add(cursor.getString(1));
                cursor.moveToNext();
            }

            cursor.close();
            database.close();



            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(100);
//            for(ActivityManager.RunningTaskInfo taskInfo : runningTasks){
//                Toast.makeText(context, taskInfo.baseActivity.getPackageName(), Toast.LENGTH_LONG).show();
//
//                Log.v("Task name: ", taskInfo.baseActivity.getPackageName());
//            }

            List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
            for(ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningApps){

                // Check for the white list.
                if(whiteNameList.contains(runningAppProcessInfo.processName)){
                    // Just skip.
                    continue;
                }
                params = new HashMap<String, Object>();
                params.put("pid", String.valueOf(runningAppProcessInfo.pid));
                params.put("pname", runningAppProcessInfo.processName);
                params.put("context", context);
                params.put("intent", intent);
//
//                systemCallMonitor = new SystemCallMonitor();
//                systemCallMonitor.execute(params);
                Log.v("Task name", runningAppProcessInfo.pid+"");
                Log.v("Task name", runningAppProcessInfo.processName);
            }
        }

        // Send to server
        if(intent.getAction().equals(CommonUtil.SYSTEM_CALL_SEND_TO_SERVER_ACTION)){
            // Sending file.
            //TODO send file to HIDS Server
            File file = new File(CommonUtil.FILE_LOCATION);
            File[] files = file.listFiles();
            for(File logFile : files){
                if(logFile.isFile() && logFile.canRead() && logFile.getName().endsWith("txt")){
                    // Send file
                    params = new HashMap<String, Object>();
                    params.put("fileName", logFile.getName());

                    systemCallSendToServer = new SystemCallSendToServer();
                    systemCallMonitor.execute(params);
                }
            }
        }

    }

}
