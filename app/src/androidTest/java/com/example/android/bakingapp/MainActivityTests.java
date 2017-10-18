package com.example.android.bakingapp;

import android.content.res.Resources;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onIdle;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasEntry;

@RunWith(AndroidJUnit4.class)
public class MainActivityTests {
    static final int CHEESECAKE_POSITION = 3;

    private static final String CHEESECAKE = "Cheesecake";

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<MainActivity>(
            MainActivity.class);

    Resources res = getInstrumentation().getTargetContext().getResources();
    //Only tests cheesecake, as an example
    @Test
    public void scrollToCheeseCake_checkIfItOpensTheCorrectRecipeActivity(){
        onView(withId(R.id.homepage_recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(CHEESECAKE_POSITION));

        onView(allOf(withText(CHEESECAKE), withId(R.id.homepage_textview)))
                .perform(click());
        intended(allOf(hasExtra(res.getString(R.string.RECIPE), CHEESECAKE_POSITION),
                hasComponent(hasShortClassName(".UI.RecipeActivity"))));
    }
    @Test
    public void checkIfTheRecyclerViewHasAllRecipeNames(){
        //We are basically testing one, but we could add these two statements for each recipe
        onView(withId(R.id.homepage_recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(CHEESECAKE_POSITION));

        onView(withText(CHEESECAKE)).check(matches(isDisplayed()));
    }

}

