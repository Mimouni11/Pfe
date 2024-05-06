package com.example.pfemini;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OverpassApiService {

    @GET("/api/interpreter")
    Call<JsonObject> searchForGasStations(
            @Query("data") String query,
            @Query("format") String format
    );
}