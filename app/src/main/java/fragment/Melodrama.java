package fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import myActivity.SeeMovie;

public class Melodrama extends Fragment implements MovieItemClickLisenter {

    private View v;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    List<Movie> movieListmeloDramma;
    DatabaseReference getDatabaseReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.fragment_melodrama, container, false);

        recyclerView = v.findViewById(R.id.RV_melodramma);

        movieListmeloDramma = new ArrayList<>();

        getDatabaseReference = FirebaseDatabase.getInstance().getReference("Melodrama");

        getDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Movie data = ds.getValue(Movie.class);

                    movieListmeloDramma.add(data);
                }

                movieAdapter = new MovieAdapter(movieListmeloDramma, getActivity(), (MovieItemClickLisenter) getActivity());
                recyclerView.setAdapter(movieAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_animation));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));


        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMovieClick(Movie movie, ImageView movieImageView) {
        Intent intent = new Intent(getContext(), SeeMovie.class);

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


        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                movieImageView, "sharedName");

        startActivity(intent, options.toBundle());

    }
}

