package id.amoled.mademovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;
import id.amoled.mademovie.MovieDetailActivity;
import id.amoled.mademovie.R;
import id.amoled.mademovie.model.MovieItems;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<MovieItems> listMovie = new ArrayList<>();

    public MovieAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<MovieItems> getListMovie() {
        return listMovie;
    }

    public void setData(ArrayList<MovieItems> items) {
        listMovie = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemRow = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.layout_movie, parent, false);

        return new ViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {

        final String judul = getListMovie().get(position).getTitle();
        final String desc = getListMovie().get(position).getOverview();
        final String urlPoster = getListMovie().get(position).getPoster_path();

        if (judul.length() > 35)
            holder.tvTitle.setText(judul.substring(0, 34) + "...");
        else
            holder.tvTitle.setText(getListMovie().get(position).getTitle());

        if (desc.length() > 140)
            holder.tvDesc.setText(desc.substring(0, 139) + "...");
        else
            holder.tvDesc.setText(getListMovie().get(position).getOverview());

        holder.tvDate.setText(getListMovie().get(position).getRelease_date());
        holder.tvRating.setText(getListMovie().get(position).getVote_average());

        Glide.with(this.context)
                .load("http://image.tmdb.org/t/p/w185" + urlPoster)
                .apply(RequestOptions.centerCropTransform())
                .into(holder.imgPoster);

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
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
        final TextView tvTitle, tvDesc, tvRating, tvDate;
        final ImageView imgPoster;

        private ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvDate = itemView.findViewById(R.id.tv_release_date);

            imgPoster = itemView.findViewById(R.id.img_poster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {

                        Intent intentDetail = new Intent(context, MovieDetailActivity.class);
                        intentDetail.putExtra("title", listMovie.get(pos).getOriginal_title());
                        intentDetail.putExtra("desc", listMovie.get(pos).getOverview());
                        intentDetail.putExtra("poster", listMovie.get(pos).getPoster_path());
                        intentDetail.putExtra("date", listMovie.get(pos).getRelease_date());
                        intentDetail.putExtra("rating", listMovie.get(pos).getVote_average());
                        intentDetail.putExtra("backdrop", listMovie.get(pos).getBackdrop());
                        intentDetail.putExtra("movie_id", listMovie.get(pos).getMovieId());
                        intentDetail.putExtra("alter_title", listMovie.get(pos).getTitle());
                        intentDetail.putExtra("isFavoriteAdapter", false);
                        intentDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentDetail);
                    }
                }
            });
        }
    }
}
