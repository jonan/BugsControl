package jp.bugscontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Window;

public class ActivityProductList extends SherlockListActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_product_list);

        final AdapterProduct adapter = new AdapterProduct(this, ActivityHome.servers.get(0).getProducts());
        getListView().setAdapter(adapter);
        ActivityHome.servers.get(0).setAdapterProduct(adapter, this);
        final Activity current = this;
        getListView().setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(current, ActivityProduct.class);
                intent.putExtra("product_id", adapter.getProductIdFromPosition(position));
                startActivity(intent);
            }
        });
    }
}
