package com.example.emptyproject;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FriendListResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("count")
    private int count;

    @SerializedName("friends")
    private List<User> friends;

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public int getUserId() { return userId; }
    public int getCount() { return count; }
    public List<User> getFriends() { return friends; }
}