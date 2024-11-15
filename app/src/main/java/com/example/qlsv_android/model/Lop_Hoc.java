package com.example.qlsv_android.model;

public class Lop_Hoc {
    private int id;
    private String tenLop;
    private String khoa;

    public Lop_Hoc(int id, String tenLop, String khoa) {
        this.id = id;
        this.tenLop = tenLop;
        this.khoa = khoa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public String getKhoa() {
        return khoa;
    }

    public void setKhoa(String khoa) {
        this.khoa = khoa;
    }
}
