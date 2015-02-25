package com.simjessimsol.simcv.menu_main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.simjessimsol.simcv.R;

/**
 * Created by Simen on 25.02.2015.
 */
public class MainMenuActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        //improves performance if we know that changes in content doesn't change layout
        mRecyclerView.setHasFixedSize(true);

        //we use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        String[] dataSet = {"Simen", "Rikard", "Christer"};

        //specify the adapter we want to use
        mAdapter = new MainMenuAdapter(dataSet);
        mRecyclerView.setAdapter(mAdapter);
    }

}
