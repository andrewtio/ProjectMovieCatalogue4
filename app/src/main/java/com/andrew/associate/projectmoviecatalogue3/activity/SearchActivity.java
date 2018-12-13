package com.andrew.associate.projectmoviecatalogue3.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.andrew.associate.projectmoviecatalogue3.R;
import com.andrew.associate.projectmoviecatalogue3.adapter.MovieAdapter;
import com.andrew.associate.projectmoviecatalogue3.entity.Movie;
import com.andrew.associate.projectmoviecatalogue3.entity.MovieResult;
import com.andrew.associate.projectmoviecatalogue3.rest.MovieClient;
import com.andrew.associate.projectmoviecatalogue3.rest.MovieInterface;
import com.andrew.associate.projectmoviecatalogue3.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.andrew.associate.projectmoviecatalogue3.utils.Utils.API_KEY;
import static com.andrew.associate.projectmoviecatalogue3.utils.Utils.BASE_URL;
import static com.andrew.associate.projectmoviecatalogue3.utils.Utils.TAG_INTENT;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recycler_view;
    Toolbar toolbar;

    MovieAdapter movieAdapter;

    List<MovieResult> movieList;
    MovieInterface movieService;
    Call<Movie> movieCall;
    MovieResult movieResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recycler_view = findViewById(R.id.recycler_search);
        toolbar = findViewById(R.id.toolbar_search);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.search_title));

        movieList = new ArrayList<>();
        movieResult = new MovieResult();

        if(getIntent() != null){
            if (getIntent().getStringExtra(TAG_INTENT).equals("cari")){
                String cari = getIntent().getStringExtra(Utils.INTENT_SEARCH);
                viewApplicator();
                searchMovies(cari);
            }
        }
    }

    private void searchMovies(final String keyword){
        movieService = MovieClient.getClient(BASE_URL).create(MovieInterface.class);
        movieCall = movieService.getMovieBySearch(keyword, API_KEY);

        movieCall.enqueue(new Callback<Movie>(){

            @Override
            public void onResponse(Call<Movie> calling, Response<Movie> responsing) {
                if (responsing.isSuccessful()){
                        movieList = responsing.body().getResults();
                        getSupportActionBar().setSubtitle(getString(R.string.search_hint_result,
                                responsing.body()
                                    .getTotalResults().toString(), keyword));
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Failed to search Movie!", Toast.LENGTH_SHORT).show();
                    }
                movieAdapter.setMovieResult(movieList);
                recycler_view.setAdapter(movieAdapter);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "You have no internet connection", Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    protected void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList("movie", new ArrayList<>(movieAdapter.getList()));
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        bundle.putParcelableArrayList("movie", new ArrayList<>(movieAdapter.getList()));
    }

    void viewApplicator(){
        movieAdapter = new MovieAdapter(this);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
    }

}
