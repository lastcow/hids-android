package edu.lastcow.hids.task;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lastcow
 * Date: 3/1/13
 * Time: 6:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class SystemCallSendToServer extends AsyncTask<Map<String, Object>, Void, StringBuilder> {

    Process suProcess = null;
    OutputStream pidOutputStream = null;

    @Override
    protected StringBuilder doInBackground(Map<String, Object>... params) {
        final String fileName = params[0].get("fileName").toString();

        File file = new File(fileName);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://192.168.1.103:8080/hids/rest/deviceaction/logfileput");
        try{
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("logfile", new FileBody(file));
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(httpPost);

            Log.v("Response", "" + httpResponse.getStatusLine().getStatusCode());

        }catch (Exception e){
            e.printStackTrace();
        }

        // Return null
        return null;
    }

    @Override
    protected void onCancelled() {
        Log.v("SystemCallMonitor", "On cancel called");
    }

    @Override
    protected void onPostExecute(StringBuilder stringBuilder) {
        super.onPostExecute(stringBuilder);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
