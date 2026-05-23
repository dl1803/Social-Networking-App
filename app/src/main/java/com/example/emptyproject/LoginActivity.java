package com.example.emptyproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    public static User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        TextView tvRegister = findViewById(R.id.tvRegister);

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng Quên mật khẩu đang được phát triển!", Toast.LENGTH_SHORT).show();
        });


        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtPassword = findViewById(R.id.edtPassword);

        Intent intentFromRegister = getIntent();
        if (intentFromRegister.hasExtra("emailReg")) {
            String registerEmail = intentFromRegister.getStringExtra("emailReg");
            edtEmail.setText(registerEmail);
        }


        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            User loginRequest = new User("", email, password, "");
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            apiService.login(loginRequest).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        AuthResponse loginResponse = response.body();
                        if (loginResponse.getStatus().equals("success")) {
                            currentUser = loginResponse.getUser();
                            Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Lỗi. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
                }

            });
        });
    }


}