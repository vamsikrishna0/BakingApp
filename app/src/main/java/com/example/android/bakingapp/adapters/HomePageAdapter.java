package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.ui.RecipeActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.HomePageViewHolder>{
    private String[] mDataSet;
    private String[] mImageUris;
    @BindString(R.string.RECIPE) public String RECIPE;

    public void updateData(String[] recipeTitles, String[] imageUris){
        mDataSet = recipeTitles;
        mImageUris = imageUris;
        notifyDataSetChanged();
    }
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
        if(!mImageUris[position].equals(""))
        Picasso.with(holder.mContext).load(mImageUris[position]).into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    class HomePageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindString(R.string.RECIPE) public String RECIPE;

        private final Context mContext;
        @BindView(R.id.homepage_textview) TextView mTextView;
        @BindView(R.id.image) ImageView mImage;
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
            if(RECIPE == null)
                Log.i("Blahhhh2", "Not sending properly");
            else
                Log.i("Blahhhh2", "sending properly" + getAdapterPosition());
            intent.putExtra(RECIPE, getAdapterPosition());
            mContext.startActivity(intent);
        }
    }
}
