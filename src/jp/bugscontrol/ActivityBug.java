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
