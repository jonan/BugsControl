package jp.bugscontrol.bugzilla;

import org.json.JSONArray;
import org.json.JSONObject;

public class Server extends jp.bugscontrol.server.Server {
    public interface Listener {
        void callback(String s);
    }

    public Server() {
        Listener l = new Listener() {
            @Override
            public void callback(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    loadProducts(object.getJSONObject("result").getString("ids"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        BugzillaTask task = new BugzillaTask("Product.get_accessible_products", l);
        task.execute();
    }

    void loadProducts(String product_ids) {
        Listener l = new Listener() {
            @Override
            public void callback(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray products_json = object.getJSONObject("result").getJSONArray("products");
                    for (int i=0; i<products_json.length(); ++i) {
                        try {
                            JSONObject p = products_json.getJSONObject(i);
                            products.add(new Product(p));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        BugzillaTask task = new BugzillaTask("Product.get", "\"ids\":" + product_ids, l);
        task.execute();
    }
}
