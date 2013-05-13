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

public class Product extends jp.bugscontrol.server.Product {
    public Product(jp.bugscontrol.server.Server server, JSONObject json) {
        super(server);
        createFromJSON(json);
    }

    @Override
    protected void loadBugs() {
        final Product p = this;
        Listener l = new Listener() {
            @Override
            public void callback(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray bugs = object.getJSONObject("result").getJSONArray("bugs");
                    p.getBugs().clear();
                    for (int i=0; i < bugs.length(); ++i) {
                        if (bugs.getJSONObject(i).getBoolean("is_open"))
                            p.addBug(new Bug(bugs.getJSONObject(i)));
                    }
                    bugsListUpdated();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        BugzillaTask task = new BugzillaTask(server, "Bug.search", "\"product\":\"" + p.getName() + "\"", l);
        task.execute();
    }

    @Override
    public void createFromString(String s) {
        try {
            createFromJSON(new JSONObject(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createFromJSON(JSONObject json) {
        try {
            id = json.getInt("id");
            name = json.getString("name");
            description = json.getString("description");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
