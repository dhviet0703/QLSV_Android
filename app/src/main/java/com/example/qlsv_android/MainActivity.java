package com.example.qlsv_android;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.qlsv_android.helper.DatabaseHelper;
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
        dbHelper.addUser("user", "user", "1", "sinhvien");
        edit_login_account = findViewById(R.id.edit_login_account);
        edit_login_password = findViewById(R.id.edit_login_password);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(view -> {
            String account = edit_login_account.getText().toString();
            String password = edit_login_password.getText().toString();
            if (account.isEmpty()) {
                edit_login_account.setError("Vui lòng nhập tài khoản!");
                return;
            }
            if (password.isEmpty()) {
                edit_login_password.setError("Vui lòng nhập mật khẩu!");
                return;
            }
            User user = dbHelper.getUser(account, password);


            if (user != null) {
                String role = user.getRole();
                String hoten = user.getHoTen();
                String username = user.getUsername();
                switch (role) {
                    case "giangvien": {
                        Toast.makeText(MainActivity.this, "Chúc mừng, bạn đã đăng nhập thành công với tư cách giảng viên!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, TeacherActivity.class);
                        intent.putExtra("hoten", hoten);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        break;
                    }
                    case "sinhvien": {
                        Toast.makeText(MainActivity.this, "Chúc mừng, bạn đã đăng nhập thành công với tư cách sinh viên!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, studentHome.class);
                        intent.putExtra("hoten", hoten);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        break;
                    }
                    case "admin":
                        Toast.makeText(MainActivity.this, "Chúc mừng, bạn đã đăng nhập thành công với tư cách admin!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Quyền không xác định!", Toast.LENGTH_SHORT).show();
                        break;
                }
            } else {
                Toast.makeText(MainActivity.this, "Tài khoản hoặc mật khẩu chưa đúng!", Toast.LENGTH_SHORT).show();
                edit_login_password.setError("Tài khoản hoặc mật khẩu chưa đúng!");
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}