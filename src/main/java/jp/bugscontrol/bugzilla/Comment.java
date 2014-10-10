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

package jp.bugscontrol.bugzilla;

import org.json.JSONObject;

public class Comment extends jp.bugscontrol.general.Comment {
    public Comment(final Bug bug, final JSONObject json) {
        super(bug);
        createFromJSON(json);
    }

    public void createFromJSON(final JSONObject json) {
        try {
            id = json.getInt("id");
            text = json.getString("text");

            if (json.has("creator")) {
                author = json.getString("creator");
            } else {
                author = json.getString("author");
            }

            if (json.has("creation_time")) {
                date = json.getString("creation_time");
            } else {
                date = json.getString("time");
            }

            if (json.has("count")) {
                number = json.getInt("count");
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
