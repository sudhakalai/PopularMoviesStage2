package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.popularmoviesstage1.Data.FavoriteMovieContract.FavoriteMovieEntry;

import com.example.android.popularmoviesstage1.Data.FavoriteMovieContract;

/*
This is the Adapter which displays the favorites movies
 */

public class FavoriteMovieCursorAdapter extends RecyclerView.Adapter<FavoriteMovieCursorAdapter.FavMovieViewHolder>{

    Context mContext;
    Cursor mCursor;
    FavMovieOnClickListener movieOnClickListener;


    public FavoriteMovieCursorAdapter(Context context, FavMovieOnClickListener onClickListener){
        mContext = context;
        movieOnClickListener = onClickListener;

    }

    public interface FavMovieOnClickListener{
        void onClick(String name);
    }

    @Override
    public FavMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.movie_fav_grid_item, parent, false);
        return  new FavMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavMovieViewHolder holder, int position) {

        int imageId = mCursor.getColumnIndex(FavoriteMovieEntry.COLUMN_IMAGE);
        int nameId = mCursor.getColumnIndex(FavoriteMovieEntry.COLUMN_MOVIE);
        int ratingId = mCursor.getColumnIndex(FavoriteMovieEntry.COLUMN_RATING);
        int idId = mCursor.getColumnIndex(FavoriteMovieEntry.COLUMN_MOVIE_ID);


        mCursor.moveToPosition(position);

        byte[] image = mCursor.getBlob(imageId);
        String name = mCursor.getString(nameId);
        String rating = mCursor.getString(ratingId);
        String id = mCursor.getString(idId);

        holder.bind(image, name, rating, id);

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    class FavMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageIv;
        TextView favMovieTv;
        TextView favRatingTv;
        TextView idTv;

        public FavMovieViewHolder(View itemView) {
            super(itemView);

            imageIv = itemView.findViewById(R.id.iv_fav_grid);
            favMovieTv = itemView.findViewById(R.id.tv_fav_name);
            favRatingTv = itemView.findViewById(R.id.ratingFavTvMain);
            idTv = itemView.findViewById(R.id.tv_id);

            itemView.setOnClickListener(this);
        }

        public void bind( byte[] imageBlob, String name, String rating, String id){
            if(imageBlob != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length);
                imageIv.setImageBitmap(bmp);
            }
            favMovieTv.setText(name);
            favRatingTv.setText(rating);
            idTv.setText(id);
        }


        @Override
        public void onClick(View view) {
            movieOnClickListener.onClick(favMovieTv.getText().toString().trim());

        }
    }
}
