package id.amoled.mademovie.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import id.amoled.mademovie.MainActivity;
import id.amoled.mademovie.R;
import id.amoled.mademovie.tools.AsyncTaskMovieSearch;
import id.amoled.mademovie.tools.MovieAdapter;
import id.amoled.mademovie.tools.MovieItems;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieItems>> {

    private MovieAdapter adapter;

    private ProgressBar progressBar;

    private static final String TAG = "SearchFragment";

    private static String EXTRAS_FILM = "";

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        RecyclerView rvMovies = view.findViewById(R.id.rv_list_movie_search);

        progressBar = view.findViewById(R.id.pb_search);
        progressBar.setVisibility(View.VISIBLE);

        if (getArguments() != null) {
            EXTRAS_FILM = getArguments().getString("EXTRAS_FILM");
        }

        adapter = new MovieAdapter(getActivity());
        rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMovies.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        getLoaderManager().initLoader(0, null, this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if ((MainActivity) getActivity() != null)
            ((MainActivity) getActivity()).setActionBar(getResources().getString(R.string.search));
    }

    @NonNull
    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(TAG, "onCreateLoader: " + EXTRAS_FILM);
        return new AsyncTaskMovieSearch(getContext(), EXTRAS_FILM);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> data) {

        adapter.setData(data);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<MovieItems>> loader) {

        adapter.setData(null);
        progressBar.setVisibility(View.GONE);

    }

}
