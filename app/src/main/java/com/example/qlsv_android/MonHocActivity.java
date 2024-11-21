package com.example.qlsv_android;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.qlsv_android.helper.DatabaseHelper;

import java.util.ArrayList;

public class MonHocActivity extends AppCompatActivity {
    ImageButton btnBack;

    TextView txtNganh;

    Spinner spinner_hk1, spinner_hk2, spinner_hk3, spinner_hk4, spinner_hk5;

    SQLiteDatabase db;

    DatabaseHelper dbHepler;
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mon_hoc);

        btnBack = findViewById(R.id.imageButton3);
        txtNganh = findViewById(R.id.txtNganh);

        spinner_hk1 = findViewById(R.id.spinner_hk1);
        spinner_hk2 = findViewById(R.id.spinner_hk2);
        spinner_hk3 = findViewById(R.id.spinner_hk3);
        spinner_hk4 = findViewById(R.id.spinner_hk4);
        spinner_hk5 = findViewById(R.id.spinner_hk5);
        txtNganh.setText("Công nghệ thông tin:");
        btnBack.setOnClickListener(v -> finish());

        dbHepler = new DatabaseHelper(this);
        db = dbHepler.getReadableDatabase();
        loadSpinnerData(spinner_hk1, 1);
        loadSpinnerData(spinner_hk2, 2);
        loadSpinnerData(spinner_hk3, 3);
        loadSpinnerData(spinner_hk4, 4);
        loadSpinnerData(spinner_hk5, 5);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    @SuppressLint("Recycle")
    private void loadSpinnerData(Spinner spinner, int i) {
        ArrayList<String> subjects = new ArrayList<>();
        Cursor cursor = null;
        int totalCredits = 0;

        try {
            cursor = db.rawQuery("SELECT ten_mon, tin_chi FROM Mon_Hoc WHERE ky = ?", new String[]{String.valueOf(i)});
            if (cursor.moveToFirst()) {
                do {
                    String subject = cursor.getString(0) + "        " + cursor.getInt(1);
                    subjects.add(subject);

                    // Cộng tín chỉ của môn học
                    totalCredits += cursor.getInt(1);
                } while (cursor.moveToNext());
            } else {
                subjects.add("No subjects found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            subjects.add("Error loading data");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        if (!subjects.isEmpty() && !subjects.get(0).equals("No subjects found") && !subjects.get(0).equals("Error loading data")) {
            subjects.add(0, "Học kì" + i + ": " + totalCredits);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}