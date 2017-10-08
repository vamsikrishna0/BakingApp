package com.example.android.bakingapp.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingapp.UI.OnTitleSelectionChangedListener;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Utilities.RecipeJsonHelper;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.android.bakingapp.UI.RecipeActivity.NUMBER_OF_STEPS;
import static com.example.android.bakingapp.UI.RecipeActivity.RECIPE_JSON;


public class RecipeDetailsFragment extends Fragment {

    private int mCurStepPosition = -1;
    private String mUriString;
    private JSONObject mJson;
    TextView mDescriptionTextView;
    Button mPrevButton, mNextButton;

    public SimpleExoPlayerView mPlayerView;
    public SimpleExoPlayer mPlayer;

    public boolean playWhenReady = true;
    public int currentWindow = 0;
    public long playbackPosition = C.TIME_UNSET;

    public static final String KEY_POSITION = "currentPosition";

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailsFragment newInstance(int recipeStepPosition, JSONObject recipeJSONObj, int numberOfSteps) {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Log.i("RecipeDetailsFrgment", "here in new instance");
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, recipeStepPosition);
        args.putString(RECIPE_JSON, recipeJSONObj.toString());
        args.putInt(NUMBER_OF_STEPS, numberOfSteps);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        mPlayerView = view.findViewById(R.id.exoplayer_view);
        mDescriptionTextView = view.findViewById(R.id.recipestep_description_view);
//        Log.i("RecipeDetailsFrgment", "here in oncreateview");

        mPrevButton = view.findViewById(R.id.prev_step_button);
        mNextButton = view.findViewById(R.id.next_step_button);

        final OnTitleSelectionChangedListener listener = (OnTitleSelectionChangedListener) getActivity();

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.showDetails(mCurStepPosition - 1);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.showDetails(mCurStepPosition + 1);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            if (savedInstanceState != null) {
                mCurStepPosition = savedInstanceState.getInt(KEY_POSITION);
                mJson = new JSONObject(savedInstanceState.getString(RECIPE_JSON));
                update();
            } else if (getArguments() != null) {
                mCurStepPosition = getArguments().getInt(KEY_POSITION);
                mJson = new JSONObject(getArguments().getString(RECIPE_JSON));
                update();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getShownIndex() {
        return getArguments().getInt(KEY_POSITION, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current description selection in case we need to recreate the fragment
        outState.putInt(KEY_POSITION, mCurStepPosition);
        outState.putString(RECIPE_JSON, mJson.toString());
    }

    public void update() {
        try {
            //Update clickability of prev and next buttons
            mPrevButton.setEnabled(mCurStepPosition != 0);
            mNextButton.setEnabled(mCurStepPosition != getArguments().getInt(NUMBER_OF_STEPS) - 1);

            //Showing Ingredients
            if (mCurStepPosition == 0) {
                mPlayerView.setVisibility(View.GONE);
                RecipeJsonHelper.getIngredientsStringForRecipe(mJson);
                mDescriptionTextView.setText(RecipeJsonHelper.getIngredientsStringForRecipe(mJson));
                return;
            } else {
                mPlayerView.setVisibility(View.VISIBLE);
            }

            //Get the right step Json object
            JSONObject stepObj = RecipeJsonHelper.getRecipeStepJsonObject(mCurStepPosition, mJson);
            String desc = stepObj.getString(RecipeJsonHelper.LONG_DESCRIPTION);
//            Log.i("RecipeDetailsFragment", mCurStepPosition + " " + desc);
            mDescriptionTextView.setText(desc);

            if (stepObj.has(RecipeJsonHelper.VIDEO_URL) && !stepObj.getString(RecipeJsonHelper.VIDEO_URL).equals(""))
                mUriString = stepObj.getString(RecipeJsonHelper.VIDEO_URL);
            else if (stepObj.has(RecipeJsonHelper.THUMBNAIL_URL) && !stepObj.getString(RecipeJsonHelper.THUMBNAIL_URL).equals(""))
                mUriString = stepObj.getString(RecipeJsonHelper.THUMBNAIL_URL);
            else
                mUriString = "";
            initializePlayer();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Exoplayer stuff
    public void initializePlayer() {
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        if (!mUriString.equals("")) {
            mPlayerView.setPlayer(mPlayer);
            mPlayer.setPlayWhenReady(playWhenReady);
            mPlayer.seekTo(currentWindow, playbackPosition);

            Uri uri = Uri.parse(mUriString);
            MediaSource mediaSource = buildMediaSource(uri);
            mPlayer.prepare(mediaSource);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            playbackPosition = mPlayer.getCurrentPosition();
            currentWindow = mPlayer.getCurrentWindowIndex();
            playWhenReady = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }
}
