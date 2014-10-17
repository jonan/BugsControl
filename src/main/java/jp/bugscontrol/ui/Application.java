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


import com.activeandroid.query.Select;

import java.util.List;

import jp.bugscontrol.general.Server;

public class Application extends com.activeandroid.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        final List<jp.bugscontrol.db.Server> dbServers = new Select().from(jp.bugscontrol.db.Server.class).execute();
        Server.servers.clear();
        for (final jp.bugscontrol.db.Server s : dbServers) {
            switch (s.type) {
                case Server.BUGZILLA:
                    Server.servers.add(new jp.bugscontrol.bugzilla.Server(s));
                    break;
                case Server.GITHUB:
                    Server.servers.add(new jp.bugscontrol.github.Server(s));
                    break;
                default:
                    break;
            }
        }
    }
}
