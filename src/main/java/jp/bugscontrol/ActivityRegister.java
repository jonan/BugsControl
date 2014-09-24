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

import jp.bugscontrol.general.Server;

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
            final List<jp.bugscontrol.db.Server> dBservers = new Select().from(jp.bugscontrol.db.Server.class).execute();
            servers.clear();
            for (jp.bugscontrol.db.Server s : dBservers) {
                servers.add(new jp.bugscontrol.bugzilla.Server(s));
            }

            if (servers.size() > 0) {
                openProductList(0);
            }
        }
    }

    public void registerServer(View view) {
        final String name = ((EditText) findViewById(R.id.name)).getText().toString();
        final String url = ((EditText) findViewById(R.id.url)).getText().toString();
        final String user = ((EditText) findViewById(R.id.user)).getText().toString();
        final String password = ((EditText) findViewById(R.id.password)).getText().toString();

        final Server newServer = new jp.bugscontrol.bugzilla.Server(name, url);
        newServer.setUser(user, password);
        newServer.save();
        servers.add(newServer);

        openProductList(servers.size()-1);
    }

    private void openProductList(final int position) {
        final Intent intent = new Intent(this, ActivityProductList.class);
        intent.putExtra("server_position", position);
        startActivity(intent);
    }
}
