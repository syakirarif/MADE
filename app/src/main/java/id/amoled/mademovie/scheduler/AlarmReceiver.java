package id.amoled.mademovie.scheduler;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.amoled.mademovie.MainActivity;
import id.amoled.mademovie.MovieDetailActivity;
import id.amoled.mademovie.R;
import id.amoled.mademovie.model.MovieItems;

import static android.support.constraint.Constraints.TAG;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String TYPE_DAILY = "DailyAlarm";
    public static final String TYPE_RELEASE = "ReleaseAlarm";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";
    public final static String NOTIFICATION_CHANNEL_ID = "11001";

    private final int NOTIF_ID_DAILY = 100;
    private final int NOTIF_ID_RELEASE = 101;

    private String movie_title, alter_title, overview, poster, backdrop, date, rating, movie_id;

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        int notifId = 2;

        String title = context.getResources().getString(R.string.app_name);

        if (type.equals(TYPE_DAILY)){
            notifId = NOTIF_ID_DAILY;

            showNotificationDaily(context, title, message, notifId);
        } else if (type.equals(TYPE_RELEASE)){
            notifId = NOTIF_ID_RELEASE;

            movie_title = intent.getStringExtra("title");
            overview = intent.getStringExtra("desc");
            poster = intent.getStringExtra("poster");
            backdrop = intent.getStringExtra("backdrop");
            date = intent.getStringExtra("date");
            rating = intent.getStringExtra("rating");
            movie_id = intent.getStringExtra("movie_id");
            alter_title = intent.getStringExtra("alter_title");

            String namaMovie = movie_title + " - " + alter_title;

            showNotificationRelease(context, "Today's release movie!", namaMovie, notifId);
        }
    }

    private void showNotificationDaily(Context context, String title, String message, int notifId) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(notifId, builder.build());
    }

    private void showNotificationRelease(Context context, String title, String message, int notifId) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, MovieDetailActivity.class);

        intent.putExtra("title", movie_title);
        intent.putExtra("desc", overview);
        intent.putExtra("poster", poster);
        intent.putExtra("backdrop", backdrop);
        intent.putExtra("date", date);
        intent.putExtra("rating", rating);
        intent.putExtra("movie_id", movie_id);
        intent.putExtra("alter_title", alter_title);
        intent.putExtra("isFavoriteAdapter", false);

        Log.d(TAG, "showNotifRelease: " + movie_id);
        Log.d(TAG, "showNotifRelease: " + movie_title);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(notifId, builder.build());
    }

    public void setReleaseAlarm(Context context, List<MovieItems> movies, String type) {
        int notificationDelay = 0;

        //Calendar timeAlarm = AlarmTimeUtils.time(UPCOMING_HOUR);
        for (MovieItems movie: movies) {
            cancelAlarm(context, TYPE_RELEASE);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(EXTRA_TYPE, type);

            intent.putExtra("title", movie.getOriginal_title());
            intent.putExtra("desc", movie.getOverview());
            intent.putExtra("poster", movie.getPoster_path());
            intent.putExtra("backdrop", movie.getBackdrop());
            intent.putExtra("date", movie.getRelease_date());
            intent.putExtra("rating", movie.getVote_average());
            intent.putExtra("movie_id", movie.getMovie_id());
            intent.putExtra("alter_title", movie.getTitle());

            Calendar calendar = Calendar.getInstance();
            ////////////// set waktu alarm berjalan setiap pukul 8 pagi //////////////
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            if (calendar.before(Calendar.getInstance())) calendar.add(Calendar.DATE, 1);

            int requestCode = NOTIF_ID_RELEASE;

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            setAlarm(alarmManager, calendar, notificationDelay, pendingIntent);

            requestCode++;
            notificationDelay += 5000;
        }
    }

    public void setDailyAlarm(Context context, String type, String message) {
        int notificationDelay = 0;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        ////////////// set waktu alarm berjalan setiap pukul 7 pagi //////////////
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) calendar.add(Calendar.DATE, 1);

        int requestCode = NOTIF_ID_DAILY;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        setAlarm(alarmManager, calendar, notificationDelay, pendingIntent);
    }

    private void setAlarm(AlarmManager alarmManager, Calendar calendar, int timeIncrement, PendingIntent intent) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.d(TAG, "setAlarm: ALARM SET FOR SDK < M");
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + timeIncrement,
                    AlarmManager.INTERVAL_DAY,
                    intent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "setAlarm: ALARM SET FOR SDK > M");
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis() + timeIncrement, intent);
        }
    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);

        int requestCode = 0;

        if (type.equals(TYPE_DAILY))
            requestCode = NOTIF_ID_DAILY;
        else if (type.equals(TYPE_RELEASE))
            requestCode = NOTIF_ID_RELEASE;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);

        alarmManager.cancel(pendingIntent);
    }


}
