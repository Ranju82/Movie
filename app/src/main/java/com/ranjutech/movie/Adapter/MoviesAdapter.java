package com.ranjutech.movie.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ranjutech.movie.Activities.DetailActivity;
import com.ranjutech.movie.Images.Cache;
import com.ranjutech.movie.Images.DownloadImage;
import com.ranjutech.movie.Models.Movies;
import com.ranjutech.movie.R;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    public List<Movies> moviesList;

    public MoviesAdapter(Context context, List<Movies> moviesList){
        this.context=context;
        this.moviesList=moviesList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType==0) {
            View popularView = inflater.inflate(R.layout.popular_layout, parent, false);
            viewHolder = new PopularViewHolder(popularView);
            return viewHolder;
        }else{
            View unpopularView = inflater.inflate(R.layout.unpopular_layout, parent, false);
            viewHolder = new UnpopularViewHolder(unpopularView);
            return viewHolder;
        }    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Movies movie=moviesList.get(position);
        String title=movie.getTitle();
        String date=movie.getReleaseDate();
        String overview=movie.getOverview();
        String backdropPath="https://image.tmdb.org/t/p/original"+movie.getBackdropPath();
        String posterPath="https://image.tmdb.org/t/p/w342"+movie.getPosterPath();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("date",date);
                intent.putExtra("overview",overview);
                intent.putExtra("url",posterPath);
                context.startActivity(intent);
            }
        });
        if(holder.getItemViewType()==0){
            PopularViewHolder popularViewHolder=(PopularViewHolder) holder;
            Bitmap bitmap= Cache.getInstance().getLru().get(backdropPath);
            if(bitmap!=null){
                popularViewHolder.popularImage.setImageBitmap(bitmap);
            }else{
                DownloadImage downloadImage=new DownloadImage(this,backdropPath);
                downloadImage.setImage();
            }
            popularViewHolder.popularButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moviesList.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }else{
            UnpopularViewHolder unpopularViewHolder=(UnpopularViewHolder)holder;
            unpopularViewHolder.unpopularTitle.setText(title);
            unpopularViewHolder.unpopularOverview.setText(overview);
            Bitmap bitmap=Cache.getInstance().getLru().get(posterPath);
            if(bitmap!=null){
                unpopularViewHolder.unpopularImage.setImageBitmap(bitmap);
            }else{
                DownloadImage downloadImage=new DownloadImage(this,posterPath);
                downloadImage.setImage();
            }
            unpopularViewHolder.unpopularButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moviesList.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }
    }




    @Override
    public int getItemViewType(int position) {
        if(moviesList.get(position).getVoteAverage()>7){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class PopularViewHolder extends RecyclerView.ViewHolder{
        final ImageView popularImage;
        final ImageButton popularButton;
        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
            popularImage =itemView.findViewById(R.id.popularImage);
            popularButton =itemView.findViewById(R.id.popularButton);
        }
    }

    public class UnpopularViewHolder extends RecyclerView.ViewHolder{
        final ImageView unpopularImage;
        final TextView unpopularTitle;
        final TextView unpopularOverview;
        final ImageButton unpopularButton;
        public UnpopularViewHolder(@NonNull View itemView) {
            super(itemView);
            unpopularImage=itemView.findViewById(R.id.unpopularImage);
            unpopularTitle=itemView.findViewById(R.id.unpopularTitle);
            unpopularOverview=itemView.findViewById(R.id.unpopularOverview);
            unpopularButton=itemView.findViewById(R.id.unpopularButton);
        }
    }
}
