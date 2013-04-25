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
            open = json.getBoolean("is_open");
            summary = json.getString("summary");
            priority = json.getString("priority");
            reporter = json.getString("creator");
            assignee = json.getString("assigned_to");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
