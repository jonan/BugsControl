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

import jp.util.Util.TaskListener;

public class Product extends jp.bugscontrol.general.Product {
    private String fullName;

    public Product(final jp.bugscontrol.general.Server server, final JSONObject json) {
        super(server);
        createFromJSON(json);
    }

    @Override
    protected void loadBugs() {
        final Product p = this;
        final List<jp.bugscontrol.general.Bug> newList = new ArrayList<jp.bugscontrol.general.Bug>();
        final GithubTask task = new GithubTask(server, "/repos/" + fullName + "/issues", new TaskListener() {
            @Override
            public void doInBackground(final String s) {
                try {
                    final JSONArray object = new JSONArray(s);
                    final int size = object.length();
                    for (int i = 0; i < size; ++i) {
                        newList.add(new Bug(p, object.getJSONObject(i)));
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPostExecute(final String s) {
                p.clearBugs();
                p.addBugs(newList);
                bugsListUpdated();
            }
        });
        task.execute();
    }

    private void createFromJSON(final JSONObject json) {
        try {
            id = json.getInt("id");
            name = json.getString("name");
            fullName = json.getString("full_name");
            description = json.getString("description");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
