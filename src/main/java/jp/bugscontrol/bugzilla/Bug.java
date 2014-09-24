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

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import jp.util.Util.Listener;


public class Bug extends jp.bugscontrol.general.Bug {
    public Bug(final jp.bugscontrol.general.Product product, final JSONObject json) {
        super(product);
        createFromJSON(json);
        //loadAllInfo();
    }

    public void loadAllInfo() {
        final Bug b = this;
        final BugzillaTask task = new BugzillaTask(product.getServer(), "Bug.comments", "'ids':[" + b.id + "]", new Listener() {
            @Override
            public void callback(final String s) {
                try {
                    final JSONObject object = new JSONObject(s);
                    final JSONArray comments = object.getJSONObject("result").getJSONObject("bugs").getJSONObject(Integer.toString(b.id)).getJSONArray("comments");
                    for (int i=0; i < comments.length(); ++i) {
                        if (i == 0) {
                            description = comments.getJSONObject(i).getString("text");
                        } else {
                            b.comments.add(new Comment(b, comments.getJSONObject(i)));
                        }
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
        task.execute();
    }

    public void createFromJSON(final JSONObject json) {
        try {
            /* Bug JSON
            *
            * {
            *  "priority": "NOR",
            *  "blocks": [ ],
            *  "creator": "dvratil@redhat.com",
            *  "last_change_time": "2013-01-02T11:45:10Z",
            *  "is_cc_accessible": true,
            *  "keywords": [ ],
            *  "cc": ["dvratil@redhat.com"],
            *  "url": "",
            *  "assigned_to": "afiestas@kde.org",
            *  "see_also": [ ],
            *  "groups": [ ],
            *  "id": 310727,
            *  "creation_time": "2012-11-26T18:30:00Z",
            *  "whiteboard": "",
            *  "qa_contact": "",
            *  "depends_on": [ ],
            *  "dupe_of": null,
            *  "resolution": "FIXED",
            *  "classification": "Unclassified",
            *  "alias": null,
            *  "op_sys": "Linux",
            *  "status": "RESOLVED",
            *  "summary": "KScreen::Edid should be created on demand",
            *  "is_open": false,
            *  "platform": "unspecified",
            *  "severity": "normal",
            *  "cf_commitlink": "",
            *  "version": "unspecified",
            *  "cf_versionfixedin": "",
            *  "component": "libkscreen",
            *  "is_creator_accessible": true,
            *  "is_confirmed": true,
            *  "product": "KScreen",
            *  "target_milestone": "---"
            * }
            *
            */
            id = json.getInt("id");
            summary = json.getString("summary");
            priority = json.getString("priority");
            status = json.getString("status");
            reporter = json.getString("creator");
            assignee = json.getString("assigned_to");
            open = TextUtils.isEmpty(json.getString("resolution"));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
