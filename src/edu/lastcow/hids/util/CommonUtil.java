package edu.lastcow.hids.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created with IntelliJ IDEA.
 * User: lastcow
 * Date: 3/1/13
 * Time: 6:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommonUtil {

    // Google project id (zhijiang@chen.me)
    public static final String SENDER_ID = "745677022546";

    // Preference name used in preferences.
    public static final String PREFERENCE_FILENAME = "edu.lastcow.hids";

    // File location
    public static final String FILE_LOCATION = "/data/local/tmp/";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "HIDS GCM";

    // Actions
    public static final String SYSTEM_CALL_TASK_ACTION = "edu.lastcow.hids.SYSTEM_CALL_TASK_ACTION";
    public static final String SYSTEM_CALL_SEND_TO_SERVER_ACTION = "edu.lastcow.hids.SYSTEM_CALL_SEND_TO_SERVER_ACTION";

    // Message from HIDS_GCM_SERVER
    public static final String MSG_GET_INSTALLED_APPS = "getInstalledApps";
    public static final String MSG_GET_RUNNING_APPS = "getRunningApps";
    public static final String MSG_MONITOR_RUNNING_APPS = "monitorRunningApps";
    public static final String MSG_GET_LOGFILES = "getLogfiles";
    public static final String MSG_GET_SYSTEM_INFO = "getSystemInfo";

    // Action awake by server
    public static final String SERVER_GET_ALL_APPS_INSTALLED = "edu.lastcow.hids.apps_installed";
    public static final String SERVER_GET_ALL_APPS_RUNNING = "edu.lastcow.hids.apps_running";
    public static final String SERVER_GET_APPS_SYSTEMCALL = "edu.lastcow.hids.apps_systemcall";
    public static final String SERVER_GET_APPS_LOGFILES = "edu.lastcow.hids.apps_logfile";
    public static final String SERVER_GET_DEVICE_STATUS = "edu.lastcow.hids.device_status";

    public static final String SERVER_IP = "192.168.1.107";
    // give your server registration url here
    public static final String SERVER_URL_REGISTER = "hids/rest/deviceaction/register";
    public static final String SERVER_URL_REGISTER_PROTOCOL = "http";
    public static final String SERVER_URL_REGISTER_PORT = "8080";

    // Package install endpoint
    public static final String SERVER_URL_INSTALL = "hids/rest/deviceaction/install";
    public static final String SERVER_URL_INSTALL_PROTOCOL = "http";
    public static final String SERVER_URL_INSTALL_PORT = "8080";

    // Package install endpoint
    public static final String SERVER_URL_APP_INSTALL = "hids/rest/deviceaction/installedapps";
    public static final String SERVER_URL_APP_INSTALL_PROTOCOL = "http";
    public static final String SERVER_URL_APP_INSTALL_PORT = "8080";

    // Package running endpoint
    public static final String SERVER_URL_APP_RUNNING = "hids/rest/deviceaction/runningapps";
    public static final String SERVER_URL_APP_RUNNING_PROTOCOL = "http";
    public static final String SERVER_URL_APP_RUNNING_PORT = "8080";


    public static final String SERVER_URL_DEVICEINFO = "hids/rest/deviceaction/info";
    public static final String SERVER_URL_DEVICEINFO_PROTOCOL = "http";
    public static final String SERVER_URL_DEVICEINFO_PORT = "8080";

    public static final String DISPLAY_MESSAGE_ACTION =
            "edu.lastcow.hids.agent.DISPLAY_MESSAGE";
    public static final String EXTRA_MESSAGE = "message";


    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayToastMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

}
