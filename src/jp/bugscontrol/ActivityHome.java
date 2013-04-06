package jp.bugscontrol;

import jp.bugscontrol.bugzilla.BugzillaTask;

import com.actionbarsherlock.app.SherlockActivity;

import android.os.Bundle;

public class ActivityHome extends SherlockActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        (new BugzillaTask("Product.get_accessible_products")).execute();
    }
}
