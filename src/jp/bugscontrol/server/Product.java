package jp.bugscontrol.server;

public abstract class Product {
    protected int id;
    protected String name, description;

    public Product() {}

    abstract public void createFromString(String s);

    public int getId() {return id;}

    public String getName()        {return name;}
    public String getDescription() {return description;}
}
