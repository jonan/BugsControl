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

package jp.bugscontrol.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import jp.bugscontrol.R;
import jp.bugscontrol.general.Server;

public class ActivityRegister extends Activity {
    static public List<Server> servers = new ArrayList<Server>();

    private static String[] typeName = {Server.BUGZILLA, Server.GITHUB};
    private static int[] typeIcon = {R.drawable.server_icon_bugzilla, R.drawable.server_icon_github};

    private Spinner serverTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (!getIntent().getBooleanExtra("new_server", false)) {
            final List<jp.bugscontrol.db.Server> dbServers = new Select().from(jp.bugscontrol.db.Server.class).execute();
            servers.clear();
            for (final jp.bugscontrol.db.Server s : dbServers) {
                servers.add(new jp.bugscontrol.bugzilla.Server(s));
            }

            if (servers.size() > 0) {
                openProductList(0);
                return;
            }
        }

        serverTypeSpinner = (Spinner) findViewById(R.id.server_type_spinner);
        serverTypeSpinner.setAdapter(new ServerTypeAdapter(this));
    }

    public void registerServer(final View view) {
        if (serverTypeSpinner.getSelectedItem() != Server.BUGZILLA) {
            Toast.makeText(this, "This server type is not yet supported", Toast.LENGTH_LONG).show();
            return;
        }

        final String name = ((EditText) findViewById(R.id.name)).getText().toString();
        final String url = ((EditText) findViewById(R.id.url)).getText().toString();
        final String user = ((EditText) findViewById(R.id.user)).getText().toString();
        final String password = ((EditText) findViewById(R.id.password)).getText().toString();

        final Server newServer = new jp.bugscontrol.bugzilla.Server(name, url);
        newServer.setUser(user, password);
        newServer.save();
        servers.add(newServer);

        openProductList(servers.size() - 1);
    }

    private void openProductList(final int position) {
        finish();
        final Intent intent = new Intent(this, ActivityServer.class);
        intent.putExtra("server_position", position);
        startActivity(intent);
    }

    private class ServerTypeAdapter extends ArrayAdapter {
        public ServerTypeAdapter(final Context context) {
            super(context, R.layout.adapter_server_type, R.id.server_type, typeName);
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            final View view = super.getDropDownView(position, convertView, parent);
            final ImageView iconImage = (ImageView) view.findViewById(R.id.icon);
            iconImage.setImageResource(typeIcon[position]);
            return view;
        }

        @Override
        public View getDropDownView(final int position, final View convertView, final ViewGroup parent) {
            return getView(position, convertView, parent);
        }
    }
}
