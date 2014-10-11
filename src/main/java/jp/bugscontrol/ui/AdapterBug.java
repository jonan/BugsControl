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

import java.util.List;

import jp.bugscontrol.R;
import jp.bugscontrol.general.Bug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdapterBug extends ArrayAdapter<Bug> {
    private final LayoutInflater inflater;

    public AdapterBug(final Context context, final List<Bug> list) {
        super(context, R.layout.adapter_bug, list);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_bug, parent, false);
        }

        final Bug item = getItem(position);
        final int color;
        if (item.isOpen()) {
            color = getContext().getResources().getColor(R.color.adapter_red);
        } else {
            color = getContext().getResources().getColor(R.color.adapter_green);
        }

        final TextView summaryView = (TextView) convertView.findViewById(R.id.summary);
        summaryView.setTextColor(color);
        summaryView.setText("[" + item.getPriority() + "] " + item.getSummary());

        ((TextView) convertView.findViewById(R.id.creation_date)).setText(item.getCreationDate());
        ((TextView) convertView.findViewById(R.id.assignee)).setText(item.getAssignee());

        return convertView;
    }

    public int getBugIdFromPosition(final int position) {
        return getItem(position).getId();
    }
}
