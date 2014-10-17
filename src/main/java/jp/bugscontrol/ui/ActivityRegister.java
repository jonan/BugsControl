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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.UUID;

import jp.bugscontrol.R;
import jp.bugscontrol.general.Server;
import jp.bugscontrol.github.GithubLogin;

public class ActivityRegister extends Activity {
    private Server server = null;

    private Spinner serverTypeSpinner;
    private EditText nameView;
    private EditText urlView;
    private EditText userView;
    private EditText passwordView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);

        serverTypeSpinner = (Spinner) findViewById(R.id.server_type_spinner);
        nameView = ((EditText) findViewById(R.id.name));
        urlView = ((EditText) findViewById(R.id.url));
        userView = ((EditText) findViewById(R.id.user));
        passwordView = ((EditText) findViewById(R.id.password));

        serverTypeSpinner.setAdapter(new ServerTypeAdapter(this));

        final int serverPos = getIntent().getIntExtra("server_position", -1);
        if (serverPos == -1) {
            setTitle(R.string.add_server);
        } else {
            setTitle(R.string.edit_server);
            server = Server.servers.get(serverPos);
            serverTypeSpinner.setSelection(Server.typeName.indexOf(server.getType()));
            serverTypeSpinner.setEnabled(false);
            nameView.setText(server.getName());
            urlView.setText(server.getUrl());
            userView.setText(server.getUser());
            passwordView.setText(server.getPassword());
        }
    }

    @Override
    public boolean onNavigateUp() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            finish();
            return true;
        } else {
            return super.onNavigateUp();
        }
    }

    public void onAccept(final View view) {
        final String name = nameView.getText().toString();
        final String url = urlView.getText().toString();
        final String user = userView.getText().toString();
        final String password = passwordView.getText().toString();

        boolean error = false;

        if (TextUtils.isEmpty(name)) {
            nameView.setError(getString(R.string.name_cant_be_empty));
            error = true;
        }

        for (final Server s : Server.servers) {
            if (s.getName().equals(name)) {
                nameView.setError(getString(R.string.server_with_that_name_exists));
                error = true;
            }
        }

        if (TextUtils.isEmpty(url)) {
            urlView.setError(getString(R.string.name_cant_be_empty));
            error = true;
        }

        if (!error) {
            if (server == null) {
                registerServer(name, url, user, password);
            } else {
                editServer(name, url, user, password);
            }
        }
    }

    private void editServer(final String name, final String url, final String user, final String password) {
        server.setName(name);
        server.setUrl(url);
        server.setUser(user, password);
        server.save();
        finish();
    }

    private void registerServer(final String name, final String url, final String user, final String password) {
        if (serverTypeSpinner.getSelectedItem() == Server.BUGZILLA) {
            final Server newServer = new jp.bugscontrol.bugzilla.Server(name, url);
            newServer.setUser(user, password);
            newServer.save();
            Server.servers.add(newServer);
        } else if (serverTypeSpinner.getSelectedItem() == Server.GITHUB) {
            startActivity(new Intent(this, GithubLogin.class));
        }

        finish();
    }

    private class ServerTypeAdapter extends ArrayAdapter<String> {
        public ServerTypeAdapter(final Context context) {
            super(context, R.layout.adapter_server_type, R.id.server_type, Server.typeName);
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            final View view = super.getView(position, convertView, parent);
            final ImageView iconImage = (ImageView) view.findViewById(R.id.icon);
            iconImage.setImageResource(Server.typeIcon.get(position));
            return view;
        }

        @Override
        public View getDropDownView(final int position, final View convertView, final ViewGroup parent) {
            return getView(position, convertView, parent);
        }
    }
}
