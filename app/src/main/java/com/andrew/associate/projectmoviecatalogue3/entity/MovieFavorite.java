package com.andrew.associate.projectmoviecatalogue3.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static com.andrew.associate.projectmoviecatalogue3.database.MovieContract.MovieColumns.MOVIE_ID;
import static com.andrew.associate.projectmoviecatalogue3.database.MovieContract.MovieColumns.MOVIE_IMAGE;
import static com.andrew.associate.projectmoviecatalogue3.database.MovieContract.MovieColumns.MOVIE_TITLE;
import static com.andrew.associate.projectmoviecatalogue3.database.MovieContract.getColumnString;

public class MovieFavorite implements Parcelable {

    private String id;
    private String title;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MovieFavorite(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.image);
    }

    private MovieFavorite(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<MovieFavorite> CREATOR = new Parcelable.Creator<MovieFavorite>() {
        @Override
        public MovieFavorite createFromParcel(Parcel source) {
            return new MovieFavorite(source);
        }

        @Override
        public MovieFavorite[] newArray(int size) {
            return new MovieFavorite[size];
        }
    };

    public MovieFavorite (Cursor cursor){
        this.id = getColumnString(cursor, MOVIE_ID);
        this.title = getColumnString(cursor, MOVIE_TITLE);
        this.image = getColumnString(cursor, MOVIE_IMAGE);
    }
}
