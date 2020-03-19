package id.amoled.mademovie.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static id.amoled.mademovie.db.DatabaseContract.DATABASE_NAME;
import static id.amoled.mademovie.db.DatabaseContract.DATABASE_VERSION;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_ALTER_TITLE;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_BACKDROP;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_ID;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_MOVIE_ID;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_OVERVIEW;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_POSTER;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_RATING;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_RELEASE_DATE;
import static id.amoled.mademovie.db.DatabaseContract.MovieColumns.COLUMN_TITLE;
import static id.amoled.mademovie.db.DatabaseContract.TB_FAVORITE;

/**
 * </> with <3 by SyakirArif
 * say no to plagiarism
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_MOVIE = "create table " + TB_FAVORITE + " (" +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_MOVIE_ID + " text not null, " +
                COLUMN_TITLE + " text not null, " +
                COLUMN_BACKDROP + " text not null, " +
                COLUMN_POSTER + " text not null, " +
                COLUMN_RELEASE_DATE + " text not null, " +
                COLUMN_RATING + " text not null, " +
                COLUMN_OVERVIEW + " text not null, " +
                COLUMN_ALTER_TITLE + " text not null " +
                ");";

        db.execSQL(CREATE_TABLE_MOVIE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_FAVORITE);
        onCreate(db);
    }
}
