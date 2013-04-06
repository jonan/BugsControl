package jp.bugscontrol.bugzilla;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;

public class BugzillaTask extends AsyncTask<Void, Void, Void> {
    String method;
    String response;

    public BugzillaTask(String method) {
        this.method = method;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL challenges = new URL("https://bugs.kde.org/jsonrpc.cgi?method=" + method);
            URLConnection connection = challenges.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            reader.close();
            response = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        System.out.println(response);
    }
}
