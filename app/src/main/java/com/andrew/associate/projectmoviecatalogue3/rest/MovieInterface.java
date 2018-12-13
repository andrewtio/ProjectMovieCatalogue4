package com.andrew.associate.projectmoviecatalogue3.rest;

import com.andrew.associate.projectmoviecatalogue3.entity.Movie;
import com.andrew.associate.projectmoviecatalogue3.entity.MovieResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieInterface {

    @GET("movie/now_playing")
    Call<Movie> getNowPlayingMovie(@Query("api_key") String apiKey);

    @GET("movie/upcoming")
    Call<Movie> getUpcomingMovie(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieResult> getMovieById(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("search/movie/")
    Call<Movie> getMovieBySearch(@Query("query") String q, @Query("api_key") String apiKey);

}
