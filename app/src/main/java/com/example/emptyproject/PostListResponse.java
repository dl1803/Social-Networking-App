package com.example.emptyproject;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostListResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private List<Post> data;
    @SerializedName("count")
    private int count;

    public String getStatus() { return status; }
    public List<Post> getData() { return data; }
    public int getCount() { return count; }
}
