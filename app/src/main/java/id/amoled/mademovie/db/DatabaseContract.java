package id.amoled.mademovie.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * </> with <3 by SyakirArif
 * say no to plagiarism
 */
public class DatabaseContract {

    public static int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "movie.db";

    public static String TB_FAVORITE = "tb_favorite";

    public static final class MovieColumns implements BaseColumns {

        public static String COLUMN_ID = "_id";
        public static String COLUMN_MOVIE_ID = "movie_id";
        public static String COLUMN_TITLE = "title";
        public static String COLUMN_BACKDROP = "backdrop";
        public static String COLUMN_POSTER = "poster";
        public static String COLUMN_RELEASE_DATE = "release_date";
        public static String COLUMN_RATING = "rating";
        public static String COLUMN_OVERVIEW = "overview";
        public static String COLUMN_ALTER_TITLE = "alter_title";

    }

    public static final String AUTHORITY = "id.amoled.mademovie";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TB_FAVORITE)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }
}
