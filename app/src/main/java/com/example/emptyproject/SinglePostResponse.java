package com.example.emptyproject;

import com.google.gson.annotations.SerializedName;

public class SinglePostResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Post data;

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public Post getData() { return data; }
}
