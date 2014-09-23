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

package jp.bugscontrol.server;

import java.util.ArrayList;
import java.util.List;

public abstract class Bug {
    protected int id;
    protected boolean open;
    protected String summary, priority, status, description;
    protected String reporter, assignee;
    protected List<Comment> comments;

    protected Product product;

    public Bug(Product product) {
        comments = new ArrayList<Comment>();
        this.product = product;
    }

    abstract public void createFromString(String s);

    abstract public void loadAllInfo();

    public int getId() {return id;}

    public boolean isOpen() {return open;}

    public String getSummary()     {return summary;}
    public String getPriority()    {return priority;}
    public String getStatus()      {return status;}
    public String getDescription() {return description;}
    public String getReporter()    {return reporter;}
    public String getAssignee()    {return assignee;}

    public List<Comment> getComments() {return comments;}
}
