package net.aicee.popularmoviesstageone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE_ID = "movie_id";
    private MovieModel movieModel;
    final static String BASE_URL = "https://api.themoviedb.org/3/movie/";
    final static String API_KEY = "api_key";
    private String movieID = "";
    @BindView(R.id.movie_backdrop) ImageView backDropImageView;
    @BindView(R.id.movie_poster) ImageView posterImageView;
    @BindView(R.id.movie_title) TextView titleTextView;
    @BindView(R.id.user_rating) TextView userRatingTextView;
    @BindView(R.id.release_date) TextView releaseDateTextView;
    @BindView(R.id.movie_overview) TextView overviewTextView;
    @BindView(R.id.details_progress_bar) ProgressBar detailsProgressBar;
    @BindView(R.id.overview_label) TextView overviewLabelTextView;
    @BindView(R.id.user_rating_label) TextView ratingLabelTextView;
    @BindView(R.id.release_date_label) TextView releaseDateLabelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        if(getIntent() != null && getIntent().hasExtra(EXTRA_MOVIE_ID)){
            movieID = getIntent().getStringExtra(EXTRA_MOVIE_ID);
            load();
        }
    }

    private void load(){
        URL url = buildUrl(this, movieID);
        if(isConnected(this)){
            new MovieDetailsApiTask().execute(url);
        }
        else{
            Toast.makeText(this, getString(R.string.error_no_network), Toast.LENGTH_LONG);
            hideProgressBar();
        }

    }

    private void loadDataToUI(){
        setTitle(movieModel.getTitle());
        Picasso.with(this).load(movieModel.getBackdropPath()).into(backDropImageView);
        Picasso.with(this).load(movieModel.getPosterPath()).into(posterImageView);
        titleTextView.setText(movieModel.getTitle());
        userRatingTextView.setText(movieModel.getVoteAverage());
        releaseDateTextView.setText(movieModel.getReleaseDate());
        overviewTextView.setText(movieModel.getOverview());
        overviewLabelTextView.setText(getString(R.string.overview_label));
        ratingLabelTextView.setText(getString(R.string.user_score_label));
        releaseDateLabelTextView.setText(getString(R.string.release_date_label));
        hideProgressBar();
    }

    private void hideProgressBar(){
        if(detailsProgressBar != null && detailsProgressBar.isShown()){
            detailsProgressBar.setVisibility(View.INVISIBLE);
        }
    }


    public static URL buildUrl(Context context, String string){
        String uriToParse = BASE_URL + string;
        URL url = null;
        try {
            url = new URL(Uri.parse(uriToParse).buildUpon()
                    .appendQueryParameter(API_KEY, context.getString(R.string.api_key))
                    .build().toString());
        } catch (MalformedURLException e) {
            Toast.makeText(context, "URL not valid", Toast.LENGTH_SHORT).show();

        }
        return url;
    }



    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public static boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


    private class MovieDetailsApiTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            String apiResponse = "";
            try {
                apiResponse = getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return apiResponse;
        }

        @Override
        protected void onPostExecute(String response) {
            Log.d("API_RESPONSE", response.toString());
            try {
                JSONObject jsonObject = new JSONObject(response);
                movieModel = JsonUtils.parseMovieJson(jsonObject);
                loadDataToUI();
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }
    }
}
