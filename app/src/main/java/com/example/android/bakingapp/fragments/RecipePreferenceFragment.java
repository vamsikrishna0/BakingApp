package com.example.android.bakingapp.fragments;


import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utilities.RecipeJsonHelper;

import butterknife.BindString;
import butterknife.ButterKnife;

import static com.example.android.bakingapp.R.string.UPDATE_WIDGETS;

public class RecipePreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static String UPDATE_WIDGETS;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UPDATE_WIDGETS = getString(R.string.UPDATE_WIDGETS);
//        Log.i("RecipePreferenceFrag", "UPDATE_WIDGETS "+UPDATE_WIDGETS);

        addPreferencesFromResource(R.xml.preference_recipe);
        ListPreference preference = (ListPreference) findPreference(
                getString(R.string.pref_recipe_position_key));
        RecipeJsonHelper.loadData(getActivity());
        String[] listentries = RecipeJsonHelper.getRecipeTitles();
        preference.setEntries(listentries);
        String[] listEntryValues = new String[listentries.length];
        for (int i = 0; i < listentries.length; i++) {
            listEntryValues[i] = i+"";
        }
        preference.setEntryValues(listEntryValues);
        preference.setDefaultValue(listentries[0]);
        preference.setSummary(preference.getEntry());
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        ListPreference preference = (ListPreference) findPreference(
                getString(R.string.pref_recipe_position_key));
        preference.setSummary(preference.getEntry());

        Intent intent = new Intent();
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(UPDATE_WIDGETS, 0);
        getActivity().sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
