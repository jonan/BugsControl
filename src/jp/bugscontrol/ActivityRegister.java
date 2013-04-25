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

import java.util.ArrayList;
import java.util.List;

import jp.bugscontrol.bugzilla.Server;

import com.actionbarsherlock.app.SherlockActivity;
import com.activeandroid.query.Select;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ActivityRegister extends SherlockActivity {
    static public List<Server> servers = new ArrayList<Server>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (!getIntent().getBooleanExtra("new_server", false)) {
            List<jp.bugscontrol.db.Server> db_servers = new Select().from(jp.bugscontrol.db.Server.class).execute();
            servers.clear();
            for (jp.bugscontrol.db.Server s : db_servers)
                servers.add(new Server(s));

            if (servers.size() > 0) {
                Intent intent = new Intent(this, ActivityProductList.class);
                intent.putExtra("server", 0);
                startActivity(intent);
            }
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
        servers.add(new_server);

        Intent intent = new Intent(this, ActivityProductList.class);
        intent.putExtra("server", servers.size()-1);
        startActivity(intent);
    }
}
