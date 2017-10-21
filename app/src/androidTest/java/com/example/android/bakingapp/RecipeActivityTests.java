package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.adapters.HomePageAdapter;
import com.example.android.bakingapp.ui.RecipeActivity;
import com.example.android.bakingapp.utilities.RecipeJsonHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
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
import static com.example.android.bakingapp.MainActivityTests.CHEESECAKE_POSITION;
import static com.example.android.bakingapp.R.string.RECIPE_DETAILS_A_SHORTNAME;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
//ActivityTestRule set up. One class for testing, one activity
public class RecipeActivityTests {
    Resources res = getInstrumentation().getTargetContext().getResources();
    public String RECIPE = res.getString(R.string.RECIPE);
    public String RECIPE_STEP_POSITION = res.getString(R.string.RECIPE_STEP_POSITION);
    public String CHEESECAKE_STEP7_STRING = res.getString(R.string.CHEESECAKE_STEP7_STRING);

    public String CHEESECAKE_STEP9_STRING = res.getString(R.string.CHEESECAKE_STEP9_STRING) ;
    private String CHEESECAKE_STEP9_DESC_STRING = res.getString(R.string.CHEESECAKE_STEP9_DESC_STRING);
    private String CHEESECAKE_STEP7_DESC_STRING = res.getString(R.string.CHEESECAKE_STEP7_DESC_STRING);
    @Rule
    public ActivityTestRule<RecipeActivity> mActivityRule = new ActivityTestRule<RecipeActivity>
            (RecipeActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = getInstrumentation()
                    .getTargetContext();
            Intent result = new Intent(targetContext, RecipeActivity.class);
            result.putExtra(RECIPE, CHEESECAKE_POSITION);
            return result;
        }
    };

    private boolean isTablet;

    @BeforeClass
    public static void initializeClass(){
        RecipeJsonHelper.loadFromJsonStringForTest();
    }

    @AfterClass
    public static void releaseRes(){
        RecipeJsonHelper.recipes = null;
    }

    @Before
    public void setUp(){
        Intents.init();

        isTablet = res.getBoolean(R.bool.isTablet);
    }

    @Test
    public void checkIfTwoPanesDisplayedInTabletMode(){
        if(isTablet){
            onData(allOf(is(instanceOf(String.class)), is(RecipeJsonHelper.INGREDIENTS)))
                    .check(matches(isDisplayed()));

            onView(withId(R.id.exoplayer_view)).check(matches(not(isDisplayed())));
            String ingString = RecipeJsonHelper.getIngredientsStringForRecipe
                    (RecipeJsonHelper.getRecipeObject(CHEESECAKE_POSITION, mActivityRule.getActivity()));

            onView(withText(ingString)).check(matches(isDisplayed()));
        }
    }
    @Test
    public void checkIfStateIsMaintainedOnScreenRotation(){
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        checkIfTwoPanesDisplayedInTabletMode();
        checkIfClickingOnAStep_withoutVideoURL_OpensTheProperFragment();
    }
    @Test
    public void checkIfStateIsMaintainedOnScreenRotation2(){
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        checkIfTwoPanesDisplayedInTabletMode();
        checkIfClickingOnAStep_withVideoURL_OpensTheProperFragment();
    }

    @Test
    public void checkIfClickingOnAStep_withVideoURL_OpensTheProperFragment(){
        onData(allOf(is(instanceOf(String.class)), is(CHEESECAKE_STEP7_STRING)))
                .perform(click());

        if(isTablet){
            //Exoplayer is VISIBLE
            onView(withId(R.id.exoplayer_view)).check(matches(isDisplayed()));

            onView(allOf(withId(R.id.recipestep_description_view), withText(CHEESECAKE_STEP7_DESC_STRING)))
                    .check(matches(isDisplayed()));
        }else{
            //Single pane
            intended(allOf(hasComponent(hasShortClassName(res.getString(R.string.RECIPE_DETAILS_A_SHORTNAME))),
                    hasExtra(RECIPE_STEP_POSITION, 8)));
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
            intended(allOf(hasComponent(hasShortClassName(res.getString(R.string.RECIPE_DETAILS_A_SHORTNAME))),
                    hasExtra(RECIPE_STEP_POSITION, 10)));
        }
    }
    @After
    public void tearDown(){
        Intents.release();
    }

}
