package edu.lastcow.hids.task;

import android.content.Context;
import android.os.AsyncTask;
import edu.lastcow.hids.data.HidsResteasyClient;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lastcow
 * Date: 3/21/13
 * Time: 9:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class SendHidsDataTask extends AsyncTask<Map<String, Object>, Void , Void> {
    @Override
    protected Void doInBackground(Map<String, Object>... objects) {

        // First object as context.
        // 2nd object is data.
        HidsResteasyClient client = new HidsResteasyClient((Context) objects[0].get("context"));
        client.testRest("aa", "bb");

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
