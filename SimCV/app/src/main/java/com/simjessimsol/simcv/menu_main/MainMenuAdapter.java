package com.simjessimsol.simcv.menu_main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simjessimsol.simcv.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Simen on 25.02.2015.
 */
public class MainMenuAdapter extends RecyclerView.Adapter<CardViewHolder> {
    private String[] dataSet;
    private Context context;
    private Boolean isNative;

    public MainMenuAdapter(Context context, String[] dataSet, Boolean isNative){
        this.context = context;
        this.dataSet = dataSet;
        this.isNative = isNative;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_card_view, null);
        CardViewHolder rh = new CardViewHolder(v);
        return rh;
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        Picasso.with(context).load(R.drawable.ic_launcher)
                .into(cardViewHolder.thumbnail);
        cardViewHolder.title.setText(dataSet[i]);

    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }
}
