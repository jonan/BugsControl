package jp.bugscontrol.bugzilla;

import org.json.JSONArray;
import org.json.JSONObject;

public class Server extends jp.bugscontrol.server.Server {
    public interface Listener {
        void callback(String s);
    }

    public Server(String name, String url) {
        super(name, url);
    }

    public Server(jp.bugscontrol.db.Server db_server) {
        super(db_server);
    }

    @Override
    protected void loadProducts() {
        // Get all the products' ids and pass it to loadProductsFromIds()
        Listener l = new Listener() {
            @Override
            public void callback(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    loadProductsFromIds(object.getJSONObject("result").getString("ids"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        BugzillaTask task = new BugzillaTask(this, "Product.get_accessible_products", l);
        task.execute();
    }

    @Override
    protected void loadBugsForProduct(final jp.bugscontrol.server.Product p) {
        Listener l = new Listener() {
            @Override
            public void callback(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray bugs = object.getJSONObject("result").getJSONArray("bugs");
                    p.getBugs().clear();
                    for (int i=0; i < bugs.length(); ++i) {
                        p.addBug(new Bug(bugs.getJSONObject(i)));
                    }
                    bugsListUpdated();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        BugzillaTask task = new BugzillaTask(this, "Bug.search", "\"product\":\"" + p.getName() + "\"", l);
        task.execute();
    }

    void loadProductsFromIds(String product_ids) {
        Listener l = new Listener() {
            @Override
            public void callback(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray products_json = object.getJSONObject("result").getJSONArray("products");
                    products.clear();
                    for (int i=0; i<products_json.length(); ++i) {
                        JSONObject p = products_json.getJSONObject(i);
                        products.add(new Product(p));
                    }
                    productsListUpdated();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        BugzillaTask task = new BugzillaTask(this, "Product.get", "\"ids\":" + product_ids, l);
        task.execute();
    }
}
