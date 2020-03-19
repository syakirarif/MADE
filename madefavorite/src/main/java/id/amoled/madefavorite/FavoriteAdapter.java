package id.amoled.madefavorite;

import android.content.Context;
import android.database.Cursor;
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
import static id.amoled.madefavorite.DatabaseContract.MovieColumns.COLUMN_OVERVIEW;
import static id.amoled.madefavorite.DatabaseContract.MovieColumns.COLUMN_POSTER;
import static id.amoled.madefavorite.DatabaseContract.MovieColumns.COLUMN_RATING;
import static id.amoled.madefavorite.DatabaseContract.MovieColumns.COLUMN_RELEASE_DATE;
import static id.amoled.madefavorite.DatabaseContract.MovieColumns.COLUMN_TITLE;
import static id.amoled.madefavorite.DatabaseContract.getColumnString;

/**
 * </> with <3 by SyakirArif
 * say no to plagiarism
 */
public class FavoriteAdapter extends CursorAdapter {

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

        final String title, desc, rating, date, poster;

        if (cursor != null) {

            title = getColumnString(cursor, COLUMN_TITLE);
            desc = getColumnString(cursor, COLUMN_OVERVIEW);
            rating = getColumnString(cursor, COLUMN_RATING);
            date = getColumnString(cursor, COLUMN_RELEASE_DATE);
            poster = getColumnString(cursor, COLUMN_POSTER);

            tvTitle.setText(title);
            tvDesc.setText(desc);
            tvRating.setText(rating);
            tvDate.setText(date);

            Glide.with(context)
                    .load("http://image.tmdb.org/t/p/w185" + poster)
                    .apply(RequestOptions.centerCropTransform())
                    .into(imgPoster);
        }

    }
}
