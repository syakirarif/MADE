package id.amoled.mademovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        TextView tvTitle = findViewById(R.id.tv_detail_title);
        TextView tvDesc = findViewById(R.id.tv_detail_desc);
        TextView tvDate = findViewById(R.id.tv_detail_date);
        TextView tvRating = findViewById(R.id.tv_detail_rating);

        ImageView imgPoster = findViewById(R.id.img_detail_poster);
        ImageView imgBackdrop = findViewById(R.id.img_detail_backdrop);

        Intent intentMovie = getIntent();
        if (intentMovie.hasExtra("title")) {

            String title = getIntent().getStringExtra("title");
            String desc = getIntent().getStringExtra("desc");
            String poster = getIntent().getStringExtra("poster");
            String backdrop = getIntent().getStringExtra("backdrop");
            String date = getIntent().getStringExtra("date");
            String rating = getIntent().getStringExtra("rating");

            tvTitle.setText(title);
            tvDesc.setText(desc);
            tvRating.setText(rating);
            tvDate.setText(date);

            Glide.with(MovieDetailActivity.this)
                    .load("http://image.tmdb.org/t/p/w780" + backdrop)
                    .into(imgBackdrop);

            Glide.with(MovieDetailActivity.this)
                    .load("http://image.tmdb.org/t/p/w300" + poster)
                    .apply(RequestOptions.centerCropTransform())
                    .into(imgPoster);

        } else {

            Toast.makeText(MovieDetailActivity.this, "No API data", Toast.LENGTH_SHORT).show();

        }
    }
}
