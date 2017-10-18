package com.example.android.bakingapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.bakingapp.fragments.RecipeDetailsFragment;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utilities.data.Recipe;

import butterknife.BindString;
import butterknife.ButterKnife;

public class RecipeDetailsActivity extends AppCompatActivity implements OnTitleSelectionChangedListener{
    @BindString(R.string.RESULT_DATA_POSITION) public String RESULT_DATA_POSITION;
    @BindString(R.string.NUMBER_OF_STEPS) public String NUMBER_OF_STEPS;
    @BindString(R.string.RECIPE_JSON) public String RECIPE_JSON;
    @BindString(R.string.RECIPE_STEP_POSITION) public String RECIPE_STEP_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            Bundle extras = getIntent().getExtras();

            RecipeDetailsFragment details = RecipeDetailsFragment
                    .newInstance(extras.getInt(RECIPE_STEP_POSITION),
                            (Recipe) extras.getSerializable(RECIPE_JSON),
                            extras.getInt(NUMBER_OF_STEPS), this);
            getSupportFragmentManager().beginTransaction().add(R.id.content, details).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void showDetails(int position) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_DATA_POSITION, position);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
