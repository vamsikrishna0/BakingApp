package com.example.android.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.bakingapp.Fragments.RecipeDetailsFragment;
import com.example.android.bakingapp.Fragments.RecipeTitlesFragment;

import org.json.JSONObject;

import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity{
    public static final String TITLE_INDEX = "index";
    public static final String RECIPE_JSON = "recipe_json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        if(findViewById(R.id.singlepane_container) != null){

            if(savedInstanceState != null)
                return;

            RecipeTitlesFragment titlesFragment = new RecipeTitlesFragment();
            titlesFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
            .add(R.id.singlepane_container,titlesFragment)
            .commit();

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
