/*
 *  BugsControl
 *  Copyright (C) 2014  Jon Ander Peñalba
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

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import jp.bugscontrol.R;
import jp.bugscontrol.general.Server;


public class ProductListFragment extends ListFragment {
    private OnProductSelectedListener listener;
    private AdapterProduct adapter;

    public interface OnProductSelectedListener {
        public void onProductSelected(final int productId);
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        listener = (OnProductSelectedListener) activity;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.product_list_fragment, container, false);
        final Activity activity = getActivity();

        activity.setProgressBarIndeterminateVisibility(true);

        final Bundle arguments = getArguments();
        final int serverPos;
        if (arguments != null) {
            serverPos = arguments.getInt("server_position", 0);
        } else {
            serverPos = 0;
        }

        final Server server = Server.servers.get(serverPos);

        adapter = new AdapterProduct(activity, server.getProducts());
        setListAdapter(adapter);
        server.setAdapterProduct(adapter, activity);

        return view;
    }

    @Override
    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        listener.onProductSelected(adapter.getProductIdFromPosition(position));
    }
}
