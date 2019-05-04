package com.example.lian.betterbattleship;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TileView extends LinearLayout {

    ImageView imageView;

    public TileView(Context context) {
        super(context);

        this.setOrientation(VERTICAL);

        imageView = new ImageView(context);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(R.drawable.border);

        imageView.setBackgroundColor(Color.LTGRAY);


        addView(imageView);
    }
}
