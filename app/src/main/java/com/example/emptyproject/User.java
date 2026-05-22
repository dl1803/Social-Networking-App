package com.example.emptyproject;

import java.util.ArrayList;

public class User {
    private String name;
    private String email;
    private String password;
    private ArrayList<Status> myHiddenList;

    private String phone;
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

    public ArrayList<Status> getMyHiddenList() { return myHiddenList; }

    public ArrayList<User> getFriendList() { return friendList; }
    public ArrayList<User> getSentRequests() { return sentRequests; }
    public ArrayList<User> getReceivedRequests() { return receivedRequests; }
}
