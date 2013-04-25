package jp.bugscontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

import jp.bugscontrol.server.Product;

public class ActivityProduct extends SherlockListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_product_list);

        final int server = getIntent().getIntExtra("server", -1);
        int product_id = getIntent().getIntExtra("product_id", -1);
        Product product = ActivityRegister.servers.get(server).getProductFromId(product_id);
        setTitle(product.getName());

        final AdapterBug adapter = new AdapterBug(this, product.getBugs());
        getListView().setAdapter(adapter);
        ActivityRegister.servers.get(server).setAdapterBug(product, adapter, this);
        final Activity current = this;
        getListView().setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(current, ActivityBug.class);
                intent.putExtra("server", server);
                intent.putExtra("bug_id", adapter.getBugIdFromPosition(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
