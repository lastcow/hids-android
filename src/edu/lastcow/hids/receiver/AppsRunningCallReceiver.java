package edu.lastcow.hids.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import edu.lastcow.hids.task.SendDataTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lastcow
 * Date: 3/19/13
 * Time: 11:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppsRunningCallReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        List<String> params = new ArrayList<String>();

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();

        PackageManager pm = context.getPackageManager();
        String packageInfoHash = null;
        String name = null;
        for(ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningApps){
            packageInfoHash = runningAppProcessInfo.processName;

            try {
                name = "|"+pm.getApplicationLabel(pm.getApplicationInfo(runningAppProcessInfo.processName, PackageManager.GET_META_DATA)).toString();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            packageInfoHash += name;

            params.add(packageInfoHash);
        }

        // Send to server.
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("postAction", "devicerunningapps");
        param.put("deviceserial", Build.SERIAL);
        param.put("apps", params);

        SendDataTask sendDataTask = new SendDataTask();
        sendDataTask.execute(param);
    }
}
