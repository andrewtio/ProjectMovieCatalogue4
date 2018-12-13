package com.andrew.associate.projectmoviecatalogue3.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.andrew.associate.projectmoviecatalogue3.database.MovieContract;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import static com.andrew.associate.projectmoviecatalogue3.database.MovieContract.MovieColumns.MOVIE_ID;
import static com.andrew.associate.projectmoviecatalogue3.database.MovieContract.MovieColumns.MOVIE_IMAGE;
import static com.andrew.associate.projectmoviecatalogue3.database.MovieContract.MovieColumns.MOVIE_TITLE;

public class MovieResult implements Parcelable {
    @SerializedName("id")
    private String mId;
    @SerializedName("overview")
    private String mOverview;
    @SerializedName("poster_path")
    private String mPosterPath;
    @SerializedName("release_date")
    private String mReleaseDate;
    @SerializedName("title")
    private String mTitle;


    public MovieResult() {
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mOverview);
        dest.writeString(this.mPosterPath);
        dest.writeString(this.mReleaseDate);
        dest.writeString(this.mTitle);
    }

    public MovieResult(Parcel in) {
        this.mId = in.readString();
        this.mOverview = in.readString();
        this.mPosterPath = in.readString();
        this.mReleaseDate = in.readString();
        this.mTitle = in.readString();
    }

    public MovieResult(Cursor cursor) {
        this.mId = MovieContract.getColumnString(cursor,MOVIE_ID);
        this.mTitle = MovieContract.getColumnString(cursor, MOVIE_TITLE);
        this.mPosterPath = MovieContract.getColumnString(cursor, MOVIE_IMAGE);
    }

    public static final Parcelable.Creator<MovieResult> CREATOR = new Parcelable.Creator<MovieResult>() {
        @Override
        public MovieResult createFromParcel(Parcel source) {
            return new MovieResult(source);
        }

        @Override
        public MovieResult[] newArray(int size) {
            return new MovieResult[size];
        }
    };

    @Override
    public String toString() {
        return getPosterPath() + getId();
    }
}

