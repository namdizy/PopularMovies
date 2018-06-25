package com.example.android.popularmovies.data;

import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Switch;

/**
 * Created by Nnamdi on 1/9/2018.
 */

public class MovieProvider extends ContentProvider {

    private static final int FAVORITE_MOVIES = 100;
    private static final int FAVORITE_MOVIES_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper  mOpenhelper;


    public static UriMatcher buildUriMatcher(){
        String content = MovieContract.CONTENT_AUTHORITY;

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, MovieContract.FAVORITE_MOVIES, FAVORITE_MOVIES);
        matcher.addURI(content, MovieContract.FAVORITE_MOVIES + "/#", FAVORITE_MOVIES_ID);
        return matcher;

    }


    @Override
    public boolean onCreate() {
        mOpenhelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mOpenhelper.getReadableDatabase();
        Cursor cursor;

        switch (sUriMatcher.match(uri)){
            case FAVORITE_MOVIES:
                cursor = db.query(
                    MovieContract.FavoriteMoviesEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                );
                break;
            case FAVORITE_MOVIES_ID:
                long movie_id = ContentUris.parseId(uri);
                cursor = db.query(
                    MovieContract.FavoriteMoviesEntry.TABLE_NAME,
                    projection,
                    MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{String.valueOf(movie_id)},
                    null,
                    null,
                    sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final SQLiteDatabase db = mOpenhelper.getWritableDatabase();
        long id;

        switch (sUriMatcher.match(uri)){
            case FAVORITE_MOVIES :
                id = db.insert(MovieContract.FavoriteMoviesEntry.TABLE_NAME, null, contentValues);
                if(id>0){
                    return MovieContract.FavoriteMoviesEntry.buildMovieUri(id);
                }else{
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
                }
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);

        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mOpenhelper.getWritableDatabase();
        int rows;
        switch (sUriMatcher.match(uri)){
            case FAVORITE_MOVIES:
                rows = db.delete(MovieContract.FavoriteMoviesEntry.TABLE_NAME, selection, selectionArgs );
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        return rows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mOpenhelper.getWritableDatabase();
        int rows;

        switch (sUriMatcher.match(uri)){
            case FAVORITE_MOVIES:
                rows = db.update(MovieContract.FavoriteMoviesEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: "+ uri);
        }

        return rows;
    }
}
