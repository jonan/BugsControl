package jp.bugscontrol.server;

import java.util.ArrayList;
import java.util.List;

import jp.bugscontrol.server.Bug;

public abstract class Product {
    protected int id;
    protected String name, description;
    protected List<Bug> bugs;

    public Product() {
        bugs = new ArrayList<Bug>();
    }

    abstract public void createFromString(String s);

    public int getId() {return id;}

    public String getName()        {return name;}
    public String getDescription() {return description;}

    public List<Bug> getBugs() {return bugs;}

    public void addBug(Bug bug) {
        bugs.add(bug);
    }
}
