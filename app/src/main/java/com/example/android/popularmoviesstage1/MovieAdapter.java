package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * This is the recycler adapter class
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    //Declaring global variables
    private ArrayList<Movie> mMovies = new ArrayList<>();
    private final MovieAdapterOnClickHandler mClickHandler;


    //This the interface to handle click events
    public interface MovieAdapterOnClickHandler{
        void onClick(Movie movie);
    }

    //Constructor
    public MovieAdapter(ArrayList<Movie> movies, MovieAdapterOnClickHandler clickHandler){
         mMovies = movies;
         mClickHandler = clickHandler;
    }



    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_grid_item, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

            holder.movie = mMovies.get(position);

            String moviePosterUrl = holder.movie.getImageUrl();
            String movieName = holder.movie.getOriginalTitle();
            String movieRating = holder.movie.getVoteAverage();

            holder.bind(moviePosterUrl, movieName, movieRating);

    }

    @Override
    public int getItemCount() {
        if(mMovies!= null){
            return mMovies.size();
        }else{
            return 0;
        }
    }

    /**
     * This method notifies when the ArrayList has changed
     * @param list
     */
    public void setMovieList(ArrayList<Movie> list){
        this.mMovies = list;
        notifyDataSetChanged();
    }


    //ViewHolder class
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView moviePosterIv;
        private TextView nameTv;
        private TextView ratingTvMain;
        private Movie movie;
        private Context context;

        private MovieViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();

            moviePosterIv= itemView.findViewById(R.id.iv_poster_grid);
            nameTv = itemView.findViewById(R.id.tv_name);
            ratingTvMain = itemView.findViewById(R.id.ratingTvMain);
            itemView.setOnClickListener(this);
        }

        private void bind(String imageUrl, String movieName, String rating){
            String posterUrl = "http://image.tmdb.org/t/p/w342/" + imageUrl;
            Picasso.with(context)
                    .load(posterUrl)
                    .into(moviePosterIv);

            ratingTvMain.setText(rating);
            nameTv.setText(movieName);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie clickedMovie = mMovies.get(adapterPosition);
            mClickHandler.onClick(clickedMovie);
        }
    }

}
