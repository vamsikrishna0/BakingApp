package com.example.android.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.bakingapp.adapters.HomePageAdapter;
import com.example.android.bakingapp.utilities.RecipeJsonHelper;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.homepage_recycler_view)
    RecyclerView mRecyclerView;

    HomePageAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @BindBool(R.bool.isTablet)
    boolean isTablet;
    public static final int numOfSpans = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(isTablet){
            mLayoutManager = new GridLayoutManager(this, numOfSpans);
        }else{
            mLayoutManager = new LinearLayoutManager(this);
        }

        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new HomePageAdapter(new String[0]);
        RecipeJsonHelper.loadAdapterDataAfterNetworkCall(mAdapter, this);
        mRecyclerView.setAdapter(mAdapter);
    }
}
