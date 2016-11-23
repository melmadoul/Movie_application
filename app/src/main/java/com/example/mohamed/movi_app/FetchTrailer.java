package com.example.mohamed.movi_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchTrailer extends AsyncTask<String, Void, Trailer[]> {

    private final String LOG_TAG = FetchTrailer.class.getSimpleName();
    private LinearLayout List_trailer;
    private LayoutInflater inflater;
    private Context context;

    public FetchTrailer(Context context, LinearLayout l) {
        inflater = LayoutInflater.from(context);
        List_trailer =l;
        this.context=context;
    }

    @Override
    protected Trailer[] doInBackground(String... params) {

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
            String baseUri = String.format("http://api.themoviedb.org/3/movie/%s/videos", movieId);
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
            Log.d(LOG_TAG, "Trailers JSON Answer: " + answerJSONStr);
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
    protected void onPostExecute(Trailer[] trailers) {

        final String YOUTUBE_IMG_PREFIX = "http://img.youtube.com/vi/";
        final String YOUTUBE_IMG_SUFFIX = "/0.jpg";

        if (trailers != null) {
            for (Trailer currentTrailer : trailers) {
                View trailerView = inflater.inflate(R.layout.list_item_trailer, List_trailer,false);
                TextView trailerTitle = (TextView) trailerView.findViewById(R.id.trailer_name);
                ImageView trailerThumbnail = (ImageView) trailerView.findViewById(R.id.trailer_img);
                try {
                    Picasso.with(context)
                            .load("http://img.youtube.com/vi/"+currentTrailer.getTrailerKey()+"/0.jpg")
                            .resize(250, 125)
                            .centerCrop()
                            .into(trailerThumbnail);

                } catch (IllegalArgumentException e) {
                    Log.e(LOG_TAG, "Malformed/Missing URL", e);
                }
                trailerTitle.setText(currentTrailer.getTrailerName());
                //Add a ClickListener to the whole thing
                trailerView.setOnClickListener(
                        new TrailerOnClickListener(currentTrailer.getTrailerKey())
                );
                List_trailer.addView(trailerView);
            }
        }
    }

    private Trailer[] getDataFromJson(String trailerJsonString) throws JSONException {

        final String TMDB_RESULT = "results";
        final String TMDB_TRAILER_PROVIDER = "site";
        final String TMDB_TRAILER_NAME = "name";
        final String TMDB_TRAILER_KEY = "key";

        JSONObject trailerJson = new JSONObject(trailerJsonString);
        JSONArray resultArray = trailerJson.getJSONArray(TMDB_RESULT);

        Trailer[] trailerArray = new Trailer[resultArray.length()];

        for (int i = 0; i < resultArray.length(); ++i) {
            JSONObject trailer = resultArray.getJSONObject(i);
            //Let's only grab trailers available on Youtube
            if (trailer.getString(TMDB_TRAILER_PROVIDER).equals("YouTube")) {
                String trailerName = trailer.getString(TMDB_TRAILER_NAME);
                String trailerKey = trailer.getString(TMDB_TRAILER_KEY);
                //Build a new trailer object with this data
                //Add it to the return param
                trailerArray[i] = new Trailer(trailerName, trailerKey);
            }
        }
        return trailerArray;
    }

    public class TrailerOnClickListener implements View.OnClickListener {

        private String TrailerKey;

        public TrailerOnClickListener (String trailerKey){
            TrailerKey = trailerKey;
        }

        @Override
        public void onClick(View view) {
            //Throw a video intent with the youtube's link to the video
            final String YOUTUBE_PATH = "https://www.youtube.com/watch";
            final String  VIDEO_PARAM = "v";

            Uri uri = Uri.parse(YOUTUBE_PATH)
                    .buildUpon()
                    .appendQueryParameter(VIDEO_PARAM,TrailerKey)
                    .build();

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
    }
}
