package edu.lastcow.hids;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.android.gcm.GCMBaseIntentService;
import edu.lastcow.hids.util.CommonUtil;
import edu.lastcow.hids.util.ServerUtilities;

/**
 * Created with IntelliJ IDEA.
 * User: lastcow
 * Date: 3/1/13
 * Time: 6:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class GCMIntentService extends GCMBaseIntentService {

    /**
     * Constructor with Send ID
     */
    public GCMIntentService(){
        super(CommonUtil.SENDER_ID);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {

        Intent requestIntent = null;

        // Start server based on message.
        String message = intent.getStringExtra("action");

        Log.v("GCM Service", "Action message received: " + message);
        if(message.equals(CommonUtil.MSG_GET_INSTALLED_APPS)){
            // Get all installed applications
            requestIntent = new Intent(CommonUtil.SERVER_GET_ALL_APPS_INSTALLED);
        }
        else if(message.equals(CommonUtil.MSG_GET_RUNNING_APPS)){
            // Get all running applications
            requestIntent = new Intent(CommonUtil.SERVER_GET_ALL_APPS_RUNNING);
        }
        else if(message.equals(CommonUtil.MSG_MONITOR_RUNNING_APPS)){
            // Monitor all running apps
            requestIntent = new Intent(CommonUtil.SERVER_GET_APPS_SYSTEMCALL);
            requestIntent.putExtra("processName", intent.getStringExtra("processName"));
        }
        else if(message.equals(CommonUtil.MSG_GET_LOGFILES)){
            // Get all logfiles and send it back
            requestIntent = new Intent(CommonUtil.SERVER_GET_APPS_LOGFILES);
        }
        else if(message.equals(CommonUtil.MSG_GET_SYSTEM_INFO)){
            // Get all system information.
            requestIntent = new Intent(CommonUtil.SERVER_GET_DEVICE_STATUS);
        }
        else{
            // Do nothing.
        }

        if(requestIntent != null){
            // Broadcast
            this.sendBroadcast(requestIntent);
        }
    }

    @Override
    protected void onError(Context context, String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void onRegistered(Context context, String s) {
        // Save to shared preference.
        SharedPreferences sharedPreferences = getSharedPreferences(CommonUtil.PREFERENCE_FILENAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("regid", s).commit();

        // Send to server
        ServerUtilities.register(context, s);

//        Map<String, Object> params = new HashMap<String, Object>();
//        List<String> apps = new ArrayList<String>();
//        apps.add("google.andriod.system");
//
//        params.put("apps", apps);
//
//        try {
//             ServerUtilities.installedApps(context, params);
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
    }

    @Override
    protected void onUnregistered(Context context, String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
