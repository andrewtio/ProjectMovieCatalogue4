package com.andrew.associate.myfavoritemovies.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrew.associate.myfavoritemovies.R;
import com.andrew.associate.myfavoritemovies.database.MovieContract;
import com.andrew.associate.myfavoritemovies.entity.MovieResult;
import com.andrew.associate.myfavoritemovies.utils.DateFormat;
import com.andrew.associate.myfavoritemovies.utils.Utils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityDetailMovie extends AppCompatActivity {
    @BindView(R.id.detail_overview_tv)
    TextView tvOverview;
    @BindView(R.id.item_date_detail)
    TextView tvDate;
    @BindView(R.id.item_title_detail)
    TextView tvTitle;
    @BindView(R.id.movie_poster_detail)
    ImageView poster;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;
    String id, title;

    MovieResult movie;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.detail_movie));


        movie = getIntent().getParcelableExtra(Utils.MOVIE_DETAIL);
        updateImage(movie);

        id = movie.getId().toString();
        title = movie.getTitle();

        if (isFavorite(movie.getId().toString())) {
            if (floatingActionButton != null) {
                floatingActionButton.setImageResource(R.drawable.ic_star);
                Log.v("MovieDetail", "" + movie.getId());
            }
        }

        if (floatingActionButton != null) {
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    if (!isFavorite(movie.getId().toString())) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MovieContract.MovieColumns.MOVIE_ID, id);
                        contentValues.put(MovieContract.MovieColumns.MOVIE_TITLE, title);
                        getContentResolver().insert(MovieContract.CONTENT_URI, contentValues);
                        floatingActionButton.setImageResource(R.drawable.ic_star);
                        Snackbar.make(v, "Added to your Favorite", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        Uri uri = MovieContract.CONTENT_URI;
                        uri = uri.buildUpon().appendPath(id).build();
                        Log.v("MovieDetail", "" + uri);

                        getContentResolver().delete(uri, null, null);
                        floatingActionButton.setImageResource(R.drawable.ic_star_check);
                        Log.v("MovieDetail", uri.toString());
                        Snackbar.make(v, "Removed from your Favorite", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            });
        }

    }


    void updateImage(MovieResult movie) {
        getSupportActionBar().setTitle(movie.getTitle());
        Picasso.get()
                .load(Utils.BASE_POSTER_URL + movie.getPosterPath())
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_image)
                .into(poster);

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvDate.setText(getResources().getString(R.string.releaseDate,
                DateFormat.getDateDay(movie.getReleaseDate())));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isFavorite(String id) {
        String selection = " movie_id = ?";
        String[] selectionArgs = {id};
        String[] projection = {MovieContract.MovieColumns.MOVIE_ID};
        Uri uri = MovieContract.CONTENT_URI;
        uri = uri.buildUpon().appendPath(id).build();

        Cursor cursor = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            cursor = getContentResolver().query(uri, projection,
                    selection, selectionArgs, null, null);
        }

        boolean exists = false;
        if (cursor != null){
            exists = cursor.getCount() > 0;
        }
        if (cursor != null) {
            cursor.close();
        }
        return exists;

    }
}

