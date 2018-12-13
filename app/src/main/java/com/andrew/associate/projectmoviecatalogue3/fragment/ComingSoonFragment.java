package com.andrew.associate.projectmoviecatalogue3.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrew.associate.projectmoviecatalogue3.R;
import com.andrew.associate.projectmoviecatalogue3.adapter.MovieAdapter;
import com.andrew.associate.projectmoviecatalogue3.entity.Movie;
import com.andrew.associate.projectmoviecatalogue3.entity.MovieResult;
import com.andrew.associate.projectmoviecatalogue3.rest.MovieClient;
import com.andrew.associate.projectmoviecatalogue3.rest.MovieInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.andrew.associate.projectmoviecatalogue3.utils.Utils.API_KEY;
import static com.andrew.associate.projectmoviecatalogue3.utils.Utils.BASE_URL;

public class ComingSoonFragment extends Fragment {
    private static final String TAG = ComingSoonFragment.class.getSimpleName();

    @BindView(R.id.recycler_movie_upcoming)
    RecyclerView recycler_view;

    List<MovieResult> movieList;
    MovieAdapter movieAdapter;

    MovieInterface movieInterface;
    Call<Movie> call;


    public ComingSoonFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup,
                             Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_coming_soon, viewGroup, false);
        ButterKnife.bind(this, view);

        viewApplicator();
        loadDatas();
        setRetainInstance(true);

        return view;
    }

    void viewApplicator() {

        movieAdapter = new MovieAdapter(getActivity());
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
    }

    private void loadDatas() {

        movieInterface = MovieClient.getClient(BASE_URL).create(MovieInterface.class);
        call = movieInterface.getUpcomingMovie(API_KEY);

        movieList = new ArrayList<>();

        call.enqueue(new Callback<Movie>() {

            @Override
            public void onResponse(Call<Movie> calling, Response<Movie> responsing) {
                if (responsing.isSuccessful()) {
                    movieList = responsing.body().getResults();
                }
                movieAdapter.setMovieResult(movieList);
                recycler_view.setAdapter(movieAdapter);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movie", new ArrayList<>(movieAdapter.getList()));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ArrayList<MovieResult> mList;
            mList = savedInstanceState.getParcelableArrayList("movie");
            movieAdapter.setMovieResult(mList);
            recycler_view.setAdapter(movieAdapter);
        }
    }
}