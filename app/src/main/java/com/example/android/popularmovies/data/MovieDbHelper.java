package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nnamdi on 1/9/2018.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movieList.db";


    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        addFavoriteMoviesTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavoriteMoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    private void addFavoriteMoviesTable(SQLiteDatabase db){
        final String SQL_CREATE_POPULARMOVIES_TABLE = "CREATE TABLE "
                + MovieContract.FavoriteMoviesEntry.TABLE_NAME
                + " ("
                + MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.FavoriteMoviesEntry.COLUMN_ORIGINAL_TITLE + " REAL NOT NULL, "
                + MovieContract.FavoriteMoviesEntry.COLUMN_OVERVIEW + " REAL NOT NULL, "
                + MovieContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH + " REAL NOT NULL, "
                + MovieContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE + " REAL NOT NULL, "
                + MovieContract.FavoriteMoviesEntry.COLUMN_VOTE_AVERAGE + " INTEGER NOT NULL "
                + ");";

        db.execSQL(SQL_CREATE_POPULARMOVIES_TABLE);
    }

}
