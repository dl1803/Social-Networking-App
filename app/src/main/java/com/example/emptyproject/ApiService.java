package com.example.emptyproject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/login")
    Call<AuthResponse> login(@Body User loginRequest);

    @POST("api/register")
    Call<AuthResponse> register(@Body User registerRequest);

    @GET("api/users/emails")
    Call<EmailListResponse> getAllEmails();

    @GET("api/users/{user_id}/profile")
    Call<AuthResponse> getUserProfile(@Path("user_id") int userId);

    @PATCH("api/users/{user_id}/profile")
    Call<AuthResponse> updateProfile(@Path("user_id") int userId, @Body User updateRequest);

    @GET("api/posts")
    Call<PostListResponse> getAllPosts();

    @POST("api/posts")
    Call<SinglePostResponse> createPost(@Body Post postRequest);
}
