package com.andrew.associate.projectmoviecatalogue3.utils;

import com.andrew.associate.projectmoviecatalogue3.BuildConfig;

public class Utils {
    public final static String DATE_FORMAT_DAY ="EEEE, MMM d,yyyy";
    public final static String BASE_URL = "http://api.themoviedb.org/3/";
    public final static String API_KEY = BuildConfig.MOVIE_API_KEY;
    public final static String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";
    public final static String MOVIE_DETAIL = "movie_detail";
    public final static String INTENT_SEARCH = "intent_search";
    public final static String TAG_INTENT = "tag";
    public final static String INTENT_DETAIL = "detail";
    public final static String PENGINGAT_WAKTU_RILIS = "PengingatHarian";
    public final static String PESAN_PENGINGAT_WAKTU_RILIS = "PesanPengingatHarian";
    public final static int NOTIFICATION_ID = 501;
    public final static String LAYANAN_PENERIMA_PENGINGAT = "layananPenerimaPengingat";
    public final static String NONAKTIF_PENGINGAT_RILIS = "nonaktifPengingatRilis";
    public final static String KEY_FIELD_DAILY_REMINDER = "checkedDaily";
    public final static String PREFERENSI_TIPE_PENGINGAT = "preferensiTipePengingat";
    public final static String NONAKTIF_PENGINGAT_HARIAN = "nonaktifPengingatHarian";
    public final static String KEY_FIELD_UPCOMING_REMINDER = "checkedUpcoming";
    public final static String KEY_HEADER_UPCOMING_REMINDER = "upcomingReminder";
    public final static String KEY_HEADER_DAILY_REMINDER = "dailyReminder";
    public final static String BACKDROP_URL_WIDGET = "http://image.tmdb.org/t/p/w500";

}
