package id.amoled.mademovie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.amoled.mademovie.fragment.FavoriteFragment;
import id.amoled.mademovie.fragment.NowPlayingFragment;
import id.amoled.mademovie.fragment.SearchFragment;
import id.amoled.mademovie.fragment.UpcomingFragment;
import id.amoled.mademovie.pref.AppPreferences;

import android.content.res.Configuration;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private ActionBarDrawerToggle toggle;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private boolean doubleBackToExitPressedOnce = false;

    //public static String lang = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        AppPreferences pref = new AppPreferences(this);

        setSupportActionBar(toolbar);

        Boolean firstRun = pref.getFirstRun();

        if (firstRun) {
            pref.setLanguage("in");
            pref.setRegion("id");
            pref.setFirstRun(false);

            setLocale(pref.getLanguage());
        }

        String lang = pref.getLanguage();
        setLocale(lang);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            Fragment currentFragment = new NowPlayingFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, currentFragment)
                    .commit();
        }

    }

    public void setActionBar(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please tap TWICE to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        //drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawer.removeDrawerListener(toggle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();

        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (TextUtils.isEmpty(query))
                    Toast.makeText(MainActivity.this, "Query empty", Toast.LENGTH_SHORT).show();

                Log.d(TAG, "onQueryTextSubmit: " + query);

                Fragment searchFragment = new SearchFragment();
                FragmentManager fM = getSupportFragmentManager();
                FragmentTransaction fT = fM.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("EXTRAS_FILM", query);

                searchFragment.setArguments(bundle);
                fT.replace(R.id.frame_container, searchFragment);
                fT.commit();

                hideKeyboard(MainActivity.this);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.langEN) {
            //lang = "en";
            pref.setLanguage("en");
            pref.setRegion("us");
            updateLocale();
            return true;
        } else if (id == R.id.langID) {
            //lang = "in";
            pref.setLanguage("in");
            pref.setRegion("id");
            updateLocale();
            return true;

        } else if (id == R.id.langJP) {
            //lang = "ja";
            pref.setLanguage("ja");
            pref.setRegion("jp");
            updateLocale();
            return true;

        }*/

        if (id == R.id.menu_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_now_playing) {
            fragment = new NowPlayingFragment();
        } else if (id == R.id.nav_upcoming) {
            fragment = new UpcomingFragment();
        } else if (id == R.id.nav_favorite) {
            fragment = new FavoriteFragment();
        }

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        View view = activity.getCurrentFocus();

        if (view == null) {
            view = new View(activity);
        }

        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setLocale(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(lang);
        //saveLocale(lang);//Save the selected locale
        Locale.setDefault(myLocale);//set new locale as default
        Configuration config = new Configuration();//get Configuration
        config.locale = myLocale;//set config locale as selected locale
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());//Update the config
        //updateTexts();//Update texts according to locale
    }

    /*public void updateLocale() {
        lang = pref.getLanguage();

        setLocale(lang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }*/
}
