package id.amoled.mademovie.asynctask;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import id.amoled.mademovie.BuildConfig;
import id.amoled.mademovie.model.MovieItems;
import id.amoled.mademovie.pref.AppPreferences;

public class AsyncTaskMovieUpcoming extends AsyncTaskLoader<ArrayList<MovieItems>> {

    private ArrayList<MovieItems> list;

    private boolean hasResult;

    private URI uri;

    private AppPreferences prefs;


    public AsyncTaskMovieUpcoming(Context context) {
        super(context);
        prefs = new AppPreferences(context);
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged()) {
            forceLoad();
        } else if (hasResult) {
            deliverResult(list);
        }
    }

    @Override
    public ArrayList<MovieItems> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();

        final ArrayList<MovieItems> movieItemses = new ArrayList<>();

        String region = prefs.getRegion();

        String urlFilmNowShowing = "https://api.themoviedb.org/3/movie/upcoming?api_key=" +
                BuildConfig.API_KEY + "&language=en-US&region=" + region;

        client.get(urlFilmNowShowing, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray arrayResult = responseObject.getJSONArray("results");

                    for (int i = 0; i < arrayResult.length(); i++) {

                        JSONObject movie = arrayResult.getJSONObject(i);

                        MovieItems movieItems = new MovieItems(movie);
                        movieItemses.add(movieItems);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        return movieItemses;
    }

    @Override
    public void deliverResult(ArrayList<MovieItems> data) {
        list = data;
        hasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (hasResult) {
            list = null;
            hasResult = false;
        }
    }

}
