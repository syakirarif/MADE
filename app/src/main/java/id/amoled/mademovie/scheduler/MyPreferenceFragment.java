package id.amoled.mademovie.scheduler;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import id.amoled.mademovie.BuildConfig;
import id.amoled.mademovie.MainActivity;
import id.amoled.mademovie.R;
import id.amoled.mademovie.SettingsActivity;
import id.amoled.mademovie.model.MovieItems;
import id.amoled.mademovie.pref.AppPreferences;

import static android.support.constraint.Constraints.TAG;
import static id.amoled.mademovie.BuildConfig.API_KEY;

/**
 * </> with <3 by SyakirArif
 * say no to plagiarism
 */
public class MyPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private AlarmReceiver alarmReceiver = new AlarmReceiver();
    private FirebaseJobDispatcher mDispatcher;
    private AppPreferences prefs;

    private Resources resources;

    private String DISPATCHER_TAG = "movie_dispatcher";

    @BindString(R.string.key_reminder_daily)
    String reminder_daily;

    @BindString(R.string.key_reminder_playing)
    String reminder_playing;

    @BindString(R.string.pref_language_key)
    String pref_language;

    @BindString(R.string.pref_region_key)
    String pref_region;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        ButterKnife.bind(this, getActivity());
        prefs = new AppPreferences(getActivity());
        resources = getActivity().getResources();

        findPreference(reminder_daily).setOnPreferenceChangeListener(this);
        findPreference(reminder_playing).setOnPreferenceChangeListener(this);

        mDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getActivity()));

        ListPreference langPref = (ListPreference) findPreference(pref_language);
        ListPreference regionPref = (ListPreference) findPreference(pref_region);

        switch (prefs.getLanguage()) {
            case "en":
                langPref.setValueIndex(0);
                break;
            case "in":
                langPref.setValueIndex(1);
                break;
            case "ja":
                langPref.setValueIndex(2);
                break;
        }

        langPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                prefs.setLanguage((String) newValue);
                Log.d(TAG, "onPreferenceChange: " + prefs.getLanguage());

                String[] languageAlias = resources.getStringArray(R.array.language_alias);
                for (int i = 0; i < languageAlias.length; i++) {
                    if (languageAlias[i].equals(prefs.getLanguage())) {
                        //mainActivity.updateLocale();
                        Locale myLocale = new Locale(prefs.getLanguage());
                        Locale.setDefault(myLocale);
                        Configuration config = new Configuration();
                        config.locale = myLocale;
                        getActivity().getResources().updateConfiguration(config, getActivity().getResources().getDisplayMetrics());//Update the config

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getActivity().finish();
                        startActivity(intent);
                        break;
                    }
                }

                return false;
            }
        });

        switch (prefs.getRegion()) {
            case "us":
                regionPref.setValueIndex(0);
                break;
            case "id":
                regionPref.setValueIndex(1);
                break;
            case "jp":
                regionPref.setValueIndex(2);
                break;
            case "kr":
                regionPref.setValueIndex(3);
                break;
            case "cn":
                regionPref.setValueIndex(4);
                break;
            case "in":
                regionPref.setValueIndex(5);
                break;
        }

        regionPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                prefs.setRegion((String) newValue);

                String[] regAlias = resources.getStringArray(R.array.region_alias);
                for (int i = 0; i < regAlias.length; i++) {
                    if (regAlias[i].equals(prefs.getRegion())) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getActivity().finish();
                        startActivity(intent);
                        break;
                    }
                }
                return false;
            }
        });

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object object) {
        String key = preference.getKey();

        boolean isOn = (boolean) object;

        //////////// setting untuk daily alarm ////////////////////

        if (key.equals(reminder_daily)) {
            if (isOn) {
                alarmReceiver.setDailyAlarm(getActivity(), AlarmReceiver.TYPE_DAILY, getString(R.string.message_alarm_daily_reminder));
                Toast.makeText(getActivity(), getString(R.string.label_activated), Toast.LENGTH_SHORT).show();
            } else {
                alarmReceiver.cancelAlarm(getActivity(), AlarmReceiver.TYPE_DAILY);
                Toast.makeText(getActivity(), getString(R.string.label_deactivated), Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        //////////// setting untuk alarm release movie ////////////////////

        if (key.equals(reminder_playing)) {
            if (isOn) {
                setReleaseAlarm();
                Toast.makeText(getActivity(), getString(R.string.label_activated_release), Toast.LENGTH_SHORT).show();
            } else {
                alarmReceiver.cancelAlarm(getActivity(), AlarmReceiver.TYPE_RELEASE);
                Toast.makeText(getActivity(), getString(R.string.label_deactivated), Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

    ///////////////// mengambil data movie yang akan tayang /////////////////////////

    private void setReleaseAlarm(){

        final List<MovieItems> movieItemses = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date_now = new Date();
        final String dateNow = dateFormat.format(date_now);

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=" +
                BuildConfig.API_KEY + "&language=en-US&region=" + prefs.getRegion();
        Log.d(TAG, "URL: " + url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray arrayResult = jsonObject.getJSONArray("results");

                    for (int i = 0; i < arrayResult.length(); i++) {

                        JSONObject movie = arrayResult.getJSONObject(i);

                        MovieItems movieItems = new MovieItems(movie);
                        Log.d(TAG, "getReleaseDate: " + movieItems.getRelease_date());
                        Log.d(TAG, "dateNow: " + dateNow);

                        ////////////// seleksi movie yang tayang di hari ini ///////////
                        if (movieItems.getRelease_date().equals(dateNow)) {
                            movieItemses.add(movieItems);
                        }

                        Log.d("movieReleaseToday:", " "+movieItemses.size());

                    }

                    alarmReceiver.setReleaseAlarm(getActivity(), movieItemses, AlarmReceiver.TYPE_RELEASE);

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "Something went wrong while getting today release movie", Toast.LENGTH_LONG).show();
            }
        });
    }
}

