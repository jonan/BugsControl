package jp.bugscontrol.bugzilla;

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
                    System.out.println(object.getJSONObject("result").getString("ids"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        BugzillaTask task = new BugzillaTask("Product.get_accessible_products", l);
        task.execute();
    }
}
