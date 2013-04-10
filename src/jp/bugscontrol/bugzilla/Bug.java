package jp.bugscontrol.bugzilla;

import org.json.JSONObject;


public class Bug extends jp.bugscontrol.server.Bug {
    public Bug(JSONObject json) {
        createFromJSON(json);
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
            summary = json.getString("summary");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
