package com.andrew.associate.myfavoritemovies.rest;


import android.graphics.Movie;

import com.andrew.associate.myfavoritemovies.entity.MovieResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieInterface {

    @GET("movie/{id}")
    Call<MovieResult> getMovieById(@Path("id") String id, @Query("api_key") String apiKey);

}
