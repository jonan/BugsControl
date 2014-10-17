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


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.UUID;

public class GithubLogin extends Activity {
    private static final String CLIENT_ID = "7b3fcb9f74ac6386db23";
    private static final String CLIENT_SECRET = "451a20387960e984e649dd806e0ec98f7826ce02";

    private String state;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            state = savedInstanceState.getString("state");
        }

        final Uri data = getIntent().getData();
        if (data != null) {
            // Activity opened in response to a registry
            final String code = data.getQueryParameter("code");
            new OauthTask(code).execute();
            // TODO compare state
            /*final String responseState = data.getQueryParameter("state");
            if (state.equals(responseState)) {
                final String code = data.getQueryParameter("code");
                new OauthTask(code).execute();
            } else {
                // give feedback to the user
                finish();
            }*/
            return;
        }

        state = String.valueOf(UUID.randomUUID().hashCode());
        final String githubUrl = "https://github.com/login/oauth/authorize?client_id=" + CLIENT_ID + "&state=" + state;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl)));
    }

    private class OauthTask extends AsyncTask<Void, Void, Void> {
        private String code;
        private String response;

        public OauthTask(final String code) {
            this.code = code;
        }

        @Override
        protected Void doInBackground(final Void... p) {
            try {
                // Send the request
                final HttpClient httpClient = new DefaultHttpClient();
                final String url = "https://github.com/login/oauth/access_token" +
                        "?client_id=" + CLIENT_ID +
                        "&client_secret=" + CLIENT_SECRET +
                        "&code=" + code;
                final HttpGet httpGet = new HttpGet(url);
                httpGet.addHeader("Accept", "application/json");
                HttpEntity entity = httpClient.execute(httpGet).getEntity();
                response = EntityUtils.toString(entity);
            } catch (final Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            try {
                final JSONObject json = new JSONObject(response);
                final String token = json.getString("access_token");
                final jp.bugscontrol.general.Server newServer = new jp.bugscontrol.github.Server(token);
                newServer.save();
                jp.bugscontrol.general.Server.servers.add(newServer);
                finish();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}
