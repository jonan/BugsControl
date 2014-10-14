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

package jp.bugscontrol.ui;

import android.app.ListActivity;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import jp.bugscontrol.R;
import jp.bugscontrol.general.Bug;
import jp.util.ImageLoader;
import jp.util.Util;

public class ActivityBug extends ListActivity {
    private int serverPos;
    private int productId;

    private Bug bug;

    private View mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_bug);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);

        serverPos = getIntent().getIntExtra("server_position", -1);
        productId = getIntent().getIntExtra("product_id", -1);
        final int bugId = getIntent().getIntExtra("bug_id", -1);

        if (serverPos == -1 || productId == -1 || bugId == -1) {
            Toast.makeText(this, R.string.invalid_bug, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bug = ActivityRegister.servers.get(serverPos).getBugFromId(bugId);

        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.bug_info, getListView(), false);
        updateView();

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

        ((TextView) mainView.findViewById(R.id.reporter)).setText(bug.getReporter());
        ((TextView) mainView.findViewById(R.id.assignee)).setText(bug.getAssignee());

        ((TextView) mainView.findViewById(R.id.priority)).setText(bug.getPriority());
        ((TextView) mainView.findViewById(R.id.status)).setText(bug.getStatus());

        ((TextView) mainView.findViewById(R.id.description)).setText(bug.getDescription());
    }

    @Override
    public boolean onNavigateUp() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            finish();
        } else {
            final Intent upIntent = getParentActivityIntent();
            upIntent.putExtra("server_position", serverPos);
            upIntent.putExtra("product_id", productId);
            if (shouldUpRecreateTask(upIntent)) {
                TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
            } else {
                navigateUpTo(upIntent);
            }
        }

        return true;
    }
}
