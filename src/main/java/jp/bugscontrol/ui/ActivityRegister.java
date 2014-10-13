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
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
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

    public static void readDbServers() {
        final List<jp.bugscontrol.db.Server> dbServers = new Select().from(jp.bugscontrol.db.Server.class).execute();
        servers.clear();
        for (final jp.bugscontrol.db.Server s : dbServers) {
            servers.add(new jp.bugscontrol.bugzilla.Server(s));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_register);

        serverTypeSpinner = (Spinner) findViewById(R.id.server_type_spinner);
        serverTypeSpinner.setAdapter(new ServerTypeAdapter(this));
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }

    public void registerServer(final View view) {
        if (serverTypeSpinner.getSelectedItem() != Server.BUGZILLA) {
            Toast.makeText(this, "This server type is not yet supported", Toast.LENGTH_LONG).show();
            return;
        }

        final EditText nameView = ((EditText) findViewById(R.id.name));
        final EditText urlView = ((EditText) findViewById(R.id.url));
        final EditText userView = ((EditText) findViewById(R.id.user));
        final EditText passwordView = ((EditText) findViewById(R.id.password));

        final String name = nameView.getText().toString();
        final String url = urlView.getText().toString();
        final String user = userView.getText().toString();
        final String password = passwordView.getText().toString();

        boolean error = false;

        if (TextUtils.isEmpty(name)) {
            nameView.setError(getString(R.string.name_cant_be_empty));
            error = true;
        }

        for (final Server s : servers) {
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
            final Server newServer = new jp.bugscontrol.bugzilla.Server(name, url);
            newServer.setUser(user, password);
            newServer.save();
            servers.add(newServer);

            openProductList(servers.size() - 1);
        }
    }

    private void openProductList(final int position) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            finish();
            final Intent intent = new Intent(this, ActivityServer.class);
            intent.putExtra("server_position", position);
            startActivity(intent);
        } else {
            final Intent upIntent = getParentActivityIntent();
            upIntent.putExtra("server_position", position);
            if (shouldUpRecreateTask(upIntent)) {
                TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
            } else {
                navigateUpTo(upIntent);
            }
        }
    }

    private class ServerTypeAdapter extends ArrayAdapter<String> {
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
