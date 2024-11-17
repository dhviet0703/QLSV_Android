package com.example.qlsv_android.model;

import java.io.Serializable;

public class SinhVien_Details implements Serializable {
    private int userId;
    private int lopId;
    private String nganhHoc;
    private String khoaHoc;

    public SinhVien_Details(int userId, int lopId, String nganhHoc, String khoaHoc) {
        this.userId = userId;
        this.lopId = lopId;
        this.nganhHoc = nganhHoc;
        this.khoaHoc = khoaHoc;
    }

    public SinhVien_Details() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLopId() {
        return lopId;
    }

    public void setLopId(int lopId) {
        this.lopId = lopId;
    }

    public String getNganhHoc() {
        return nganhHoc;
    }

    public void setNganhHoc(String nganhHoc) {
        this.nganhHoc = nganhHoc;
    }

    public String getKhoaHoc() {
        return khoaHoc;
    }

    public void setKhoaHoc(String khoaHoc) {
        this.khoaHoc = khoaHoc;
    }
}
