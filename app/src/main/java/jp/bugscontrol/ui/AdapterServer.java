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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import jp.bugscontrol.R;
import jp.bugscontrol.general.Server;


public class AdapterServer extends RecyclerView.Adapter<AdapterServer.ViewHolder> {
    private LeftMenuFragment.OnServerSelectedListener listener;

    public AdapterServer(final LeftMenuFragment.OnServerSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_server, parent, false);
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Server s = Server.servers.get(position);

        holder.name.setText(s.getName());
        holder.iconImage.setImageResource(Server.typeIcon.get(Server.typeName.indexOf(s.getType())));
    }

    @Override
    public int getItemCount() {
        return Server.servers.size();
    }

    static public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView iconImage;

        public ViewHolder(final View v, final LeftMenuFragment.OnServerSelectedListener listener) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            iconImage = (ImageView) v.findViewById(R.id.icon);
            v.findViewById(R.id.delete_button).setVisibility(View.GONE);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onServerSelected(getPosition());
                }
            });
        }
    }
}
