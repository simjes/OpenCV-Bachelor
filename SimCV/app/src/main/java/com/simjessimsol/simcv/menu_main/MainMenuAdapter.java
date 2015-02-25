package com.simjessimsol.simcv.menu_main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simjessimsol.simcv.R;

/**
 * Created by Simen on 25.02.2015.
 */
public class MainMenuAdapter extends RecyclerView.Adapter<FunctionRowHolder> {
    private String[] mDataset;

    public MainMenuAdapter(String[] myDataSet){
        mDataset = myDataSet;
    }

    @Override
    public FunctionRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_list_row, null);
        FunctionRowHolder rh = new FunctionRowHolder(v);
        return rh;
    }

    @Override
    public void onBindViewHolder(FunctionRowHolder functionRowHolder, int holderPosition) {
        functionRowHolder.title.setText(mDataset[holderPosition]);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
