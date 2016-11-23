package com.example.mohamed.movi_app;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PrivateKey;


public class FetchReview extends AsyncTask<String, Void, Review[]> {

    private final String LOG_TAG = FetchReview.class.getSimpleName();
    private LinearLayout List_review;
    private LayoutInflater inflater;

    public FetchReview(Context context, LinearLayout l) {
        inflater = LayoutInflater.from(context);
         List_review= l;

    }

    @Override
    protected Review[] doInBackground(String... params) {
        //insert your TMDB API key on the next line. For more information please look at the README
        //on this repository
        final String API_KEY = BuildConfig.API_KEY;
        final String KEY_PARAM = "api_key";

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieId;
        String answerJSONStr;

        if (params[0] != null) {
            movieId = params[0];
        } else {
            Log.e(LOG_TAG, "Missing movie ID");
            return null;
        }
        try {
            // Construct the URL for the API
            String baseUri = String.format("http://api.themoviedb.org/3/movie/%s/reviews", movieId);
            Uri builder = Uri.parse(baseUri).buildUpon().appendQueryParameter(KEY_PARAM, API_KEY)
                    .build();
            URL url = new URL(builder.toString());

            // Create the request to TheMovieDB, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            answerJSONStr = buffer.toString();
            Log.d(LOG_TAG, "Reviews JSON Answer: " + answerJSONStr);
            //Return a call to a method that will contain the videos ID
            try {
                return getDataFromJson(answerJSONStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e("", "Error", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Review[] reviews) {
        if (reviews != null) {
            for (Review currentReview : reviews) {
                View reviewView = inflater.inflate(R.layout.list_item_review,List_review, false);
                TextView author = (TextView) reviewView.findViewById(R.id.author_text_view);
                author.setText(currentReview.getAuthor());

                TextView commentView = (TextView) reviewView.findViewById(R.id.comment_text_view);
                commentView.setText(currentReview.getComment());
                List_review.addView(reviewView);
            }
        }
    }

    public Review[] getDataFromJson(String reviewJsonString) throws JSONException {

        final String RESULT = "results";
        final String AUTHOR = "author";
        final String CONTENT = "content";

        JSONObject reviewJson = new JSONObject(reviewJsonString);
        JSONArray resultArray = reviewJson.getJSONArray(RESULT);

        Review[] reviewArray = new Review[resultArray.length()];

        for (int i = 0; i < resultArray.length();i++) {
            JSONObject review = resultArray.getJSONObject(i);

            String Author = review.getString(AUTHOR);
            String Comment = review.getString(CONTENT);
            reviewArray[i] = new Review(Author,Comment);
        }
        return reviewArray;
    }
}
