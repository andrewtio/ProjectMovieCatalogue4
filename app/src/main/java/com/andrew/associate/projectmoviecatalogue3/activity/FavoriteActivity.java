package com.andrew.associate.projectmoviecatalogue3.activity;

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

import com.andrew.associate.projectmoviecatalogue3.R;
import com.andrew.associate.projectmoviecatalogue3.adapter.FavoriteAdapter;
import com.andrew.associate.projectmoviecatalogue3.database.MovieContract;
import com.andrew.associate.projectmoviecatalogue3.entity.MovieFavorite;
import com.andrew.associate.projectmoviecatalogue3.entity.MovieResult;
import com.andrew.associate.projectmoviecatalogue3.rest.MovieClient;
import com.andrew.associate.projectmoviecatalogue3.rest.MovieInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.andrew.associate.projectmoviecatalogue3.database.MovieContract.CONTENT_URI;
import static com.andrew.associate.projectmoviecatalogue3.utils.Utils.API_KEY;
import static com.andrew.associate.projectmoviecatalogue3.utils.Utils.BASE_URL;

public class FavoriteActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.recyclerFavorite)
    RecyclerView recycler_view;

    Call<MovieResult> movieResultCall;
    FavoriteAdapter adapter;
    ArrayList<MovieResult> movieResults;
    ArrayList<MovieFavorite> movieFavorites;
    MovieInterface movieService;

    private final int MOVIE_ID = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);
        adapter = new FavoriteAdapter(getApplicationContext());
        recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(new DefaultItemAnimator());

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
        movieService = MovieClient.getClient(BASE_URL).create(MovieInterface.class);
        movieResultCall = movieService.getMovieById(id, API_KEY);

        movieResultCall.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(@NonNull Call<MovieResult> call, @NonNull Response<MovieResult> response) {
                movieResults.add(response.body());
                adapter.setMovieResult(movieResults);
                recycler_view.setAdapter(adapter);
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
            recycler_view.setAdapter(adapter);
        }
        getSupportLoaderManager().restartLoader(MOVIE_ID, null,this);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList("movie", new ArrayList<>(adapter.getList()));
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        bundle.putParcelableArrayList("movie", new ArrayList<>(adapter.getList()));
    }
}
