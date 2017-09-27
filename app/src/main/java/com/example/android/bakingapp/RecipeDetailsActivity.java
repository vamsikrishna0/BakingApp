package com.example.android.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.Fragments.RecipeDetailsFragment;
import com.example.android.bakingapp.Fragments.RecipeTitlesFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            Bundle extras = getIntent().getExtras();

            RecipeDetailsFragment details = null;
            try {
                details = RecipeDetailsFragment
                        .newInstance(extras.getInt(RecipeTitlesFragment.CUR_CHOICE), new JSONObject(extras.getString(RecipeTitlesFragment.RECIPE_JSON)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            getSupportFragmentManager().beginTransaction().add(R.id.content, details).commit();
        }
    }
}
