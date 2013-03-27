package edu.lastcow.hids.data;

import android.content.Context;
import edu.lastcow.hids.util.CommonUtil;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: lastcow
 * Date: 3/21/13
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class HidsResteasyClient implements HidsClient {

    private final Context context;
    private final HttpClient httpClient;

    public HidsResteasyClient(Context context){
        this.context = context;

        BasicHttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
        HttpProtocolParams.setUseExpectContinue(params, false);
        httpClient = new DefaultHttpClient(params);
    }

    @Override
    public Response testRest(String id, String serial) {

        String content = post("test/" + id, serial);

        if(content != null){
            try {
                JSONTokener tokener = new JSONTokener(content);
                JSONObject obj = (JSONObject) tokener.nextValue();

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    private String get(String path) {
        try {
            HttpGet request = new HttpGet(getRequestURI(path));
            HttpResponse res = httpClient.execute(request);
            String content = EntityUtils.toString(res.getEntity());
            return content;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String put(String path, String title) {
        try {
            HttpPut request = new HttpPut(getRequestURI(path + "?title=" + title));
            HttpResponse res = httpClient.execute(request);
            String content = EntityUtils.toString(res.getEntity());
            return content;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String post(String path, String title) {
        try {
            HttpPost request = new HttpPost(getRequestURI(path));
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(title));
            HttpResponse res = httpClient.execute(request);
            String content = EntityUtils.toString(res.getEntity());
            return content;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String delete(String path) {
        try {
            HttpDelete request = new HttpDelete(getRequestURI(path));
            HttpResponse res = httpClient.execute(request);
            String content = EntityUtils.toString(res.getEntity());
            return content;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private URI getRequestURI(String path) throws URISyntaxException {

        URI requestURI = new URI("http://" + CommonUtil.SERVER_IP + ":8080" + "/rest/deviceaction/" + path);
        return requestURI;
    }
}
