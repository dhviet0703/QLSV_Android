package com.example.qlsv_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.qlsv_android.helper.DatabaseHelper;
import com.example.qlsv_android.model.GiangVienFullInfo;
import com.google.android.material.navigation.NavigationView;

public class TeacherActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView ;
    Toolbar toolbar;

    ImageButton btnMonHoc, btnLichHoc, btnSinhVien ;

    SQLiteDatabase db;

    DatabaseHelper dbHelper;
    TextView userInfo;
    String username;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        userInfo = headerView.findViewById(R.id.info_user);

        String hoten = getIntent().getStringExtra("hoten");

        dbHelper = new DatabaseHelper(this);
        username = getIntent().getStringExtra("username");
        TextView helloUserInfo = findViewById(R.id.hello_user_info);
        if (helloUserInfo != null && hoten != null ) {
            helloUserInfo.setText("Xin chào, " + hoten);
        }
        if(userInfo != null && hoten != null){
            userInfo.setText(hoten);
        }

        Toast.makeText(this, "Chào mừng " + hoten, Toast.LENGTH_SHORT).show();
        btnMonHoc = findViewById(R.id.btn_GV_MonHoc);
        btnLichHoc = findViewById(R.id.btn_GV_LichHoc);
        btnSinhVien = findViewById(R.id.btn_GV_SinhVien);


        btnSinhVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherActivity.this, ActSinhVienActivity.class);
                startActivity(intent);
            }
        });

        btnLichHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherActivity.this, LichHocActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
        btnMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherActivity.this, MonHocActivity.class);
                startActivity(intent);
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_info) {
            GiangVienFullInfo giangVienInfo = (GiangVienFullInfo) dbHelper.getUserNotPass(username);

            Intent intentInfo = new Intent(TeacherActivity.this, InfoActivity.class);
            intentInfo.putExtra("username", username);

            if (giangVienInfo != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", giangVienInfo.getUser());
                bundle.putSerializable("giangVienDetails", giangVienInfo.getGiangVienDetails());

                intentInfo.putExtras(bundle);
            }

            startActivity(intentInfo);

        } else if (id == R.id.nav_password) {
            Intent intentPassword = new Intent(TeacherActivity.this, ResetPasswordActivity.class);
            intentPassword.putExtra("username", username);
            startActivity(intentPassword);

        } else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        finish();
                    })
                    .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        } else {
            return false;
        }

        return true;
    }


}