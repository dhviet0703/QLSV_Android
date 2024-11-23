package com.example.qlsv_android;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.qlsv_android.helper.DatabaseHelper;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText etxtCurrentPass , etxtNewPass, etxtReNewPass ;

    DatabaseHelper dbHelper ;

    SQLiteDatabase db ;

    ImageButton imgButton ;

    Button btnComfirm ;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        etxtCurrentPass = findViewById(R.id.etxtCurrentPass);
        etxtNewPass = findViewById(R.id.etxtNewPass);
        etxtReNewPass = findViewById(R.id.etxtReNewPass);

        btnComfirm = findViewById(R.id.btnComfirm);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();
        String username = getIntent().getStringExtra("username") ;
        String password ="";

        if (username != null) {
            String query = "SELECT password FROM user WHERE username = ?";
            Cursor cursor = db.rawQuery(query, new String[]{username});
            if (cursor != null && cursor.moveToFirst()) {
                password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                System.out.println("Password: " + password);
            } else {
                System.out.println("Username không tồn tại!");
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        String finalPassword = password;
        btnComfirm.setOnClickListener(v -> {
            if(etxtCurrentPass.getText().toString().equals(finalPassword)){
                if(etxtNewPass.getText().toString().equals(etxtReNewPass.getText().toString())){
                    ContentValues  values = new ContentValues();
                    String selection = "username = ?";
                    String[] selectionArgs = {username};
                    values.put("password", etxtNewPass.getText().toString());
                    int count = db.update("user", values,selection, selectionArgs);

                    if (count > 0) {
                        Toast.makeText(ResetPasswordActivity.this, "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "Cập nhật mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ResetPasswordActivity.this, "Mật khẩu hiện tại không đúng!", Toast.LENGTH_SHORT).show();
            }
        });
        imgButton = findViewById(R.id.imageButton);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}