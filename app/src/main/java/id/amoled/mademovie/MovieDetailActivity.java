package id.amoled.mademovie;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.amoled.mademovie.helper.DatabaseHelper;
import id.amoled.mademovie.helper.FavoriteHelper;
import id.amoled.mademovie.scheduler.DateConverter;

import static id.amoled.mademovie.db.DatabaseContract.CONTENT_URI;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_ALTER_TITLE;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_BACKDROP;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_MOVIE_ID;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_OVERVIEW;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_POSTER;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_RATING;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_RELEASE_DATE;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_TITLE;
import static id.amoled.mademovie.db.DatabaseContract.TB_FAVORITE;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = "MovieDetailActivity";

    private String title, alter_title, overview, poster, backdrop, date, rating, movie_id, column_id;

    private Boolean isFavorite = false;
    private Boolean isFavoriteAdapter = false;

    @BindView(R.id.tv_detail_title)
    TextView tvTitle;

    @BindView(R.id.tv_detail_desc)
    TextView tvDesc;

    @BindView(R.id.tv_detail_date)
    TextView tvDate;

    @BindView(R.id.tv_detail_rating)
    TextView tvRating;

    @BindView(R.id.tv_detail_alter_title)
    TextView tvTitleAlter;

    @BindView(R.id.img_detail_poster)
    ImageView imgPoster;

    @BindView(R.id.img_detail_backdrop)
    ImageView imgBackdrop;

    private Cursor cursor;

    private SQLiteDatabase database;

    private FavoriteHelper favoriteHelper;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        ButterKnife.bind(this);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        favoriteHelper = new FavoriteHelper(this);

        getDataIntentExtra();

        /////////// mengecek status favorit dengan 2 metode /////////////
        if (isFavoriteAdapter) {
            column_id = getIntent().getStringExtra("column_id"); //mengambil StringExtra utk column_id
            checkFavoriteByContentProvider();
        } else {
            checkFavoriteBySQL();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        this.menu = menu;
        setFavoriteIcon();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_favorite) {
            if (isFavorite) removeFavorite(movie_id);
            else saveFavorite();

            isFavorite = !isFavorite;
            setFavoriteIcon();
        }

        return super.onOptionsItemSelected(item);
    }

    public void getDataIntentExtra() {
        Intent intentMovie = getIntent();
        if (intentMovie.hasExtra("movie_id")) {

            title = getIntent().getStringExtra("title");
            overview = getIntent().getStringExtra("desc");
            poster = getIntent().getStringExtra("poster");
            backdrop = getIntent().getStringExtra("backdrop");
            date = getIntent().getStringExtra("date");
            rating = getIntent().getStringExtra("rating");
            movie_id = getIntent().getStringExtra("movie_id");
            alter_title = getIntent().getStringExtra("alter_title");
            isFavoriteAdapter = getIntent().getExtras().getBoolean("isFavoriteAdapter");

            Log.d(TAG, "idMovie: " + movie_id);
            Log.d(TAG, "titleMovie: " + title);

            tvTitle.setText(title);
            tvTitleAlter.setText(alter_title);
            tvDesc.setText(overview);
            tvRating.setText(rating);
            tvDate.setText(DateConverter.getLongDate(date));

            if (!backdrop.equals("null")) {
                Glide.with(MovieDetailActivity.this)
                        .load("http://image.tmdb.org/t/p/w780" + backdrop)
                        .into(imgBackdrop);
            } else {
                Glide.with(MovieDetailActivity.this)
                        .load(getResources().getDrawable(R.drawable.no_photo))
                        .into(imgBackdrop);
                //Log.d(TAG, "movieBackdrop: NULL" );
            }

            Glide.with(MovieDetailActivity.this)
                    .load("http://image.tmdb.org/t/p/w300" + poster)
                    .apply(RequestOptions.centerCropTransform())
                    .into(imgPoster);

        } else {
            Toast.makeText(MovieDetailActivity.this, "No API data", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkFavoriteBySQL() {

        cursor = database.query(TB_FAVORITE,
                null,
                COLUMN_MOVIE_ID + " = ?",
                new String[]{movie_id},
                null,
                null,
                null,
                null);

        if (cursor != null) {
            if (cursor.moveToFirst()) isFavorite = true;
            cursor.close();
        }

    }

    public void checkFavoriteByContentProvider() {
        favoriteHelper.open();

        cursor = getContentResolver().query(
                Uri.parse(CONTENT_URI + "/" + column_id),
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) isFavorite = true;
            cursor.close();
        }

    }

    public void removeFavorite(String movie_id) {

        favoriteHelper.open();
        favoriteHelper.delete(movie_id);

        Toast.makeText(this, "Film " + title + " dihapus dari Favorit", Toast.LENGTH_SHORT).show();
        favoriteHelper.close();

    }

    public void saveFavorite() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MOVIE_ID, movie_id);
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_BACKDROP, backdrop);
        cv.put(COLUMN_OVERVIEW, overview);
        cv.put(COLUMN_POSTER, poster);
        cv.put(COLUMN_RELEASE_DATE, date);
        cv.put(COLUMN_RATING, rating);
        cv.put(COLUMN_ALTER_TITLE, alter_title);

        Log.d(TAG, "movie_id: " + movie_id);
        Log.d(TAG, "title: " + title);
        Log.d(TAG, "backdrop: " + backdrop);
        Log.d(TAG, "overview: " + overview);
        Log.d(TAG, "date: " + date);
        Log.d(TAG, "rating: " + rating);
        Log.d(TAG, "poster: " + poster);
        Log.d(TAG, "alter: " + alter_title);

        getContentResolver().insert(CONTENT_URI, cv);
        Toast.makeText(this, "Film " + title + " ditambahkan ke Favorit", Toast.LENGTH_SHORT).show();
    }

    public void setFavoriteIcon() {
        if (isFavorite)
            menu.findItem(R.id.menu_favorite).setIcon(getResources().getDrawable(R.drawable.ic_bookmark_white_28dp));
            //menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_bookmark_white_28dp));
        else
            menu.findItem(R.id.menu_favorite).setIcon(getResources().getDrawable(R.drawable.ic_bookmark_border_white_28dp));
        //menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_bookmark_border_white_28dp));
    }

}
