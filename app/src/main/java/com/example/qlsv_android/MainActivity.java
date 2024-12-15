package com.example.qlsv_android;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.qlsv_android.helper.DatabaseHelper;
import com.example.qlsv_android.model.User;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    EditText edit_login_account, edit_login_password;
    Button btn_login;

    TextView txtRegister, txtForgot;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        dbHelper.addUser("Tran Viet", "viet1", "1234", "sinhvien", "viet1@gmail.com");
        dbHelper.addUser("Giang Vien Tran Viet", "gvviet", "1234", "giangvien", "gvviet@gmail.com");
//        dbHelper.addUser("user", "user", "1", "sinhvien");
        edit_login_account = findViewById(R.id.edit_forgot_user);
        edit_login_password = findViewById(R.id.edit_login_password);
        txtRegister = findViewById(R.id.text_register);
        txtForgot = findViewById(R.id.txt_forgot);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(view -> {
            String account = edit_login_account.getText().toString().trim();
            String password = edit_login_password.getText().toString().trim();

            if (account.isEmpty()) {
                edit_login_account.setError("Vui lòng nhập tài khoản!");
                edit_login_account.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                edit_login_password.setError("Vui lòng nhập mật khẩu!");
                edit_login_password.requestFocus();
                return;
            }

            User user = dbHelper.getUser(account);

            if (user != null) {
                int login = 0;
                if (account.equals("nguyenvana") || account.equals("tranthib") || account.equals("levanc")) {
                    if (password.equals(user.getPassword())){
                        login = 1;
                    }
                } else {
                    if (PasswordUtils.verifyPassword(password, user.getPassword())){
                        login = 1;
                    }
                }

                if (login == 1) {
                    String role = user.getRole();
                    String hoten = user.getHoTen();
                    String username = user.getUsername();

                    switch (role.toLowerCase()) {
                        case "giangvien":
                            Toast.makeText(MainActivity.this, "Đăng nhập thành công với tư cách giảng viên!", Toast.LENGTH_SHORT).show();
                            Intent teacherIntent = new Intent(MainActivity.this, TeacherActivity.class);
                            teacherIntent.putExtra("hoten", hoten);
                            teacherIntent.putExtra("username", username);
                            startActivity(teacherIntent);
                            break;

                        case "sinhvien":
                            Toast.makeText(MainActivity.this, "Đăng nhập thành công với tư cách sinh viên!", Toast.LENGTH_SHORT).show();
                            Intent studentIntent = new Intent(MainActivity.this, studentHome.class);
                            studentIntent.putExtra("hoten", hoten);
                            studentIntent.putExtra("username", username);
                            startActivity(studentIntent);
                            break;

                        case "admin":
                            Toast.makeText(MainActivity.this, "Đăng nhập thành công với tư cách admin!", Toast.LENGTH_SHORT).show();
                            // Add admin redirection logic here
                            break;

                        default:
                            Toast.makeText(MainActivity.this, "Quyền không xác định!", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                    edit_login_password.setError("Sai mật khẩu!");
                    edit_login_password.requestFocus();
                }
            } else {
                Toast.makeText(MainActivity.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                edit_login_account.setError("Tài khoản không tồn tại!");
                edit_login_account.requestFocus();
            }
        });


        txtRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgotActivity.class);
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