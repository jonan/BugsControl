package jp.bugscontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockListActivity;

public class ActivityProductList extends SherlockListActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        final AdapterProduct adapter = new AdapterProduct(this, ActivityHome.server.getProducts());
        getListView().setAdapter(adapter);
        ActivityHome.server.setAdapterProduct(adapter);
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
