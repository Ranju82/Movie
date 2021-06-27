package com.ranjutech.movie.Images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ranjutech.movie.Adapter.MoviesAdapter;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadImage {

    ExecutorService executor= Executors.newSingleThreadExecutor();
    Handler handler=new Handler(Looper.getMainLooper());
    String imageUrl;
    Bitmap bitmap;
    ImageView imageView;
    MoviesAdapter moviesAdapter;

    public DownloadImage(MoviesAdapter moviesAdapter,String imageUrl){
        this.imageUrl=imageUrl;
        this.moviesAdapter=moviesAdapter;
    }

    public DownloadImage(ImageView imageView,String imageUrl){
        this.imageUrl=imageUrl;
        this.imageView=imageView;
    }

    public void setImage() {
        if (Cache.getInstance().getLru().get(imageUrl) == null) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL urlConnection = new URL(imageUrl);
                        HttpURLConnection connection = (HttpURLConnection) urlConnection
                                .openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        bitmap = BitmapFactory.decodeStream(input);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(moviesAdapter!=null){
                                Cache.getInstance().getLru().put(imageUrl,bitmap);
                                moviesAdapter.notifyDataSetChanged();
                            }else if(imageView!=null){
                                Cache.getInstance().getLru().put(imageUrl,bitmap);
                                imageView.setImageBitmap(bitmap);
                            }
                        }
                    });
                }
            });
        }
    }
}
