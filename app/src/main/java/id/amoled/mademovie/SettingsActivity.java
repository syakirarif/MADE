package id.amoled.mademovie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import id.amoled.mademovie.scheduler.AlarmReceiver;
import id.amoled.mademovie.scheduler.MyPreferenceFragment;

/**
 * </> with <3 by SyakirArif
 * say no to plagiarism
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
