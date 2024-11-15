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

        edit_login_account = (EditText) findViewById(R.id.edit_login_account);
        edit_login_password = (EditText) findViewById(R.id.edit_login_password);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = edit_login_account.getText().toString();
                String password = edit_login_password.getText().toString();

                if (account.equals("admin") && password.equals("123")) {
                    Toast.makeText(MainActivity.this, "Chúc mừng, bạn đã đăng nhập thành công với tư cách admin!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, teacherHome.class);
                    startActivity(intent);
                } else if (account.equals("student") && password.equals("111")) {
                    Toast.makeText(MainActivity.this, "Chúc mừng, bạn đã đăng nhập thành công với tư cách student!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, studentHome.class);
                    startActivity(intent);
                } else {
                    if (!account.equals("admin") && !account.equals("student")) {
                        edit_login_account.setError("Account chưa đúng!");
                    } else {
                        edit_login_password.setError("Password chưa đúng!");
                    }
                }

                edit_login_account.setText(null);
                edit_login_password.setText(null);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}