package com.simjessimsol.simcv.menu_main;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.simjessimsol.simcv.R;

/**
 * Created by Simen on 25.02.2015.
 */
public class ItemViewHolder extends RecyclerView.ViewHolder {
    protected ImageView thumbnail;
    protected TextView title;
    protected ToggleButton toggleNative;
    protected ImageButton btnStart;

    public ItemViewHolder(View v) {
        super(v);
        this.thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
        this.title = (TextView) v.findViewById(R.id.title);
        this.toggleNative = (ToggleButton) v.findViewById(R.id.toggleNative);
        this.btnStart = (ImageButton) v.findViewById(R.id.btnStart);
    }
}
