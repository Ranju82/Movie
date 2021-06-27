package com.ranjutech.movie.Images;

import android.graphics.Bitmap;
import android.util.LruCache;

public class Cache {

    private static Cache instance;
    private LruCache<String, Bitmap> lru;

    private Cache() {

        lru = new LruCache<String, Bitmap>(1024);

    }

    public static Cache getInstance() {

        if (instance == null) {

            instance = new Cache();
        }

        return instance;

    }

    public LruCache<String, Bitmap> getLru() {
        return lru;
    }
}

