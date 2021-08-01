package Myadapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import models.Movie;

import com.example.moviestime.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> implements Filterable {


    List<Movie> movieList;
    List<Movie> movieListFiltered;
    Context mContext;
    MovieItemClickLisenter movieItemClickLisenter;

    public MovieAdapter(List<Movie> movie, Context mContext, MovieItemClickLisenter lisenter) {
        this.movieList = movie;
        this.movieListFiltered = movieList;
        this.mContext = mContext;
        movieItemClickLisenter = lisenter;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_movie, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.movieName.setText(movieListFiltered.get(position).getName());

        Picasso.with(mContext).load(movieListFiltered.get(position).getImgURL()).into(holder.movieImg, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                holder.progressBar.setVisibility(View.VISIBLE);


            }
        });

        holder.movieImg.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_text));
        holder.movieName.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_text));


    }

    @Override
    public int getItemCount() {
        return movieListFiltered.size();
    }

    @Override
    public Filter getFilter() {


        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String Key = constraint.toString();

                if (Key.isEmpty()) {
                    movieListFiltered = movieList;



                } else {
                    List<Movie> lstFiltered = new ArrayList<>();

                    for (Movie row : movieList) {

                        if (row.getName().toLowerCase().contains(Key.toLowerCase())) {
                            lstFiltered.add(row);
                        }

                    }

                    movieListFiltered = lstFiltered;

                }


                FilterResults filterResults = new FilterResults();
                filterResults.values = movieListFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                movieListFiltered = (List<Movie>) results.values;
                notifyDataSetChanged();
            }
        };


    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView movieName;
        private ImageView movieImg;
        private ProgressBar progressBar;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImg = itemView.findViewById(R.id.imgURL);
            movieName = itemView.findViewById(R.id.movieName);
            progressBar = itemView.findViewById(R.id.progress_bar_item);

            itemView.setOnClickListener(v -> movieItemClickLisenter.onMovieClick(movieListFiltered.get(getAdapterPosition()), movieImg));
        }

    }

}
