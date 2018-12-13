package com.andrew.associate.projectmoviecatalogue3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.andrew.associate.projectmoviecatalogue3.entity.MovieFavorite;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.andrew.associate.projectmoviecatalogue3.database.MovieContract.MovieColumns.MOVIE_ID;
import static com.andrew.associate.projectmoviecatalogue3.database.MovieContract.MovieColumns.TABLE_MOVIE;

public class MovieHelper {

    private static String DATABASE_TABLE = MovieContract.MovieColumns.TABLE_MOVIE;
    private Context context;
    private DatabaseHelper databaseHelper;

    private SQLiteDatabase database;

    public MovieHelper(Context context){
        this.context = context;
    }

    public MovieHelper open() throws SQLException{
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        databaseHelper.close();
    }

    public Cursor queryProvider(){
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                MovieContract.MovieColumns._ID + " DESC"
        );
    }

    public Cursor queryByIdProvider(String id){
        return database.query(DATABASE_TABLE, null
            , MovieContract.MovieColumns.MOVIE_ID + " = ?"
            , new String[]{id}
            , null
            , null
            , null
            , null);
    }

    public long insertProvider(ContentValues values){
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values){
        return database.update(DATABASE_TABLE, values,
                MovieContract.MovieColumns.MOVIE_ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id){
        return database.delete(DATABASE_TABLE,
                MovieContract.MovieColumns.MOVIE_ID + " = ?", new String[]{id});
    }
}
