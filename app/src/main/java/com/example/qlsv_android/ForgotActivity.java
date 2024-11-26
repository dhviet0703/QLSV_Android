package com.example.qlsv_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.qlsv_android.Utils.EmailSettings;
import com.example.qlsv_android.Utils.JWTUtil;
import com.example.qlsv_android.Utils.PasswordUtils;
import com.example.qlsv_android.Utils.VerifyCodeUtils;
import com.example.qlsv_android.helper.DatabaseHelper;

import javax.mail.MessagingException;

public class ForgotActivity extends AppCompatActivity {
    Button btn_forgot ;
    TextView txt_login ;

    EditText Username_or_Email ;
    ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot);
        Username_or_Email = findViewById(R.id.edit_forgot_user);
        txt_login = findViewById(R.id.txt_forgot) ;
        btn_forgot = findViewById(R.id.btn_forgot);

        progressBar = findViewById(R.id.progress_bar);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameOrEmail = Username_or_Email.getText().toString().trim();

                if (usernameOrEmail.isEmpty()) {
                    Toast.makeText(ForgotActivity.this, "Please enter your username or email.", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                new Thread(() -> {
                    String query = "SELECT email FROM user WHERE username = ? OR email = ?";
                    Cursor cursor = db.rawQuery(query, new String[]{usernameOrEmail, usernameOrEmail});

                    runOnUiThread(() -> {
                        if (cursor.moveToFirst()) {
                            @SuppressLint("Range")
                            String email = cursor.getString(cursor.getColumnIndex("email"));

                            if (email != null && !email.isEmpty()) {
                                // String verificationCode = JWTUtil.generateToken(email);
                                String verificationCode = VerifyCodeUtils.generateVerifyCode();
                                long currentTimeMillis = System.currentTimeMillis();
                                long expiryTimeMillis = currentTimeMillis + 5 * 60 * 1000;

                                db.execSQL("UPDATE user SET verify_code = ?, timestamp = ? WHERE email = ?",
                                        new Object[]{verificationCode, expiryTimeMillis, email});

                                sendVerificationEmail(email, verificationCode);
                            } else {
                                Toast.makeText(ForgotActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ForgotActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
                        }

                        cursor.close();
                        progressBar.setVisibility(View.GONE);
                    });
                }).start();
            }

            private void sendVerificationEmail(String email, String verificationCode) {
                String subject = "Password Reset Verification Code";
                String body = "Your verification code is: " + verificationCode +
                        "\nPlease enter this code to reset your password.";

                String senderEmail = BuildConfig.SEND_EMAIL;
                String senderPassword = BuildConfig.SEND_PASSWORD;

                EmailSettings emailSettings = new EmailSettings(senderEmail, senderPassword);
                emailSettings.sendEmail(email, subject, body, new EmailSettings.EmailSendCallback() {
                    public void onEmailSent(boolean success) {
                        runOnUiThread(() -> {
                            if (success) {
                                Toast.makeText(ForgotActivity.this, "Verification code sent to your email.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ForgotActivity.this, VerificationActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ForgotActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        });
                    }
                });
            }


        });


        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(ForgotActivity.this, MainActivity.class);
                startActivity(intentLogin);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}