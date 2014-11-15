/*
 *  BugsControl
 *  Copyright (C) 2014  Jon Ander Peñalba
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jp.bugscontrol.github;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import jp.bugscontrol.general.Server;
import jp.util.Util.TaskListener;

public class GithubTask extends AsyncTask<Void, Void, Void> {
    private final String method;
    private String response;

    private TaskListener listener;

    private Server server;

    public GithubTask(final Server server, final String method, final TaskListener listener) {
        this.server = server;
        this.method = method;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(final Void... p) {
        try {
            // Send the request
            final HttpClient httpClient = new DefaultHttpClient();
            final HttpGet httpGet = new HttpGet(method.contains("http") ? method : "https://api.github.com" + method);
            httpGet.addHeader("Authorization", "token " + server.getPassword());
            httpGet.addHeader("Accept", "application/vnd.github.v3+json");
            HttpEntity entity = httpClient.execute(httpGet).getEntity();
            response = EntityUtils.toString(entity);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        listener.doInBackground(response);
        return null;
    }

    @Override
    protected void onPostExecute(final Void result) {
        listener.onPostExecute(response);
    }
}
