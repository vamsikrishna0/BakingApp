package com.example.android.bakingapp.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.bakingapp.Adapters.HomePageAdapter;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeDetailsActivity;
import com.example.android.bakingapp.Utilities.RecipeJsonHelper;

import org.json.JSONException;
import org.json.JSONObject;

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
    JSONObject mRecipeJson;

    public static final String CUR_CHOICE = "curChoice";
    public static final String RECIPE_JSON = "recipeJson";
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set the adapter
        mRecipePosition = getActivity().getIntent()
                .getIntExtra(HomePageAdapter.RECIPE, 0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item,
                RecipeJsonHelper.getRecipeStepDescriptions(mRecipePosition));
        setListAdapter(adapter);

        //Check if dualpane mode
        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if(savedInstanceState != null){
            mRecipeStepPosition = savedInstanceState.getInt(CUR_CHOICE, 0);
            try {
                mRecipeJson = new JSONObject(savedInstanceState.getString(RECIPE_JSON));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            mRecipeStepPosition = 0;
            mRecipeJson = RecipeJsonHelper.getRecipeJsonObject(mRecipePosition);
        }


        if(mDualPane){
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
            showDetails(mRecipeStepPosition);
        }
    }

    private void showDetails(int recipeStepPosition) {
        mRecipeStepPosition = recipeStepPosition;

        if(mDualPane){
            getListView().setItemChecked(recipeStepPosition, true);

            RecipeDetailsFragment details = (RecipeDetailsFragment) getFragmentManager()
                    .findFragmentById(R.id.details);
            if(details == null || details.getShownIndex() != recipeStepPosition){
                details = RecipeDetailsFragment.newInstance(recipeStepPosition, mRecipeJson);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.details, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        }else{
            Intent intent = new Intent();
            intent.setClass(getActivity(), RecipeDetailsActivity.class);
            intent.putExtra(CUR_CHOICE, recipeStepPosition);
            intent.putExtra(RECIPE_JSON, mRecipeJson.toString());
            startActivity(intent);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("TitlesFragment", position+"");
        showDetails(position);
    }

    //Any activity which wants to use this fragment has to implement this interface.
    //That is why, it was moved into Fragment itself, as it is by design part of this fragment.
//    public interface OnSelectionChangedListener{
//        void onSelectionChanged(int index, JSONObject recipeData);
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CUR_CHOICE, mRecipeStepPosition);
        outState.putString(RECIPE_JSON, mRecipeJson.toString());
    }
}
