package com.andrew.associate.projectmoviecatalogue3.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrew.associate.projectmoviecatalogue3.R;
import com.andrew.associate.projectmoviecatalogue3.adapter.MovieAdapter;
import com.andrew.associate.projectmoviecatalogue3.database.MovieContract;
import com.andrew.associate.projectmoviecatalogue3.entity.MovieResult;
import com.andrew.associate.projectmoviecatalogue3.utils.DateFormat;
import com.andrew.associate.projectmoviecatalogue3.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ActivityDetailMovie extends AppCompatActivity {

    TextView tvOverview;
    TextView tvDate;
    TextView tvTitle;
    ImageView poster;
    Toolbar toolbar;
    Button tombolAddFavorit, tombolHapusFavorit;
    String id, title, image;
    MovieAdapter movieAdapter;

    MovieResult movieResult;

    private boolean apakahfavorit;
    private static final String TANDA = ActivityDetailMovie.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        tvOverview = findViewById(R.id.detail_overview_tv);
        tvDate = findViewById(R.id.item_date_detail);
        tvTitle = findViewById(R.id.item_title_detail);
        poster = findViewById(R.id.movie_poster_detail);
        toolbar = findViewById(R.id.toolbar);
        tombolAddFavorit = findViewById(R.id.tombol_tambah_ke_favorit);
        tombolHapusFavorit = findViewById(R.id.tombol_hapus_dari_favorit);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.detail_movie));


        movieResult = getIntent().getParcelableExtra(Utils.MOVIE_DETAIL);
        updateImage(movieResult);
        id = movieResult.getId();
        title = movieResult.getTitle();
        image = movieResult.getPosterPath();

        if (apakahFavorit(id)){
            apakahfavorit = true;
            tombolHapusFavorit.setVisibility(View.VISIBLE);
            tombolAddFavorit.setVisibility(View.GONE);
        }

//        if (floatingActionButton != null) {
//            floatingActionButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!isFavorite(movieResult.getId())) {
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put(MovieContract.MovieColumns.MOVIE_ID, id);
//                        contentValues.put(MovieContract.MovieColumns.MOVIE_TITLE, title);
//                        contentValues.put(MovieContract.MovieColumns.MOVIE_IMAGE, image);
//                        getContentResolver().insert(MovieContract.CONTENT_URI, contentValues);
//                        floatingActionButton.setImageResource(R.drawable.ic_star);
//                        Snackbar.make(v, "Added to your Favorite", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
//                    } else {
//                        Uri uri = MovieContract.CONTENT_URI;
//                        uri = uri.buildUpon().appendPath(id).build();
//                        Log.v("MovieDetail", "" + uri);
//
//                        getContentResolver().delete(uri, null, null);
//                        floatingActionButton.setImageResource(R.drawable.ic_star_check);
//                        Log.v("MovieDetail", uri.toString());
//                        Snackbar.make(v, "Removed from your Favorite", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
//                    }
//                }
//            });
//        }

    }

    private void simpanDalamFavorit(){
        if(apakahfavorit){
            ContentValues cV = new ContentValues();
            cV.put(MovieContract.MovieColumns.MOVIE_ID, id);
            cV.put(MovieContract.MovieColumns.MOVIE_TITLE, title);
            cV.put(MovieContract.MovieColumns.MOVIE_IMAGE, image);
            Uri u = getContentResolver().insert(MovieContract.CONTENT_URI, cV);
            if (u != null){
                Log.d(TANDA, "Uri " + u);
            }
        }else{
            Uri u = MovieContract.CONTENT_URI.buildUpon().appendPath(id).build();
            getContentResolver().delete(u, null, null);
        }
    }

    private boolean apakahFavorit(String id){
        Uri u = MovieContract.CONTENT_URI.buildUpon().appendPath(id).build();
        Cursor c = getContentResolver().query(u,null,null,null,null);
        return c.moveToFirst();
    }

    public void saatTombolDiClick(View v){
        if(!apakahfavorit){
            apakahfavorit = true;
            tombolHapusFavorit.setVisibility(View.VISIBLE);
            tombolAddFavorit.setVisibility(View.GONE);
            Snackbar.make(v, "Film ini telah ditambahkan ke dalam daftar Film Favorit", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else{
            apakahfavorit = false;
            tombolAddFavorit.setVisibility(View.VISIBLE);
            tombolHapusFavorit.setVisibility(View.GONE);
            Snackbar.make(v, "Film ini telah dihapuskan dari daftar Film Favorit", Snackbar. LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        simpanDalamFavorit();
    }

    @SuppressLint("StringFormatMatches")
    void updateImage(MovieResult movie) {
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



//    @Override
//    protected void onSaveInstanceState(Bundle bundle){
//        super.onSaveInstanceState(bundle);
//        bundle.putParcelableArrayList("movie", new ArrayList<>(movieAdapter.getList()));
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle bundle) {
//        super.onRestoreInstanceState(bundle);
//        bundle.putParcelableArrayList("movie", new ArrayList<>(movieAdapter.getList()));
//    }
}
