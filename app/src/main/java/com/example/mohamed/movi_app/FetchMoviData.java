package com.example.mohamed.movi_app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.app.PendingIntent.getActivity;

/**
 * Created by Mohamed on 25/10/2016.
 */

public class FetchMoviData extends AsyncTask<String,Void,String>
{
    String UserChoice;                 //comming from setting
    List<String> list_posters=new ArrayList<>();
    List<String> list_titles=new ArrayList<>();
    List<String> list_dates=new ArrayList<>();
    List<String> list_overviews=new ArrayList<>();
    List<Double> list_rate=new ArrayList<>();
    List<Long> list_id=new ArrayList<>();
    Context context;
    GridView gridView;
    CustomAdapter adapter;
    @Override
    protected String doInBackground(String ... params)
    {
        String MoviJsonStr=null;              // will contan the results
        String apiKey = BuildConfig.API_KEY;
        String Baseurl = "http://api.themoviedb.org/3/movie/"+UserChoice+"?api_key="+apiKey;
        Log.e("URL",Baseurl);
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
       if(isOnline()) {
            try {
                URL url = new URL(Baseurl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // there is values
                    buffer.append(line);
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                MoviJsonStr = buffer.toString();
                Log.e("LOG_TAG", "MOVI JSON STRING " + MoviJsonStr);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else
        {
            return "";
        }
        return MoviJsonStr;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
       if(s=="")
        {
            Toast toast=Toast.makeText(context,"sorry NO INTERNET CONNECTION",Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            try {
                JSONObject jsonObject = new JSONObject(s);    // all data
                JSONArray posters = jsonObject.getJSONArray("results");
                for (int i = 0; i < posters.length(); i++) {
                    JSONObject ob = posters.getJSONObject(i);
                    list_posters.add(ob.getString("poster_path"));
                    list_overviews.add(ob.getString("overview"));
                    list_dates.add(ob.getString("release_date"));
                    list_titles.add(ob.getString("title"));
                    list_rate.add(ob.getDouble("vote_average"));
                    list_id.add(ob.getLong("id"));
                    Log.e("hgjhg", list_posters.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new CustomAdapter(list_posters, context);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
           gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                   Intent intent=new Intent(context,Detailed_activity.class);
                   Bundle bundle=new Bundle();
                   bundle.putString("TITLE",list_titles.get(position));
                   bundle.putString("POSTER",list_posters.get(position));
                   bundle.putString("YEAR",list_dates.get(position));
                   bundle.putString("RATE",String.valueOf(list_rate.get(position)));
                   bundle.putString("VIEW",list_overviews.get(position));
                   bundle.putString("ID",String.valueOf(list_id.get(position)));
                   intent.putExtras(bundle);
                   context.startActivity(intent);

               }
           });
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setGridView(GridView gridView) {
        this.gridView = gridView;
    }

    public boolean isOnline() {
        ConnectivityManager cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void setUserChoice(String userChoice) {
        UserChoice = userChoice;
    }
}

