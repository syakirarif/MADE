package id.amoled.mademovie.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * </> with <3 by SyakirArif
 * say no to plagiarism
 */

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}