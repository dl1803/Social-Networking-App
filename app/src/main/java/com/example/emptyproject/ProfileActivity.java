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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    TextView tvName;
    EditText edtName, edtEmail, edtPhone, edtAvatar, edtAddress, edtDescription;
    ImageView imgAvatar;

    int currentViewedUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tvName);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        imgAvatar = findViewById(R.id.imgAvatar);
        edtAvatar = findViewById(R.id.edtAvatar);

        edtAddress = findViewById(R.id.edtAddress);
        edtDescription = findViewById(R.id.edtDescription);

        if (edtEmail != null) {
            edtEmail.setEnabled(false);
        }

        if (getIntent().hasExtra("user_id")) {
            currentViewedUserId = getIntent().getIntExtra("user_id", -1);
        } else if (LoginActivity.currentUser != null) {
            currentViewedUserId = LoginActivity.currentUser.getId();
        }

        if (currentViewedUserId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy dữ liệu người dùng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchUserProfile(currentViewedUserId);


        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show();

            LoginActivity.currentUser = null;

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            finish();
        });

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            String newName = edtName.getText().toString().trim();
            String newPhone = edtPhone.getText().toString().trim();
            String avatarUrl = edtAvatar.getText().toString().trim();

            String newAddress = edtAddress != null ? edtAddress.getText().toString().trim() : "";
            String newDesc = edtDescription != null ? edtDescription.getText().toString().trim() : "";

            if (newName.isEmpty() || newPhone.isEmpty()) {
                Toast.makeText(this, "Tên và Số điện thoại không được để trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            User updatePayload = new User(newName, null, null, newPhone);
            updatePayload.setAvatarUrl(avatarUrl);
            updatePayload.setAddress(newAddress);
            updatePayload.setDescription(newDesc);

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            apiService.updateProfile(currentViewedUserId, updatePayload).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        AuthResponse authResponse = response.body();
                        if ("success".equals(authResponse.getStatus())) {

                            User updatedUser = authResponse.getUser();

                            tvName.setText(updatedUser.getName());

                            if (updatedUser.getAvatarUrl() != null && !updatedUser.getAvatarUrl().isEmpty()) {
                                Glide.with(ProfileActivity.this)
                                        .load(updatedUser.getAvatarUrl())
                                        .placeholder(R.drawable.ic_background_img)
                                        .error(R.drawable.ic_background_img)
                                        .into(imgAvatar);
                            }

                            if (LoginActivity.currentUser != null && currentViewedUserId == LoginActivity.currentUser.getId()) {
                                LoginActivity.currentUser = updatedUser;
                            }

                            Toast.makeText(ProfileActivity.this, authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        try {
                            if (response.errorBody() != null) {
                                String errorJson = response.errorBody().string();
                                org.json.JSONObject jsonObject = new org.json.JSONObject(errorJson);
                                String errorMessage = jsonObject.getString("message");
                                Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ProfileActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(ProfileActivity.this, "Đã xảy ra lỗi không xác định!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Toast.makeText(ProfileActivity.this, "Lỗi kết nối mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    private void fetchUserProfile(int userId) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getUserProfile(userId).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse profileResponse = response.body();

                    if ("success".equals(profileResponse.getStatus())) {
                        User userDetail = profileResponse.getUser();

                        tvName.setText(userDetail.getName() != null ? userDetail.getName() : "Username");
                        edtName.setText(userDetail.getName() != null ? userDetail.getName() : "");
                        edtEmail.setText(userDetail.getEmail() != null ? userDetail.getEmail() : "");
                        edtPhone.setText(userDetail.getPhone() != null ? userDetail.getPhone() : "");

                        if (edtAddress != null) {
                            edtAddress.setText(userDetail.getAddress() != null ? userDetail.getAddress() : "");
                        }
                        if (edtDescription != null) {
                            edtDescription.setText(userDetail.getDescription() != null ? userDetail.getDescription() : "");
                        }

                        if (userDetail.getAvatarUrl() != null && !userDetail.getAvatarUrl().isEmpty()) {
                            edtAvatar.setText(userDetail.getAvatarUrl());

                            Glide.with(ProfileActivity.this)
                                    .load(userDetail.getAvatarUrl())
                                    .placeholder(R.drawable.ic_background_img)
                                    .error(R.drawable.ic_background_img)
                                    .into(imgAvatar);
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, profileResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Không thể tải thông tin profile!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi kết nối mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}