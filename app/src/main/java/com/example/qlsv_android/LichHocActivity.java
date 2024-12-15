package com.example.qlsv_android;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.qlsv_android.helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class LichHocActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    Button btnTaoLich, btnXoaLich;

    ImageButton imageButtonB;
    SQLiteDatabase db;
    private int selectedPosition = -1;


    private List<String> lichHocList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lich_hoc);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        ;

        String username = getIntent().getStringExtra("username");
        String role = "";

        try (Cursor cursor1 = db.rawQuery("SELECT role FROM user WHERE username = ?", new String[]{username})) {
            if (cursor1.moveToFirst()) {
                role = cursor1.getString(cursor1.getColumnIndex("role"));
            }
        }

        ListView listView = findViewById(R.id.listView);

        lichHocList = new ArrayList<>();

        String query = "";
        if ("sinhvien".equals(role)) {
            query += "SELECT " +
                    "LichHoc.id AS lichhoc_id, " +
                    "LichHoc.ngay_hoc, " +
                    "LichHoc.gio_bat_dau, " +
                    "LichHoc.gio_ket_thuc, " +
                    "Lop_Hoc.ten_lop, " +
                    "Mon_Hoc.ten_mon, " +
                    "user.ho_ten AS giangvien_ho_ten " +
                    "FROM LichHoc " +
                    "JOIN Lop_Hoc ON LichHoc.lop_id = Lop_Hoc.id " +
                    "JOIN Mon_Hoc ON LichHoc.monhoc_id = Mon_Hoc.id " +
                    "JOIN giangvien_detail ON LichHoc.giangvien_id = giangvien_detail.user_id " +
                    "JOIN user ON giangvien_detail.user_id = user.id " +
                    "WHERE LichHoc.lop_id IN ( " +
                    "    SELECT lop_id " +
                    "    FROM sinhvien_detail " +
                    "    WHERE user_id = ? " +
                    ")";
        } else if ("giangvien".equals(role)) {
            query += "SELECT " +
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
                    "JOIN user ON LichHoc.giangvien_id = user.id " +
                    "WHERE user.ho_ten = ?";
        }
        try (Cursor cursor = db.rawQuery(query, new String[]{username})) {
            if (cursor.moveToFirst()) {
                do {
                    String lichHocDetails = String.format(
                            "ID: %s\nMôn học: %s\nGiờ bắt đầu: %s\nLớp: %s\nGiờ kết thúc: %s\nGiảng viên: %s\nNgày học: %s",
                            cursor.getInt(cursor.getColumnIndex("lichhoc_id")),
                            cursor.getString(cursor.getColumnIndex("ten_mon")),
                            cursor.getString(cursor.getColumnIndex("gio_bat_dau")),
                            cursor.getString(cursor.getColumnIndex("ten_lop")),
                            cursor.getString(cursor.getColumnIndex("gio_ket_thuc")),
                            cursor.getString(cursor.getColumnIndex("ho_ten")),
                            cursor.getString(cursor.getColumnIndex("ngay_hoc"))
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

        btnXoaLich.setVisibility(View.GONE);
        if ("sinhvien".equals(role)){
            btnTaoLich.setVisibility(View.GONE);
        }

        imageButtonB.setOnClickListener(v -> finish());
        String finalRole = role;
        btnTaoLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalRole.equals("giangvien")) {
                    openCreateScheduleDialog();
                }
                else {
                    Toast.makeText(LichHocActivity.this,"Chỉ có giáo viên mới có quyền tạo" ,Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnXoaLich.setOnClickListener(v -> {
            if (selectedPosition >= 0) {
                String selectedItem = lichHocList.get(selectedPosition);

                // Extract the lichHocId from the selectedItem
                int lichHocId = getLichHocIdFromDetails(selectedItem);

                if (lichHocId != -1) {
                    // Delete the schedule from the database
                    int rowsDeleted = db.delete("LichHoc", "id = ?", new String[]{String.valueOf(lichHocId)});
                    if (rowsDeleted > 0) {
                        Toast.makeText(LichHocActivity.this, "Xóa lịch học thành công!", Toast.LENGTH_SHORT).show();

                        // Update the list and UI
                        lichHocList.remove(selectedPosition);
                        adapter.notifyDataSetChanged();
                        selectedPosition = -1; // Reset selected position
                    } else {
                        Toast.makeText(LichHocActivity.this, "Xóa lịch học thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LichHocActivity.this, "Không thể xác định lịch học để xóa!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LichHocActivity.this, "Vui lòng chọn một lịch học để xóa!", Toast.LENGTH_SHORT).show();
            }
        });

        btnXoaLich.setOnClickListener(v -> {
            if (selectedPosition >= 0) {
                String selectedItem = lichHocList.get(selectedPosition);

                // Extract the lichHocId from the selectedItem
                int lichHocId = getLichHocIdFromDetails(selectedItem);

                if (lichHocId != -1) {
                    // Get the current user's ID
                    int currentUserId = getCurrentUserId();

                    // Check if the current user is the creator of this schedule
                    boolean isCreator = checkScheduleCreator(lichHocId, currentUserId);

                    if (isCreator) {
                        // Delete the schedule from the database
                        int rowsDeleted = db.delete("LichHoc", "id = ?", new String[]{String.valueOf(lichHocId)});
                        if (rowsDeleted > 0) {
                            Toast.makeText(LichHocActivity.this, "Xóa lịch học thành công!", Toast.LENGTH_SHORT).show();

                            // Update the list and UI
                            lichHocList.remove(selectedPosition);
                            adapter.notifyDataSetChanged();
                            selectedPosition = -1; // Reset selected position
                        } else {
                            Toast.makeText(LichHocActivity.this, "Xóa lịch học thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LichHocActivity.this, "Bạn không có quyền xóa lịch học này!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LichHocActivity.this, "Không thể xác định lịch học để xóa!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LichHocActivity.this, "Vui lòng chọn một lịch học để xóa!", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position; // Lưu vị trí được chọn
            Toast.makeText(LichHocActivity.this, "Đã chọn: " + lichHocList.get(position), Toast.LENGTH_SHORT).show();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private int getLichHocIdFromDetails(String selectedItem) {
        try {
        String firstLine = selectedItem.split("\n")[0];

        String idPart = firstLine.split(":")[1].trim();

        return Integer.parseInt(idPart);
    } catch (NumberFormatException | IndexOutOfBoundsException e) {
        e.printStackTrace();
        return -1;
    }
    }


    private void openCreateScheduleDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_schedule, null);
        builder.setView(dialogView);

        // Input fields
        EditText edtNgayHoc = dialogView.findViewById(R.id.edtNgayHoc);
        EditText edtGioBatDau = dialogView.findViewById(R.id.edtGioBatDau);
        EditText edtGioKetThuc = dialogView.findViewById(R.id.edtGioKetThuc);
        Spinner spinnerLop = dialogView.findViewById(R.id.spinnerLop);
        Spinner spinnerMonHoc = dialogView.findViewById(R.id.spinnerMonHoc);

        Button btnLuu = dialogView.findViewById(R.id.btnLuu);
        Button btnHuy = dialogView.findViewById(R.id.btnHuy);

        // Populate the spinners with data
        ArrayAdapter<String> lopAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getLopList());
        lopAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLop.setAdapter(lopAdapter);

        ArrayAdapter<String> monHocAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getMonHocList());
        monHocAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonHoc.setAdapter(monHocAdapter);

        AlertDialog dialog = builder.create();

        btnLuu.setOnClickListener(v -> {
            String ngayHoc = edtNgayHoc.getText().toString().trim();
            String gioBatDau = edtGioBatDau.getText().toString().trim();
            String gioKetThuc = edtGioKetThuc.getText().toString().trim();
            String selectedLop = spinnerLop.getSelectedItem().toString();
            String selectedMonHoc = spinnerMonHoc.getSelectedItem().toString();

            if (ngayHoc.isEmpty() || gioBatDau.isEmpty() || gioKetThuc.isEmpty()) {
                Toast.makeText(LichHocActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            int lopId = getLopId(selectedLop, db);
            int monHocId = getMonHocId(selectedMonHoc, db);

            // Insert data into database
            ContentValues values = new ContentValues();
            values.put("ngay_hoc", ngayHoc);
            values.put("gio_bat_dau", gioBatDau);
            values.put("gio_ket_thuc", gioKetThuc);
            values.put("lop_id", lopId);
            values.put("monhoc_id", monHocId);
            values.put("giangvien_id", getCurrentUserId()); // Current lecturer ID

            long result = db.insert("LichHoc", null, values);

            if (result != -1) {
                Toast.makeText(LichHocActivity.this, "Tạo lịch học thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                reloadScheduleList();
            } else {
                Toast.makeText(LichHocActivity.this, "Có lỗi xảy ra, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });

        btnHuy.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @SuppressLint("Range")
    private List<String> getMonHocList() {
        List<String> monHocList = new ArrayList<>();
        String query = "SELECT ten_mon FROM Mon_Hoc";
        try (Cursor cursor = db.rawQuery(query, null)) {
            while (cursor.moveToNext()) {
                monHocList.add(cursor.getString(cursor.getColumnIndex("ten_mon")));
            }
        }
        return monHocList;
    }

    @SuppressLint("Range")
    private List<String> getLopList() {
        List<String> lopList = new ArrayList<>();
        String query = "SELECT ten_lop FROM Lop_Hoc";
        try (Cursor cursor = db.rawQuery(query, null)) {
            while (cursor.moveToNext()) {
                lopList.add(cursor.getString(cursor.getColumnIndex("ten_lop")));
            }
        }
        return lopList;
    }
    @SuppressLint("Range")
    private Integer getCurrentUserId() {
        String username = getIntent().getStringExtra("username");
        int userId = -1;
        try (Cursor cursor = db.rawQuery("SELECT id FROM user WHERE username = ?", new String[]{username})) {
            if (cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndex("id"));
            }
        }
        return userId;
    }


    @SuppressLint("Range")
    private int getLopId(String tenLop, SQLiteDatabase db) {
        if (db == null) {
            Log.e("DatabaseError", "Database instance is null!");
            return -1;
        }
        if (tenLop == null || tenLop.isEmpty()) {
            Log.e("DatabaseError", "Lop name is null or empty");
            return -1;
        }

        String query = "SELECT id FROM Lop_Hoc WHERE ten_lop = ?";
        Cursor cursor = db.rawQuery(query, new String[]{tenLop});

        int lopId = -1;
        if (cursor.moveToFirst()) {
            lopId = cursor.getInt(cursor.getColumnIndex("id"));
        }
        cursor.close();

        return lopId;
    }

    @SuppressLint("Range")
    private int getMonHocId(String tenMon, SQLiteDatabase db) {
        if (db == null) {
            Log.e("DatabaseError", "Database instance is null!");
            return -1;
        }
        if (tenMon == null || tenMon.isEmpty()) {
            Log.e("DatabaseError", "Mon Hoc name is null or empty");
            return -1;
        }

        String query = "SELECT id FROM Mon_Hoc WHERE ten_mon = ?";
        Cursor cursor = db.rawQuery(query, new String[]{tenMon});

        int monHocId = -1;
        if (cursor.moveToFirst()) {
            monHocId = cursor.getInt(cursor.getColumnIndex("id"));
        }
        cursor.close();

        return monHocId;
    }

    @SuppressLint("Range")
    private boolean checkScheduleCreator(int lichHocId, int currentUserId) {
        String query = "SELECT giangvien_id FROM LichHoc WHERE id = ?";
        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(lichHocId)})) {
            if (cursor.moveToFirst()) {
                int creatorId = cursor.getInt(cursor.getColumnIndex("giangvien_id"));
                return creatorId == currentUserId;
            }
        } catch (Exception e) {
            Log.e("CheckCreator", "Error checking schedule creator", e);
        }
        return false;
    }

    private void reloadScheduleList() {
        lichHocList.clear();
        String query = "SELECT LichHoc.id AS lichhoc_id, " +
                "LichHoc.ngay_hoc, " +
                "LichHoc.gio_bat_dau, " +
                "LichHoc.gio_ket_thuc, " +
                "Lop_Hoc.ten_lop, " +
                "Mon_Hoc.ten_mon, " +
                "user.ho_ten " +
                "FROM LichHoc " +
                "JOIN Lop_Hoc ON LichHoc.lop_id = Lop_Hoc.id " +
                "JOIN Mon_Hoc ON LichHoc.monhoc_id = Mon_Hoc.id " +
                "JOIN user ON LichHoc.giangvien_id = user.id";

        try (Cursor cursor = db.rawQuery(query, null)) {
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String lichHocDetails = String.format(
                            "Môn học: %s\nGiờ bắt đầu: %s\nLớp: %s\nGiờ kết thúc: %s\nGiảng viên: %s\nNgày học: %s",
                            cursor.getString(cursor.getColumnIndex("ten_mon")),
                            cursor.getString(cursor.getColumnIndex("gio_bat_dau")),
                            cursor.getString(cursor.getColumnIndex("ten_lop")),
                            cursor.getString(cursor.getColumnIndex("gio_ket_thuc")),
                            cursor.getString(cursor.getColumnIndex("ho_ten")),
                            cursor.getString(cursor.getColumnIndex("ngay_hoc"))
                    );
                    lichHocList.add(lichHocDetails); // Add the details to the list
                } while (cursor.moveToNext());
            }
        }

        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onResume() {
        super.onResume();
        reloadScheduleList();
    }

    @Override
    protected void onDestroy() {
        if (db != null) {
            db.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}



