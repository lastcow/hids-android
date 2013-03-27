package edu.lastcow.hids.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
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
public class AppsInstalledCallReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        List<String> params = new ArrayList<String>();

        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(
                PackageManager.GET_ACTIVITIES|
                        PackageManager.GET_CONFIGURATIONS|
                        PackageManager.GET_INTENT_FILTERS|
                        PackageManager.GET_PERMISSIONS|
                        PackageManager.GET_PROVIDERS|
                        PackageManager.GET_RECEIVERS|
                        PackageManager.GET_SERVICES|
                        PackageManager.GET_SHARED_LIBRARY_FILES|
                        PackageManager.GET_SIGNATURES|
                        PackageManager.GET_URI_PERMISSION_PATTERNS|
                        PackageManager.GET_UNINSTALLED_PACKAGES);

        String packageInfoHash = null;
        String name = null;

        PackageManager pm = context.getPackageManager();
        for(PackageInfo packageInfo : packages){


            packageInfoHash = packageInfo.packageName;
            packageInfoHash += "|"+packageInfo.versionName;
            packageInfoHash += "|"+packageInfo.applicationInfo.processName;
            packageInfoHash += "|"+pm.getApplicationLabel(packageInfo.applicationInfo).toString();


            params.add(packageInfoHash);
        }

        // Send to server.
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("postAction", "deviceinstalledapps");
        param.put("deviceserial", Build.SERIAL);
        param.put("apps", params);

        SendDataTask sendDataTask = new SendDataTask();
        sendDataTask.execute(param);
    }
}
