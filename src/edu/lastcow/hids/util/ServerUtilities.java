package edu.lastcow.hids.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import com.google.android.gcm.GCMRegistrar;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: lastcow
 * Date: 2/19/13
 * Time: 9:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerUtilities {

    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();


    /**
     * Register this account/device pair within the server.
     *
     */
    public static void register(final Context context, final String gcmRegId) {

        // Get server IP from sharedPreference.
        SharedPreferences preferences = context.getSharedPreferences(CommonUtil.PREFERENCE_FILENAME, Context.MODE_PRIVATE);
//        String serverIp = preferences.getString("edul.astcow.hids.serverIp", null);
        String serverIp = CommonUtil.SERVER_IP;

        //TODO  Check for null of serverIp
        if(serverIp == null){
            CommonUtil.displayToastMessage(context, "IP Address is null");
        }

        String serverUrl = CommonUtil.SERVER_URL_REGISTER_PROTOCOL + "://" +
                serverIp + ":" + CommonUtil.SERVER_URL_REGISTER_PORT + "/" +
                CommonUtil.SERVER_URL_REGISTER;


        Log.i(CommonUtil.TAG, "registering device (regId = " + gcmRegId + ")");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("gcmregid", gcmRegId);
        params.put("deviceserial", Build.SERIAL);
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        // Once GCM returns a registration id, we need to register it in the
        // demo server. As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(CommonUtil.TAG, "Attempt #" + i + " to register");
            try {
//                CommonUtilities.displayToastMessage(context, context.getString(
//                        R.string.server_registering, i, MAX_ATTEMPTS));
                post(serverUrl, params);
                // Registered on server, Default lifespan (7 days)
                GCMRegistrar.setRegisteredOnServer(context, true);
                return;
            } catch (Exception e) {
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                Log.e(CommonUtil.TAG, "Failed to register on attempt " + i + ":" + e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Log.d(CommonUtil.TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    Log.d(CommonUtil.TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }
        CommonUtil.displayToastMessage(context, "ERROR");
    }

    /**
     * Unregister this account/device pair within the server.
     */
    static void unregister(final Context context, final String regId) {
        Log.i(CommonUtil.TAG, "unregistering device (regId = " + regId + ")");
        String serverUrl = CommonUtil.SERVER_URL_REGISTER + "/unregister";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("deviceid", regId);
        try {
            post(serverUrl, params);
            GCMRegistrar.setRegisteredOnServer(context, false);
            CommonUtil.displayToastMessage(context, "UNREGISTERED");
        } catch (IOException e) {
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
            CommonUtil.displayToastMessage(context, e.getLocalizedMessage());
        }
    }

    public static void newPackageInstalled(final Context context, final Intent intent ) throws IOException {

        Map<String, Object> params = new HashMap<String, Object>();

//        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(intent.getPackage(), 0);

        // Get server IP from sharedPreference.
        SharedPreferences preferences = context.getSharedPreferences("hids", Context.MODE_PRIVATE);
        String serverIp = preferences.getString("serverIp", null);

        params.put("package", intent.getDataString());
        params.put("fresh_install", String.valueOf(intent.getBooleanExtra("android.intent.extra.REPLACING", false)));

        String serverUrl = CommonUtil.SERVER_URL_INSTALL_PROTOCOL + "://" +
                serverIp + ":" + CommonUtil.SERVER_URL_INSTALL_PORT + "/" +
                CommonUtil.SERVER_URL_INSTALL;

        // Post to server
        post(serverUrl, params);
    }

    /**
     * Send installed application data.
     * @param params
     */
    public static void installedApps(Map<String, Object> params)  {

        String serverUrl = CommonUtil.SERVER_URL_APP_INSTALL_PROTOCOL + "://" +
                CommonUtil.SERVER_IP + ":" + CommonUtil.SERVER_URL_APP_INSTALL_PORT + "/" +
                CommonUtil.SERVER_URL_APP_INSTALL;

        try {
            post(serverUrl, params);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    /**
     * Send running application data.
     * @param params
     */
    public static void runningApps(Map<String, Object> params)  {

        String serverUrl = CommonUtil.SERVER_URL_APP_RUNNING_PROTOCOL + "://" +
                CommonUtil.SERVER_IP + ":" + CommonUtil.SERVER_URL_APP_RUNNING_PORT + "/" +
                CommonUtil.SERVER_URL_APP_RUNNING;

        try {
            post(serverUrl, params);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void deviceStatus(Map<String, Object> params){
        String serverUrl = CommonUtil.SERVER_URL_DEVICEINFO_PROTOCOL + "://" +
                CommonUtil.SERVER_IP + ":" + CommonUtil.SERVER_URL_DEVICEINFO_PORT + "/" +
                CommonUtil.SERVER_URL_DEVICEINFO;


        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        // Once GCM returns a registration id, we need to register it in the
        // demo server. As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            try {
                // Sending device info
                post(serverUrl, params);
                return;
            } catch (Exception e) {
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Log.d(CommonUtil.TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    Log.d(CommonUtil.TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }
        Log.e(CommonUtil.TAG, "Can't send device information to server");
    }

    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params request parameters.
     *
     * @throws java.io.IOException propagated from POST.
     */
    private static void post(String endpoint, Map<String, Object> params)
            throws IOException {

        // Convert map to namevalue part.
        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        for(String key: params.keySet()){
            NameValuePair nameValuePair = new BasicNameValuePair(key, params.get(key).toString());
            nameValuePairList.add(nameValuePair);
        }
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext httpContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(endpoint);
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList));


        HttpResponse httpResponse = httpClient.execute(httpPost, httpContext);

        StatusLine statusLine = httpResponse.getStatusLine();

//        httpResponse.getEntity().getContent().close();
    }

    private static void post(String endpoint, List<String[]> params){

    }

}
