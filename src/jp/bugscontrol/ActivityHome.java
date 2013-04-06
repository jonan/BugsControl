package jp.bugscontrol;

import jp.bugscontrol.bugzilla.Server;

import com.actionbarsherlock.app.SherlockActivity;

import android.os.Bundle;

public class ActivityHome extends SherlockActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        new Server();
    }
}
