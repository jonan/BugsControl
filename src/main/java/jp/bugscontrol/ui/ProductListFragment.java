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

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import jp.bugscontrol.R;
import jp.bugscontrol.general.Server;


public class ProductListFragment extends ListFragment {
    private AdapterProduct adapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.product_list_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setProgressBarIndeterminateVisibility(true);
        final Bundle arguments = getArguments();
        final int index;
        if (arguments != null) {
            index = arguments.getInt("server_position", 0);
        } else {
            index = 0;
        }
        final Server server = Server.servers.get(index);
        adapter = new AdapterProduct(getActivity(), server.getProducts());
        setListAdapter(adapter);
        server.setAdapterProduct(adapter, getActivity());
    }

    @Override
    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
    }
}
