package com.example.mohamed.movi_app;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed on 25/10/2016.
 */

public class CustomAdapter extends BaseAdapter
{
    private Context context;
    private List<String> list=new ArrayList<>();
    private LayoutInflater inflater;

    public CustomAdapter(List list, Context context)
    {
        this.list=list;
        this.context=context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder viewHolder;
        if(view==null)
        {
            inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.img_view_item,null);
            viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder)view.getTag();
        }
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185"+list.get(position)).
                into(viewHolder.getImageView());
        Log.e("LOG_TAG","http://image.tmdb.org/t/p/w185"+list.get(position));
        return view;
    }
}
