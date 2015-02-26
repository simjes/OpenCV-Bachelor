package com.simjessimsol.simcv.menu_main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.simjessimsol.simcv.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Simen Sollie on 25.02.2015.
 */
public class MainMenuAdapter extends RecyclerView.Adapter<CardViewHolder> {
    private String[] dataSet;
    private Context context;
    private Boolean isNative;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View v, int pos);
    }

    public MainMenuAdapter(Context context, String[] dataSet, Boolean isNative, OnItemClickListener mOnItemClickListener){
        this.context = context;
        this.dataSet = dataSet;
        this.isNative = isNative;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_card_view, null);
        CardViewHolder vh = new CardViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, final int position) {
        Picasso.with(context).load(R.drawable.ic_launcher)
                .into(cardViewHolder.thumbnail);
        cardViewHolder.title.setText(dataSet[position]);
        cardViewHolder.toggleNative.setVisibility(View.INVISIBLE);
        cardViewHolder.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }
}
