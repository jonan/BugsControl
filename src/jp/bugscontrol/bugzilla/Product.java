package jp.bugscontrol.bugzilla;

import org.json.JSONObject;

public class Product extends jp.bugscontrol.server.Product {
    @Override
    public void createFromString(String s) {
        try {
            JSONObject object = new JSONObject(s);
            id = object.getInt("id");
            name = object.getString("name");
            description = object.getString("description");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
