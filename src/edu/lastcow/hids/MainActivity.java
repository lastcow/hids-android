package edu.lastcow.hids;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import com.google.android.gcm.GCMRegistrar;
import edu.lastcow.hids.db.model.Device;
import edu.lastcow.hids.receiver.SystemCallReceiver;
import edu.lastcow.hids.task.SendHidsDataTask;
import edu.lastcow.hids.util.CommonUtil;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    private BroadcastReceiver systemCallReceiver;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        systemCallReceiver = new SystemCallReceiver();

        // Google Cloud Messaging Service
        // Check device for GCM
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
            // If not
            GCMRegistrar.register(this, CommonUtil.SENDER_ID);
        }

        // Register on system call task action
//        registerReceiver(systemCallReceiver, new IntentFilter(CommonUtil.SYSTEM_CALL_TASK_ACTION));
    }

    @Override
    protected void onStart() {
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
//        unregisterReceiver(systemCallReceiver);

    }

    public void doSystemMonitor(View view){

        // Request super user permission
        // Request superuser
        // arbitrary number of your choosing
//        final int SUPERUSER_REQUEST = 2323;
//        Intent intent = new Intent("android.intent.action.superuser");
//        intent.putExtra("name", "Shell");
//        intent.putExtra("packagename", "edu.lastcow.hids");
//        this.startActivityForResult(intent, SUPERUSER_REQUEST);

        Intent requestIntent = new Intent(CommonUtil.SYSTEM_CALL_TASK_ACTION);
        this.sendBroadcast(requestIntent);
    }

    /**
     * Send to server action.
     * @param view
     */
    public void doSendToServer(View view){
        Intent requestIntent = new Intent(CommonUtil.SYSTEM_CALL_SEND_TO_SERVER_ACTION);
        this.sendBroadcast(requestIntent);
    }

    public void doTest(View view){
        Map<String, Object> param = new HashMap<String, Object>();
        Device device = new Device();
        device.setDeviceSerial(Build.SERIAL);
        param.put("context", this);
        param.put("data", device);

        SendHidsDataTask sendHidsDataTask = new SendHidsDataTask();
        sendHidsDataTask.execute(param);
    }


}
