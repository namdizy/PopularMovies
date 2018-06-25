package com.example.android.popularmovies.utils;

import com.example.android.popularmovies.Models.Movies;
import com.example.android.popularmovies.Models.Review;
import com.example.android.popularmovies.Models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class PopularMoviesJsonUtils {

    public static ArrayList<Movies> getMoviesFromJson(String moviesJsonString) throws JSONException{

        final String PM_LIST = "results";
        final String PM_MESSAGE_CODE = "cod";

        JSONObject moviesJson = new JSONObject(moviesJsonString);


        if (moviesJson.has(PM_MESSAGE_CODE)) {
            int errorCode = moviesJson.getInt(PM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray moviesJsonArray = new JSONArray(moviesJson.getString(PM_LIST));
        ArrayList<Movies> parsedMovies = new ArrayList<>();

        System.out.println(moviesJsonArray);

        for(int i = 0 ; i< moviesJsonArray.length(); i++){
            Movies m = new Movies();
            JSONObject movie = moviesJsonArray.getJSONObject(i);

            m.setId(movie.getLong("id"));
            m.setOriginalTitle(movie.getString("original_title"));
            m.setVoteAverage(movie.getInt("vote_average"));
            m.setOverview(movie.getString("overview"));
            m.setPosterPath(movie.getString("poster_path"));
            m.setReleaseDate(movie.getString("release_date"));
            parsedMovies.add(m);
        }
        return parsedMovies;
    }

    public static ArrayList<Review> getReviewsFromJson(String reviewJsonString) throws JSONException{

        final String PM_LIST = "results";
        final String PM_MESSAGE_CODE = "cod";
        JSONObject reviewJson = new JSONObject(reviewJsonString);


        if (reviewJson.has(PM_MESSAGE_CODE)) {
            int errorCode = reviewJson.getInt(PM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray reviewJsonArray = new JSONArray(reviewJson.getString(PM_LIST));
        ArrayList<Review> parsedReviews = new ArrayList<>();

        for(int i = 0 ; i< reviewJsonArray.length(); i++){
            Review r = new Review();
            JSONObject review = reviewJsonArray.getJSONObject(i);

            r.setId(review.getString("id"));
            r.setAuthor(review.getString("author"));
            r.setContent(review.getString("content"));
            r.setUrl(review.getString("url"));
            parsedReviews.add(r);
        }
        return parsedReviews;
    }

    public static ArrayList<Trailer> getTrailersFromJson(String JsonString) throws JSONException{
        final String PM_LIST = "results";
        final String PM_MESSAGE_CODE = "cod";
        JSONObject trailerJson = new JSONObject(JsonString);

        if (trailerJson.has(PM_MESSAGE_CODE)) {
            int errorCode = trailerJson.getInt(PM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray reviewJsonArray = new JSONArray(trailerJson.getString(PM_LIST));
        ArrayList<Trailer> parsedTrailers = new ArrayList<>();

        for(int i = 0 ; i< reviewJsonArray.length(); i++){
            Trailer t = new Trailer();
            JSONObject trailer = reviewJsonArray.getJSONObject(i);

            t.setId(trailer.getString("id"));
            t.setKey(trailer.getString("key"));
            t.setName(trailer.getString("name"));
            parsedTrailers.add(t);
        }
        return parsedTrailers;
    }

}
