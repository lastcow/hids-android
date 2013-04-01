package edu.lastcow.hids.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: lastcow
 * Date: 3/31/13
 * Time: 10:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileSendAlarmReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Log.i("File", "Sending file for: " + intent.getStringExtra("processName"));

        // Sending file
    }

    /**
     * Cancel alarm
     * @param context
     */
    private void cancelAlarmSendingFile(Context context){
        // Cancel alarm service.
        Intent cancelIntent = new Intent(context, FileSendAlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, cancelIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
