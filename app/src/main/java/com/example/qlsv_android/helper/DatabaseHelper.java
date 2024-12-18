package com.example.qlsv_android.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.qlsv_android.Utils.PasswordUtils;
import com.example.qlsv_android.model.GiangVienFullInfo;
import com.example.qlsv_android.model.Giangvien_Details;
import com.example.qlsv_android.model.SinhVienFullInfo;
import com.example.qlsv_android.model.SinhVien_Details;
import com.example.qlsv_android.model.User;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SinhVien.db";
    private static final int DATABASE_VERSION = 7;

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
                "image BLOB," +
                "verify_code TEXT," +
                "timestamp TEXT," +
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

        db.execSQL("INSERT INTO Mon_Hoc (ten_mon, tin_chi, ky, mo_ta) VALUES " +
                "('Toán cao cấp 1', 3, 1, 'Môn học về toán cao cấp cơ bản')," +
                "('Lập trình cơ bản', 4, 1, 'Môn học về lập trình cơ bản trong ngôn ngữ C')," +
                "('Kỹ năng mềm', 2, 1, 'Phát triển kỹ năng giao tiếp và làm việc nhóm');");

        db.execSQL("INSERT INTO Mon_Hoc (ten_mon, tin_chi, ky, mo_ta) VALUES " +
                "('Toán cao cấp 2', 3, 2, 'Môn học tiếp theo của toán cao cấp 1')," +
                "('Cấu trúc dữ liệu', 4, 2, 'Học về cấu trúc dữ liệu trong lập trình')," +
                "('Hệ điều hành', 3, 2, 'Giới thiệu về hệ điều hành và các nguyên lý cơ bản');");

        db.execSQL("INSERT INTO Mon_Hoc (ten_mon, tin_chi, ky, mo_ta) VALUES " +
                "('Cơ sở dữ liệu', 3, 3, 'Kiến thức cơ bản về cơ sở dữ liệu và SQL')," +
                "('Mạng máy tính', 4, 3, 'Giới thiệu mạng máy tính và giao thức mạng')," +
                "('Phân tích thiết kế hệ thống', 3, 3, 'Phương pháp phân tích và thiết kế hệ thống phần mềm');");

        db.execSQL("INSERT INTO Mon_Hoc (ten_mon, tin_chi, ky, mo_ta) VALUES " +
                "('Lập trình hướng đối tượng', 4, 4, 'Lập trình theo phương pháp hướng đối tượng với Java')," +
                "('Đồ án phần mềm', 3, 4, 'Thực hiện dự án phần mềm thực tế')," +
                "('Kiến trúc máy tính', 3, 4, 'Giới thiệu kiến trúc phần cứng của máy tính');");

        db.execSQL("INSERT INTO Mon_Hoc (ten_mon, tin_chi, ky, mo_ta) VALUES " +
                "('Trí tuệ nhân tạo', 3, 5, 'Cơ bản về trí tuệ nhân tạo và học máy')," +
                "('An toàn thông tin', 3, 5, 'Kiến thức về bảo mật và an toàn thông tin')," +
                "('Phát triển ứng dụng web', 4, 5, 'Thiết kế và phát triển ứng dụng web hiện đại');");

        // Lop_Hoc
        db.execSQL("INSERT INTO Lop_Hoc (ten_lop, khoa) VALUES ('CNTT1', '2023');");
        db.execSQL("INSERT INTO Lop_Hoc (ten_lop, khoa) VALUES ('CNTT2', '2022');");
        db.execSQL("INSERT INTO Lop_Hoc (ten_lop, khoa) VALUES ('QTKD1', '2023');");

        // user
        db.execSQL("INSERT INTO user (ho_ten, username, password, ngay_sinh, gioi_tinh, dia_chi, email, dien_thoai, role, created_at, updated_at) " +
                "VALUES ('Nguyen Van A', 'nguyenvana', '1234', '2001-05-12', 'Nam', 'Ha Noi', 'vana@example.com', '0912345678', 'sinhvien', '2023-11-01', '2023-11-01');");
        db.execSQL("INSERT INTO user (ho_ten, username, password, ngay_sinh, gioi_tinh, dia_chi, email, dien_thoai, role, created_at, updated_at) " +
                "VALUES ('Tran Thi B', 'tranthib', '1234', '2000-11-22', 'Nu', 'Hai Phong', 'thib@example.com', '0934567890', 'sinhvien', '2023-11-01', '2023-11-01');");
        db.execSQL("INSERT INTO user (ho_ten, username, password, ngay_sinh, gioi_tinh, dia_chi, email, dien_thoai, role, created_at, updated_at) " +
                "VALUES ('Le Van C', 'levanc', '1234', '1990-01-15', 'Nam', 'Da Nang', 'vanc@example.com', '0987654321', 'giangvien', '2023-11-01', '2023-11-01');");
        db.execSQL("INSERT INTO user (ho_ten, username, password, ngay_sinh, gioi_tinh, dia_chi, email, dien_thoai, role, created_at, updated_at) " +
                "VALUES ('Admin User', 'admin', '1234', '1985-08-01', 'Nam', 'Ho Chi Minh', 'admin@example.com', '0901234567', 'admin', '2023-11-01', '2023-11-01');");

        //sinhvien_detail

        db.execSQL("INSERT INTO sinhvien_detail (user_id, lop_id, nganh_hoc, khoa_hoc) " +
                "VALUES (1, 1, 'Công nghệ thông tin', 'Khoa CNTT');");
        db.execSQL("INSERT INTO sinhvien_detail (user_id, lop_id, nganh_hoc, khoa_hoc) " +
                "VALUES (2, 2, 'Quản trị kinh doanh', 'Khoa QTKD');");

        db.execSQL("INSERT INTO LichHoc (lop_id, monhoc_id, giangvien_id, ngay_hoc, gio_bat_dau, gio_ket_thuc) " +
                "VALUES (1, 1, 3, '2024-11-23', '08:00', '10:00');");

        db.execSQL("INSERT INTO LichHoc (lop_id, monhoc_id, giangvien_id, ngay_hoc, gio_bat_dau, gio_ket_thuc) " +
                "VALUES (1, 2, 3, '2024-11-24', '10:15', '12:15');");

        db.execSQL("INSERT INTO LichHoc (lop_id, monhoc_id, giangvien_id, ngay_hoc, gio_bat_dau, gio_ket_thuc) " +
                "VALUES (2, 3, 3, '2024-11-25', '13:00', '15:00');");

        db.execSQL("INSERT INTO LichHoc (lop_id, monhoc_id, giangvien_id, ngay_hoc, gio_bat_dau, gio_ket_thuc) " +
                "VALUES (2, 4, 3, '2024-11-26', '15:30', '17:30');");

        db.execSQL("INSERT INTO LichHoc (lop_id, monhoc_id, giangvien_id, ngay_hoc, gio_bat_dau, gio_ket_thuc) " +
                "VALUES (3, 5, 3, '2024-11-27', '08:00', '10:00');");

        db.execSQL("INSERT INTO LichHoc (lop_id, monhoc_id, giangvien_id, ngay_hoc, gio_bat_dau, gio_ket_thuc) " +
                "VALUES (3, 6, 3, '2024-11-28', '10:15', '12:15');");

        db.execSQL("INSERT INTO user (ho_ten, username, password, ngay_sinh, gioi_tinh, dia_chi, email, dien_thoai, role, created_at, updated_at) " +
                "VALUES ('Nguyen Thi D', 'nguyenthid', 'password123', '1988-03-15', 'Nu', 'Hue', 'thid@example.com', '0911122233', 'giangvien', '2023-11-01', '2023-11-01');");

        db.execSQL("INSERT INTO giangvien_detail (user_id, khoa_id, hoc_vi, chuc_vu) " +
                "VALUES (5, 'QTKD', 'Thạc sĩ', 'Giảng viên');");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS LichHoc");
        db.execSQL("DROP TABLE IF EXISTS Mon_Hoc");
        db.execSQL("DROP TABLE IF EXISTS giangvien_detail");
        db.execSQL("DROP TABLE IF EXISTS sinhvien_detail");
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS Lop_Hoc");
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE user ADD COLUMN image BLOB");
        }
        onCreate(db);
    }

    public void addUser(String hoTen, String username, String password, String role, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String hashedPassword = PasswordUtils.hashPassword(password);

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", hashedPassword);
        values.put("ho_ten", hoTen);
        values.put("role", role);
        values.put("email", email);

        long userId = db.insert("user", null, values);

        if (userId != -1) {
            if ("giangvien".equalsIgnoreCase(role)) {
                ContentValues giangVienValues = new ContentValues();
                giangVienValues.put("user_id", userId);
                giangVienValues.put("hoc_vi", "null");
                giangVienValues.put("chuc_vu", "Giảng viên");
                giangVienValues.put("khoa_id", "null");

                db.insert("giangvien_detail", null, giangVienValues);
            } else if ("sinhvien".equalsIgnoreCase(role)) {
                ContentValues sinhVienValues = new ContentValues();
                sinhVienValues.put("user_id", userId);
                sinhVienValues.put("lop_id", "null");
                sinhVienValues.put("nganh_hoc", "null");
                sinhVienValues.put("khoa_hoc", "null");

                db.insert("sinhvien_detail", null, sinhVienValues);
            }
        }

//        db.insert("user", null, values);
        db.close();
    }




    @SuppressLint("Range")
    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM user WHERE username = ?";
        Cursor cursor = null;
        User user = null;

        try {
            cursor = db.rawQuery(query, new String[]{username});

            if (cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                user.setHoTen(cursor.getString(cursor.getColumnIndex("ho_ten")));
                user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                user.setPassword(cursor.getString(cursor.getColumnIndex("password"))); // Hashed password
                user.setNgaySinh(cursor.getString(cursor.getColumnIndex("ngay_sinh")));
                user.setGioiTinh(cursor.getString(cursor.getColumnIndex("gioi_tinh")));
                user.setDiaChi(cursor.getString(cursor.getColumnIndex("dia_chi")));
                user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                user.setDienThoai(cursor.getString(cursor.getColumnIndex("dien_thoai")));
                user.setRole(cursor.getString(cursor.getColumnIndex("role")));
                user.setCreatedAt(cursor.getString(cursor.getColumnIndex("created_at")));
                user.setUpdatedAt(cursor.getString(cursor.getColumnIndex("updated_at")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return user;
    }

    @SuppressLint("Range")
    public Object getUserNotPass(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }

        SQLiteDatabase db = this.getReadableDatabase();

        String userQuery = "SELECT * FROM user WHERE username = ?";
        Cursor userCursor = db.rawQuery(userQuery, new String[]{username});

        User user = null;
        if (userCursor.moveToFirst()) {
            user = new User();
            user.setId(userCursor.getInt(userCursor.getColumnIndex("id")));
            user.setHoTen(userCursor.getString(userCursor.getColumnIndex("ho_ten")));
            user.setUsername(userCursor.getString(userCursor.getColumnIndex("username")));
            user.setNgaySinh(userCursor.getString(userCursor.getColumnIndex("ngay_sinh")));
            user.setGioiTinh(userCursor.getString(userCursor.getColumnIndex("gioi_tinh")));
            user.setDiaChi(userCursor.getString(userCursor.getColumnIndex("dia_chi")));
            user.setEmail(userCursor.getString(userCursor.getColumnIndex("email")));
            user.setDienThoai(userCursor.getString(userCursor.getColumnIndex("dien_thoai")));
            user.setRole(userCursor.getString(userCursor.getColumnIndex("role")));
            user.setCreatedAt(userCursor.getString(userCursor.getColumnIndex("created_at")));
            user.setUpdatedAt(userCursor.getString(userCursor.getColumnIndex("updated_at")));
        }

        Object details = null;
        if (user != null) {
            if ("giangvien".equalsIgnoreCase(user.getRole())) {
                String detailsQuery = "SELECT * FROM giangvien_detail WHERE user_id = ?";
                Cursor detailsCursor = db.rawQuery(detailsQuery, new String[]{String.valueOf(user.getId())});

                if (detailsCursor.moveToFirst()) {
                    Giangvien_Details giangvienDetails = new Giangvien_Details();
                    giangvienDetails.setKhoaId(detailsCursor.getString(detailsCursor.getColumnIndex("khoa_id")));
                    giangvienDetails.setHocVi(detailsCursor.getString(detailsCursor.getColumnIndex("hoc_vi")));
                    giangvienDetails.setChucVu(detailsCursor.getString(detailsCursor.getColumnIndex("chuc_vu")));
                    details = new GiangVienFullInfo(user, giangvienDetails);
                }
                detailsCursor.close();
            } else if ("sinhvien".equalsIgnoreCase(user.getRole())) {
                String detailsQuery = "SELECT * FROM sinhvien_detail WHERE user_id = ?";
                Cursor detailsCursor = db.rawQuery(detailsQuery, new String[]{String.valueOf(user.getId())});

                if (detailsCursor.moveToFirst()) {
                    SinhVien_Details sinhvienDetails = new SinhVien_Details();
                    sinhvienDetails.setLopId(detailsCursor.getInt(detailsCursor.getColumnIndex("lop_id")));
                    sinhvienDetails.setNganhHoc(detailsCursor.getString(detailsCursor.getColumnIndex("nganh_hoc")));
                    sinhvienDetails.setKhoaHoc(detailsCursor.getString(detailsCursor.getColumnIndex("khoa_hoc")));
                    details = new SinhVienFullInfo(user, sinhvienDetails);
                }
                detailsCursor.close();
            }
        }

        userCursor.close();
        db.close();

        return details;
    }


    public boolean updateUser(Map<String, Object> userData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            // Cập nhật bảng user
            values.put("ho_ten", (String) userData.get("ho_ten"));
            values.put("username", (String) userData.get("username"));
            values.put("ngay_sinh", (String) userData.get("ngay_sinh"));
            values.put("gioi_tinh", (String) userData.get("gioi_tinh"));
            values.put("dia_chi", (String) userData.get("dia_chi"));
            values.put("email", (String) userData.get("email"));
            values.put("dien_thoai", (String) userData.get("dien_thoai"));
            values.put("updated_at", getCurrentDateTime());

            int userId = ((Integer) userData.get("id")).intValue();
            db.update("user", values, "id = ?", new String[]{String.valueOf(userId)});

            values.clear();
            values.put("khoa_id", (String) userData.get("khoa_id"));
            values.put("hoc_vi", (String) userData.get("hoc_vi"));
            values.put("chuc_vu", (String) userData.get("chuc_vu"));

            db.update("giangvien_detail", values, "user_id = ?",
                    new String[]{String.valueOf(userId)});

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    public void updateGiangVienDetails(Giangvien_Details giangvienDetails) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("khoa_id", giangvienDetails.getKhoaId());
        values.put("hoc_vi", giangvienDetails.getHocVi());
        values.put("chuc_vu", giangvienDetails.getChucVu());

        db.update("giangvien_detail", values, "user_id = ?", new String[]{String.valueOf(giangvienDetails.getUserId())});
        db.close();
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    @SuppressLint("Range")
    public List<Map<String, Object>> getGiangVienWithDetails() {
        List<Map<String, Object>> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT u.*, gv.khoa_id, gv.hoc_vi, gv.chuc_vu " +
                "FROM user u " +
                "LEFT JOIN giangvien_detail gv ON u.id = gv.user_id " +
                "WHERE u.role = 'giangvien'";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Map<String, Object> item = new HashMap<>();

                // Lấy thông tin từ bảng User
                item.put("id", cursor.getInt(cursor.getColumnIndex("id")));
                item.put("ho_ten", cursor.getString(cursor.getColumnIndex("ho_ten")));
                item.put("username", cursor.getString(cursor.getColumnIndex("username")));
                item.put("ngay_sinh", cursor.getString(cursor.getColumnIndex("ngay_sinh")));
                item.put("gioi_tinh", cursor.getString(cursor.getColumnIndex("gioi_tinh")));
                item.put("dia_chi", cursor.getString(cursor.getColumnIndex("dia_chi")));
                item.put("email", cursor.getString(cursor.getColumnIndex("email")));
                item.put("dien_thoai", cursor.getString(cursor.getColumnIndex("dien_thoai")));
                item.put("role", cursor.getString(cursor.getColumnIndex("role")));
                item.put("created_at", cursor.getString(cursor.getColumnIndex("created_at")));
                item.put("updated_at", cursor.getString(cursor.getColumnIndex("updated_at")));

                // Lấy thông tin từ bảng giangvien_detail
                item.put("khoa_id", cursor.getString(cursor.getColumnIndex("khoa_id")));
                item.put("hoc_vi", cursor.getString(cursor.getColumnIndex("hoc_vi")));
                item.put("chuc_vu", cursor.getString(cursor.getColumnIndex("chuc_vu")));

                result.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    // Phương thức chuyển dữ liệu từ Cursor sang đối tượng User
    @SuppressLint("Range")
    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndex("id")));
        user.setHoTen(cursor.getString(cursor.getColumnIndex("ho_ten")));
        user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
        user.setNgaySinh(cursor.getString(cursor.getColumnIndex("ngay_sinh")));
        user.setGioiTinh(cursor.getString(cursor.getColumnIndex("gioi_tinh")));
        user.setDiaChi(cursor.getString(cursor.getColumnIndex("dia_chi")));
        user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
        user.setDienThoai(cursor.getString(cursor.getColumnIndex("dien_thoai")));
        user.setRole(cursor.getString(cursor.getColumnIndex("role")));
        user.setCreatedAt(cursor.getString(cursor.getColumnIndex("created_at")));
        user.setUpdatedAt(cursor.getString(cursor.getColumnIndex("updated_at")));
        return user;
    }

    // Phương thức chuyển dữ liệu từ Cursor sang đối tượng Giangvien_Details
    @SuppressLint("Range")
    private Giangvien_Details cursorToGiangvienDetails(Cursor cursor) {
        Giangvien_Details details = new Giangvien_Details();
        details.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
        details.setKhoaId(cursor.getString(cursor.getColumnIndex("khoa_id")));
        details.setHocVi(cursor.getString(cursor.getColumnIndex("hoc_vi")));
        details.setChucVu(cursor.getString(cursor.getColumnIndex("chuc_vu")));
        return details;
    }

    public boolean updateMultipleRecords(List<Map<String, Object>> recordsList) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        boolean success = true;

        try {
            for (Map<String, Object> record : recordsList) {
                success = updateUser(record);
                if (!success) {
                    break;
                }
            }
            if (success) {
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        } finally {
            db.endTransaction();
            db.close();
        }
        return success;
    }

    public long updateUserImage(int userId, byte[] imageBytes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image", imageBytes);

        return db.update("user", values, "id = ?", new String[]{String.valueOf(userId)});
    }

    private byte[] convertImageToByteArray(String imagePath) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Phương thức upload ảnh
    public void uploadUserImage(int userId, String imagePath) {
        byte[] imageBytes = convertImageToByteArray(imagePath);
        if (imageBytes != null) {
            updateUserImage(userId, imageBytes);
        }
    }

    // Lấy ảnh từ database
    public Bitmap getUserImage(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("user", new String[]{"image"},
                "id = ?", new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
            cursor.close();
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        }
        cursor.close();
        return null;
    }

    public boolean getEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT email FROM user WHERE email = ?";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

}
