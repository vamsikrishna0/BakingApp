package com.example.android.bakingapp.UI;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.example.android.bakingapp.Fragments.RecipePreferenceFragment;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Utilities.RecipeJsonHelper;

import java.nio.charset.IllegalCharsetNameException;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String recipe_position = preferences.getString(context.getString(R.string.pref_recipe_position_key), "0");
        int pos = Integer.parseInt(recipe_position);
        String widgetText = RecipeJsonHelper
                .getIngredientsStringForRecipe(RecipeJsonHelper.getRecipeJsonObject(pos));
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
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.hasExtra(RecipePreferenceFragment.UPDATE_WIDGETS)){
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            int[] ids = manager.getAppWidgetIds(new ComponentName(context, RecipeWidget.class));
            onUpdate(context, manager, ids);
        }else
        super.onReceive(context, intent);
    }
}

