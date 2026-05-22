package com.example.emptyproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    public static String registerEmail = "";
    public static String registerPassword = "";
    public static String name = "";
    public static ArrayList<User> userDatabase = new ArrayList<>();

    public static User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (userDatabase.isEmpty()) {
            userDatabase.add(new User("Alice", "alice@gmail.com", "123", "0911111111"));
            userDatabase.add(new User("Bob", "bob@gmail.com", "123", "0922222222"));
            userDatabase.add(new User("Charlie", "charlie@gmail.com", "123", "0933333333"));
        }

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
            registerEmail = intentFromRegister.getStringExtra("emailReg");
            registerPassword = intentFromRegister.getStringExtra("passwordReg");
            name = intentFromRegister.getStringExtra("nameReg");

            String phoneReg = intentFromRegister.getStringExtra("phoneReg");
            userDatabase.add(new User(name, registerEmail, registerPassword, phoneReg));

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
            boolean isLoginSuccess = false;
            for (User u : userDatabase) {
                if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                    isLoginSuccess = true;
                    currentUser = u;

                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.putExtra("name", u.getName());
                    intent.putExtra("email", u.getEmail());

                    startActivity(intent);
                    finish();
                    break;
                }
            }
            if (!isLoginSuccess) {
                Toast.makeText(this, "Sai email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
            }
        });

    }


}