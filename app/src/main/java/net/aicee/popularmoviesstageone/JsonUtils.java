package net.aicee.popularmoviesstageone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static final String MOVIE_KEY_RESULTS = "results";
    public static final String MOVIE_KEY_ID = "id";
    public static final String MOVIE_KEY_BACKDROP_PATH = "backdrop_path";
    public static final String MOVIE_KEY_POSTER_PATH = "poster_path";
    public static final String MOVIE_KEY_TITLE = "title";
    public static final String MOVIE_KEY_OVERVIEW = "overview";
    public static final String MOVIE_KEY_RELEASE_DATE = "release_date";
    public static final String MOVIE_KEY_VOTE_AVERAGE = "vote_average";

    public static MovieModel parseMovieJson(JSONObject jsonMovie){
        String movie_id = jsonMovie.optString(MOVIE_KEY_ID);
        String backdrop_path = jsonMovie.optString(MOVIE_KEY_BACKDROP_PATH);
        String poster_path = jsonMovie.optString(MOVIE_KEY_POSTER_PATH);
        String movie_title = jsonMovie.optString(MOVIE_KEY_TITLE);
        String release_date = jsonMovie.optString(MOVIE_KEY_RELEASE_DATE);
        String vote_average = jsonMovie.optString(MOVIE_KEY_VOTE_AVERAGE);
        String movie_overview = jsonMovie.optString(MOVIE_KEY_OVERVIEW);

        MovieModel movie = new MovieModel(movie_id, vote_average, movie_title, poster_path,
                backdrop_path, movie_overview, release_date);

        return movie;
    }

    public static List<MovieModel> parseMoviesList(String json) {
        List<MovieModel> movieList = new ArrayList<>();

        try {
            JSONObject jsonMoviesList = new JSONObject(json);
            JSONArray jsonMoviesArray = jsonMoviesList.getJSONArray(MOVIE_KEY_RESULTS);

            if(jsonMoviesArray.length() > 0){
                for (int counter = 0; counter < jsonMoviesArray.length(); counter++){
                    JSONObject jsonObject = jsonMoviesArray.getJSONObject(counter);
                    MovieModel movieObjects = parseMovieJson(jsonObject);
                    movieList.add(movieObjects);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieList;
    }

}
