package id.amoled.mademovie.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_ID;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_MOVIE_ID;
import static id.amoled.mademovie.db.DatabaseContract.TB_FAVORITE;

/**
 * </> with <3 by SyakirArif
 * say no to plagiarism
 */
public class FavoriteHelper {

    private static final String TAG = "FavoriteHelper";

    private Context context;
    private DatabaseHelper databaseHelper;

    private SQLiteDatabase database;

    public FavoriteHelper(Context context){
        this.context = context;
    }

    public FavoriteHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        databaseHelper.close();
    }

    public Cursor queryProvider() {
        return database.query(
                TB_FAVORITE,
                null,
                null,
                null,
                null,
                null,
                COLUMN_ID + " DESC"
        );
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(
                TB_FAVORITE,
                null,
                COLUMN_ID + " = ?",
                new String[]{id},
                null,
                null,
                null
        );
    }

    public long insert(ContentValues values) {
        return database.insert(
                TB_FAVORITE,
                null,
                values
        );
    }

    public int delete(String movie_id) {

        Log.d(TAG, "movie_id: "+ movie_id);

        return database.delete(
                TB_FAVORITE,
                COLUMN_MOVIE_ID + " = ?",
                new String[]{movie_id}
        );
    }

}
