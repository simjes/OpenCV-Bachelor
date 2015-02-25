package com.simjessimsol.simcv.menu_main;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simjessimsol.simcv.R;

/**
 * Created by Simen on 25.02.2015.
 */
public class CardViewHolder extends RecyclerView.ViewHolder {
    protected ImageView thumbnail;
    protected TextView title;

    public CardViewHolder(View itemView) {
        super(itemView);
        this.thumbnail = (ImageView) itemView.findViewById(R.id.banner);
        this.title = (TextView) itemView.findViewById(R.id.title);
    }
}
