package jp.bugscontrol;

import jp.bugscontrol.bugzilla.Server;

import com.actionbarsherlock.app.SherlockActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ActivityRegister extends SherlockActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void registerServer(View view) {
        String url = ((EditText) findViewById(R.id.url)).getText().toString();
        String user = ((EditText) findViewById(R.id.user)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        Server new_server = new Server(url);
        new_server.setUser(user, password);
        ActivityHome.servers.add(new_server);
        startActivity(new Intent(this, ActivityProductList.class));
    }
}
