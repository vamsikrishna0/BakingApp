package com.example.android.bakingapp.Utilities.networkutils;

public class ApiUtils {

    public static final String BASE_URL = "https://go.udacity.com/";

    public static RecipeService getRecipeService() {
        return RetrofitClient.getClient(BASE_URL).create(RecipeService.class);
    }
}
