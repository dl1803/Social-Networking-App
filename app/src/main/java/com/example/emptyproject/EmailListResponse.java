package com.example.emptyproject;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmailListResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("emails")
    private List<String> emails;

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public List<String> getEmails() { return emails; }
}
