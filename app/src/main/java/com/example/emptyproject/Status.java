package com.example.emptyproject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Status {
    private String name;
    private String content;
    private String email;
    private String date;

    public Status(String name, String content, String email) {
        this.name = name;
        this.content = content;
        this.email = email;
        this.date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    public Status(String name, String content, String email, String customDate) {
        this.name = name;
        this.content = content;
        this.email = email;
        this.date = customDate;
    }

    public String getName() { return name; }
    public String getContent() { return content; }
    public String getEmail() { return email; }
    public String getDate() { return date; }
}