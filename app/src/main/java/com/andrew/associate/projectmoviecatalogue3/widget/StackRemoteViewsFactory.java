package com.andrew.associate.projectmoviecatalogue3.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.andrew.associate.projectmoviecatalogue3.R;
import com.andrew.associate.projectmoviecatalogue3.entity.MovieResult;
import com.andrew.associate.projectmoviecatalogue3.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.andrew.associate.projectmoviecatalogue3.database.MovieContract.CONTENT_URI;

public class StackRemoteViewsFactory implements
        RemoteViewsService.RemoteViewsFactory {

    private Cursor cursor;
    MovieResult movieResult;
    private List<Bitmap> mmWidgetItems = new ArrayList<>();
    private Context mContext;
    int mAppWidgetId;

    private ArrayList<MovieResult> mWidgetItems = new ArrayList<>();


    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        cursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        cursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        if (cursor.moveToPosition(position)){
            movieResult = new MovieResult(cursor);
            Bitmap bmp;
            try {
                bmp = Glide.with(mContext)
                        .asBitmap()
                        .load(Utils.BASE_POSTER_URL + movieResult.getPosterPath())
                        .apply(new RequestOptions().fitCenter())
                        .submit()
                        .get();
                rv.setImageViewBitmap(R.id.imageView, bmp);
                rv.setTextViewText(R.id.movie_title, movieResult.getTitle());
            } catch (InterruptedException | ExecutionException e) {
                Log.d("Widget Load Error", "error");
            }
        }

        Bundle extras = new Bundle();
        extras.putInt(ImageBannerWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
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
}