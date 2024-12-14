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

public class ActSinhVienActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;

    SQLiteDatabase db;
    EditText etxtAddKhoa, etxtAdd, etxtDelete, etxtView, etxtList, etxtUpdate, etxtAddClass;
    Button btnAdd, btnDelete, btnView, btnList, btnSearch, btnAddClass, btnDeleteClass, btnUpdateClass;

    Spinner spnUpdateClass;
    ImageButton imageBack;
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_act_sinh_vien);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        etxtAdd = findViewById(R.id.etxtAdd);
        etxtDelete = findViewById(R.id.etxtDelete);
        etxtView = findViewById(R.id.etxtView);
        etxtList = findViewById(R.id.etxtList);
        etxtUpdate = findViewById(R.id.etxtUpdate);
        etxtAddClass = findViewById(R.id.etxtAddClass);
        etxtAddKhoa = findViewById(R.id.etxtAddKhoa);
        spnUpdateClass = findViewById(R.id.spnUpdateClass);

        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnView = findViewById(R.id.btnView);
        btnList = findViewById(R.id.btnList);
        btnSearch = findViewById(R.id.btnSearch);
        btnAddClass = findViewById(R.id.btnAddClass);
        btnDeleteClass = findViewById(R.id.btnDeleteClass);
        btnUpdateClass = findViewById(R.id.btnUpdateClass);

        imageBack = findViewById(R.id.imageBack);

        imageBack.setOnClickListener(v -> finish());

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = etxtAdd.getText().toString().trim();

                if (!value.isEmpty()) {
                    String query = "SELECT u.id, u.ho_ten, u.email, u.dien_thoai, u.dia_chi, " +
                            "s.nganh_hoc, s.khoa_hoc, l.ten_lop, l.khoa " +
                            "FROM user u " +
                            "LEFT JOIN sinhvien_detail s ON u.id = s.user_id " +
                            "LEFT JOIN Lop_Hoc l ON s.lop_id = l.id " +
                            "WHERE u.id = ?" + "AND u.role = 'sinhvien'";

                    Cursor cursor = db.rawQuery(query, new String[]{value});

                    if (cursor.moveToFirst()) {
                        @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("id"));
                        @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("ho_ten"));
                        @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
                        @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex("dien_thoai"));
                        @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("dia_chi"));
                        @SuppressLint("Range") String nganhHoc = cursor.getString(cursor.getColumnIndex("nganh_hoc"));
                        @SuppressLint("Range") String khoaHoc = cursor.getString(cursor.getColumnIndex("khoa_hoc"));
                        @SuppressLint("Range") String lopId = cursor.getString(cursor.getColumnIndex("ten_lop"));
                        @SuppressLint("Range") String khoa = cursor.getString(cursor.getColumnIndex("khoa"));
                        // Hiển thị Custom Dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActSinhVienActivity.this);
                        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_student, null);
                        builder.setView(dialogView);

                        // Ánh xạ các thành phần trong Dialog
                        EditText etDialogId = dialogView.findViewById(R.id.etDialogId);
                        EditText etDialogName = dialogView.findViewById(R.id.etDialogName);
                        EditText etDialogEmail = dialogView.findViewById(R.id.etDialogEmail);
                        EditText etDialogPhone = dialogView.findViewById(R.id.etDialogPhone);
                        EditText etDialogAddress = dialogView.findViewById(R.id.etDialogAddress);
                        EditText etDialogNganhHoc = dialogView.findViewById(R.id.etDialogNganhHoc);
                        EditText etDialogKhoaHoc = dialogView.findViewById(R.id.etDialogKhoaHoc);
                        EditText etDialogClassId = dialogView.findViewById(R.id.etDialogClassId);
                        EditText etDialogKhoa = dialogView.findViewById(R.id.etDialogKhoa);

                        Button btnUpdateDialog = dialogView.findViewById(R.id.btnUpdateDialog);

                        // Gán dữ liệu vào EditText
                        etDialogId.setText(id);
                        etDialogName.setText(name);
                        etDialogEmail.setText(email);
                        etDialogPhone.setText(phone);
                        etDialogAddress.setText(address);
                        etDialogNganhHoc.setText(nganhHoc);
                        etDialogKhoaHoc.setText(khoaHoc);
                        etDialogClassId.setText(lopId);
                        etDialogKhoa.setText(khoa);

                        // Tạo và hiển thị Dialog
                        AlertDialog dialog = builder.create();
                        dialog.show();

                        // Xử lý sự kiện nút Update
                        btnUpdateDialog.setOnClickListener(v1 -> {
                            String newName = etDialogName.getText().toString().trim();
                            String newEmail = etDialogEmail.getText().toString().trim();
                            String newPhone = etDialogPhone.getText().toString().trim();
                            String newAddress = etDialogAddress.getText().toString().trim();
                            String newNganhHoc = etDialogNganhHoc.getText().toString().trim();
                            String newKhoaHoc = etDialogKhoaHoc.getText().toString().trim();
                            String newClassId = etDialogClassId.getText().toString().trim();
                            String newKhoa = etDialogKhoa.getText().toString().trim();

                            try {
                                db.beginTransaction();
                                ContentValues userValues = new ContentValues();
                                userValues.put("ho_ten", newName);
                                userValues.put("email", newEmail);
                                userValues.put("dien_thoai", newPhone);
                                userValues.put("dia_chi", newAddress);

                                int userRows = db.update("user", userValues, "id = ?", new String[]{id});
                                Log.d("UPDATE", "Detail rows updated: " + userRows);

                                ContentValues detailValues = new ContentValues();
                                detailValues.put("nganh_hoc", newNganhHoc);
                                detailValues.put("khoa_hoc", newKhoaHoc);

                                int detailRows = db.update("sinhvien_detail", detailValues, "user_id = ?", new String[]{id});
                                Log.d("UPDATE", "Detail rows updated: " + detailRows);

                                ContentValues classValues = new ContentValues();
                                classValues.put("id", newClassId);
                                classValues.put("khoa", newKhoa);

                                if (userRows > 0 && detailRows > 0) {
                                    db.setTransactionSuccessful();
                                    Toast.makeText(ActSinhVienActivity.this, "Student updated successfully!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(ActSinhVienActivity.this, "Update failed!", Toast.LENGTH_SHORT).show();
                                    Log.e("UPDATE", "Update failed - userRows: " + userRows +
                                            ", detailRows: " + detailRows);
                                }
                            } catch (Exception e) {
                                Log.e("UPDATE_ERROR", "Error during update: " + e.getMessage(), e);
                                Toast.makeText(ActSinhVienActivity.this, "An error occurred while updating. Please try again.", Toast.LENGTH_SHORT).show();
                            } finally {
                                if (db.inTransaction()) {
                                    db.endTransaction();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(ActSinhVienActivity.this, "No student found with ID: " + value, Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                } else {
                    Toast.makeText(ActSinhVienActivity.this, "Please enter an ID!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = "SELECT u.ho_ten, u.id AS user_id, l.ten_lop " +
                        "FROM user u " +
                        "LEFT JOIN sinhvien_detail s ON u.id = s.user_id " +
                        "LEFT JOIN Lop_Hoc l ON s.lop_id = l.id " +
                        "WHERE u.role = 'sinhvien' AND l.ten_lop = ?";

                String value = etxtList.getText().toString().trim();

                Cursor cursor = db.rawQuery(query, new String[]{value});

                if (cursor != null && cursor.moveToFirst()) {
                    List<String> userList = new ArrayList<>();

                    do {
                        @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("ho_ten"));
                        @SuppressLint("Range") String userId = cursor.getString(cursor.getColumnIndex("user_id"));
                        @SuppressLint("Range") String className = cursor.getString(cursor.getColumnIndex("ten_lop"));

                        userList.add("Name: " + name + ",ID: " + userId + ", Class: " + className);
                    } while (cursor.moveToNext());

                    cursor.close();

                    AlertDialog.Builder builder = new AlertDialog.Builder(ActSinhVienActivity.this);
                    builder.setTitle("Student List");

                    ListView listView = new ListView(ActSinhVienActivity.this);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ActSinhVienActivity.this, android.R.layout.simple_list_item_1, userList);
                    listView.setAdapter(adapter);

                    builder.setView(listView);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    Toast.makeText(ActSinhVienActivity.this, "No students found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = etxtUpdate.getText().toString().trim();

                if (!value.isEmpty()) {
                    String query = "SELECT u.id, u.ho_ten, u.email, u.dien_thoai, u.dia_chi, " +
                            "s.nganh_hoc, s.khoa_hoc, l.ten_lop, l.khoa " +
                            "FROM user u " +
                            "LEFT JOIN sinhvien_detail s ON u.id = s.user_id " +
                            "LEFT JOIN Lop_Hoc l ON s.lop_id = l.id " +
                            "WHERE u.ho_ten LIKE ? AND u.role = 'sinhvien'";

                    Cursor cursor = db.rawQuery(query, new String[]{"%" + value + "%"});

                    if (cursor != null && cursor.moveToFirst()) {
                        List<String> studentNames = new ArrayList<>();
                        final List<String> studentDetails = new ArrayList<>();

                        do {
                            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("ho_ten"));
                            @SuppressLint("Range") String userId = cursor.getString(cursor.getColumnIndex("id"));
                            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
                            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex("dien_thoai"));
                            @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("dia_chi"));
                            @SuppressLint("Range") String major = cursor.getString(cursor.getColumnIndex("nganh_hoc"));
                            @SuppressLint("Range") String course = cursor.getString(cursor.getColumnIndex("khoa_hoc"));
                            @SuppressLint("Range") String className = cursor.getString(cursor.getColumnIndex("ten_lop"));
                            @SuppressLint("Range") String department = cursor.getString(cursor.getColumnIndex("khoa"));

                            String studentInfo = "ID: " + userId + "\n" +
                                    "Họ và tên: " + name + "\n" +
                                    "Email: " + email + "\n" +
                                    "Điện thoại: " + phone + "\n" +
                                    "Địa chỉ: " + address + "\n" +
                                    "Ngành học: " + major + "\n" +
                                    "Khóa học: " + course + "\n" +
                                    "Lớp: " + className + "\n" +
                                    "Khoa: " + department;

                            studentNames.add(name);
                            studentDetails.add(studentInfo);
                        } while (cursor.moveToNext());

                        cursor.close();

                        AlertDialog.Builder builder = new AlertDialog.Builder(ActSinhVienActivity.this);
                        builder.setTitle("Search Results");

                        ListView listView = new ListView(ActSinhVienActivity.this);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ActSinhVienActivity.this, android.R.layout.simple_list_item_1, studentNames);
                        listView.setAdapter(adapter);

                        builder.setView(listView);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String studentInfo = studentDetails.get(position);

                                AlertDialog.Builder detailBuilder = new AlertDialog.Builder(ActSinhVienActivity.this);
                                detailBuilder.setTitle("Student Details");
                                detailBuilder.setMessage(studentInfo);
                                detailBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                detailBuilder.create().show();
                            }
                        });

                        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builder.create().show();

                    } else {
                        Toast.makeText(ActSinhVienActivity.this, "No students found!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActSinhVienActivity.this, "Please enter a name to search!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateClassSpinner();

        btnUpdateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String choseClass = "";
                if (spnUpdateClass.getAdapter() != null && spnUpdateClass.getAdapter().getCount() > 0) {
                    choseClass  = spnUpdateClass.getSelectedItem().toString();;
                    Log.d("btnUpdateClass", "choseClass" + choseClass);

                } else {
                    Log.d("FirstClass", "Spinner is empty.");
                }
                String query = "SELECT u.ho_ten, u.id AS user_id, l.ten_lop " +
                        "FROM user u " +
                        "LEFT JOIN sinhvien_detail s ON u.id = s.user_id " +
                        "LEFT JOIN Lop_Hoc l ON s.lop_id = l.id " +
                        "WHERE u.role = 'sinhvien' AND l.ten_lop = ?";

                Cursor cursor = db.rawQuery(query, new String[]{choseClass});
                if(cursor.moveToFirst()) {
                        List<String> userList = new ArrayList<>();
                        do {
                            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("ho_ten"));
                            @SuppressLint("Range") String userId = cursor.getString(cursor.getColumnIndex("user_id"));
                            @SuppressLint("Range") String className = cursor.getString(cursor.getColumnIndex("ten_lop"));

                            userList.add("Name: " + name + ",ID: " + userId + ", Class: " + className);
                    } while (cursor.moveToNext());
                    cursor.close();


                    AlertDialog.Builder builder = new AlertDialog.Builder(ActSinhVienActivity.this);
                    builder.setTitle("Student List");

                    ListView listView = new ListView(ActSinhVienActivity.this);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ActSinhVienActivity.this, android.R.layout.simple_list_item_1, userList);
                    listView.setAdapter(adapter);

                    builder.setView(listView);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });

        btnAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenLop = etxtAddClass.getText().toString().trim();
                String khoa = etxtAddKhoa.getText().toString().trim();

                // Kiểm tra dữ liệu đầu vào
                if (tenLop.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập tên lớp", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (khoa.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập khóa học", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra xem lớp đã tồn tại trong cơ sở dữ liệu chưa
                String queryCheck = "SELECT COUNT(*) FROM Lop_Hoc WHERE ten_lop = ? AND khoa = ?";
                Cursor cursor = null;
                try {
                    cursor = db.rawQuery(queryCheck, new String[]{tenLop, khoa});
                    if (cursor.moveToFirst()) {
                        int count = cursor.getInt(0);
                        if (count > 0) {
                            Toast.makeText(getApplicationContext(), "Lớp đã tồn tại", Toast.LENGTH_SHORT).show();
                            return; // Dừng lại nếu lớp đã tồn tại
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Lỗi khi kiểm tra lớp: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                // Nếu lớp chưa tồn tại, thực hiện thêm vào cơ sở dữ liệu
                ContentValues values = new ContentValues();
                values.put("ten_lop", tenLop);
                values.put("khoa", khoa);

                try {
                    long result = db.insert("Lop_Hoc", null, values);
                    if (result != -1) {
                        Toast.makeText(getApplicationContext(), "Thêm lớp thành công", Toast.LENGTH_SHORT).show();
                        etxtAddClass.setText("");
                        etxtAddKhoa.setText("");

                        updateClassSpinner();
                    } else {
                        Toast.makeText(getApplicationContext(), "Lỗi khi thêm lớp", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Lỗi khi thêm lớp: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDeleteClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedClass = spnUpdateClass.getSelectedItem().toString();

                if (selectedClass.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng chọn lớp để xóa", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Confirm the deletion
                AlertDialog.Builder builder = new AlertDialog.Builder(ActSinhVienActivity.this);
                builder.setTitle("Xóa lớp");
                builder.setMessage("Bạn có chắc chắn muốn xóa lớp: " + selectedClass + "?");

                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform the delete operation using SQLiteDatabase.delete()
                        String query = "ten_lop = ?";
                        String[] whereArgs = { selectedClass };
                        int rowsAffected = db.delete("Lop_Hoc", query, whereArgs);

                        if (rowsAffected > 0) {
                            Toast.makeText(getApplicationContext(), "Lớp đã được xóa thành công", Toast.LENGTH_SHORT).show();
                            updateClassSpinner(); // Refresh the Spinner after deletion
                        } else {
                            Toast.makeText(getApplicationContext(), "Không thể xóa lớp", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void updateClassSpinner() {
        String query = "SELECT ten_lop, khoa FROM Lop_Hoc";
        List<String> classList = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                @SuppressLint("Range") String tenLop = cursor.getString(cursor.getColumnIndex("ten_lop"));
                classList.add(tenLop);
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                ActSinhVienActivity.this,
                android.R.layout.simple_list_item_1,
                classList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnUpdateClass.setAdapter(adapter);
    }
}