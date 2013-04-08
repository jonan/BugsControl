package jp.bugscontrol;

import jp.bugscontrol.bugzilla.Server;

import com.actionbarsherlock.app.SherlockActivity;

import android.content.Intent;
import android.os.Bundle;

public class ActivityHome extends SherlockActivity {
    static public jp.bugscontrol.server.Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        server = new Server();
        startActivity(new Intent(this, ActivityProductList.class));
    }
}
