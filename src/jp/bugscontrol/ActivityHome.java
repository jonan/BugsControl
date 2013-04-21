package jp.bugscontrol;

import java.util.ArrayList;
import java.util.List;

import jp.bugscontrol.server.Server;

import com.actionbarsherlock.app.SherlockActivity;

import android.content.Intent;
import android.os.Bundle;

public class ActivityHome extends SherlockActivity {
    static public List<Server> servers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        servers = new ArrayList<Server>();
        startActivity(new Intent(this, ActivityRegister.class));
    }
}
