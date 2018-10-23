package id.amoled.mademovie.asynctask;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;
import id.amoled.mademovie.BuildConfig;
import id.amoled.mademovie.model.MovieItems;

public class AsyncTaskMovieSearch extends AsyncTaskLoader<ArrayList<MovieItems>> {

    private ArrayList<MovieItems> list;

    private boolean hasResult;

    private URI uri;

    private final String filmDicari;

    public AsyncTaskMovieSearch(Context context, String judulFilm) {
        super(context);
        onContentChanged();
        this.filmDicari = judulFilm;
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

        try {
            uri = new URI(filmDicari.replace(" ", "%20"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String url = "https://api.themoviedb.org/3/search/movie?api_key=" +
                BuildConfig.API_KEY +
                "&language=en-US&query=" +
                uri +
                "&include_adult=false";

        client.get(url, new AsyncHttpResponseHandler() {

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
                    JSONArray Arrayresult = responseObject.getJSONArray("results");

                    for (int i = 0; i < Arrayresult.length(); i++) {
                        JSONObject movie = Arrayresult.getJSONObject(i);
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
