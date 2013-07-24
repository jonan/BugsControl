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

public class Comment extends jp.bugscontrol.server.Comment {
    public Comment(Bug bug, JSONObject json) {
        super(bug);
        createFromJSON(json);
    }

    @Override
    public void createFromString(String s) {
        try {
            createFromJSON(new JSONObject(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createFromJSON(JSONObject json) {
        /* Comment JSON
         *
         * {
         *  "is_private": false,
         *  "creator": "author@email",
         *  "attachment_id": null,
         *  "time": "2012-11-26T18:30:48Z",
         *  "bug_id": 310727,
         *  "text": "comment text",
         *  "id": 1318664
         * }
         *
         */
        try {
            id = json.getInt("id");
            text = json.getString("text");
            author = json.getString("creator");
            date = json.getString("time");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
