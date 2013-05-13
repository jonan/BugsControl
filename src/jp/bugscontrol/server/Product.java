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

package jp.bugscontrol.server;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockListActivity;

import jp.bugscontrol.AdapterBug;
import jp.bugscontrol.server.Bug;

public abstract class Product {
    protected int id;
    protected String name, description;
    protected List<Bug> bugs;

    protected AdapterBug adapter_bug;
    protected SherlockListActivity bugs_activity;

    protected Server server;

    public Product(Server server) {
        bugs = new ArrayList<Bug>();
        this.server = server;
    }

    protected abstract void loadBugs();

    public void setAdapterBug(Product product, AdapterBug adapter, SherlockListActivity activity) {
        adapter_bug = adapter;
        bugs_activity = activity;

        bugs_activity.setSupportProgressBarIndeterminateVisibility(true);
        loadBugs();
    }

    protected void bugsListUpdated() {
        adapter_bug.notifyDataSetChanged();
        bugs_activity.setSupportProgressBarIndeterminateVisibility(false);
    }

    abstract public void createFromString(String s);

    public int getId() {return id;}

    public String getName()        {return name;}
    public String getDescription() {return description;}

    public List<Bug> getBugs() {return bugs;}

    public void addBug(Bug bug) {
        bugs.add(bug);
    }
}
