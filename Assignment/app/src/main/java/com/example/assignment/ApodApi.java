package com.example.assignment;
import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Query;
public interface ApodApi {
    @GET("planetary/apod")
    Call<ApodResponse> getApod(@Query("api_key") String apiKey);
}
