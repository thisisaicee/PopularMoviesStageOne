package net.aicee.popularmoviesstageone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    final static String BASE_URL = "https://api.themoviedb.org/3/movie/";
    final static String API_KEY = "api_key";

    @BindView(R.id.movies_recyclerview)
    RecyclerView moviesRecyclerView;

    @BindView(R.id.main_progress_bar)
    ProgressBar mainProgressBar;

    @BindView(R.id.network_error)
    TextView networkErrorTextView;

    RecyclerView.Adapter movieAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<MovieModel> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        moviesRecyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        moviesRecyclerView.setLayoutManager(layoutManager);
        loadFromAPI(getString(R.string.popular_sorting));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_popular:
                loadFromAPI(getString(R.string.popular_sorting));
                setTitle(getString(R.string.title_popular_sorting));
                break;
            case R.id.action_top_rated:
                loadFromAPI(getString(R.string.sort_by_rating));
                setTitle(getString(R.string.title_top_rated));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadFromAPI(String sortBy){
        URL url = buildUrl(this, sortBy);
        networkErrorTextView.setVisibility(View.INVISIBLE);

        if(isConnected(this)){
            new movieTask().execute(url);
        }
        else{
            networkErrorTextView.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressBar(){
        if(mainProgressBar != null && mainProgressBar.isShown()){
            mainProgressBar.setVisibility(View.INVISIBLE);
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

    private class movieTask extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(mainProgressBar != null && !mainProgressBar.isShown()){
                mainProgressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String apiResult = null;

            try {
                apiResult = getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return apiResult;
        }

        @Override
        protected void onPostExecute(String s) {
            movieList =  JsonUtils.parseMoviesList(s);
            movieAdapter = new MovieAdapter(movieList, MainActivity.this);
            moviesRecyclerView.setAdapter(movieAdapter);
            hideProgressBar();
        }
    }
}