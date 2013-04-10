package jp.bugscontrol.server;

public abstract class Bug {
    protected int id;
    protected String summary;

    public Bug() {}

    abstract public void createFromString(String s);

    public int getId() {return id;}

    public String getSummary() {return summary;}
}
