package com.example.emptyproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView tvName = findViewById(R.id.tvName);
        TextView edtName = findViewById(R.id.edtName);
        TextView edtEmail = findViewById(R.id.edtEmail);
        TextView tvPhone = findViewById(R.id.edtPhone);
        ImageView imgAvatar = findViewById(R.id.imgAvatar);
        EditText edtAvatar = findViewById(R.id.edtAvatar);

        Intent intentFromHome = getIntent();
        String email = intentFromHome.getStringExtra("email");
        String name = intentFromHome.getStringExtra("name");



        if (name != null) {
            tvName.setText(name);
            edtName.setText(name);
        }

        if (email != null) {
            edtEmail.setText(email);
        }

        if (LoginActivity.currentUser != null) {
            tvPhone.setText(LoginActivity.currentUser.getPhone());
        }


        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            finish();
        });

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {

            String newName = edtName.getText().toString().trim();
            if (!newName.isEmpty()) {
                tvName.setText(newName);

                if (LoginActivity.currentUser != null) {
                    LoginActivity.currentUser.setName(newName);
                }
            }

            String avatarUrl = edtAvatar.getText().toString().trim();

            if (!avatarUrl.isEmpty()) {
                Glide.with(this)
                        .load(avatarUrl)
                        .placeholder(R.drawable.ic_background_img)
                        .error(R.drawable.ic_background_img)
                        .into(imgAvatar);
            }


            Toast.makeText(this, "Lưu thông tin thành công!", Toast.LENGTH_SHORT).show();
        });

    }
}