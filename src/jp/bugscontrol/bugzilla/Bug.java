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

import jp.bugscontrol.bugzilla.Server.Listener;

import org.json.JSONArray;
import org.json.JSONObject;


public class Bug extends jp.bugscontrol.server.Bug {
    public Bug(jp.bugscontrol.server.Product product, JSONObject json) {
        super(product);
        createFromJSON(json);
        loadAllInfo();
    }

    @Override
    public void createFromString(String s) {
        try {
            createFromJSON(new JSONObject(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadAllInfo() {
        final Bug b = this;
        Listener l = new Listener() {
            @Override
            public void callback(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray comments = object.getJSONObject("result").getJSONObject("bugs").getJSONObject(Integer.toString(b.id)).getJSONArray("comments");
                    for (int i=0; i < comments.length(); ++i) {
                        b.comments.add(comments.getJSONObject(i).getString("text"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        BugzillaTask task = new BugzillaTask(product.getServer(), "Bug.comments", "\"ids\":[" + b.id + "]", l);
        task.execute();
    }

    public void createFromJSON(JSONObject json) {
        try {
            id = json.getInt("id");
            open = json.getBoolean("is_open");
            summary = json.getString("summary");
            priority = json.getString("priority");
            status = json.getString("status");
            reporter = json.getString("creator");
            assignee = json.getString("assigned_to");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
