package com.example.android.bakingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.UI.RecipeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vamsi on 9/7/2017.
 */

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.HomePageViewHolder> {
    private String[] mDataSet;
    public static final String RECIPE = "recipe";

    public HomePageAdapter(String[] recipeTitles){
        mDataSet = recipeTitles;
    }
    @Override
    public HomePageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout parentview = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homepage_card, parent, false);
        return new HomePageViewHolder(parentview);
    }

    @Override
    public void onBindViewHolder(HomePageViewHolder holder, int position) {
        holder.mTextView.setText(mDataSet[position]);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    class HomePageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final Context mContext;
        @BindView(R.id.homepage_textview)
        TextView mTextView;
        HomePageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mContext = itemView.getContext();
        }

        @Override
        public void onClick(View view) {
            //Intent takes you to the recipe activity
            Intent intent = new Intent(mContext, RecipeActivity.class);
            intent.putExtra(RECIPE, getAdapterPosition());
            mContext.startActivity(intent);
        }
    }
}
