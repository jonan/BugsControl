package jp.bugscontrol;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockListActivity;

public class ActivityProductList extends SherlockListActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        AdapterProduct adapter = new AdapterProduct(this, ActivityHome.server.getProducts());
        getListView().setAdapter(adapter);
        ActivityHome.server.setAdapterProduct(adapter);
    }
}
