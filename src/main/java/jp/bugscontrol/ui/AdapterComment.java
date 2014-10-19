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
import jp.bugscontrol.general.Comment;
import jp.bugscontrol.general.User;
import jp.util.ImageLoader;
import jp.util.Util;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterComment extends ArrayAdapter<Comment> {
    private LayoutInflater inflater;

    public AdapterComment(final Context context, final List<Comment> list) {
        super(context, R.layout.adapter_comment, list);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_comment, parent, false);
        }

        final Comment item = getItem(position);
        ((TextView) convertView.findViewById(R.id.creator)).setText(item.getAuthor().name);
        ((TextView) convertView.findViewById(R.id.text)).setText(item.getText());
        ((TextView) convertView.findViewById(R.id.date)).setText(item.getDate());
        ((TextView) convertView.findViewById(R.id.bug_number)).setText("#" + ( (item.getNumber() > 0) ? item.getNumber() : (position + 1) ));

        final User author = item.getAuthor();
        if (!TextUtils.isEmpty(author.avatarUrl)) {
            ImageLoader.loadImage(author.avatarUrl, (ImageView) convertView.findViewById(R.id.author_img));
        } else {
            ImageLoader.loadImage("http://www.gravatar.com/avatar/" + Util.md5(author.email), (ImageView) convertView.findViewById(R.id.author_img));
        }

        return convertView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(final int position) {
        return false;
    }
}
