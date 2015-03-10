package com.simjessimsol.simcv.menu_main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    protected View arrow;
    protected View hairLine;
    protected View expandArea;
    protected View collapseExpandArea;
    protected LinearLayout listItem;

    public ItemViewHolder(View v) {
        super(v);
        this.thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
        this.title = (TextView) v.findViewById(R.id.title);
        this.toggleNative = (ToggleButton) v.findViewById(R.id.toggleNative);
        this.btnStart = (ImageButton) v.findViewById(R.id.btnStart);
        this.arrow = v.findViewById(R.id.arrow);
        this.hairLine = v.findViewById(R.id.hairline);
        this.expandArea = v.findViewById(R.id.expand_area);
        this.collapseExpandArea = v.findViewById(R.id.collapse_expand);
        this.listItem = (LinearLayout) v.findViewById(R.id.list_item);
    }
}
