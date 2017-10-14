package com.example.android.bakingapp.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.bakingapp.Adapters.HomePageAdapter;
import com.example.android.bakingapp.UI.OnTitleSelectionChangedListener;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Utilities.RecipeJsonHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeTitlesFragment extends ListFragment {

    public RecipeTitlesFragment() {
        // Required empty public constructor
    }
    int mRecipePosition;
    int mRecipeStepPosition;
    boolean mDualPane;


    public static final String RECIPE_STEP_POSITION = "curChoice";
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set the adapter
        mRecipePosition = getActivity().getIntent()
                .getIntExtra(HomePageAdapter.RECIPE, 0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item,
                RecipeJsonHelper.getRecipeStepDescriptions(mRecipePosition));
        setListAdapter(adapter);

        if(savedInstanceState != null){
            mRecipeStepPosition = savedInstanceState.getInt(RECIPE_STEP_POSITION, 0);

        }else{
            mRecipeStepPosition = 0;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("TitlesFragment", position+"");
        OnTitleSelectionChangedListener listener = (OnTitleSelectionChangedListener) getActivity();
        listener.showDetails(position);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECIPE_STEP_POSITION, mRecipeStepPosition);
    }

    public int getCurRecipeStepPosition() {
        return mRecipeStepPosition;
    }
}
