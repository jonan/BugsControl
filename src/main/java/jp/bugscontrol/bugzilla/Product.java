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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;

import jp.util.Util.Listener;

public class Product extends jp.bugscontrol.general.Product {
    public Product(final jp.bugscontrol.general.Server server, final JSONObject json) {
        super(server);
        createFromJSON(json);
    }

    @Override
    protected void loadBugs() {
        final Product p = this;
        final BugzillaTask task = new BugzillaTask(server, "Bug.search", "'product':'" + p.getName() + "', 'resolution':'', 'limit':0, 'include_fields':['id', 'summary', 'priority', 'status', 'creator', 'assigned_to', 'resolution', 'creation_time']",  new Listener() {
            @Override
            public void callback(final String s) {
                try {
                    final JSONObject object = new JSONObject(s);
                    final JSONArray bugs = object.getJSONObject("result").getJSONArray("bugs");
                    final int size = bugs.length();
                    p.getBugs().clear();
                    for (int i = 0; i < size; ++i) {
                        p.addBug(new Bug(p, bugs.getJSONObject(i)));
                    }
                    Collections.reverse(p.getBugs());
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                bugsListUpdated();
            }
        });
        task.execute();
    }

    public void createFromJSON(final JSONObject json) {
        try {
            id = json.getInt("id");
            name = json.getString("name");
            description = json.getString("description");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
