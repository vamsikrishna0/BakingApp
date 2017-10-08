package com.example.android.bakingapp.UI;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.android.bakingapp.Adapters.HomePageAdapter;
import com.example.android.bakingapp.Fragments.RecipeDetailsFragment;
import com.example.android.bakingapp.Fragments.RecipeTitlesFragment;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Utilities.RecipeJsonHelper;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.android.bakingapp.Fragments.RecipeTitlesFragment.RECIPE_STEP_POSITION;

public class RecipeActivity extends AppCompatActivity implements OnTitleSelectionChangedListener {
    public static final String NUMBER_OF_STEPS = "number_of_steps";
    public static final String RECIPE_JSON = "recipe_json";
    public static final String RECIPE_POSITION = "recipe_position";
    public static final int REQUEST_CODE = 3;

    boolean mDualPane;
    JSONObject mRecipeJson;
    int mRecipePosition;
    int mNoOfRecipeSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        if(savedInstanceState != null){
            mRecipePosition = savedInstanceState.getInt(RECIPE_POSITION, 0);
            mNoOfRecipeSteps = savedInstanceState.getInt(NUMBER_OF_STEPS);
            try {
                mRecipeJson = new JSONObject(savedInstanceState.getString(RECIPE_JSON));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            mRecipePosition = getIntent().getIntExtra(HomePageAdapter.RECIPE, 0);
            mRecipeJson = RecipeJsonHelper.getRecipeJsonObject(mRecipePosition);
            mNoOfRecipeSteps = RecipeJsonHelper.getNumberOfRecipeSteps(mRecipePosition);
        }

        //Check if dualpane mode
        View detailsFrame = findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (mDualPane) { 

                RecipeTitlesFragment titlesFragment = (RecipeTitlesFragment)
                        getSupportFragmentManager().findFragmentById(R.id.titles);
                titlesFragment.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                int recipeStepPosition = titlesFragment.getCurRecipeStepPosition();
                showDetails(recipeStepPosition);
        } else {
            RecipeTitlesFragment titlesFragment = new RecipeTitlesFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.singlepane_container, titlesFragment).commit();
        }

    }

    public void showDetails(int recipeStepPosition) {
        //mRecipeStepPosition = recipeStepPosition;
        if(recipeStepPosition < 0 || recipeStepPosition >= mNoOfRecipeSteps)
            return;

        if(mDualPane){
            //getListView().setItemChecked(recipeStepPosition, true);

            RecipeDetailsFragment details = (RecipeDetailsFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.details);
            if(details == null || details.getShownIndex() != recipeStepPosition){
                details = RecipeDetailsFragment.newInstance(recipeStepPosition, mRecipeJson, mNoOfRecipeSteps);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.details, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        }else{
            Intent intent = new Intent();
            intent.setClass(this, RecipeDetailsActivity.class);
            intent.putExtra(RECIPE_STEP_POSITION, recipeStepPosition);
            intent.putExtra(RECIPE_JSON, mRecipeJson.toString());
            intent.putExtra(NUMBER_OF_STEPS, mNoOfRecipeSteps);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECIPE_POSITION, mRecipePosition);
        outState.putString(RECIPE_JSON, mRecipeJson.toString());
        outState.putInt(NUMBER_OF_STEPS, mNoOfRecipeSteps);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                int temp = data.getIntExtra(RecipeDetailsActivity.RESULT_DATA_POSITION, 0);
                showDetails(temp);
            }
        }
    }
    //    public void onSelectionChanged(int index, JSONObject recipeData) {
//        RecipeDetailsFragment detailFragment = (RecipeDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details);
//        //If two pane?
//        if(detailFragment != null){
//            Log.i("RecipeActivity", index+"");
//            detailFragment.update(index, recipeData);
//        }else{
//            RecipeDetailsFragment newDetailsFragment = new RecipeDetailsFragment();
//            Bundle args = new Bundle();
//            args.putInt(TITLE_INDEX, index);
//            args.putString(RECIPE_JSON, recipeData.toString());
//            newDetailsFragment.setArguments(args);
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.singlepane_container, newDetailsFragment)
//                    .addToBackStack(null)
//                    .commit();
//        }
//  }
}
