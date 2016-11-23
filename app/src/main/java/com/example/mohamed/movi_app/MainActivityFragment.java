package com.example.mohamed.movi_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    String user_choice;
    SharedPreferences preferences;
    public MainActivityFragment() {
    }
    public MainActivityFragment(SharedPreferences s) {
        this.preferences=s;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user_choice=preferences.getString(getString(R.string.choice),"popular");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View Rootview=inflater.inflate(R.layout.fragment_main,container,false);
        GridView gridView=(GridView)Rootview.findViewById(R.id.grig_view);
        if(user_choice.equals("favorite"))
        {
            Log.e("ENTER IN ",user_choice);
            final DatabaseHandler handler=new DatabaseHandler(getContext());
            final Movi_info[] movi_info = new Movi_info[1];
            List<String> posters= handler.getAllPosters();
            for(int i=0;i<posters.size();i++)
            {
                Log.e("FAV LINKES "+i,posters.get(i));
            }
            final List<Integer>ids=handler.getAllID();
            CustomAdapter adapter=new CustomAdapter(posters,getContext());
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   movi_info[0] =handler.getMovie(ids.get(position));
                    Intent intent=new Intent(getContext(),Detailed_activity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("TITLE", movi_info[0].getTitle());
                    bundle.putString("POSTER", movi_info[0].getPoster());
                    bundle.putString("YEAR", movi_info[0].getRelese_year());
                    bundle.putString("RATE", movi_info[0].getUser_rating());
                    bundle.putString("VIEW", movi_info[0].getOver_view());
                    bundle.putString("ID",String.valueOf(movi_info[0].getId()));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            //SharedPreferences.Editor editor= sharedPref.edit();
            //editor.putString(getString(R.string.choice),"popular").commit();
        }
        else {
            FetchMoviData feachMoviData = new FetchMoviData();
            feachMoviData.setContext(getActivity());
            feachMoviData.setGridView(gridView);
            feachMoviData.setUserChoice(user_choice);
            feachMoviData.execute("");
        }
        return Rootview;
    }

}
