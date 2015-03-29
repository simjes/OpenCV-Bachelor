package com.simjessimsol.simcv.menu_main;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.simjessimsol.simcv.R;
import com.simjessimsol.simcv.colortracker.Drawtivity;
import com.simjessimsol.simcv.detection.CircleDetection;
import com.simjessimsol.simcv.detection.FaceDetection;
import com.simjessimsol.simcv.detection.ForegroundDetection;
import com.simjessimsol.simcv.nonopencv.noncvdrawwithgl.TrackerOpenGL;

/**
 * Created by Simen on 25.02.2015.
 */
public class ItemViewHolder extends RecyclerView.ViewHolder {
    protected ImageView thumbnail;
    protected TextView title;
    protected ImageButton btnStart;
    protected View arrow;
    protected View hairLine;
    protected View expandArea;
    protected View collapseExpandArea;
    protected LinearLayout listItem;
    protected Switch switchAlternative;
    protected SeekBar seekScale;
    protected TextView textScaleValue;
    protected FrameLayout collapse_expand;

    public ItemViewHolder(View v) {
        super(v);
        this.thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
        this.title = (TextView) v.findViewById(R.id.title);
        this.btnStart = (ImageButton) v.findViewById(R.id.btnStart);
        this.arrow = v.findViewById(R.id.arrow);
        this.hairLine = v.findViewById(R.id.hairline);
        this.expandArea = v.findViewById(R.id.expand_area);
        this.collapseExpandArea = v.findViewById(R.id.collapse_expand);
        this.listItem = (LinearLayout) v.findViewById(R.id.list_item);
        this.switchAlternative = (Switch) v.findViewById(R.id.switchAlternative);
        this.seekScale = (SeekBar) v.findViewById(R.id.seekScale);
        this.textScaleValue = (TextView) v.findViewById(R.id.textScaleValue);
        this.collapse_expand = (FrameLayout) v.findViewById(R.id.collapse_expand);

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
