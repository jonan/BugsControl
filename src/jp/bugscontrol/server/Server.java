package jp.bugscontrol.server;

import java.util.ArrayList;
import java.util.List;

public abstract class Server {
    protected List<Product> products;

    public Server() {
        products = new ArrayList<Product>();
    }
}
