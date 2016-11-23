package com.example.mohamed.movi_app;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class Detailed_activityFragment extends Fragment {
    String ititle;
    String iposter;
    String irelese_year;
    String iuser_rating;
    String iover_view;
    String iid;

    CheckBox fav;

    FetchReview fetchReview;
    FetchTrailer fetchTrailer;
    DatabaseHandler databaseHandler;
    Movi_info movi_info=new Movi_info();

    public Detailed_activityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootView= inflater.inflate(R.layout.fragment_detailed_activity, container, false);
        //define layouts
        TextView title=(TextView) rootView.findViewById(R.id.title);
        ImageView poster=(ImageView) rootView.findViewById(R.id.poster_image);
        TextView year=(TextView) rootView.findViewById(R.id.release_year);
        TextView rate=(TextView) rootView.findViewById(R.id.user_rating);
        TextView overview=(TextView)rootView.findViewById(R.id.overview);
        LinearLayout listView_review=(LinearLayout)rootView.findViewById(R.id.review);
        LinearLayout listView_trailer=(LinearLayout)rootView.findViewById(R.id.trailer);
        fav=(CheckBox)rootView.findViewById(R.id.favorite);
        // get data from the intent
        Intent intent =getActivity().getIntent();
        Bundle bundle=this.getActivity().getIntent().getExtras();
        ititle=bundle.getString("TITLE");
        iposter=bundle.getString("POSTER");
        irelese_year=bundle.getString("YEAR");
        iuser_rating=bundle.getString("RATE");
        iover_view=bundle.getString("VIEW");
        iid=bundle.getString("ID");
        // set data to layouts
        title.setText(ititle);
        Picasso.with(this.getContext()).
                load("http://image.tmdb.org/t/p/w185"+iposter).into(poster);
        year.setText(irelese_year);
        rate.setText(iuser_rating);
        overview.setText(iover_view);
        // get trailers
        fetchTrailer=new FetchTrailer(getContext(),listView_trailer);
        fetchTrailer.execute(iid);
        //get reviews
        fetchReview=new FetchReview(getContext(),listView_review);
        fetchReview.execute(iid);

        fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                databaseHandler=new DatabaseHandler(getContext());
                Log.e("VALUE OF CHECK BOX",String.valueOf(isChecked));
                favorite();
            }
        });
        return rootView;
    }

    public void favorite ()
    {
        int id=Integer.parseInt(iid);
        boolean Exist=false;
        List<Integer> ids=databaseHandler.getAllID();
        for(int i=0;i<ids.size();i++)
        {
            if(id==ids.get(i))
            {
                Exist=true;
                break;
            }
        }
        if(!Exist)
        {
            movi_info.setid(id);
            movi_info.setTitle(ititle);
            movi_info.setPoster(iposter);
            Log.e("poster has saved",iposter);
            movi_info.setRelese_year(irelese_year);
            movi_info.setOver_view(iover_view);
            movi_info.setUser_rating(iuser_rating);
            databaseHandler.addMovie(movi_info);
            Log.e("Data has been saved","good");
        }
    }

}
