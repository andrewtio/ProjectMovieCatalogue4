package com.andrew.associate.projectmoviecatalogue3.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.andrew.associate.projectmoviecatalogue3.BuildConfig;
import com.andrew.associate.projectmoviecatalogue3.R;
import com.andrew.associate.projectmoviecatalogue3.activity.MainActivity;
import com.andrew.associate.projectmoviecatalogue3.entity.Movie;
import com.andrew.associate.projectmoviecatalogue3.entity.MovieResult;
import com.andrew.associate.projectmoviecatalogue3.rest.MovieClient;
import com.andrew.associate.projectmoviecatalogue3.rest.MovieInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.andrew.associate.projectmoviecatalogue3.utils.Utils.BASE_URL;
import static com.andrew.associate.projectmoviecatalogue3.utils.Utils.NOTIFICATION_ID;

public class MovieReceiver extends BroadcastReceiver {
    public List<MovieResult> movieResultList = new ArrayList<>();

    public static final String TYPE_ONE_TIME = "OneTimeAlarm";
    public static final String TYPE_REPEATING = "Repeating Alarm";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";
    private final int NOTIF_ID_ALARM = 100;
    private final int NOTIF_ID_CANCEL = 101;

    public MovieReceiver(){

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(final Context context, Intent intent) {
        MovieInterface movieInterface = MovieClient.getClient(BASE_URL).create(MovieInterface.class);
        Call<Movie> Calling = movieInterface.getUpcomingMovie(BuildConfig.MOVIE_API_KEY);
        Calling.enqueue(new Callback<Movie>(){

            @Override
            public void onResponse(Call<Movie> calling, Response<Movie> responsing) {
                movieResultList = responsing.body().getResults();
                List<MovieResult> entities = responsing.body().getResults();
                int paging = new Random().nextInt(entities.size());
                MovieResult entity = entities.get(paging);
                int popUpId = 503;
                String banner = entities.get(paging).getTitle();
                String letter = entities.get(paging).getOverview();
                showAlarmNotification(context, banner, letter, popUpId);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.d("getReleaseMovie", "Failed to Fetch: " + t.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showAlarmNotification(Context context, String title, String message, int notifId){
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.alarm)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setAutoCancel(true)
                .setSound(alarmSound);
        notificationManagerCompat.notify(notifId, builder.build());
    }

    public void setOneTimeAlarm(Context context, String tipe, String waktu, String pesan){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MovieReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, pesan);
        intent.putExtra(EXTRA_TYPE, tipe);
        String timeArray[] = waktu.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND,0);

        int requestCode = NOTIF_ID_ALARM;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(context, "Alarm Rilis Film Dipasang", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context, String type){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MovieReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_ONE_TIME) ? NOTIF_ID_ALARM : NOTIF_ID_CANCEL;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, 0);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(context, "Alarm Rilis Film dibatalkan", Toast.LENGTH_SHORT).show();
    }
}