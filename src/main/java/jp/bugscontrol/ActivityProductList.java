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

import jp.bugscontrol.general.Server;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Window;

public class ActivityProductList extends SherlockListActivity implements ActionBar.OnNavigationListener {
    private int serverPos;
    private Server server;

    private AdapterProduct adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_product_list);

        serverPos = getIntent().getIntExtra("server_position", -1);
        server = ActivityRegister.servers.get(serverPos);

        final Context context = getSupportActionBar().getThemedContext();
        ArrayAdapter<CharSequence> list = new ArrayAdapter<CharSequence>(context, R.layout.sherlock_spinner_item);
        for (Server s : ActivityRegister.servers) {
            list.add(s.getName());
        }
        list.add(getResources().getString(R.string.add_server));
        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setListNavigationCallbacks(list, this);
        getSupportActionBar().setSelectedNavigationItem(serverPos);

        adapter = new AdapterProduct(this, server.getProducts());
        setListAdapter(adapter);
        server.setAdapterProduct(adapter, this);
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
            final Intent intent = new Intent(this, ActivityRegister.class);
            intent.putExtra("new_server", true);
            startActivity(intent);
            return true;
        }

        final Intent intent = new Intent(this, ActivityProductList.class);
        intent.putExtra("server_position", position);
        startActivity(intent);
        return true;
    }
}
