package id.amoled.mademovie.fragment;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.amoled.mademovie.MainActivity;
import id.amoled.mademovie.R;
import id.amoled.mademovie.adapter.MovieAdapterPoster;
import id.amoled.mademovie.asynctask.AsyncTaskMoviePlaying;
import id.amoled.mademovie.model.MovieItems;

public class NowPlayingFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieItems>> {

    @BindView(R.id.rv_list_movie_showing)
    RecyclerView rvMovies;

    @BindView(R.id.pb_playing)
    ProgressBar progressBar;

    private MovieAdapterPoster adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);

        ButterKnife.bind(this, view);

        adapter = new MovieAdapterPoster(getContext());

        progressBar.setVisibility(View.VISIBLE);

        showMovies();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if ((MainActivity) getActivity() != null)
            ((MainActivity) getActivity()).setActionBar(getResources().getString(R.string.now_playing));
    }

    private void showMovies() {

        adapter = new MovieAdapterPoster(getActivity());
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvMovies.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            rvMovies.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        }

        rvMovies.setItemAnimator(new DefaultItemAnimator());
        rvMovies.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        getLoaderManager().initLoader(0, null, this);

    }

    @NonNull
    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskMoviePlaying(getContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> data) {

        adapter.setListMovie(data);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<MovieItems>> loader) {

        adapter.setListMovie(null);
        progressBar.setVisibility(View.GONE);

    }
}
