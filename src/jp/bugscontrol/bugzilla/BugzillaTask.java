package jp.bugscontrol.bugzilla;

import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import jp.bugscontrol.bugzilla.Server.Listener;

import jp.bugscontrol.server.Server;

import android.os.AsyncTask;

public class BugzillaTask extends AsyncTask<Void, Void, Void> {
    String method, params, response;
    Listener listener;
    Server server;

    public BugzillaTask(Server server, String method, Listener listener) {
        this.server = server;
        this.method = method;
        this.params = "";
        this.listener = listener;
    }

    public BugzillaTask(Server server, String method, String params, Listener listener) {
        this.server = server;
        this.method = method;
        this.params = params;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(Void... p) {
        try {
            //HttpClient http_client = new DefaultHttpClient();
            HttpClient http_client = MySSLSocketFactory.getNewHttpClient();
            HttpPost http_post = new HttpPost(server.getUrl() + "/jsonrpc.cgi");
            http_post.addHeader("Content-Type", "application/json");

            JSONObject request = new JSONObject();
            request.put("id", UUID.randomUUID().hashCode());
            request.put("method", method);
            JSONArray array;
            if (server.hasUser()) {
                if (params.length() > 0)
                    params += ",";
                params += "\"Bugzilla_login\":\"" + server.getUser() + "\",\"Bugzilla_password\":\"" + server.getPassword() + "\"";
            }
            if (params.length() > 0)
                array = new JSONArray("[{" + params + "}]");
            else
                array = new JSONArray();
            request.put("params", array);

            http_post.setEntity(new StringEntity(request.toString()));
            HttpEntity entity = http_client.execute(http_post).getEntity();
            response = EntityUtils.toString(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        listener.callback(response);
    }
}
