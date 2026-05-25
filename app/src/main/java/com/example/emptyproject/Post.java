package com.example.emptyproject;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Post {
    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int userId;
    @SerializedName("content")
    private String content;
    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("author")
    private User author;

    public Post(int userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    public String getContent() { return content; }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getCreatedAt() { return createdAt; }
    public User getAuthor() { return author; }

    public void setId(int id) { this.id = id; }

    public void setUserId(int userId) { this.userId = userId; }

    public void setContent(String content) { this.content = content; }

    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public void setAuthor(User author) { this.author = author; }
}