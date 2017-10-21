package com.example.android.bakingapp.ui;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utilities.RecipeJsonHelper;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
//        Log.i("RecipeWidget", "update views");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String recipe_position = preferences.getString(
                context.getString(R.string.pref_recipe_position_key), "0");

        String jsonStr = preferences.getString(RecipeJsonHelper.JSON_STRING, "");
        int pos = Integer.parseInt(recipe_position);
        String widgetText = "";
        if(!jsonStr.equals(""))
        widgetText = RecipeJsonHelper
                .getIngredientsStringForRecipe(RecipeJsonHelper.getRecipeObject(pos, context));

        widgetText = widgetText + "\n [Click here to choose recipe]";
        views.setTextViewText(R.id.appwidget_text, widgetText);

        Intent intent = new Intent(context, RecipePreferenceActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.hasExtra(context.getString(R.string.UPDATE_WIDGETS))){
            Log.i("RecipeWidget", "Intent received, when pref changed");
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            int[] ids = manager.getAppWidgetIds(new ComponentName(context, RecipeWidget.class));
            onUpdate(context, manager, ids);
        }else{
            Log.i("RecipeWidget", "Intent not received, when pref changed");
            super.onReceive(context, intent);
        }
    }
}

