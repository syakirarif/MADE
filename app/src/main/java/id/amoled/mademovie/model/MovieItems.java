package id.amoled.mademovie.model;

import android.database.Cursor;

import org.json.JSONObject;

import static android.provider.BaseColumns._ID;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_BACKDROP;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_ID;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_OVERVIEW;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_POSTER;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_RATING;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_RELEASE_DATE;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_TITLE;
import static id.amoled.mademovie.db.DatabaseContract.getColumnInt;
import static id.amoled.mademovie.db.DatabaseContract.getColumnString;

public class MovieItems {

    private String title, original_title, poster_path, overview, release_date, vote_average, backdrop, movie_id;
    private int id;

    public MovieItems(JSONObject object) {

        try {
            String original_title = object.getString("original_title");
            String title = object.getString("title");
            String poster_path = object.getString("poster_path");
            String overview = object.getString("overview");
            String release_date = object.getString("release_date");
            String vote_avg = object.getString("vote_average");
            String backdrop = object.getString("backdrop_path");
            String movie_id = object.getString("id");

            this.original_title = original_title;
            this.poster_path = poster_path;
            this.overview = overview;
            this.release_date = release_date;
            this.vote_average = vote_avg;
            this.backdrop = backdrop;
            this.movie_id = movie_id;
            this.title = title;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMovieId() {
        return movie_id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public int getId() {
        return id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public MovieItems() {

    }

    public MovieItems(Cursor cursor) {
        this.id = getColumnInt(cursor, COLUMN_ID);
        this.title = getColumnString(cursor, COLUMN_TITLE);
        this.backdrop = getColumnString(cursor, COLUMN_BACKDROP);
        this.poster_path = getColumnString(cursor, COLUMN_POSTER);
        this.release_date = getColumnString(cursor, COLUMN_RELEASE_DATE);
        this.vote_average = getColumnString(cursor, COLUMN_RATING);
        this.overview = getColumnString(cursor, COLUMN_OVERVIEW);
    }

}
