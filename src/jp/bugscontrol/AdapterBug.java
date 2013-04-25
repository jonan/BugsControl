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

package jp.bugscontrol;

import java.util.List;

import jp.bugscontrol.server.Bug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdapterBug extends ArrayAdapter<Bug> {
    LayoutInflater inflater;

    public AdapterBug(Context context, List<Bug> list) {
        super(context, R.layout.adapter_bug, list);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.adapter_bug, parent, false);

        Bug item = getItem(position);
        int color;
        if (item.isOpen())
            color = getContext().getResources().getColor(R.color.adapter_red);
        else
            color = getContext().getResources().getColor(R.color.adapter_green);
        ((TextView) view.findViewById(R.id.summary)).setTextColor(color);
        ((TextView) view.findViewById(R.id.summary)).setText("[" + item.getPriority() + "] " + item.getSummary());
        ((TextView) view.findViewById(R.id.assignee)).setText(item.getAssignee());

        return view;
    }

    public int getBugIdFromPosition(int position) {
        return getItem(position).getId();
    }
}
