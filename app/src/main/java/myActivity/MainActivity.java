
package myActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import Myadapter.ViewPagerAdapter;
import es.dmoral.toasty.Toasty;
import fragment.Adventure;
import fragment.Biographical;
import fragment.Comedy;
import fragment.Detective;
import fragment.Dramma;
import fragment.Fantastic;
import fragment.Historical;
import fragment.Horror;
import fragment.Melodrama;
import fragment.Musical;
import fragment.Thriller;
import models.Movie;
import Myadapter.MovieAdapter;
import Myadapter.MovieItemClickLisenter;


import Myadapter.SliderPagerAdapter;

import com.example.moviestime.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements MovieItemClickLisenter {

    private ViewPager sliderPagerMainActivity;
    private RecyclerView moviesRVpop,
            moviesRV_week,
            moviesRV_soon,
            moviesRV_all;
    private Animation scaleAnimation;
    private Button intent_wifi_settings;
    private Dialog dialog_app;
    private ImageView parentactivity;
    private TextView isSucssesWifi;
    private Toolbar toolbar;
    private TabLayout indicator;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private CoordinatorLayout mainActivity;
    private FrameLayout loading;
    private ImageView searchImage;
    private ViewPagerAdapter adapter;
    private TextView toolbarName;
    private TextView popularMovieTV,
            weekMovieTV,
            soonMovieTV,
            allMovieTV;
    private LinearLayout allMovie;
    private List<Movie> listSlides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbarmy);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setSupportActionBar(toolbar);

        initViews();
        SlidesIMG();
        week_movies();
        getFragment();
        animationM();
        intentAllMovie();
        getSoonMovies();
        getListSlider();
        getAllMovieList();
        getPopularMovie();
        isOnline();
        welcomeLoading();


        searchImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AllMovie.class);
                ActivityOptions options  =ActivityOptions.makeCustomAnimation(MainActivity.this,R.anim.scale_animation,R.anim.fade_left);

                startActivity(intent,options.toBundle());
            }
        });

    }


    private void welcomeLoading() {
        loading.setVisibility(View.VISIBLE);
        toolbarName.setVisibility(View.GONE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                loading.setVisibility(View.GONE);
                toolbarName.setVisibility(View.VISIBLE);
                toolbarName.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.welcomeanim));

            }
        }, 2000);
    }

    private void getSoonMovies() {

        List<Movie> soonList;
        DatabaseReference soonDatabase;

        soonList = new ArrayList<>();

        soonDatabase = FirebaseDatabase.getInstance().getReference("getSonnMovie");

        soonDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Movie data = ds.getValue(Movie.class);

                    soonList.add(data);
                }

                MovieAdapter movieAdapterPopular = new MovieAdapter(soonList, MainActivity.this, MainActivity.this);
                moviesRV_soon.setAdapter(movieAdapterPopular);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        moviesRV_soon.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
        moviesRV_soon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


    }

    private void getAllMovieList() {

        List<Movie> allMovieList;
        DatabaseReference allMovieDatabase;

        allMovieList = new ArrayList<>();

        allMovieDatabase = FirebaseDatabase.getInstance().getReference("getAllMovie");

        allMovieDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                allMovieDatabase.limitToLast(2);
                ;


                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Movie data = ds.getValue(Movie.class);

                    allMovieList.add(data);
                    Collections.shuffle(allMovieList);
                }

                MovieAdapter movieAdapterWeek = new MovieAdapter(allMovieList, MainActivity.this, MainActivity.this);
                moviesRV_all.setAdapter(movieAdapterWeek);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        moviesRV_all.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
        moviesRV_all.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    private void week_movies() {

        List<Movie> movieListWeek;
        DatabaseReference getDatabaseReference;

        movieListWeek = new ArrayList<>();

        getDatabaseReference = FirebaseDatabase.getInstance().getReference("getWeekMovie");

        getDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Movie data = ds.getValue(Movie.class);

                    movieListWeek.add(data);
                }

                MovieAdapter movieAdapterWeek = new MovieAdapter(movieListWeek, MainActivity.this, MainActivity.this);
                moviesRV_week.setAdapter(movieAdapterWeek);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        moviesRV_week.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
        moviesRV_week.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


    }

    private void getPopularMovie() {

        List<Movie> movieListPopular;
        DatabaseReference getDatabaseReference;


        movieListPopular = new ArrayList<>();

        getDatabaseReference = FirebaseDatabase.getInstance().getReference("getPopularMovie");

        getDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Movie data = ds.getValue(Movie.class);

                    movieListPopular.add(data);
                    Collections.shuffle(movieListPopular);
                }


                MovieAdapter movieAdapterPopular = new MovieAdapter(movieListPopular, MainActivity.this, MainActivity.this);
                moviesRVpop.setAdapter(movieAdapterPopular);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        moviesRV_week.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
        moviesRVpop.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


    }

    private void getListSlider() {

        DatabaseReference databaseReference;


        listSlides = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("SliderMovie");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Movie data = ds.getValue(Movie.class);

                    listSlides.add(data);
                }
                SliderPagerAdapter adapter = new SliderPagerAdapter(listSlides, MainActivity.this, MainActivity.this);
                sliderPagerMainActivity.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void intentAllMovie() {
        initViews();

        allMovie.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllMovie.class);
                ActivityOptions activityOptions  =ActivityOptions.makeCustomAnimation(MainActivity.this,R.anim.scale_animation,R.anim.fade_left);
                startActivity(intent,activityOptions.toBundle());
            }
        });

    }

    private void getFragment() {
        initViews();

        tabLayout = findViewById(R.id.tablLayout);
        viewPager = findViewById(R.id.viewPager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new Comedy(), "Կատագերգական");
        adapter.AddFragment(new Adventure(), "Արկածային");
        adapter.AddFragment(new Dramma(), "Դրամա");
        adapter.AddFragment(new Melodrama(), "Մելոդրամա");
        adapter.AddFragment(new Thriller(), "Թրիլլեր");
        adapter.AddFragment(new Biographical(), "Կենսագրական");
        adapter.AddFragment(new Detective(), "Դետեկտիվ");
        adapter.AddFragment(new Fantastic(), "Ֆանտաստիկ");
        adapter.AddFragment(new Historical(), "Պատմական");
        adapter.AddFragment(new Horror(), "Սարսափ");
        adapter.AddFragment(new Musical(), "Երաժշտական");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void initViews() {
        dialog_app = new Dialog(this);

        dialog_app.setContentView(R.layout.dialog_wi_fi);
        toolbarName = findViewById(R.id.toolbarname);
        intent_wifi_settings = dialog_app.findViewById(R.id.wifi_settings);
        isSucssesWifi = dialog_app.findViewById(R.id.isSucssesWifi);

        dialog_app.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        moviesRV_all = findViewById(R.id._all_movieRv);
        allMovie = findViewById(R.id.allMovieLinear);
        moviesRVpop = findViewById(R.id.Rv_movies);
        sliderPagerMainActivity = findViewById(R.id.slider_pager);
        indicator = findViewById(R.id.indicator);
        moviesRV_week = findViewById(R.id.Rv_movies_week);
        moviesRV_soon = findViewById(R.id.RV_soon_movie);
        mainActivity = findViewById(R.id.mainactivity);
        searchImage = findViewById(R.id.searchimgMainActivity);
        loading = findViewById(R.id.loading);

        moviesRV_soon.setAnimation(AnimationUtils.loadAnimation(this, R.anim.welcomeanim));
        moviesRV_week.setAnimation(AnimationUtils.loadAnimation(this, R.anim.welcomeanim));
        moviesRVpop.setAnimation(AnimationUtils.loadAnimation(this, R.anim.welcomeanim));
        moviesRV_all.setAnimation(AnimationUtils.loadAnimation(this, R.anim.welcomeanim));
        sliderPagerMainActivity.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
        ;
        indicator.setAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_animation));
        ;

        popularMovieTV = findViewById(R.id.textView2);
        weekMovieTV = findViewById(R.id.week_movies);
        soonMovieTV = findViewById(R.id.soon_movie);

        allMovieTV = findViewById(R.id.allMovieM);


    }

    private void animationM() {
        initViews();
        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation);
        popularMovieTV.setAnimation(scaleAnimation);
        weekMovieTV.setAnimation(scaleAnimation);
        soonMovieTV.setAnimation(scaleAnimation);
        allMovieTV.setAnimation(scaleAnimation);


    }

    private void SlidesIMG() {

        initViews();


        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new MainActivity.SliderTimer(), 4000, 6000);

        indicator.setupWithViewPager(sliderPagerMainActivity, true);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMovieClick(Movie movie, ImageView movieImageView) {

        Intent intent = new Intent(this, SeeMovie.class);

        intent.putExtra("title", movie.getName());
        intent.putExtra("imageURL", movie.getImgURL());
        intent.putExtra("imageCover", movie.getCovURL());
        intent.putExtra("movieUrl", movie.getMovieURL());
        intent.putExtra("description", movie.getDesc());
        intent.putExtra("rating", movie.getRating());
        intent.putExtra("actor", movie.getActor());
        intent.putExtra("country", movie.getCountry());
        intent.putExtra("year", movie.getYear());
        intent.putExtra("genre", movie.getGenre());


        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
                movieImageView, "sharedName");

        startActivity(intent, options.toBundle());


    }

    class SliderTimer extends TimerTask {

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (sliderPagerMainActivity.getCurrentItem() < listSlides.size() - 1) {
                        sliderPagerMainActivity.setCurrentItem(sliderPagerMainActivity.getCurrentItem() + 1);

                    } else {
                        sliderPagerMainActivity.setCurrentItem(0);
                    }
                }
            });
        }
    }

    public boolean isOnline() {
        initViews();


        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            dialog_app.show();
            dialog_app.setCancelable(true);
            Toasty.error(this, "Ինտերնետ Կապ Չկա", Toast.LENGTH_SHORT).show();
            intent_wifi_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

                }
            });
            return false;
        } else if (netInfo.isConnected()) {
            isSucssesWifi.setText("Կապը հաստատվեց");

        }

        return true;
    }
}



/*
 *
 * name
 * rating
 * year
 * country
 * genre
 * actor
 * desc
 * imgURL
 * covURL
 * movieURL
 *
 * //////////
 *
 * actor
 * country
 * covURL
 * desc
 * genre
 * imgURL
 * movieURL
 * name
 * rating
 * year
 *
 * */