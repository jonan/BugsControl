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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Window;

public class ActivityProductList extends SherlockListActivity implements ActionBar.OnNavigationListener {
    int server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_product_list);

        server = getIntent().getIntExtra("server", -1);

        Context context = getSupportActionBar().getThemedContext();
        ArrayAdapter<CharSequence> list = new ArrayAdapter<CharSequence>(context, R.layout.sherlock_spinner_item);
        for (Server s : ActivityRegister.servers)
            list.add(s.getName());
        list.add(getResources().getString(R.string.add_server));
        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setListNavigationCallbacks(list, this);
        getSupportActionBar().setSelectedNavigationItem(server);

        final AdapterProduct adapter = new AdapterProduct(this, ActivityRegister.servers.get(server).getProducts());
        getListView().setAdapter(adapter);
        ActivityRegister.servers.get(server).setAdapterProduct(adapter, this);
        final Activity current = this;
        getListView().setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(current, ActivityProduct.class);
                intent.putExtra("server", server);
                intent.putExtra("product_id", adapter.getProductIdFromPosition(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        if (position == server)
            return true;

        if (position == ActivityRegister.servers.size()) {
            Intent intent = new Intent(this, ActivityRegister.class);
            intent.putExtra("new_server", true);
            startActivity(intent);
            return true;
        }

        Intent intent = new Intent(this, ActivityProductList.class);
        intent.putExtra("server", position);
        startActivity(intent);
        return true;
    }
}
