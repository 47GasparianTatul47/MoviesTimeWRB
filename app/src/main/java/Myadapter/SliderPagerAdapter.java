package Myadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.moviestime.R;
import com.squareup.picasso.Picasso;

import models.Movie;

import java.util.List;

public class SliderPagerAdapter extends PagerAdapter {

    private List<Movie> mList;
    private Context mcontext;
    private MovieItemClickLisenter movieItemClickLisenter;

    public SliderPagerAdapter(List<Movie> mList, Context context, MovieItemClickLisenter lisenter) {
        this.mList = mList;
        this.movieItemClickLisenter = lisenter;
        this.mcontext = context;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View slideLayout = inflater.inflate(R.layout.slide_item, null);


        ImageView slideImage = slideLayout.findViewById(R.id.slide_img);
        TextView slideText = slideLayout.findViewById(R.id.slide_title);
        ImageView slide_click = slideLayout.findViewById(R.id.slide_click);
        ImageView slideImageMovie = slideLayout.findViewById(R.id.imgSlide);

        View view = slideLayout.findViewById(R.id.viewSlide);

        Picasso.with(container.getContext()).load(mList.get(position).getCovURL()).into(slideImage);
        Picasso.with(container.getContext()).load(mList.get(position).getImgURL()).into(slideImageMovie);

        slideText.setText(mList.get(position).getName());
        slide_click.setOnClickListener(v -> movieItemClickLisenter.onMovieClick(mList.get(position), slideImageMovie));
        slide_click.setAnimation(AnimationUtils.loadAnimation(mcontext, R.anim.scale_animation));
        slideImageMovie.setAnimation(AnimationUtils.loadAnimation(mcontext, R.anim.scale_animation));




        container.addView(slideLayout);
        return slideLayout;

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);

    }
}
