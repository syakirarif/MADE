package id.amoled.mademovie.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import java.util.concurrent.ExecutionException;
import id.amoled.mademovie.R;
import id.amoled.mademovie.model.MovieItems;
import static id.amoled.mademovie.db.DatabaseContract.CONTENT_URI;

/**
 * </> with <3 by SyakirArif
 * say no to plagiarism
 */

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private int mAppWidgetId;

    private Cursor cursor;

    public StackRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        cursor = mContext.getContentResolver().query(
                CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        MovieItems item = getItem(i);
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.stack_widget_item);

        Bitmap bitmap = null;
        String urlPoster = item.getPoster_path();
        try {


            //// revised: telah ditambahkan seleksi kondisi jika gambar NULL  //////
            if (!urlPoster.equals("null")) {
                bitmap = Glide.with(mContext)
                        .asBitmap()
                        .load("http://image.tmdb.org/t/p/w500" + item.getPoster_path())
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } else {
                bitmap = Glide.with(mContext)
                        .asBitmap()
                        .load(R.drawable.no_photo)
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
                //Log.d(TAG, "movieBackdrop: NULL" );
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        rv.setImageViewBitmap(R.id.img_stack_widget, bitmap);

        Bundle extras = new Bundle();
        extras.putInt(StackWidget.EXTRA_ITEM, i);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.img_stack_widget, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private MovieItems getItem(int position) {
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid!");
        }

        return new MovieItems(cursor);
    }
}
