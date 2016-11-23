package com.example.mohamed.movi_app;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by Mohamed on 25/10/2016.
 */

public class ViewHolder {

    private ImageView imageView;

    public ViewHolder(View view)
    {
        imageView=(ImageView) view.findViewById(R.id.img_view);
    }

    public ImageView getImageView() {
        return imageView;
    }
}
