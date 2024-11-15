package com.example.qlsv_android;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.qlsv_android.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SinhVien.db";
    private static final int DATABASE_VERSION  = 1;
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Lop_Hoc (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ten_lop TEXT," +
                "khoa TEXT" +
                ");");

        db.execSQL("CREATE TABLE user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ho_ten TEXT," +
                "username TEXT UNIQUE," +
                "password TEXT," +
                "ngay_sinh DATE," +
                "gioi_tinh CHECK(gioi_tinh IN ('Nam', 'Nu'))," +
                "dia_chi TEXT," +
                "email TEXT," +
                "dien_thoai TEXT," +
                "role TEXT CHECK(role IN ('sinhvien', 'giangvien', 'admin'))," +
                "created_at TEXT," +
                "updated_at TEXT" +
                ");");

        db.execSQL("CREATE TABLE sinhvien_detail (" +
                "user_id INTEGER," +
                "lop_id INTEGER," +
                "nganh_hoc TEXT," +
                "khoa_hoc TEXT," +
                "FOREIGN KEY(user_id) REFERENCES user(id)," +
                "FOREIGN KEY(lop_id) REFERENCES Lop_Hoc(id)" +
                ");");

        db.execSQL("CREATE TABLE giangvien_detail (" +
                "user_id INTEGER," +
                "khoa_id TEXT," +
                "hoc_vi TEXT," +
                "chuc_vu TEXT," +
                "FOREIGN KEY(user_id) REFERENCES user(id)" +
                ");");

        db.execSQL("CREATE TABLE Mon_Hoc (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ten_mon TEXT," +
                "tin_chi INTEGER," +
                "ky INTEGER," +
                "mo_ta TEXT" +
                ");");

        db.execSQL("CREATE TABLE LichHoc (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "lop_id INTEGER," +
                "monhoc_id INTEGER," +
                "giangvien_id INTEGER," +
                "ngay_hoc DATE," +
                "gio_bat_dau TEXT," +
                "gio_ket_thuc TEXT," +
                "FOREIGN KEY(lop_id) REFERENCES Lop_Hoc(id)," +
                "FOREIGN KEY(monhoc_id) REFERENCES Mon_Hoc(id)," +
                "FOREIGN KEY(giangvien_id) REFERENCES giangvien_detail(user_id)" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS LichHoc");
        db.execSQL("DROP TABLE IF EXISTS Mon_Hoc");
        db.execSQL("DROP TABLE IF EXISTS giangvien_detail");
        db.execSQL("DROP TABLE IF EXISTS sinhvien_detail");
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS Lop_Hoc");
        onCreate(db);
    }

    public void addUser(String hoTen, String username, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ho_ten", hoTen);
        values.put("username", username);
        values.put("password", password);
        values.put("role", role);

        db.insert("user", null, values);
        db.close();
    }

    @SuppressLint("Range")
    public User getUser(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + "user" + " WHERE " + "username" + " = ? AND " + "password" + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("id")));
            user.setHoTen(cursor.getString(cursor.getColumnIndex("ho_ten")));
            user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setNgaySinh(cursor.getString(cursor.getColumnIndex("ngay_sinh")));
            user.setGioiTinh(cursor.getString(cursor.getColumnIndex("gioi_tinh")));
            user.setDiaChi(cursor.getString(cursor.getColumnIndex("dia_chi")));
            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            user.setDienThoai(cursor.getString(cursor.getColumnIndex("dien_thoai")));
            user.setRole(cursor.getString(cursor.getColumnIndex("role")));
            user.setCreatedAt(cursor.getString(cursor.getColumnIndex("created_at")));
            user.setUpdatedAt(cursor.getString(cursor.getColumnIndex("updated_at")));
        }

        cursor.close();
        db.close();

        return user;
    }


}
