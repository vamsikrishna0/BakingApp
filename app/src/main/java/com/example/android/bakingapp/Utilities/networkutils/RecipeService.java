package com.example.android.bakingapp.Utilities.networkutils;

import com.example.android.bakingapp.Utilities.data.Recipe;

import retrofit2.http.GET;
import java.util.List;
import retrofit2.Call;

public interface RecipeService {

    @GET("/android-baking-app-json")
    Call<List<Recipe>> getRecipes();
}
