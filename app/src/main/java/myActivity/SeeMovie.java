 package myActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.AndroidViewModel;

import com.example.moviestime.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

public class SeeMovie extends AppCompatActivity {
    AndroidViewModel model;
    private ImageView Movie_Image_URL, Movie_Cover_Image;
    private TextView movie_Title, movie_description, movie_actor, movie_year,
            movie_country, movie_genre, not_found_movie;
    private PlayerView playerView;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private ImageView imageViewLogo;
    private ImageView parentActivityIV;
    private FrameLayout frameLayout;
    private ImageView vertMore, shareMovie;
    private ConstraintLayout infomovies;
    private TextView movie_name;
    private TextView shareMovieTV;
    private ImageView btn_fullScreen;
    private ConstraintLayout constraintLayoutYear;
    private ConstraintLayout constraintLayoutActor;
    private ConstraintLayout constraintLayoutGenre;
    private ImageView closeShareLayout;
    private ConstraintLayout constraintLayoutRating;
    private ConstraintLayout constraintLayoutCountry;
    private boolean flag = false;
    private RatingBar ratingBar;
    private SimpleExoPlayer simpleExoPlayer;
    private FrameLayout not_internet;
    private RelativeLayout shareMovieRL;
    public static String VIDEO_TEST_URL = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_movie);
        toolbar = findViewById(R.id.toolbarmv);
        initViews();

        setSupportActionBar(toolbar);

        shareMovieRL.setVisibility(View.GONE);



        showDialog();
        exoPlayer();
        animations();
        notFound();
        getDataMain();
        shareMoveiesName();
        intentparentActivity();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


    }

    private void showDialog() {


        shareMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                shareMovieRL.setVisibility(View.VISIBLE);
                shareMovieRL.setAnimation(AnimationUtils.loadAnimation(SeeMovie.this, R.anim.right_anim));


            }
        });

        closeShareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareMovieRL.setVisibility(View.GONE);
                shareMovieRL.setAnimation(AnimationUtils.loadAnimation(SeeMovie.this, R.anim.fade_left));

            }
        });
    }

    private void shareMoveiesName() {
        shareMovieTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareMOVIES();
            }
        });
    }

    private void shareMOVIES() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        String year = movie_year.getText().toString();
        String name = "«" + movie_name.getText().toString() + " " + year + "»" + " " + "ֆիլմ";
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, name);
        startActivity(Intent.createChooser(intent, ""));

    }

    private void intentparentActivity() {
        initViews();
        imageViewLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentactivityMethod();
            }
        });


        parentActivityIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        movie_actor = findViewById(R.id.actor);
        shareMovieTV = findViewById(R.id.shareMovieTV);
        movie_description = findViewById(R.id.description_movie);
        Movie_Image_URL = findViewById(R.id.seeMovieImage);
        closeShareLayout = findViewById(R.id.closeShareLayout);
        Movie_Cover_Image = findViewById(R.id.detail_movie_cover);
        ratingBar = findViewById(R.id.rating);
        imageViewLogo = findViewById(R.id.imageViewLogo);
        parentActivityIV = findViewById(R.id.parentiv);
        shareMovie = findViewById(R.id.shareMovie);
        movie_Title = findViewById(R.id.movie_nameSM);
        shareMovieRL = findViewById(R.id.shareMovieRL);
        vertMore = findViewById(R.id.vertMoreDialog);
        constraintLayoutYear = findViewById(R.id.fremae_layout_year);
        constraintLayoutRating = findViewById(R.id.freame_layout_ratinh);
        constraintLayoutGenre = findViewById(R.id.freame_layout_genere);
        constraintLayoutCountry = findViewById(R.id.fremae_layout_country);
        constraintLayoutActor = findViewById(R.id.freame_layout_actor);

        not_internet = findViewById(R.id.not_internet);
        frameLayout = findViewById(R.id.relativeLayoutPL);
        infomovies = findViewById(R.id.infoMovie);
        playerView = findViewById(R.id.seeMovie_player);
        progressBar = findViewById(R.id.progress_bar);
        btn_fullScreen = playerView.findViewById(R.id.bt_fullScreen);
        movie_country = findViewById(R.id.movie_country);
        movie_year = findViewById(R.id.movie_year);
        movie_genre = findViewById(R.id.genere);
        movie_name = playerView.findViewById(R.id.controller_movie_name);
        not_found_movie = findViewById(R.id.not_found_film);


    }

    private void getDataMain() {

        initViews();

        String movieName = getIntent().getExtras().getString("title");
        String movie_Actor = getIntent().getExtras().getString("actor");
        String imageMovie = getIntent().getExtras().getString("imageURL");
        String imageCover = getIntent().getExtras().getString("imageCover");
        String movieYear = getIntent().getExtras().getString("year");
        String movieCountry = getIntent().getExtras().getString("country");
        String movieGenre = getIntent().getExtras().getString("genre");
        String movieDescription = getIntent().getExtras().getString("description");
        String movieRating = getIntent().getExtras().getString("rating");


        Picasso.with(this).load(imageMovie).into(Movie_Image_URL);
        Picasso.with(this).load(imageCover).into(Movie_Cover_Image);

        movie_actor.setText(movie_Actor);

        movie_description.setText(movieDescription);
        movie_Title.setText(movieName);
        ratingBar.setRating(Float.parseFloat(movieRating));
        movie_country.setText(movieCountry);
        movie_year.setText(movieYear);
        movie_genre.setText(movieGenre);
        movie_name.setText(movieName);


    }

    private void animations() {
        initViews();
        Movie_Cover_Image.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));

        movie_Title.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
        constraintLayoutRating.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
        constraintLayoutCountry.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
        constraintLayoutGenre.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
        constraintLayoutActor.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
        constraintLayoutYear.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
    }

    private void exoPlayer() {
        initViews();

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "appaname"));

        String movieUrl = getIntent().getExtras().getString("movieUrl");


        VIDEO_TEST_URL = movieUrl;

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).
                createMediaSource(Uri.parse(VIDEO_TEST_URL));

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this);

        playerView.setPlayer(simpleExoPlayer);

        simpleExoPlayer.prepare(videoSource);

        simpleExoPlayer.setPlayWhenReady(true);


        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                if (playbackState == Player.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);

                } else if (playbackState == Player.STATE_READY) {

                    progressBar.setVisibility(View.GONE);

                }

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

                not_found_movie.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }

        });


        btn_fullScreen.setOnClickListener(v -> {

            if (flag) {
                btn_fullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_black_24dp));

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                if (getSupportActionBar() != null) {
                    getSupportActionBar().show();
                }


                infomovies.setVisibility(View.VISIBLE);

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) frameLayout.getLayoutParams();
                FrameLayout.LayoutParams paramsMovie = (FrameLayout.LayoutParams) playerView.getLayoutParams();


                params.width = params.MATCH_PARENT;
                params.height = params.WRAP_CONTENT;


                paramsMovie.width = params.MATCH_PARENT;
                paramsMovie.height = (int) getResources().getDimension(R.dimen.freame_height);


                playerView.setLayoutParams(paramsMovie);

                frameLayout.setLayoutParams(params);
                flag = false;

            } else {
                btn_fullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_fullscreen_exit_24));


                if (getSupportActionBar() != null) {
                    getSupportActionBar().hide();
                }

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                infomovies.setVisibility(View.GONE);

                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) frameLayout.getLayoutParams();
                FrameLayout.LayoutParams paramsMovie = (FrameLayout.LayoutParams) playerView.getLayoutParams();


                params.width = params.MATCH_PARENT;
                params.height = params.MATCH_PARENT;


                paramsMovie.width = (int) getResources().getDimension(R.dimen.widhtMovie);
                paramsMovie.height = (int) getResources().getDimension(R.dimen.height_Movie);

                playerView.setLayoutParams(paramsMovie);
                frameLayout.setLayoutParams(params);
                flag = true;
            }
        });

    }

    private void notFound() {

        if (VIDEO_TEST_URL == null) {

            not_found_movie.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) frameLayout.getLayoutParams();
        FrameLayout.LayoutParams paramsMovie = (FrameLayout.LayoutParams) playerView.getLayoutParams();


        params.width = params.MATCH_PARENT;
        params.height = params.WRAP_CONTENT;


        paramsMovie.width = params.MATCH_PARENT;
        paramsMovie.height = (int) getResources().getDimension(R.dimen.freame_height);


        playerView.setLayoutParams(paramsMovie);

        frameLayout.setLayoutParams(params);

    }

    private void parentactivityMethod() {
        onBackPressed();
    }

}
