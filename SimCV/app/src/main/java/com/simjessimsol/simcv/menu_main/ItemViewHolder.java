package com.simjessimsol.simcv.menu_main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.simjessimsol.simcv.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    protected ImageView thumbnail;
    protected TextView title;
    protected ImageButton btnStart;
    protected View arrow;
    protected View hairLine;
    protected View expandArea;
    protected FrameLayout collapse_expand;
    protected LinearLayout listItem;
    protected LinearLayout seekbarLayout;
    protected LinearLayout switchLayout;
    protected Switch switchAlternative;
    protected SeekBar seekScale;
    protected TextView textScaleValue;
    protected TextView textDescription;

    public ItemViewHolder(View v) {
        super(v);
        this.thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
        this.title = (TextView) v.findViewById(R.id.title);
        this.btnStart = (ImageButton) v.findViewById(R.id.btnStart);
        this.arrow = v.findViewById(R.id.arrow);
        this.hairLine = v.findViewById(R.id.hairline);
        this.expandArea = v.findViewById(R.id.expand_area);
        this.collapse_expand = (FrameLayout) v.findViewById(R.id.collapse_expand);
        this.listItem = (LinearLayout) v.findViewById(R.id.list_item);
        this.seekbarLayout = (LinearLayout) v.findViewById(R.id.seekbarLayout);
        this.switchLayout = (LinearLayout) v.findViewById(R.id.switchLayout);
        this.switchAlternative = (Switch) v.findViewById(R.id.switchAlternative);
        this.seekScale = (SeekBar) v.findViewById(R.id.seekScale);
        this.textScaleValue = (TextView) v.findViewById(R.id.textScaleValue);
        this.textDescription = (TextView) v.findViewById(R.id.textDescription);

        this.seekScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textScaleValue.setText("1/" + String.valueOf(progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }
}
