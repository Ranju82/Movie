package com.ranjutech.movie.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Collection {
    @SerializedName("results")
    private List<Movies> results;

    public List<Movies> getResults() {
        return results;
    }

    public void setResults(List<Movies> results) {
        this.results = results;
    }
}
