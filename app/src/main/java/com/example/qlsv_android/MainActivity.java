package com.example.qlsv_android;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.qlsv_android.model.User;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    EditText edit_login_account, edit_login_password;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        dbHelper.addUser("admin", "admin", "123456", "giangvien");

        edit_login_account = (EditText) findViewById(R.id.edit_login_account);
        edit_login_password = (EditText) findViewById(R.id.edit_login_password);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = edit_login_account.getText().toString();
                String password = edit_login_password.getText().toString();
                User user = dbHelper.getUser(account, password);
                if (user != null) {
                    String role = user.getRole();
                    if (role.equals("giangvien")) {
                        Toast.makeText(MainActivity.this, "Chúc mừng, bạn đã đăng nhập thành công với tư cách giảng viên!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, teacherHome.class);
                        startActivity(intent);
                    } else if (role.equals("sinhvien")) {
                        Toast.makeText(MainActivity.this, "Chúc mừng, bạn đã đăng nhập thành công với tư cách sinh viên!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, studentHome.class);
                        startActivity(intent);
                    } else if (role.equals("admin")) {
                        Toast.makeText(MainActivity.this, "Chúc mừng, bạn đã đăng nhập thành công với tư cách admin!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (account.isEmpty()) {
                        edit_login_account.setError("Vui lòng nhập tài khoản!");
                    } else {
                        edit_login_password.setError("Tài khoản hoặc mật khẩu chưa đúng!");
                    }
                }

            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}