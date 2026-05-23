package com.example.emptyproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

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
}