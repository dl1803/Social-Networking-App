package com.example.emptyproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private List<String> existingEmails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText edtName = findViewById(R.id.edtName);
        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtPassword = findViewById(R.id.edtPassword);
        EditText edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        EditText edtPhone = findViewById(R.id.edtPhone);
        Button btnCreate = findViewById(R.id.btnCreate);

        fetchAllExistingEmails();

        edtEmail.addTextChangedListener(new TextWatcher() {
            // s : text hện tại trước khi sửa
            // start : vị trí bắt đầu sửa
            // before : số kí tự cũ bị thay thế
            // after : số kí tự mới sẽ được thêm vào

            // gọi sau khi text bị thay đổi
            // count : số ký tự sẽ bị xóa/thay thế
            @Override
            public void afterTextChanged(Editable s) {

            }
            // gọi trước khi text bị thay đổi
            // count : số kí tự mới được thêm
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            // gọi khi text thay đổi
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currentTypingEmail = s.toString().trim();

                if (existingEmails.contains(currentTypingEmail)) {
                    edtEmail.setError("Email này đã được sử dụng!");
                } else {
                    edtEmail.setError(null);
                }
            }
        });

        btnCreate.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }


            User registerRequest = new User(name, email, password, phone);
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            apiService.register(registerRequest).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getStatus().equals("success")) {
                            Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra("emailReg", email);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            if (response.errorBody() != null) {
                                // Lấy chuỗi JSON lỗi từ Server
                                String errorJson = response.errorBody().string();

                                // Tách lấy chữ "message" trong chuỗi lỗi
                                org.json.JSONObject jsonObject = new org.json.JSONObject(errorJson);
                                String errorMessage = jsonObject.getString("message");

                                // Hiển thị lỗi thật sự từ S
                                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Lỗi từ máy chủ. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(RegisterActivity.this, "Đã xảy ra lỗi không xác định!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
                }
            });
        });


        TextView tvHaveAccount = findViewById(R.id.tvHaveAccount);

        tvHaveAccount.setOnClickListener(v ->{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void fetchAllExistingEmails() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getAllEmails().enqueue(new Callback<EmailListResponse>() {
            @Override
            public void onResponse(Call<EmailListResponse> call, Response<EmailListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if ("success".equals(response.body().getStatus())) {
                        existingEmails = response.body().getEmails();
                    }
                }
            }

            @Override
            public void onFailure(Call<EmailListResponse> call, Throwable t) {
            }
        });
    }
}