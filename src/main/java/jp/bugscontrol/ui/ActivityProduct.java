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

import android.app.ListActivity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import jp.bugscontrol.R;
import jp.bugscontrol.general.Product;

public class ActivityProduct extends ListActivity {
    private int serverPos;
    private int productId;
    private AdapterBug adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_product_list);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);

        serverPos = getIntent().getIntExtra("server_position", -1);
        productId = getIntent().getIntExtra("product_id", -1);

        if (serverPos == -1 || productId == -1) {
            Toast.makeText(this, R.string.invalid_product, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        final Product product = ActivityRegister.servers.get(serverPos).getProductFromId(productId);
        setTitle(product.getName());

        adapter = new AdapterBug(this, product.getBugs());
        getListView().setAdapter(adapter);
        product.setAdapterBug(adapter, this);
    }

    @Override
    protected void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final Intent intent = new Intent(this, ActivityBug.class);
        intent.putExtra("server_position", serverPos);
        intent.putExtra("product_id", productId);
        intent.putExtra("bug_id", adapter.getBugIdFromPosition(position));
        startActivity(intent);
    }

    @Override
    public boolean onNavigateUp() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            finish();
        } else {
            final Intent upIntent = getParentActivityIntent();
            upIntent.putExtra("server_position", serverPos);
            if (shouldUpRecreateTask(upIntent)) {
                TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
            } else {
                navigateUpTo(upIntent);
            }
        }

        return true;
    }
}
