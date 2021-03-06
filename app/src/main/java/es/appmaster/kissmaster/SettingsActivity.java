package es.appmaster.kissmaster;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

/**
 * Preferences activity
 *
 * @author manolovn
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
