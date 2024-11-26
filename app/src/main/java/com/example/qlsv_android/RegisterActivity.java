package com.example.qlsv_android;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.qlsv_android.Utils.PasswordUtils;
import android.util.Patterns;
import com.example.qlsv_android.helper.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {
    EditText etxtUserName, etxtPassword , etxtRepassword, etxtEmail ;

    TextView login;
    Button Register ;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        etxtUserName = findViewById(R.id.edit_register_account) ;
        etxtPassword = findViewById(R.id.edit_password);
        etxtRepassword = findViewById(R.id.edit_re_password);
        etxtEmail = findViewById(R.id.edit_register_email);

        login = findViewById(R.id.text_login);

        Register = findViewById(R.id.btn_register);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etxtUserName.getText().toString().trim();
                String password = etxtPassword.getText().toString().trim();
                String repassword = etxtRepassword.getText().toString().trim();
                String email = etxtEmail.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty() || repassword.isEmpty() || email.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(repassword)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu bạn nhập không trùng khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etxtEmail.setError("Email không hợp lệ!");
                    etxtEmail.requestFocus();
                    return;
                }

                if (dbHelper.getEmail(email)) {
                    Toast.makeText(RegisterActivity.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbHelper.getUser(username) != null) {
                    Toast.makeText(RegisterActivity.this, "Username đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHelper.addUser("null", username, password, "sinhvien", email);
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}