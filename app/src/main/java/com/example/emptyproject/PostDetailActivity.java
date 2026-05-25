package com.example.emptyproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);

        TextView tvNameDetail = findViewById(R.id.tvNameDetail);
        TextView tvDateDetail = findViewById(R.id.tvDateDetail);
        TextView tvContentDetail = findViewById(R.id.tvContentDetail);

        Intent intent = getIntent();
        String name = intent.getStringExtra("detail_name");
        String date = intent.getStringExtra("detail_date");
        String content = intent.getStringExtra("detail_content");

        if(name != null) tvNameDetail.setText(name);
        if(date != null) tvDateDetail.setText(date);
        if(content != null) tvContentDetail.setText(content);

        LinearLayout btnBack = findViewById(R.id.btn_Back);
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

}
