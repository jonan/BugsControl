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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import jp.bugscontrol.R;
import jp.bugscontrol.general.Server;

public class ActivityServer extends ActionBarActivity implements ActionBar.OnNavigationListener, ProductListFragment.OnProductSelectedListener, BugListFragment.OnBugSelectedListener, ServerListFragment.OnServerSelectedListener {
    private int serverPos;
    private int productId;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        //setSupportProgressBarIndeterminateVisibility(false);
        setContentView(R.layout.activity_server);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Load ad
        final AdView adView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
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
    public boolean onNavigationItemSelected(final int position, final long id) {
        if (position == serverPos) {
            return true;
        }

        if (position == Server.servers.size()) {
            openServerRegistry();
            return true;
        }

        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().popBackStack();
        setServer(position);
        return true;
    }

    @Override
    public void onServerSelected(final int position) {
        drawerLayout.closeDrawer(Gravity.START);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().popBackStack();
        setServer(position);
    }

    @Override
    public void onProductSelected(final int productId) {
        this.productId = productId;
        Fragment fragment = new BugListFragment();
        final Bundle arguments = new Bundle();
        arguments.putInt("server_position", serverPos);
        arguments.putInt("product_id", productId);
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onBugSelected(final int bugId) {
        Fragment fragment = new BugInfoFragment();
        final Bundle arguments = new Bundle();
        arguments.putInt("server_position", serverPos);
        arguments.putInt("product_id", productId);
        arguments.putInt("bug_id", bugId);
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    private void setActionBar() {
        if (Server.servers.size() == 0) {
            return;
        }

        final ActionBar actionBar = getSupportActionBar();
        final Context context = actionBar.getThemedContext();
        ArrayAdapter<CharSequence> list = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_dropdown_item);
        for (final Server s : Server.servers) {
            list.add(s.getName());
        }
        list.add(getResources().getString(R.string.manage_servers));

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(list, this);
    }

    private void setServer(final int pos) {
        serverPos = pos;

        if (serverPos == -1) {
            if (Server.servers.size() == 0) {
                openServerRegistry();
                return;
            } else {
                serverPos = 0;
            }
        }

        getSupportActionBar().setSelectedNavigationItem(serverPos);

        Fragment fragment = new ProductListFragment();
        final Bundle arguments = new Bundle();
        arguments.putInt("server_position", serverPos);
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private void openServerRegistry() {
        startActivity(new Intent(this, ActivityServerManager.class));
        if (serverPos != -1) {
            getSupportActionBar().setSelectedNavigationItem(serverPos);
        }
    }
}
