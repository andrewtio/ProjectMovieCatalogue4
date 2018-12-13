package com.andrew.associate.projectmoviecatalogue3.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.andrew.associate.projectmoviecatalogue3.database.MovieContract;
import com.andrew.associate.projectmoviecatalogue3.database.MovieHelper;

import static com.andrew.associate.projectmoviecatalogue3.database.MovieContract.AUTHORITY;
import static com.andrew.associate.projectmoviecatalogue3.database.MovieContract.CONTENT_URI;

public class MovieProvider extends ContentProvider {

    private static final int MOVIE = 100;
    private static final int MOVIE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        // content://com.andrew.associate.projectmoviecatalogue3/movie
        sUriMatcher.addURI(AUTHORITY,
                MovieContract.MovieColumns.TABLE_MOVIE, MOVIE);

        // content://com.andrew.associate.projectmoviecatalogue3/movie/id
        sUriMatcher.addURI(AUTHORITY,
                MovieContract.MovieColumns.TABLE_MOVIE +
                        "/#", MOVIE_ID);
    }

    private MovieHelper movieHelper;

    @Override
    public boolean onCreate() {
        movieHelper = new MovieHelper(getContext());
        movieHelper.open();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] strings, String s,
                        String[] strings1, String s1) {
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        Log.v("MovieDetail", ""+match);
        Log.v("MovieDetail", ""+uri);
        Log.v("MovieDetail", ""+uri.getLastPathSegment());
        switch(match){
            case MOVIE:
                cursor = movieHelper.queryProvider();
                break;
            case MOVIE_ID:
                cursor = movieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null){
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        long added;

        switch (sUriMatcher.match(uri)){
            case MOVIE:
                added = movieHelper.insertProvider(contentValues);
                break;
            default:
                added = 0;
                break;
        }

        if (added > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        int updated;
        switch (sUriMatcher.match(uri)){
            case MOVIE_ID:
                updated = movieHelper.updateProvider(uri.getLastPathSegment(),contentValues);
                break;
            default:
                updated = 0;
                break;
        }

        if (updated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updated;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        int deleted;

        Log.v("MovieDetail", ""+uri);
        int match = sUriMatcher.match(uri);
        Log.v("MovieDetail", ""+match);
        switch (match){
            case MOVIE_ID:
                deleted = movieHelper.deleteProvider(uri.getLastPathSegment());
                Log.v("MovieDetail", ""+deleted);
                break;
            default:
                deleted = 0;
                break;
        }

        if (deleted > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleted;
    }
}
