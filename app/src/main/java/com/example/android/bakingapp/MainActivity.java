package com.example.android.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.bakingapp.Adapters.HomePageAdapter;
import com.example.android.bakingapp.Utilities.RecipeJsonHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.homepage_recycler_view)
    RecyclerView mRecyclerView;

    RecyclerView.Adapter mAdapter;
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

        mAdapter = new HomePageAdapter(RecipeJsonHelper.getRecipeTitles());
        mRecyclerView.setAdapter(mAdapter);
    }
}
