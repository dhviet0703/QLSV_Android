package com.example.qlsv_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.qlsv_android.helper.DatabaseHelper;
import com.example.qlsv_android.model.SinhVienFullInfo;
import com.google.android.material.navigation.NavigationView;

public class studentHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView ;
    Toolbar toolbar;
    ImageButton btnMonHocSV, btnLichHocSV ;
    TextView userInfo;

    SQLiteDatabase db;
    String username;

    DatabaseHelper dbHelper;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar2);


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

        TextView helloUserInfo = findViewById(R.id.hello_sv_user_info);
        if (helloUserInfo != null && hoten != null ) {
            helloUserInfo.setText("Xin chào, " + hoten);
        }
        if(userInfo != null && hoten != null){
            userInfo.setText(hoten);
        }

        Toast.makeText(this, "Chào mừng " + hoten, Toast.LENGTH_SHORT).show();
        btnMonHocSV = findViewById(R.id.btn_SV_MonHoc);
        btnLichHocSV = findViewById(R.id.btn_SV_LichHoc);

        btnMonHocSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSubject = new Intent(studentHome.this, MonHocActivity.class);
                startActivity(intentSubject);
            }
        });
        btnLichHocSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCalendar = new Intent(studentHome.this, LichHocActivity.class);
                intentCalendar.putExtra("username", username);
                startActivity(intentCalendar);
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
                SinhVienFullInfo sinhVienFullInfo = (SinhVienFullInfo) dbHelper.getUserNotPass(username);

                Intent intentInfoSV = new Intent(studentHome.this, InfoActivity.class);
                intentInfoSV.putExtra("username", username);
                if (sinhVienFullInfo != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", sinhVienFullInfo.getUser());
                    bundle.putSerializable("sinhVienDetails", sinhVienFullInfo.getSinhVienDetails());

                    intentInfoSV.putExtras(bundle);
                }

                startActivity(intentInfoSV);


        } else if (id == R.id.nav_password) {
            Intent intentPassword = new Intent(studentHome.this, ResetPasswordActivity.class);
            intentPassword .putExtra("username", username);
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