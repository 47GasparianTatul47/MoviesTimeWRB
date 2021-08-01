package myActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.moviestime.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Myadapter.MovieAdapter;
import Myadapter.MovieItemClickLisenter;
import models.Movie;

public class AllMovie extends AppCompatActivity implements MovieItemClickLisenter {

    private RecyclerView recyclerView;
    private ImageView parentactivityAllMovie;
    private ImageView searchImage, closeSearch;
    private EditText searchInput;
    private ConstraintLayout constraintSearch;
    private Movie data;
    private MovieAdapter movieAdapterPopular;
    private List<Movie> allMovieList;
    private DatabaseReference getDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_movie);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


        recyclerView = findViewById(R.id.RV_all_movies);

        parentactivityAllMovie = findViewById(R.id.parentactivityallmovie);

        searchInput = findViewById(R.id.searchInput);
        searchImage = findViewById(R.id.searchimg);
        closeSearch = findViewById(R.id.closeSearch);
        constraintSearch = findViewById(R.id.constraintsearch);

        constraintSearch.setVisibility(View.GONE);
        searchImage.setVisibility(View.VISIBLE);

        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                constraintSearch.setAnimation(AnimationUtils.loadAnimation(AllMovie.this, R.anim.right_anim));
                constraintSearch.setVisibility(View.VISIBLE);
                searchImage.setVisibility(View.GONE);

            }
        });
        closeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchImage.setAnimation(AnimationUtils.loadAnimation(AllMovie.this, R.anim.right_anim));
                constraintSearch.setVisibility(View.GONE);
                parentactivityAllMovie.setAnimation(AnimationUtils.loadAnimation(AllMovie.this, R.anim.welcomeanim));
                searchImage.setVisibility(View.VISIBLE);

                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                movieAdapterPopular.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        parentactivityAllMovie.setOnClickListener(v ->

        {

            onBackPressed();
        });


        allMovieList = new ArrayList<>();

        getDatabaseReference = FirebaseDatabase.getInstance().

                getReference("getAllMovie");

        getDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    data = ds.getValue(Movie.class);
                    allMovieList.add(data);
                }
                movieAdapterPopular = new MovieAdapter(allMovieList, AllMovie.this, AllMovie.this);
                recyclerView.setAdapter(movieAdapterPopular);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        recyclerView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
        recyclerView.setLayoutManager(new

                GridLayoutManager(this, 3));


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


        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AllMovie.this,
                movieImageView, "sharedName");

        startActivity(intent, options.toBundle());
    }

}
