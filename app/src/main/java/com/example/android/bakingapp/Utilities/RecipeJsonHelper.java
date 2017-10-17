package com.example.android.bakingapp.Utilities;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.bakingapp.Adapters.HomePageAdapter;
import com.example.android.bakingapp.Utilities.data.Ingredient;
import com.example.android.bakingapp.Utilities.data.Recipe;
import com.example.android.bakingapp.Utilities.data.Step;
import com.example.android.bakingapp.Utilities.networkutils.ApiUtils;
import com.example.android.bakingapp.Utilities.networkutils.RecipeService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vamsi on 8/28/2017.
 */

//Helper class to get json data from the given data
public class RecipeJsonHelper {

    public static final String INGREDIENTS = "ingredients";
//    public static final String INGREDIENT = "ingredient";
//    public static final String QUANTITY = "quantity";
//    public static final String MEASURE = "measure";
//    public static final String STEPS = "steps";
//    public static final String DESCRIPTION = "shortDescription";
//    public static final String LONG_DESCRIPTION = "description";
//    public static final String VIDEO_URL = "videoURL";
//    public static final String THUMBNAIL_URL = "thumbnailURL";
    public static List<Recipe> recipes = null;

    //Get a String containing all the ingredients
    public static String getIngredientsStringForRecipe(Recipe recipeObj) {
        StringBuilder objString = new StringBuilder();
        List<Ingredient> ingredientsObj = recipeObj.getIngredients();
        for (int i = 0; i < ingredientsObj.size(); i++) {
            Ingredient ing = ingredientsObj.get(i);
            objString.append("- ").append(ing.getIngredient())
                    .append(" : ").append(ing.getQuantity())
                    .append(" ").append(ing.getMeasure())
                    .append("\n");
        }

        return objString.toString();
    }

    //Get the RecipeObject for a particular recipe
    public static Recipe getRecipeObject(int id) {
//        List<Recipe> recipes = loadRecipes();
        return recipes.get(id);
    }

    //Get the JSONObject for the recipe step, 'stepPosition' on the JSONObject, 'json'
    public static Step getRecipeStepJsonObject(int stepPosition, Recipe recipeObj) {
        return recipeObj.getSteps().get(stepPosition);
    }

    //Get a string[] of description on steps for recipe at position, 'id'
    public static String[] getRecipeStepDescriptions(int id) {
//        List<Recipe> recipes = loadRecipes();
        ArrayList<String> data = new ArrayList<>();
        data.add(INGREDIENTS);
        Recipe recipe = recipes.get(id);
        List<Step> steps = recipe.getSteps();
        for (int i = 0; i < steps.size(); i++) {
            String desc = steps.get(i).getShortDescription();
            data.add(desc);
        }
        return data.toArray(new String[data.size()]);
    }

    public static int getNumberOfRecipeSteps(int id) {
//        List<Recipe> recipes = loadRecipes();
        return recipes.get(id).getSteps().size();
    }

    //Get Titles of all recipes as a String[]
    public static String[] getRecipeTitles() {
        int len = recipes.size();
        String[] dataSet = new String[len];
        for (int i = 0; i < len; i++) {
            dataSet[i] = recipes.get(i).getName();
        }
        return dataSet;
    }

    public static List<Recipe> loadRecipes() {
        if (recipes == null) {

        }

        return recipes;
    }

    public static void loadAdapterData(final HomePageAdapter mAdapter) {
        Log.i("RecipeJsonHelper", "API call");
        RecipeService mService = ApiUtils.getRecipeService();
        mService.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    recipes = response.body();
                    mAdapter.updateTitles(getRecipeTitles());
                    Log.d("MainActivity", "posts loaded from API");
                } else {
                    int statusCode = response.code();
                    // handle request errors depending on status code
                    Log.d("MainActivity", "posts not loaded from API");
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d("MainActivity", "error loading from API");
            }
        });
    }
}
