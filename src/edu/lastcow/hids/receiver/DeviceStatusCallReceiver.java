package edu.lastcow.hids.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import edu.lastcow.hids.task.SendDataTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lastcow
 * Date: 3/19/13
 * Time: 11:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeviceStatusCallReceiver extends BroadcastReceiver {


    public void onReceive(Context context, Intent intent) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("postAction", "deviceinfo");

        params.put("manufacturer", Build.MANUFACTURER);
        params.put("device", Build.DEVICE);
        params.put("hardware", Build.HARDWARE);
        params.put("serial", Build.SERIAL);

        params.put("id", Build.ID);
        params.put("model", Build.MODEL);
        params.put("codename", Build.VERSION.CODENAME);

        // Get internal storage
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        long totalinternalsize = (long)stat.getBlockCount() * (long)stat.getBlockSize();
        long totalinternalsizegb = totalinternalsize / 1048576;
        long totalinternalfreesize = (long)stat.getAvailableBlocks() * (long)stat.getBlockSize();
        long totalinternalfreesizegb = totalinternalfreesize / 1048576;
        params.put("totalinternalsize", totalinternalsizegb/ 1024);
        params.put("freeinternalsize", totalinternalfreesizegb/ 1024);


        // Send to server.

        SendDataTask sendDataTask = new SendDataTask();
        sendDataTask.execute(params);
    }
}
