/*
 *  BugsControl
 *  Copyright (C) 2013  Jon Ander Peñalba
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

package jp.bugscontrol.bugzilla;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import jp.util.Util.Listener;


public class Bug extends jp.bugscontrol.general.Bug {
    public Bug(final jp.bugscontrol.general.Product product, final JSONObject json) {
        super(product);
        createFromJSON(json);
    }

    @Override
    protected void loadComments() {
        final Bug b = this;
        final BugzillaTask task = new BugzillaTask(product.getServer(), "Bug.comments", "'ids':[" + b.id + "]", new Listener() {
            @Override
            public void callback(final String s) {
                try {
                    final JSONObject object = new JSONObject(s);
                    final JSONArray comments = object.getJSONObject("result").getJSONObject("bugs").getJSONObject(Integer.toString(b.id)).getJSONArray("comments");
                    b.comments.clear();
                    for (int i=0; i < comments.length(); ++i) {
                        if (i == 0) {
                            description = comments.getJSONObject(i).getString("text");
                        } else {
                            b.comments.add(new Comment(b, comments.getJSONObject(i)));
                        }
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                commentsListUpdated();
            }
        });
        task.execute();
    }

    private void createFromJSON(final JSONObject json) {
        try {
            id = json.getInt("id");
            summary = json.getString("summary");
            creationDate = json.getString("creation_time");
            priority = json.getString("priority");
            status = json.getString("status");
            reporter = json.getString("creator");
            assignee = json.getString("assigned_to");
            open = TextUtils.isEmpty(json.getString("resolution"));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
