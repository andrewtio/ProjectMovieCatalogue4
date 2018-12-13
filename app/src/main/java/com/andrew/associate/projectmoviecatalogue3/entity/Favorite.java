package com.andrew.associate.projectmoviecatalogue3.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.andrew.associate.projectmoviecatalogue3.database.MovieContract;
import com.google.gson.annotations.SerializedName;

import static android.provider.BaseColumns._ID;
import static com.andrew.associate.projectmoviecatalogue3.database.MovieContract.getColumnInt;
import static com.andrew.associate.projectmoviecatalogue3.database.MovieContract.getColumnString;

public class Favorite implements Parcelable {

    private int id;
    private String name;
    private String poster;
    private String date;
    private String description;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    public String getPoster() {
        return poster;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.poster);
        dest.writeString(this.date);
        dest.writeString(this.description);
    }

    public Favorite() {

    }

    public Favorite(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.name = getColumnString(cursor, MovieContract.MovieColumns.MOVIE_TITLE);
        this.poster = getColumnString(cursor, MovieContract.MovieColumns.MOVIE_IMAGE);
    }

    protected Favorite(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.poster = in.readString();
        this.date = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<Favorite> CREATOR = new Parcelable.Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel source) {
            return new Favorite(source);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };
}