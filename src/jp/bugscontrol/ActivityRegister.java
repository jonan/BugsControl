package jp.bugscontrol;

import java.util.List;

import jp.bugscontrol.bugzilla.Server;

import com.actionbarsherlock.app.SherlockActivity;
import com.activeandroid.query.Select;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ActivityRegister extends SherlockActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        List<jp.bugscontrol.db.Server> db_servers = new Select().from(jp.bugscontrol.db.Server.class).execute();
        if (db_servers.size() > 0) {
            jp.bugscontrol.db.Server s = db_servers.get(0);
            ActivityHome.servers.add(new Server(s));
            Intent intent = new Intent(this, ActivityProductList.class);
            intent.putExtra("server", ActivityHome.servers.size()-1);
            startActivity(intent);
        }
    }

    public void registerServer(View view) {
        String name = ((EditText) findViewById(R.id.name)).getText().toString();
        String url = ((EditText) findViewById(R.id.url)).getText().toString();
        String user = ((EditText) findViewById(R.id.user)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        Server new_server = new Server(name, url);
        new_server.setUser(user, password);
        new_server.save();
        ActivityHome.servers.add(new_server);

        Intent intent = new Intent(this, ActivityProductList.class);
        intent.putExtra("server", ActivityHome.servers.size()-1);
        startActivity(intent);
    }
}
