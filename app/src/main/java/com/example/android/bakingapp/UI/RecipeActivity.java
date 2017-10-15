package com.example.android.bakingapp.UI;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.android.bakingapp.Adapters.HomePageAdapter;
import com.example.android.bakingapp.Fragments.RecipeDetailsFragment;
import com.example.android.bakingapp.Fragments.RecipeTitlesFragment;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Utilities.RecipeJsonHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindString;
import butterknife.ButterKnife;


public class RecipeActivity extends AppCompatActivity implements OnTitleSelectionChangedListener {

    String NUMBER_OF_STEPS;
    String RECIPE_JSON ;
    String RECIPE_POSITION;
    String RECIPE;
    String RECIPE_STEP_POSITION;
    String CHEESECAKE_STEP7_STRING;

    public static int REQUEST_CODE = 3;

    boolean mDualPane;
    JSONObject mRecipeJson;
    int mRecipePosition;
    int mNoOfRecipeSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        NUMBER_OF_STEPS = getString(R.string.NUMBER_OF_STEPS);
        RECIPE_JSON = getString(R.string.RECIPE_JSON);
         RECIPE_POSITION = getString(R.string.RECIPE_POSITION);
         RECIPE = getString(R.string.RECIPE);
         RECIPE_STEP_POSITION = getString(R.string.RECIPE_STEP_POSITION);
         CHEESECAKE_STEP7_STRING = getString(R.string.CHEESECAKE_STEP7_STRING);
        if(savedInstanceState != null){
            mRecipePosition = savedInstanceState.getInt(RECIPE_POSITION, 0);
            mNoOfRecipeSteps = savedInstanceState.getInt(NUMBER_OF_STEPS);
            try {
                mRecipeJson = new JSONObject(savedInstanceState.getString(RECIPE_JSON));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            if(RECIPE_STEP_POSITION == null)
            Log.i("Blahhhh", "Not sending properly");
            else
                Log.i("Blahhhh", "sending properly" + RECIPE +" "+ getIntent().getExtras().getInt(RECIPE));
            if(RECIPE == null)
                Log.i("Blahhhh", "Not sending properly");
            else
                Log.i("Blahhhh", "sending properly" + RECIPE +" "+ getIntent().getExtras().getInt(RECIPE));
            if(RECIPE_POSITION == null)
                Log.i("Blahhhh", "Not sending properly");
            else
                Log.i("Blahhhh", "sending properly" + RECIPE +" "+ getIntent().getExtras().getInt(RECIPE));

            mRecipePosition = getIntent().getExtras().getInt(RECIPE);
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
        if(recipeStepPosition < 0 || recipeStepPosition >= mNoOfRecipeSteps)
            return;

        if(mDualPane){

            RecipeDetailsFragment details = (RecipeDetailsFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.details);
            if(details == null || details.getShownIndex() != recipeStepPosition){
                details = RecipeDetailsFragment.newInstance(recipeStepPosition, mRecipeJson, mNoOfRecipeSteps, this);

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
                int temp = data.getIntExtra(getString(R.string.RESULT_DATA_POSITION), 0);
                showDetails(temp);
            }
        }
    }
}
