package com.example.emptyproject;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("user")
    private User user;

    public String getStatus() { return status; }

    public String getMessage() { return message; }

    public User getUser() { return user; }
}
