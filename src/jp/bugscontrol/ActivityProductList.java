package jp.bugscontrol;

import jp.bugscontrol.server.Server;
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
        for (Server s : ActivityHome.servers)
            list.add(s.getName());
        list.add("Add new server");
        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setListNavigationCallbacks(list, this);
        getSupportActionBar().setSelectedNavigationItem(server);

        final AdapterProduct adapter = new AdapterProduct(this, ActivityHome.servers.get(server).getProducts());
        getListView().setAdapter(adapter);
        ActivityHome.servers.get(server).setAdapterProduct(adapter, this);
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

        if (position == ActivityHome.servers.size()) {
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
