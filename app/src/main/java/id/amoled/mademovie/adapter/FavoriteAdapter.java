package id.amoled.mademovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.amoled.mademovie.MovieDetailActivity;
import id.amoled.mademovie.R;
import id.amoled.mademovie.scheduler.DateConverter;

import static id.amoled.mademovie.db.DatabaseContract.CONTENT_URI;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_ALTER_TITLE;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_BACKDROP;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_ID;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_MOVIE_ID;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_OVERVIEW;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_POSTER;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_RATING;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_RELEASE_DATE;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_TITLE;
import static id.amoled.mademovie.db.DatabaseContract.getColumnString;

/**
 * </> with <3 by SyakirArif
 * say no to plagiarism
 */
public class FavoriteAdapter extends CursorAdapter {

    //private Cursor cursor;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_desc)
    TextView tvDesc;

    @BindView(R.id.tv_rating)
    TextView tvRating;

    @BindView(R.id.tv_release_date)
    TextView tvDate;

    @BindView(R.id.img_poster)
    ImageView imgPoster;


    public FavoriteAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super(context, cursor, autoRequery);
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_movie, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        ButterKnife.bind(this, view);

        //this.cursor = cursor;
        final String title, alter_title, desc, rating, date, poster, backdrop, movie_id, column_id;

        if (cursor != null) {

            title = getColumnString(cursor, COLUMN_TITLE);
            desc = getColumnString(cursor, COLUMN_OVERVIEW);
            rating = getColumnString(cursor, COLUMN_RATING);
            date = getColumnString(cursor, COLUMN_RELEASE_DATE);
            poster = getColumnString(cursor, COLUMN_POSTER);
            backdrop = getColumnString(cursor, COLUMN_BACKDROP);
            movie_id = getColumnString(cursor, COLUMN_MOVIE_ID);
            column_id = getColumnString(cursor, COLUMN_ID);
            alter_title = getColumnString(cursor, COLUMN_ALTER_TITLE);

            tvTitle.setText(title);
            tvDesc.setText(desc);
            tvRating.setText(rating);
            tvDate.setText(DateConverter.getLongDate(date));

            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w185" + poster)
                    .apply(RequestOptions.centerCropTransform())
                    .into(imgPoster);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentDetail = new Intent(context, MovieDetailActivity.class);

                    intentDetail.putExtra("title", title);
                    intentDetail.putExtra("desc", desc);
                    intentDetail.putExtra("poster", poster);
                    intentDetail.putExtra("date", date);
                    intentDetail.putExtra("rating", rating);
                    intentDetail.putExtra("backdrop", backdrop);
                    intentDetail.putExtra("movie_id", movie_id);
                    intentDetail.putExtra("column_id", column_id);
                    intentDetail.putExtra("alter_title", alter_title);
                    intentDetail.putExtra("isFavoriteAdapter", true);
                    intentDetail.setData(Uri.parse(CONTENT_URI + "/" + column_id));
                    intentDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentDetail);
                }
            });
        }

    }
}
