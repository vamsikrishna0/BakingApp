package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.Adapters.HomePageAdapter;
import com.example.android.bakingapp.UI.RecipeActivity;
import com.example.android.bakingapp.Utilities.RecipeJsonHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
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
import static com.example.android.bakingapp.Fragments.RecipeTitlesFragment.RECIPE_STEP_POSITION;
import static com.example.android.bakingapp.MainActivityTests.CHEESECAKE_POSITION;
import static com.example.android.bakingapp.UI.RecipeActivity.NUMBER_OF_STEPS;
import static com.example.android.bakingapp.UI.RecipeActivity.RECIPE_JSON;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
//ActivityTestRule set up. One class for testing, one activity
public class RecipeActivityTests {

    private static final String CHEESECAKE_STEP9_STRING = "Bake the cheesecake.";
    private static final String CHEESECAKE_STEP9_DESC_STRING = "9. Bake the cheesecake on a middle rack of the oven above the roasting pan full of water for 50 minutes. ";
    private static final String CHEESECAKE_STEP7_DESC_STRING = "7. Add the cream and remaining tablespoon of vanilla to the batter and beat on medium-low speed until just incorporated. ";
    private static final String RECIPE_DETAILS_A_SHORTNAME= ".UI.RecipeDetailsActivity";
    @Rule
    public ActivityTestRule<RecipeActivity> mActivityRule = new ActivityTestRule<RecipeActivity>
            (RecipeActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            Intent result = new Intent(targetContext, RecipeActivity.class);
            result.putExtra(HomePageAdapter.RECIPE, CHEESECAKE_POSITION);
            return result;
        }
    };

    private boolean isTablet;

    @Before
    public void setUp(){
        Intents.init();
        isTablet = mActivityRule.getActivity().getResources().getBoolean(R.bool.isTablet);
    }

    @Test
    public void checkIfTwoPanesDisplayedInTabletMode(){
        if(isTablet){
//            onView(allOf(withText(RecipeJsonHelper.INGREDIENTS), )).
            onData(allOf(is(instanceOf(String.class)), is(RecipeJsonHelper.INGREDIENTS)))
                    .check(matches(isDisplayed()));

            onView(withId(R.id.exoplayer_view)).check(matches(not(isDisplayed())));
            String ingString = RecipeJsonHelper.getIngredientsStringForRecipe
                    (RecipeJsonHelper.getRecipeJsonObject(CHEESECAKE_POSITION));

            onView(withText(ingString)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void checkIfClickingOnAStep_withVideoURL_OpensTheProperFragment(){
        onData(allOf(is(instanceOf(String.class)), is(MainActivityTests.CHEESECAKE_STEP7_STRING)))
                .perform(click());

        if(isTablet){
            //Exoplayer is VISIBLE
            onView(withId(R.id.exoplayer_view)).check(matches(isDisplayed()));

            onView(allOf(withId(R.id.recipestep_description_view), withText(CHEESECAKE_STEP7_DESC_STRING)))
                    .check(matches(isDisplayed()));
        }else{
            //Single pane
            intended(allOf(hasComponent(hasShortClassName(RECIPE_DETAILS_A_SHORTNAME)),
                    hasExtra(RECIPE_STEP_POSITION, 7)));
        }
    }

    @Test
    public void checkIfClickingOnAStep_withoutVideoURL_OpensTheProperFragment(){
        onData(allOf(is(instanceOf(String.class)), is(CHEESECAKE_STEP9_STRING)))
                .perform(click());
        if(isTablet){
            //Exoplayer is set to GONE
            onView(withId(R.id.exoplayer_view)).check(matches(not(isDisplayed())));
            onView(allOf(withId(R.id.recipestep_description_view), withText(CHEESECAKE_STEP9_DESC_STRING)))
                    .check(matches(isDisplayed()));
        }else{
            //Single pane
            intended(allOf(hasComponent(hasShortClassName(RECIPE_DETAILS_A_SHORTNAME)),
                    hasExtra(RECIPE_STEP_POSITION, 9)));
        }
    }
    @After
    public void tearDown(){
        Intents.release();
    }
}
