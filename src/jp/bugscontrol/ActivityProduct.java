package jp.bugscontrol;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockListActivity;

import jp.bugscontrol.server.Product;

public class ActivityProduct extends SherlockListActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        int product_id = getIntent().getIntExtra("product_id", -1);
        Product product = ActivityHome.server.getProductFromId(product_id);

        final AdapterBug adapter = new AdapterBug(this, product.getBugs());
        getListView().setAdapter(adapter);
        ActivityHome.server.setAdapterBug(product, adapter);
    }
}
