package id.amoled.mademovie.tools;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import id.amoled.mademovie.MovieDetailActivity;
import id.amoled.mademovie.R;

public class MovieAdapterPoster extends RecyclerView.Adapter<MovieAdapterPoster.ViewHolder> {

    private final Context context;
    private ArrayList<MovieItems> listMovie;

    public MovieAdapterPoster(Context context) {
        this.context = context;
    }


    private ArrayList<MovieItems> getListMovie() {
        return listMovie;
    }

    public void setListMovie(ArrayList<MovieItems> listMovie) {
        this.listMovie = listMovie;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movie_poster, parent, false);

        return new ViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final String urlPoster = getListMovie().get(position).getPoster_path();

        Glide.with(this.context)
                .load("http://image.tmdb.org/t/p/w500" + urlPoster)
                .apply(RequestOptions.centerCropTransform())
                .into(holder.imgPoster);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {

        if (listMovie == null) return 0;
        return listMovie.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgPoster;

        private ViewHolder(View itemView) {
            super(itemView);

            imgPoster = itemView.findViewById(R.id.img_poster_movie);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {

                        Intent intentDetail = new Intent(context, MovieDetailActivity.class);

                        intentDetail.putExtra("title", listMovie.get(pos).getTitle());
                        intentDetail.putExtra("desc", listMovie.get(pos).getOverview());
                        intentDetail.putExtra("poster", listMovie.get(pos).getPoster_path());
                        intentDetail.putExtra("date", listMovie.get(pos).getRelease_date());
                        intentDetail.putExtra("rating", listMovie.get(pos).getVote_average());
                        intentDetail.putExtra("backdrop", listMovie.get(pos).getBackdrop());
                        intentDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentDetail);
                    }

                }
            });
        }
    }

    /*private View MyViewHolder(final int position, View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDetail = new Intent(context, MovieDetailActivity.class);

                intentDetail.putExtra("title", list.get(position).getTitle());
                intentDetail.putExtra("desc", list.get(position).getOverview());
                intentDetail.putExtra("poster", list.get(position).getPoster_path());
                intentDetail.putExtra("date", list.get(position).getRelease_date());
                intentDetail.putExtra("rating", list.get(position).getVote_average());
                intentDetail.putExtra("backdrop", list.get(position).getBackdrop());
                intentDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentDetail);
            }
        });

        return view;
    }*/
}
