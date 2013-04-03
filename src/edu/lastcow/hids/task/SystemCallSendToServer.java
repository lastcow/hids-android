package edu.lastcow.hids.task;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import edu.lastcow.hids.db.HidsDbHelper;
import edu.lastcow.hids.receiver.FileSendAlarmReceiver;
import edu.lastcow.hids.util.CommonUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
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
public class SystemCallSendToServer extends AsyncTask<Map<String, Object>, Void, Boolean> {

    Process suProcess = null;
    OutputStream pidOutputStream = null;
    Context context = null;
    int alarmCode = 0;
    String processName = null;
    File file = null;

    @Override
    protected Boolean doInBackground(Map<String, Object>... params) {
        final String fileName = params[0].get("fileName").toString();
        context = (Context) params[0].get("context");
        alarmCode = Integer.valueOf(params[0].get("alarmCode").toString());
        processName = params[0].get("processName").toString();

        file = new File(fileName);
        SharedPreferences sharedPreferences = context.getSharedPreferences(CommonUtil.PREFERENCE_FILENAME, Context.MODE_PRIVATE);
        String processId = sharedPreferences.getString(processName, "-1");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String endPoint = CommonUtil.SERVER_URL_FILE_UPLOAD_PROTOCOL + "://" +
                CommonUtil.SERVER_IP + ":" + CommonUtil.SERVER_URL_FILE_UPLOAD_PORT + "/" +
                CommonUtil.SERVER_URL_FILE_UPLOAD;
        HttpPost httpPost = new HttpPost(endPoint);
        try{
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("logfile", new FileBody(file));
            entity.addPart("processId", new StringBody(processId));
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(httpPost);

            Log.v("Response", "" + httpResponse.getStatusLine().getStatusCode());

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        // Return null
        return true;
    }

    @Override
    protected void onCancelled() {
        Log.v("SystemCallMonitor", "On cancel called");
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if(result){
            // Cancel the alarm of sending file.
            Intent cancelIntent = new Intent(context, FileSendAlarmReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(context, alarmCode, cancelIntent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(sender);

            // And delete the log file.
//            file.delete();

            // And remove DB record
            HidsDbHelper dbHelper = new HidsDbHelper(context);
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            sqLiteDatabase.delete("app", "actionType = 'scan' and appName = ?", new String[]{processName});
            sqLiteDatabase.close();

            // Remove sharedpreference value
            SharedPreferences sharedPreferences = context.getSharedPreferences(CommonUtil.PREFERENCE_FILENAME, Context.MODE_PRIVATE);
            sharedPreferences.edit().remove(processName);
            sharedPreferences.edit().commit();

            // Log
            Log.i("HIDS Send File", "File successfully uploaded to server.");

        }else{
            // Do nothing.
        }
    }
}
