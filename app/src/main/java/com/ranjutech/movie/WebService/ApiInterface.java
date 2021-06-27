package com.ranjutech.movie.WebService;

import com.ranjutech.movie.Models.Collection;
import com.ranjutech.movie.Models.Movies;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("movie/now_playing")
    Call<Collection> getCollection();
}
