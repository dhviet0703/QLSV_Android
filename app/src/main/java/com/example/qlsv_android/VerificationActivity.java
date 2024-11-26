package com.example.qlsv_android;

import android.annotation.SuppressLint;
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
import com.example.qlsv_android.helper.DatabaseHelper;

public class VerificationActivity extends AppCompatActivity {
    EditText edit_verify_code, edit_new_password;
    Button btn_verify;

    TextView txt_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verification);

        edit_new_password = findViewById(R.id.edit_new_password);
        edit_verify_code = findViewById(R.id.edit_verify_code);
        btn_verify = findViewById(R.id.btn_verify);
        txt_login = findViewById(R.id.txt_login);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String email = getIntent().getStringExtra("email");

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputCode = edit_verify_code.getText().toString().trim();
                String newPassword = edit_new_password.getText().toString().trim();

                if (inputCode.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(VerificationActivity.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String query = "SELECT verify_code, timestamp FROM user WHERE email = ?";
                Cursor cursor = db.rawQuery(query, new String[]{email});

                if (cursor.moveToFirst()) {
                    @SuppressLint("Range") String savedCode = cursor.getString(cursor.getColumnIndex("verify_code"));
                    @SuppressLint("Range") long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));

                    long currentTimeMillis = System.currentTimeMillis();
                    long elapsedTime = currentTimeMillis - timestamp;

                    if (elapsedTime > 5 * 60 * 1000) {
                        db.execSQL("UPDATE user SET verify_code = NULL, timestamp = NULL WHERE email = ?",
                                new Object[]{email});
                        Toast.makeText(VerificationActivity.this, "Verification code expired!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (inputCode.equals(savedCode)) {
                        String hashedPassword = PasswordUtils.hashPassword(newPassword);

                        db.execSQL("UPDATE user SET password = ?, verify_code = NULL, timestamp = NULL WHERE email = ?",
                                new Object[]{hashedPassword, email});
                        Toast.makeText(VerificationActivity.this, "Password reset successful!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(VerificationActivity.this, "Invalid verification code!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VerificationActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                }

                cursor.close();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}