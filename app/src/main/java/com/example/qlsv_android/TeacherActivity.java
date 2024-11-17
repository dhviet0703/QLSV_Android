package com.example.qlsv_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
//import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class TeacherActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView ;
    Toolbar toolbar;
    ImageButton btnMonHoc, btnLichHoc, btnSinhVien ;

    TextView userInfo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        View headerView = navigationView.getHeaderView(0);

        userInfo = headerView.findViewById(R.id.info_user);

        String userName = getIntent().getStringExtra("userName");

        TextView helloUserInfo = findViewById(R.id.hello_user_info);
        if (helloUserInfo != null && userName != null ) {
            helloUserInfo.setText("Xin chào, " + userName);
        }
        if(userInfo != null && userName != null){
            userInfo.setText(userName);

        }


        btnMonHoc = findViewById(R.id.btn_GV_MonHoc);
        btnLichHoc = findViewById(R.id.btn_GV_LichHoc);
        btnSinhVien = findViewById(R.id.btn_GV_SinhVien);

        btnMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherActivity.this, MonHocActivity.class);
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