package edu.lastcow.hids.task;

import android.os.AsyncTask;
import edu.lastcow.hids.util.ServerUtilities;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lastcow
 * Date: 3/21/13
 * Time: 4:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class SendDataTask extends AsyncTask<Map<String, Object>, Void , Void> {


    @Override
    protected Void doInBackground(Map<String, Object>... maps) {
        Map<String, Object> params = maps[0];
        if("deviceinfo".equals(params.get("postAction").toString())){
            ServerUtilities.deviceStatus(params);
        }
        else if("deviceinstalledapps".equals(params.get("postAction").toString())){
            ServerUtilities.installedApps(params);
        }
        else if("devicerunningapps".equals(params.get("postAction").toString())){
            ServerUtilities.runningApps(params);
        }

        return null;
    }
}
