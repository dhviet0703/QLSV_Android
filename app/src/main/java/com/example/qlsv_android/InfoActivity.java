package com.example.qlsv_android;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

import com.example.qlsv_android.adapter.CustomAdapter;
import com.example.qlsv_android.helper.DatabaseHelper;
import com.example.qlsv_android.model.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PICKER = 1001;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    TextView txtName;
    ListView listView;
    Button btnUpdate;
    ImageButton btnBack ;
    private boolean isEditing = false;
    private CustomAdapter adapter;

    ImageView imageView;

    @SuppressLint({"MissingInflatedId", "IntentReset"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();
        txtName = findViewById(R.id.name);
        btnUpdate = findViewById(R.id.btnSearch);
        listView = findViewById(R.id.listView);
        btnBack = findViewById(R.id.imageBtnBack);
        imageView = findViewById(R.id.imgUser);
        User user = (User) getIntent().getSerializableExtra("user");

        if (user != null) {
            List<String> dataList = loadUserData(user);
            adapter = new CustomAdapter(this, dataList);
            listView.setAdapter(adapter);
            txtName.setText(user.getHoTen());
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnUpdate.setOnClickListener(v -> toggleEditing(user));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bitmap userImage = getImageFromDatabase(user.getId());
        if (userImage != null) {
            imageView.setImageBitmap(userImage);
        } else {
            imageView.setImageResource(R.drawable.student); // Ảnh mặc định
        }

        imageView.setOnClickListener(v -> {
            if (userHasImage(user.getId())) {
                new AlertDialog.Builder(this)
                        .setTitle("Thay đổi ảnh")
                        .setMessage("Bạn có muốn thay đổi ảnh không?")
                        .setPositiveButton("Đồng ý", (dialog, which) -> openImagePicker())
                        .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                openImagePicker();
            }
        });

    }
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICKER);
    }

    private boolean userHasImage(int userId) {
        Cursor cursor = db.query("user", new String[]{"image"}, "id = ?", new String[]{String.valueOf(userId)}, null, null, null);
        boolean hasImage = false;
        if (cursor.moveToFirst()) {
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
            hasImage = imageBytes != null && imageBytes.length > 0;
        }
        cursor.close();
        return hasImage;
    }


    private List<String> loadUserData(User user) {
        List<String> dataList = new ArrayList<>();
        String roleQuery = "SELECT role FROM user WHERE id = ?";
        Cursor roleCursor = db.rawQuery(roleQuery, new String[]{String.valueOf(user.getId())});

        String userRole = "";
        if (roleCursor.moveToFirst()) {
            userRole = roleCursor.getString(roleCursor.getColumnIndexOrThrow("role"));
        }
        roleCursor.close();

        if ("giangvien".equalsIgnoreCase(userRole)) {
            String query = "SELECT user.*, " +
                    "giangvien_detail.khoa_id, " +
                    "giangvien_detail.hoc_vi, " +
                    "giangvien_detail.chuc_vu " +
                    "FROM user " +
                    "LEFT JOIN giangvien_detail ON user.id = giangvien_detail.user_id " +
                    "WHERE user.id = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(user.getId())});

            if (cursor.moveToFirst()) {
                dataList.add("Họ tên: " + cursor.getString(cursor.getColumnIndexOrThrow("ho_ten")));
                dataList.add("Username: " + cursor.getString(cursor.getColumnIndexOrThrow("username")));
                dataList.add("Ngày sinh: " + cursor.getString(cursor.getColumnIndexOrThrow("ngay_sinh")));
                dataList.add("Giới tính: " + cursor.getString(cursor.getColumnIndexOrThrow("gioi_tinh")));
                dataList.add("Địa chỉ: " + cursor.getString(cursor.getColumnIndexOrThrow("dia_chi")));
                dataList.add("Email: " + cursor.getString(cursor.getColumnIndexOrThrow("email")));
                dataList.add("Điện thoại: " + cursor.getString(cursor.getColumnIndexOrThrow("dien_thoai")));
                dataList.add("Vai trò: " + userRole);
                dataList.add("Khoa: " + cursor.getString(cursor.getColumnIndexOrThrow("khoa_id")));
                dataList.add("Học vị: " + cursor.getString(cursor.getColumnIndexOrThrow("hoc_vi")));
                dataList.add("Chức vụ: " + cursor.getString(cursor.getColumnIndexOrThrow("chuc_vu")));
            }
            cursor.close();
        } else if ("sinhvien".equalsIgnoreCase(userRole)) {
            String query = "SELECT user.*, " +
                    "sinhvien_detail.lop_id, " +
                    "sinhvien_detail.nganh_hoc, " +
                    "sinhvien_detail.khoa_hoc " +
                    "FROM user " +
                    "LEFT JOIN sinhvien_detail ON user.id = sinhvien_detail.user_id " +
                    "WHERE user.id = ?";

            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(user.getId())});
            if (cursor.moveToFirst()) {
                dataList.add("Họ tên: " + cursor.getString(cursor.getColumnIndexOrThrow("ho_ten")));
                dataList.add("Username: " + cursor.getString(cursor.getColumnIndexOrThrow("username")));
                dataList.add("Ngày sinh: " + cursor.getString(cursor.getColumnIndexOrThrow("ngay_sinh")));
                dataList.add("Giới tính: " + cursor.getString(cursor.getColumnIndexOrThrow("gioi_tinh")));
                dataList.add("Địa chỉ: " + cursor.getString(cursor.getColumnIndexOrThrow("dia_chi")));
                dataList.add("Email: " + cursor.getString(cursor.getColumnIndexOrThrow("email")));
                dataList.add("Điện thoại: " + cursor.getString(cursor.getColumnIndexOrThrow("dien_thoai")));
                dataList.add("Vai trò: " + userRole);
                dataList.add("Lớp: " + cursor.getString(cursor.getColumnIndexOrThrow("lop_id")));
                dataList.add("Ngành học: " + cursor.getString(cursor.getColumnIndexOrThrow("nganh_hoc")));
                dataList.add("Khóa học: " + cursor.getString(cursor.getColumnIndexOrThrow("khoa_hoc")));

            }
            cursor.close();
        }
        return dataList;
    }

    private void toggleEditing(User user) {
        isEditing = !isEditing;
        btnUpdate.setText(isEditing ? "Lưu" : "Cập Nhật");
        adapter.setEditable(isEditing);
        if (!isEditing) {
            loadUserData(user).clear();
            List<String> updatedData = adapter.getUpdatedData();
            saveDataToDatabase(updatedData, user);
            Toast.makeText(this, "Changes saved!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDataToDatabase(List<String> updatedData, User user) {
        String roleQuery = "SELECT role FROM user WHERE id = ?";
        Cursor roleCursor = db.rawQuery(roleQuery, new String[]{String.valueOf(user.getId())});

        String userRole = "";
        if (roleCursor.moveToFirst()) {
            userRole = roleCursor.getString(roleCursor.getColumnIndexOrThrow("role"));
        }
        roleCursor.close();

        for (String data : updatedData) {
            String[] parts = data.split(": ", 2);
            if (parts.length == 2) {
                String fieldName = parts[0].trim();
                String value = parts[1].trim();

                String[] tableAndColumn = mapFieldToTableAndColumn(fieldName, userRole);
                if (tableAndColumn != null) {
                    String tableName = tableAndColumn[0];
                    String columnName = tableAndColumn[1];

                    ContentValues values = new ContentValues();
                    values.put(columnName, value);

                    if (tableName.equals("user")) {
                        String selection = "id = ?";
                        String[] selectionArgs = {String.valueOf(user.getId())};
                        db.update("user", values, selection, selectionArgs);
                    } else if (tableName.equals("giangvien_detail") && "giangvien".equalsIgnoreCase(userRole)) {
                        String selection = "user_id = ?";
                        String[] selectionArgs = {String.valueOf(user.getId())};
                        int count = db.update("giangvien_detail", values, selection, selectionArgs);

                        if (count == 0) {
                            values.put("user_id", user.getId());
                            db.insert("giangvien_detail", null, values);
                        }
                    } else if (tableName.equals("sinhvien_detail") && "sinhvien".equalsIgnoreCase(userRole)) {
                        String selection = "user_id = ?";
                        String[] selectionArgs = {String.valueOf(user.getId())};
                        int count = db.update("sinhvien_detail", values, selection, selectionArgs);

                        if (count == 0) {
                            values.put("user_id", user.getId());
                            db.insert("sinhvien_detail", null, values);
                        }
                    }
                }
            }
        }
    }
    private String[] mapFieldToTableAndColumn(String fieldName, String role) {
        if ("giảng viên".equalsIgnoreCase(role)) {
            switch (fieldName) {
                case "Khoa":
                    return new String[]{"giangvien_detail", "khoa_id"};
                case "Học vị":
                    return new String[]{"giangvien_detail", "hoc_vi"};
                case "Chức vụ":
                    return new String[]{"giangvien_detail", "chuc_vu"};
                default:
                    break;
            }
        } else if ("sinh viên".equalsIgnoreCase(role)) {
            switch (fieldName) {
                case "Lớp":
                    return new String[]{"sinhvien_detail", "lop_id"};
                case "Ngành học":
                    return new String[]{"sinhvien_detail", "nganh_hoc"};
                case "Khóa học":
                    return new String[]{"sinhvien_detail", "khoa_hoc"};
                default:
                    break;
            }
        }

        switch (fieldName) {
            case "Họ tên":
                return new String[]{"user", "ho_ten"};
            case "Username":
                return new String[]{"user", "username"};
            case "Ngày sinh":
                return new String[]{"user", "ngay_sinh"};
            case "Giới tính":
                return new String[]{"user", "gioi_tinh"};
            case "Địa chỉ":
                return new String[]{"user", "dia_chi"};
            case "Email":
                return new String[]{"user", "email"};
            case "Điện thoại":
                return new String[]{"user", "dien_thoai"};
            case "Vai trò":
                return new String[]{"user", "role"};
            default:
                return null;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);

                User user = (User) getIntent().getSerializableExtra("user");
                if (user != null) {
                    saveImageToDatabase(user.getId(), bitmap);
                }

                Toast.makeText(this, "Ảnh đã được cập nhật!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi khi chọn ảnh!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveImageToDatabase(int id, Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        ContentValues values = new ContentValues();
        values.put("image", imageBytes);

        String userId = getUserId();
        if (userId != null) {
            String selection = "id = ?";
            String[] selectionArgs = {userId};
            db.update("user", values, selection, selectionArgs);
        }
    }

    private Bitmap getImageFromDatabase(int userId) {
        String query = "SELECT image FROM user WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
            cursor.close();
            if (imageBytes != null) {
                return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            }
        }
        cursor.close();
        return null;
    }

    private String getUserId() {
        User user = (User) getIntent().getSerializableExtra("user");
        if (user != null) {
            return String.valueOf(user.getId());
        }
        return null;
    }



}
