package jp.bugscontrol;

import jp.bugscontrol.server.Bug;
import android.os.Bundle;
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
}
