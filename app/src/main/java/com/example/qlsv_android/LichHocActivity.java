package com.example.qlsv_android;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv_android.helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class LichHocActivity extends AppCompatActivity {

    private ListView listView;

    private ArrayAdapter<String> adapter;
    private DatabaseHelper dbHelper;
    Button btnTaoLich , btnXoaLich;

    ImageButton imageButtonB ;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lich_hoc);

        dbHelper = new DatabaseHelper(this);
         listView = findViewById(R.id.listView);

        List<String> lichHocList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT " +
                "LichHoc.id AS lichhoc_id, " +
                "LichHoc.ngay_hoc, " +
                "LichHoc.gio_bat_dau, " +
                "LichHoc.gio_ket_thuc, " +
                "Lop_Hoc.ten_lop, " +
                "Mon_Hoc.ten_mon, " +
                "user.ho_ten " +
                "FROM LichHoc " +
                "JOIN Lop_Hoc ON LichHoc.lop_id = Lop_Hoc.id " +
                "JOIN Mon_Hoc ON LichHoc.monhoc_id = Mon_Hoc.id " +
                "JOIN user ON LichHoc.giangvien_id = user.id;";

        try (Cursor cursor = db.rawQuery(query, null)) {
            if (cursor.moveToFirst()) {
                do {
                    String lichHocDetails = String.format(
                            "Môn học: %s\nLớp: %s\nGiờ bắt đầu: %s\nGiờ kết thúc: %s\nGiảng viên: %s\nNgày học: %s",
                            cursor.getString(cursor.getColumnIndexOrThrow("ten_mon")),
                            cursor.getString(cursor.getColumnIndexOrThrow("gio_bat_dau")),
                            cursor.getString(cursor.getColumnIndexOrThrow("ten_lop")),
                            cursor.getString(cursor.getColumnIndexOrThrow("gio_ket_thuc")),
                            cursor.getString(cursor.getColumnIndexOrThrow("ho_ten")),
                            cursor.getString(cursor.getColumnIndexOrThrow("ngay_hoc"))
                    );
                    lichHocList.add(lichHocDetails);
                } while (cursor.moveToNext());
            }
        }

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                lichHocList
        );
        listView.setAdapter(adapter);

        imageButtonB = findViewById(R.id.imageBackB);
        btnTaoLich = findViewById(R.id.btnTaoLich);
        btnXoaLich = findViewById(R.id.btnXoaLich);

        imageButtonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnXoaLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}