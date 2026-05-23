package com.example.emptyproject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/login")
    Call<AuthResponse> login(@Body User loginRequest);

    @POST("api/register")
    Call<AuthResponse> register(@Body User registerRequest);
}
