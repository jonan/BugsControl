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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.util.Util;


public class Bug extends jp.bugscontrol.general.Bug {
    private String commentsUrl;

    public Bug(final jp.bugscontrol.general.Product product, final JSONObject json) {
        super(product);
        createFromJSON(json);
    }

    @Override
    protected void loadComments() {
        final Bug b = this;
        final List<Comment> newList = new ArrayList<Comment>();
        final GithubTask task = new GithubTask(product.getServer(), commentsUrl, new Util.TaskListener() {
            @Override
            public void doInBackground(final String s) {
                try {
                    final JSONArray object = new JSONArray(s);
                    final int size = object.length();
                    for (int i = 0; i < size; ++i) {
                        newList.add(new Comment(b, object.getJSONObject(i)));
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPostExecute(final String s) {
                comments.clear();
                comments.addAll(newList);
                commentsListUpdated();
            }
        });
        task.execute();
    }

    private void createFromJSON(final JSONObject json) {
        try {
            id = json.getInt("id");
            summary = json.getString("title");
            description = json.getString("body");
            creationDate = Util.formatDate("yyyy-MM-dd'T'HH:mm:ss'Z'", json.getString("created_at"));
            status = json.getString("state");
            String userJson = json.getString("user");
            if (!userJson.equals("null")) {
                reporter = new User(new JSONObject(userJson));
            } else {
                reporter = null;
            }
            userJson = json.getString("assignee");
            if (!userJson.equals("null")) {
                assignee = new User(new JSONObject(userJson));
            } else {
                assignee = null;
            }
            open = json.getString("closed_at").equals("null");
            commentsUrl = json.getString("comments_url");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
