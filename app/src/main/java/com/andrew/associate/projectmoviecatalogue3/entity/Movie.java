package com.andrew.associate.projectmoviecatalogue3.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.andrew.associate.projectmoviecatalogue3.database.MovieContract;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.andrew.associate.projectmoviecatalogue3.database.MovieContract.getColumnString;

public class Movie implements Parcelable {

    @SerializedName("results")
    private List<MovieResult> mMovieResults;
    @SerializedName("total_results")
    private Long mTotalResults;


    public List<MovieResult> getResults() {
        return mMovieResults;
    }

    public void setResults(List<MovieResult> movieResults) {
        mMovieResults = movieResults;
    }


    public Long getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(Long totalResults) {
        mTotalResults = totalResults;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mMovieResults);
        dest.writeValue(this.mTotalResults);
    }

    public Movie() {
    }


    protected Movie(Parcel in) {
        this.mMovieResults = in.createTypedArrayList(MovieResult.CREATOR);
        this.mTotalResults = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
