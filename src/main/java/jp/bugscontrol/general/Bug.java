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

package jp.bugscontrol.general;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import jp.bugscontrol.ActivityBug;

public abstract class Bug {
    protected int id;
    protected boolean open;

    protected String summary;
    protected String priority;
    protected String status;
    protected String description;
    protected String creationDate;

    protected String reporter;
    protected String assignee;

    protected final List<Comment> comments = new ArrayList<Comment>();

    protected final Product product;

    protected BaseAdapter adapter;
    protected ActivityBug activity;

    public Bug(final Product product) {
        this.product = product;
    }

    protected abstract void loadComments();

    public void setAdapterComment(final BaseAdapter adapter, final ActivityBug activity) {
        this.adapter = adapter;
        this.activity = activity;

        activity.setProgressBarIndeterminateVisibility(true);
        loadComments();
    }

    protected void commentsListUpdated() {
        adapter.notifyDataSetChanged();
        activity.setProgressBarIndeterminateVisibility(false);
        activity.updateView();
    }

    public int getId() {
        return id;
    }

    public boolean isOpen() {
        return open;
    }

    public String getSummary() {
        return summary;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getReporter() {
        return reporter;
    }

    public String getAssignee() {
        return assignee;
    }

    public List<Comment> getComments() {
        return comments;
    }
}
