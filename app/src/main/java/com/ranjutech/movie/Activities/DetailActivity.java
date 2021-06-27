package com.ranjutech.movie.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ranjutech.movie.Images.Cache;
import com.ranjutech.movie.Images.DownloadImage;
import com.ranjutech.movie.Models.Movies;
import com.ranjutech.movie.R;

public class DetailActivity extends AppCompatActivity {

    ImageView image;
    TextView title;
    TextView overview;
    TextView releaseDate;
    ImageView imageView;
    ProgressBar progressBar;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle=getIntent().getExtras();
        image=findViewById(R.id.detailImageView);
        title=findViewById(R.id.detailTitle);
        overview=findViewById(R.id.detailOverview);
        imageView=findViewById(R.id.detailImageView);
        releaseDate=findViewById(R.id.detailReleaseDate);
        progressBar=findViewById(R.id.detailProgressBar);
        linearLayout=findViewById(R.id.detailLayout);

        showProgressBar(true);

        title.setText(bundle.getString("title"));
        releaseDate.setText("Release date: "+bundle.getString("date"));
        overview.setText(bundle.getString("overview"));

        String url=bundle.getString("url");
        Bitmap bitmap=Cache.getInstance().getLru().get(url);

        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
            showProgressBar(false);
        }else{
            DownloadImage downloadImage=new DownloadImage(imageView,url);
            downloadImage.setImage();
            showProgressBar(false);
        }
    }

    public void showProgressBar(Boolean isShown){
        if(isShown){
            progressBar.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }
}