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

package jp.bugscontrol;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import jp.bugscontrol.general.Bug;
import jp.util.Util;

public class ActivityBug extends ListActivity {
    private Bug bug;

    private View mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_bug);

        int server = getIntent().getIntExtra("server_position", -1);
        int bug_id = getIntent().getIntExtra("bug_id", -1);
        bug = ActivityRegister.servers.get(server).getBugFromId(bug_id);

        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.bug_info, getListView(), false);

        getListView().addHeaderView(mainView);
        getListView().setAdapter(new AdapterComment(this, bug.getComments()));

        final AdapterComment adapter = new AdapterComment(this, bug.getComments());
        getListView().setAdapter(adapter);
        bug.setAdapterComment(adapter, this);
    }

    public void updateView() {
        ImageLoader.loadImage("http://www.gravatar.com/avatar/" + Util.md5(bug.getReporter()), (ImageView) mainView.findViewById(R.id.reporter_img));
        ImageLoader.loadImage("http://www.gravatar.com/avatar/" + Util.md5(bug.getAssignee()), (ImageView) mainView.findViewById(R.id.assignee_img));

        ((TextView) mainView.findViewById(R.id.summary)).setText(bug.getSummary());
        ((TextView) mainView.findViewById(R.id.priority)).setText(bug.getPriority());
        ((TextView) mainView.findViewById(R.id.status)).setText(bug.getStatus());

        ((TextView) mainView.findViewById(R.id.description)).setText(bug.getDescription());
    }
}
