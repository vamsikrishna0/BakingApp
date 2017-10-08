package com.example.android.bakingapp.UI;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.Fragments.RecipeDetailsFragment;
import com.example.android.bakingapp.Fragments.RecipeTitlesFragment;
import com.example.android.bakingapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeDetailsActivity extends AppCompatActivity implements OnTitleSelectionChangedListener{
    public static final String RESULT_DATA_POSITION = "result_position";

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
                        .newInstance(extras.getInt(RecipeTitlesFragment.RECIPE_STEP_POSITION),
                                new JSONObject(extras.getString(RecipeActivity.RECIPE_JSON)),
                                extras.getInt(RecipeActivity.NUMBER_OF_STEPS));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            getSupportFragmentManager().beginTransaction().add(R.id.content, details).commit();
        }
    }

    @Override
    public void showDetails(int position) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_DATA_POSITION, position);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
