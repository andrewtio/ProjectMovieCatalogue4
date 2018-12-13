package com.andrew.associate.myfavoritemovies.activity;

import android.support.v4.content.CursorLoader;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.database.Cursor;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.andrew.associate.myfavoritemovies.R;
import com.andrew.associate.myfavoritemovies.adapter.MovieFavoriteAdapter;
import com.andrew.associate.myfavoritemovies.database.MovieContract;
import com.andrew.associate.myfavoritemovies.entity.MovieFavorite;
import com.andrew.associate.myfavoritemovies.entity.MovieResult;
import com.andrew.associate.myfavoritemovies.rest.MovieClient;
import com.andrew.associate.myfavoritemovies.rest.MovieInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.andrew.associate.myfavoritemovies.database.MovieContract.CONTENT_URI;
import static com.andrew.associate.myfavoritemovies.utils.Utils.API_KEY;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.recycler_favorite)
    RecyclerView recyclerView;

    Call<MovieResult> movieResultCall;
    MovieFavoriteAdapter adapter;
    ArrayList<MovieResult> movieResults;
    ArrayList<MovieFavorite> movieFavorites;
    MovieInterface movieService;

    private final int MOVIE_ID = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        adapter = new MovieFavoriteAdapter(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getSupportLoaderManager().initLoader(MOVIE_ID, null, this);
    }


    @NonNull
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        movieFavorites = new ArrayList<>();
        movieResults = new ArrayList<>();
        return new CursorLoader(this, CONTENT_URI,
                null,null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {
        movieFavorites = getItem(cursor);
        for (MovieFavorite mF : movieFavorites){
            getFavoriteMovies(mF.getId());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {
        movieFavorites = getItem(null);
    }

    private ArrayList<MovieFavorite> getItem(Cursor cursor){
        ArrayList<MovieFavorite> movieFavoriteArrayList = new ArrayList<>();
        cursor.moveToFirst();
        MovieFavorite favorite;
        if(cursor.getCount() > 0){
            do{
                favorite = new MovieFavorite(cursor.getString(cursor.getColumnIndexOrThrow(
                        MovieContract.MovieColumns.MOVIE_ID)));
                movieFavoriteArrayList.add(favorite);
                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }
        return movieFavoriteArrayList;
    }

    private void getFavoriteMovies(String id){
        movieService = MovieClient.getClient().create(MovieInterface.class);
        movieResultCall = movieService.getMovieById(id, API_KEY);

        movieResultCall.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(@NonNull Call<MovieResult> call, @NonNull Response<MovieResult> response) {
                movieResults.add(response.body());
                adapter.setMovieResult(movieResults);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<MovieResult> call,@NonNull Throwable t) {
                movieResults = null;
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (movieResults != null){
            movieResults.clear();
            adapter.setMovieResult(movieResults);
            recyclerView.setAdapter(adapter);
        }
        getSupportLoaderManager().restartLoader(MOVIE_ID, null,this);
    }
}
