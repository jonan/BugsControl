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

import jp.bugscontrol.server.Comment;
import jp.util.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterComment extends ArrayAdapter<Comment> {
    LayoutInflater inflater;

    public AdapterComment(Context context, List<Comment> list) {
        super(context, R.layout.adapter_comment, list);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.adapter_comment, parent, false);

        Comment item = getItem(position);
        ((TextView) view.findViewById(R.id.creator)).setText(item.getAuthor());
        ((TextView) view.findViewById(R.id.text)).setText(item.getText());

        ImageLoader.loadImage("http://www.gravatar.com/avatar/" + Util.md5(item.getAuthor()), (ImageView) view.findViewById(R.id.author_img));

        return view;
    }
}
