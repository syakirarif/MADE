package id.amoled.mademovie.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import id.amoled.mademovie.R;

/**
 * </> with <3 by SyakirArif
 * say no to plagiarism
 */
public class AppPreferences {

    private String KEY_LANGUAGE = "en";
    private String KEY_REGION = "us";

    private SharedPreferences prefs;
    private Context context;

    public AppPreferences(Context context){
        String PREF_NAME = "movie_pref";
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setLanguage(String language){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_LANGUAGE, language);
        editor.apply();
    }

    public void setRegion(String region){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_REGION, region);
        editor.apply();
    }

    public void setFirstRun(Boolean input){
        SharedPreferences.Editor editor = prefs.edit();
        String key = context.getResources().getString(R.string.pref_movies);
        editor.putBoolean(key, input);
        editor.apply();
    }

    public Boolean getFirstRun(){
        String key = context.getResources().getString(R.string.pref_movies);
        return prefs.getBoolean(key, true);
    }

    public String getLanguage(){
        return prefs.getString(KEY_LANGUAGE, null);
    }

    public String getRegion(){
        return prefs.getString(KEY_REGION, null);
    }
}
