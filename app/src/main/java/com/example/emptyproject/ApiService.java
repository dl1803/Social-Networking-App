package com.example.emptyproject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @GET("api/posts/user/{user_id}")
    Call<PostListResponse> getUserPosts(@Path("user_id") int userId);

    @DELETE("api/posts/{post_id}")
    Call<SinglePostResponse> deletePost(@Path("post_id") int postId);

    @GET("api/users/{user_id}/friends")
    Call<FriendListResponse> getFriends(@Path("user_id") int userId);
}
