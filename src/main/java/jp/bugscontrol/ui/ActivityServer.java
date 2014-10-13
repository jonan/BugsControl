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

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import jp.bugscontrol.R;
import jp.bugscontrol.general.Server;

public class ActivityServer extends ListActivity implements ActionBar.OnNavigationListener {
    private int serverPos;

    private AdapterProduct adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(false);
        setContentView(R.layout.activity_product_list);

        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);

        ActivityRegister.readDbServers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setActionBar();
        setServer(getIntent().getIntExtra("server_position", -1));
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        setActionBar();
        setServer(intent.getIntExtra("server_position", -1));
    }

    @Override
    protected void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final Intent intent = new Intent(this, ActivityProduct.class);
        intent.putExtra("server_position", serverPos);
        intent.putExtra("product_id", adapter.getProductIdFromPosition(position));
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(final int position, final long id) {
        if (position == serverPos) {
            return true;
        }

        if (position == ActivityRegister.servers.size()) {
            openServerRegistry();
            return true;
        }

        setServer(position);
        return true;
    }

    private void setActionBar() {
        if (ActivityRegister.servers.size() == 0) {
            return;
        }

        final ActionBar actionBar = getActionBar();
        final Context context = actionBar.getThemedContext();
        ArrayAdapter<CharSequence> list = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_dropdown_item);
        for (final Server s : ActivityRegister.servers) {
            list.add(s.getName());
        }
        list.add(getResources().getString(R.string.manage_servers));

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(list, this);
    }

    private void setServer(final int pos) {
        serverPos = pos;

        if (serverPos == -1) {
            if (ActivityRegister.servers.size() == 0) {
                openServerRegistry();
                return;
            } else {
                serverPos = 0;
            }
        }

        getActionBar().setSelectedNavigationItem(serverPos);

        final Server server = ActivityRegister.servers.get(serverPos);

        setProgressBarIndeterminateVisibility(true);
        adapter = new AdapterProduct(this, server.getProducts());
        setListAdapter(adapter);
        server.setAdapterProduct(adapter, this);
    }

    private void openServerRegistry() {
        startActivity(new Intent(this, ActivityServerManager.class));
        if (serverPos != -1) {
            getActionBar().setSelectedNavigationItem(serverPos);
        }
    }
}
