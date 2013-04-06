package jp.bugscontrol.bugzilla;

import org.json.JSONObject;

public class Product extends jp.bugscontrol.server.Product {
    public Product(JSONObject json) {
        createFromJSON(json);
        System.out.println("[" + id + "] " + name + ": " + description);
    }

    @Override
    public void createFromString(String s) {
        try {
            createFromJSON(new JSONObject(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createFromJSON(JSONObject json) {
        try {
            id = json.getInt("id");
            name = json.getString("name");
            description = json.getString("description");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
