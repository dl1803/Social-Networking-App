package com.example.emptyproject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class User {
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    private ArrayList<Status> myHiddenList;
    @SerializedName("phone")
    private String phone;

    @SerializedName("id")
    private int id;

    @SerializedName("address")
    private String address;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("description")
    private String description;

    @SerializedName("created_at")
    private String createdAt;
    private ArrayList<User> friendList;
    private ArrayList<User> sentRequests;
    private ArrayList<User> receivedRequests;

    public User(String name, String email, String password, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.myHiddenList = new ArrayList<>();
        this.phone = phone;
        this.friendList = new ArrayList<>();
        this.sentRequests = new ArrayList<>();
        this.receivedRequests = new ArrayList<>();
    }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public ArrayList<Status> getMyHiddenList() {
        if (myHiddenList == null) {
            myHiddenList = new ArrayList<>();
        }
        return myHiddenList;
    }

    public ArrayList<User> getFriendList() {
        if (friendList == null) {
            friendList = new ArrayList<>();
        }
        return friendList;
    }
    public ArrayList<User> getSentRequests() {
        if (sentRequests == null) {
            sentRequests = new ArrayList<>();
        }
        return sentRequests;
    }
    public ArrayList<User> getReceivedRequests() {

        if (receivedRequests == null) {
            receivedRequests = new ArrayList<>();
        }
        return receivedRequests;

    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
