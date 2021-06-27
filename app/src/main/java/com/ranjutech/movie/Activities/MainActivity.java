package com.ranjutech.movie.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.ranjutech.movie.Adapter.MoviesAdapter;
import com.ranjutech.movie.Models.Collection;
import com.ranjutech.movie.Models.Movies;
import com.ranjutech.movie.R;
import com.ranjutech.movie.WebService.ApiInterface;
import com.ranjutech.movie.WebService.ServiceGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private final String TAG=MainActivity.class.getSimpleName();
    ProgressBar progressBar;
    RecyclerView recyclerView;
    List<Movies> moviesList;
    MoviesAdapter moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar=findViewById(R.id.mainProgressBar);
        recyclerView=findViewById(R.id.mainRecyclerView);
        moviesList=new ArrayList<>();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        moviesAdapter=new MoviesAdapter(MainActivity.this,moviesList);
        recyclerView.setAdapter(moviesAdapter);
        showProgressBar(true);
        getMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        return true;
    }

    public void getMovies(){
        ApiInterface apiInterface= ServiceGenerator.createService(ApiInterface.class);
        Call<Collection> call=apiInterface.getCollection();
        call.enqueue(new Callback<Collection>() {
            @Override
            public void onResponse(Call<Collection> call, Response<Collection> response) {
                if(response.isSuccessful()){
                    moviesList.addAll(response.body().getResults());
                    moviesAdapter.notifyDataSetChanged();
                }else{
                    Log.e(TAG,"Fail:"+response.message());
                }
                showProgressBar(false);
            }

            @Override
            public void onFailure(Call<Collection> call, Throwable t) {
                showProgressBar(false);
                Log.e(TAG, "Fail:"+t.getMessage());
            }
        });
    }

    public void showProgressBar(Boolean isShown){
        if(isShown){
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void filter(String queryText) {
        List<Movies> moviesCopy= new ArrayList<>(moviesList);
        if(!queryText.isEmpty()) {
            moviesList.clear();
            for (Movies movie : moviesCopy) {
                if (movie.getTitle().toLowerCase().contains(queryText.toLowerCase())) {
                    moviesList.add(movie);
                }
            }
            for(Movies movie:moviesCopy){
                if(!moviesList.contains(movie)){
                    moviesList.add(movie);
                }
            }
            moviesAdapter.notifyDataSetChanged();
        }
    }

}