package id.amoled.mademovie.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.amoled.mademovie.MainActivity;
import id.amoled.mademovie.MovieDetailActivity;
import id.amoled.mademovie.R;
import id.amoled.mademovie.adapter.FavoriteAdapter;
import static id.amoled.mademovie.db.DatabaseContract.CONTENT_URI;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_MOVIE_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private FavoriteAdapter adapter;

    @BindView(R.id.lv_favorite)
    ListView listView;

    @BindView(R.id.pb_favorite)
    ProgressBar progressBar;

    private final int LOAD_FAVORITE_ID = 110;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        ButterKnife.bind(this, view);

        progressBar.setVisibility(View.VISIBLE);

        adapter = new FavoriteAdapter(getContext(), null, true);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        getLoaderManager().initLoader(LOAD_FAVORITE_ID, null, this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(LOAD_FAVORITE_ID, null, this);

        if ((MainActivity) getActivity() != null)
            ((MainActivity) getActivity()).setActionBar(getResources().getString(R.string.favorite));
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getContext(), CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        adapter.swapCursor(data);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Cursor cursor = (Cursor) adapter.getItem(position);

        String cursor_id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_ID));

        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra("movie_id", cursor_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
