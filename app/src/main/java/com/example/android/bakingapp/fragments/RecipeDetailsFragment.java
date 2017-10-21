package com.example.android.bakingapp.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.ui.OnTitleSelectionChangedListener;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utilities.RecipeJsonHelper;
import com.example.android.bakingapp.utilities.data.Recipe;
import com.example.android.bakingapp.utilities.data.Step;
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
import com.squareup.picasso.Picasso;

import java.io.Serializable;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsFragment extends Fragment {

    @BindString(R.string.NUMBER_OF_STEPS) public String NUMBER_OF_STEPS;
    @BindString(R.string.RECIPE_JSON) public String RECIPE_JSON;
    private int mCurStepPosition = -1;
    private String mUriString;
    private String mThumbnailURL;
    private Recipe mRecipe;

    @BindView(R.id.recipestep_description_view) TextView mDescriptionTextView;
    @BindView(R.id.prev_step_button) Button mPrevButton;
    @BindView(R.id.next_step_button) Button mNextButton;
    @BindView(R.id.thumbnail) ImageView mThumbnailView;

    @BindView(R.id.exoplayer_view) public SimpleExoPlayerView mPlayerView;
    public SimpleExoPlayer mPlayer;

    public boolean playWhenReady = true;
    public int currentWindow = 0;
    public long playbackPosition = C.TIME_UNSET;

    public static final String KEY_POSITION = "currentPosition";

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailsFragment newInstance(int recipeStepPosition, Recipe recipeObj,
                                                    int numberOfSteps, Activity activity) {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, recipeStepPosition);
        args.putSerializable(activity.getString(R.string.RECIPE_JSON), (Serializable) recipeObj);
        args.putInt(activity.getString(R.string.NUMBER_OF_STEPS), numberOfSteps);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        ButterKnife.bind(this, view);

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

        if (savedInstanceState != null) {
            mCurStepPosition = savedInstanceState.getInt(KEY_POSITION);
            mRecipe = (Recipe)savedInstanceState.getSerializable(RECIPE_JSON);
            playbackPosition = savedInstanceState.getLong(CURRENT_PLAYBACK_POSITION);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            update();
        } else if (getArguments() != null) {
            mCurStepPosition = getArguments().getInt(KEY_POSITION);
            mRecipe = (Recipe) getArguments().getSerializable(RECIPE_JSON);
            update();
        }
    }

    public int getShownIndex() {
        return (mCurStepPosition != -1) ? mCurStepPosition : 0;
    }


    public void update() {
        //Update clickability of prev and next buttons
        mPrevButton.setEnabled(mCurStepPosition != 0);
        mNextButton.setEnabled(mCurStepPosition != getArguments().getInt(NUMBER_OF_STEPS));

        //Showing Ingredients
        if (mCurStepPosition == 0) {
            mPlayerView.setVisibility(View.GONE);
            mDescriptionTextView.setText(RecipeJsonHelper.getIngredientsStringForRecipe(mRecipe));
            mThumbnailView.setVisibility(View.GONE);
            return;
        } else {
            mPlayerView.setVisibility(View.VISIBLE);
            mThumbnailView.setVisibility(View.VISIBLE);
        }

        //Get the right step Json object
        Step stepObj = RecipeJsonHelper.getRecipeStepJsonObject(mCurStepPosition, mRecipe);
        String desc = stepObj.getDescription();
        mDescriptionTextView.setText(desc);

        setURLs(stepObj.getThumbnailURL(), stepObj.getVideoURL());

        if(mUriString.equals(""))
            mPlayerView.setVisibility(View.GONE);

        initializePlayer();
        if(!mThumbnailURL.equals("")){
            Picasso.with(getActivity()).load(mThumbnailURL).into(mThumbnailView);
            mThumbnailView.setVisibility(View.VISIBLE);
        }else
            mThumbnailView.setVisibility(View.GONE);

    }

    public void setURLs(String thumbnailURL, String videoUrl){
        mThumbnailURL = thumbnailURL;
        if (!videoUrl.equals(""))
            mUriString = videoUrl;
        else if (!thumbnailURL.equals("")) {
            if(thumbnailURL.endsWith(".mp4")) {
                mUriString = thumbnailURL;
                mThumbnailURL = "";
            }else {
                mUriString = "";
            }
        }
        else {
            mUriString = "";
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
        releasePlayer();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current description selection in case we need to recreate the fragment
        outState.putInt(KEY_POSITION, mCurStepPosition);
        outState.putSerializable(RECIPE_JSON, (Serializable) mRecipe);
        outState.putLong(CURRENT_PLAYBACK_POSITION, playbackPosition);
        outState.putBoolean(PLAY_WHEN_READY, playWhenReady);
        outState.putInt(CURRENT_WINDOW_INDEX, currentWindow);
    }
    public static final String CURRENT_PLAYBACK_POSITION = "CurrentPlaybackPosition";
    public static final String CURRENT_WINDOW_INDEX = "CurrentWindowIndex";
    public static final String PLAY_WHEN_READY = "PlayWhenReady";
}
