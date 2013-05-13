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

import java.security.MessageDigest;

import jp.bugscontrol.server.Bug;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class ActivityBug extends SherlockActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_bug);

        int server = getIntent().getIntExtra("server", -1);
        int bug_id = getIntent().getIntExtra("bug_id", -1);
        Bug bug = ActivityRegister.servers.get(server).getBugFromId(bug_id);

        ImageLoader.loadImage("http://www.gravatar.com/avatar/" + md5(bug.getReporter()), (ImageView) findViewById(R.id.reporter_img));
        ImageLoader.loadImage("http://www.gravatar.com/avatar/" + md5(bug.getAssignee()), (ImageView) findViewById(R.id.assignee_img));

        ((TextView) findViewById(R.id.summary)).setText(bug.getSummary());
        ((TextView) findViewById(R.id.priority)).setText(bug.getPriority());
        ((TextView) findViewById(R.id.status)).setText(bug.getStatus());

        String comments = "";
        for (String c : bug.getComments())
            comments += c + "\n\n";
        ((TextView) findViewById(R.id.comments)).setText(comments);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String md5(String s) {
        String hash = "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(s.getBytes("CP1252"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i)
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            hash = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hash;
    }
}
