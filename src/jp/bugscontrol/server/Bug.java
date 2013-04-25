package jp.bugscontrol.server;

public abstract class Bug {
    protected int id;
    protected boolean open;
    protected String summary, priority;
    protected String reporter, assignee;

    public Bug() {}

    abstract public void createFromString(String s);

    public int getId() {return id;}

    public boolean isOpen() {return open;}

    public String getSummary()  {return summary;}
    public String getPriority() {return priority;}
    public String getReporter() {return reporter;}
    public String getAssignee() {return assignee;}
}
