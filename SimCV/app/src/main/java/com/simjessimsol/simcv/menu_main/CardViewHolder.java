package com.simjessimsol.simcv.menu_main;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.simjessimsol.simcv.R;

/**
 * Created by Simen on 25.02.2015.
 */
public class CardViewHolder extends RecyclerView.ViewHolder {
    protected ImageView thumbnail;
    protected TextView title;
    protected ToggleButton toggleNative;
    protected Button btnStart;

    public CardViewHolder(View v) {
        super(v);
        this.thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
        this.title = (TextView) v.findViewById(R.id.title);
        this.toggleNative = (ToggleButton) v.findViewById(R.id.toggleNative);
        this.btnStart = (Button) v.findViewById(R.id.btnStart);
    }
}
