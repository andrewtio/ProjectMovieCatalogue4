package com.andrew.associate.projectmoviecatalogue3.activity;


import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.andrew.associate.projectmoviecatalogue3.R;
import com.andrew.associate.projectmoviecatalogue3.notification.AlarmPreference;
import com.andrew.associate.projectmoviecatalogue3.notification.AlarmReceiver;
import com.andrew.associate.projectmoviecatalogue3.notification.MovieReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.andrew.associate.projectmoviecatalogue3.utils.Utils.KEY_FIELD_DAILY_REMINDER;
import static com.andrew.associate.projectmoviecatalogue3.utils.Utils.KEY_FIELD_UPCOMING_REMINDER;
import static com.andrew.associate.projectmoviecatalogue3.utils.Utils.KEY_HEADER_DAILY_REMINDER;
import static com.andrew.associate.projectmoviecatalogue3.utils.Utils.KEY_HEADER_UPCOMING_REMINDER;
import static com.andrew.associate.projectmoviecatalogue3.utils.Utils.LAYANAN_PENERIMA_PENGINGAT;
import static com.andrew.associate.projectmoviecatalogue3.utils.Utils.NONAKTIF_PENGINGAT_HARIAN;
import static com.andrew.associate.projectmoviecatalogue3.utils.Utils.NONAKTIF_PENGINGAT_RILIS;
import static com.andrew.associate.projectmoviecatalogue3.utils.Utils.PREFERENSI_TIPE_PENGINGAT;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnDailyReminderOn, btnDailyReminderOff, btnMovieReleaseReminderOn, btnMovieReleaseReminderOff;

    public SharedPreferences pengingatRilis, pengingatHarian;
    public SharedPreferences.Editor pengingatRilisEditor, pengingatHarianEditor;

    public AlarmReceiver alarmReceiver;
    public MovieReceiver movieReceiver;
    public AlarmPreference alarmPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("MovieNotificationManager");

        btnDailyReminderOn = findViewById(R.id.btn_daily_reminder_on);
        btnDailyReminderOff = findViewById(R.id.btn_daily_reminder_off);
        btnMovieReleaseReminderOn = findViewById(R.id.btn_movie_release_reminder_on);
        btnMovieReleaseReminderOff = findViewById(R.id.btn_movie_release_reminder_off);

        btnDailyReminderOn.setOnClickListener(this);
        btnDailyReminderOff.setOnClickListener(this);
        btnMovieReleaseReminderOn.setOnClickListener(this);
        btnMovieReleaseReminderOff.setOnClickListener(this);

        alarmPreference = new AlarmPreference(this);
        alarmReceiver = new AlarmReceiver();
        movieReceiver = new MovieReceiver();
        pengaturanPreferensi();

    }

    @Override
    public void onClick(View v) {
        pengingatRilisEditor = pengingatRilis.edit();
        pengingatHarianEditor = pengingatHarian.edit();

        if (v.getId() == R.id.btn_daily_reminder_on) {
            pengingatHarianEditor.apply();
            aktifkanPengingatHarian();

        }else if (v.getId() == R.id.btn_daily_reminder_off){
            pengingatHarianEditor.apply();
            nonaktifkanPengingatHarian();

        }else if (v.getId() == R.id.btn_movie_release_reminder_on){
            pengingatRilisEditor.apply();
            aktifkanPengingatRilis();

        }else if (v.getId() == R.id.btn_movie_release_reminder_off) {
            pengingatRilisEditor.apply();
            nonaktifkanPengingatRilis();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration configChange){
        super.onConfigurationChanged(configChange);
    }

    private void aktifkanPengingatRilis(){
        String waktu = "08:00";
        String pesan = getResources().getString(R.string.pesan_rilis_movie);
        alarmPreference.setWaktuRilis(waktu);
        alarmPreference.setPesanWaktuRilis(pesan);
        movieReceiver.setOneTimeAlarm(SettingActivity.this, PREFERENSI_TIPE_PENGINGAT, waktu, pesan);
    }

    private void nonaktifkanPengingatRilis(){
        movieReceiver.cancelAlarm(SettingActivity.this, NONAKTIF_PENGINGAT_RILIS);
    }

    private void aktifkanPengingatHarian(){
        String waktu = "07:00";
        String pesan = getResources().getString(R.string.repeatingReminder);
        alarmPreference.setWaktuPengingatHarian(waktu);
        alarmPreference.setPesanPengingatHarian(pesan);
        alarmReceiver.setOneTimeAlarm(SettingActivity.this, LAYANAN_PENERIMA_PENGINGAT, waktu, pesan);
    }

    private void nonaktifkanPengingatHarian(){
        alarmReceiver.cancelAlarm(SettingActivity.this, NONAKTIF_PENGINGAT_HARIAN);
    }

    private void pengaturanPreferensi(){
        pengingatRilis = getSharedPreferences(KEY_HEADER_UPCOMING_REMINDER, MODE_PRIVATE);
        pengingatHarian = getSharedPreferences(KEY_HEADER_DAILY_REMINDER, MODE_PRIVATE);
    }
}