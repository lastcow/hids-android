package edu.lastcow.hids.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lastcow
 * Date: 3/1/13
 * Time: 6:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class SystemCallMonitor extends AsyncTask<Map<String, Object>, Void, StringBuilder> {

    Process suProcess = null;
    OutputStream pidOutputStream = null;

    @Override
    protected StringBuilder doInBackground(Map<String, Object>... params) {
        final String pid = params[0].get("pid").toString();
        final String pname = params[0].get("pname").toString();
        final Context context = (Context) params[0].get("context");
        String fileName = "/data/local/tmp/log_" + pname + ".txt";

        //TODO check for white list

        // Monitoring the process.
        Log.v("SystemCallMonitor", "System calling for " + pid);

        //TODO  Check for the process name in whitelist or not

        try {

            // umask = 000;
            List<String> args = null;

            args = new ArrayList<String>();
            args.add("su");
            args.add("-c");
            args.add("/data/local/tmp/strace");
            args.add("-p");
            args.add(pid);
            args.add("-o");
            args.add(fileName);
            suProcess = new ProcessBuilder(args).start();

            args = new ArrayList<String>();
            args.add("su");
            args.add("c");
            args.add("chmod");
            args.add("666");
            args.add(fileName);
            suProcess = new ProcessBuilder(args).start();

            Thread.sleep(30000);

            suProcess.destroy();


        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //TODO permission issue.
            suProcess = null;
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
