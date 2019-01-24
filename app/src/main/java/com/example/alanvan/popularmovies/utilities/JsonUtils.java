package com.example.alanvan.popularmovies.utilities;

import android.util.Log;

import com.example.alanvan.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String RESULTS = "results";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185/";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String POSTER_PATH = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String RUN_TIME = "runtime";
    private static final String KEY = "key";

    /**
     * This method parses a JSON string from a web response and returns an array of Strings
     * describing the movie data that matches the query
     * @param jsonString, jsonString to be parsed
     * @return Array of Strings describing the movie data
     * @throws JSONException
     */
    public static String[] getMovieData(String jsonString) throws JSONException {
        JSONObject obj = new JSONObject(jsonString);
        JSONArray moviesJson = obj.optJSONArray(RESULTS);

        String[] movieData = new String[moviesJson.length()];
        for (int i = 0; i < moviesJson.length(); i++) {
            movieData[i] = moviesJson.getString(i);
        }

        return movieData;
    }

    /**
     * This method coverts a movieJsonString into a movie object
     * @param movieJsonString Json String describing one movie data
     * @return the movie object
     */
    public static Movie convertToMovieObject(String movieJsonString) {
        try {
            JSONObject movieJson = new JSONObject(movieJsonString);

            int id = movieJson.optInt(ID, -1);

            String title = movieJson.optString(TITLE, "");

            String tempPath = movieJson.optString(POSTER_PATH, "");

            String posterPath = "";

            if (!tempPath.equals("")) {
                posterPath = IMAGE_BASE_URL + tempPath;
            }

            String overview = movieJson.optString(OVERVIEW, "");

            double voteAverage = movieJson.optDouble(VOTE_AVERAGE, -1);

            String releaseDate = movieJson.optString(RELEASE_DATE, "");


            Log.d("MOVIEJSON", movieJson.toString());

            return new Movie(id, title, posterPath, overview, voteAverage, releaseDate);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getDuration(String jsonResponse) {
        int duration = 0;
        try {
            JSONObject movieJson = new JSONObject(jsonResponse);
            duration = movieJson.optInt(RUN_TIME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return duration;
    }

    public static List<String> getVideoKeys(String videoLinkResponse) {
        List<String> videoKeys = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(videoLinkResponse);
            JSONArray jsonArray = jsonObject.optJSONArray(RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                videoKeys.add(object.optString(KEY, ""));
                Log.d("KEY", object.optString(KEY, ""));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return videoKeys;
    }
}
