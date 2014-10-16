/*
 *  BugsControl
 *  Copyright (C) 2014  Jon Ander Peñalba
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

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import jp.bugscontrol.R;
import jp.bugscontrol.general.Server;

public class ActivityServerManager extends ListActivity {
    private ServerTypeAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);

        adapter = new ServerTypeAdapter(this);
        getListView().setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_server_manager, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                startActivity(new Intent(this, ActivityRegister.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final Intent intent = new Intent(this, ActivityRegister.class);
        intent.putExtra("server_position", position);
        startActivity(intent);
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }

    private class ServerTypeAdapter extends ArrayAdapter<Server> {
        public ServerTypeAdapter(final Context context) {
            super(context, R.layout.adapter_server, R.id.name, Server.servers);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.adapter_server, parent, false);
            }

            final Server s = Server.servers.get(position);

            ((TextView) convertView.findViewById(R.id.name)).setText(s.getName());

            final ImageView iconImage = (ImageView) convertView.findViewById(R.id.icon);
            iconImage.setImageResource(Server.typeIcon.get(Server.typeName.indexOf(s.getType())));

            convertView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final Server s = Server.servers.get(position);
                    new DialogDeleteServer().setAdapter(adapter).setServerPos(position).show(getFragmentManager(), "DeleteServerDialog");
                }
            });

            return convertView;
        }
    }
}
