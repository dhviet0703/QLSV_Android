package com.example.qlsv_android.model;

public class Giangvien_Details {
    private int userId;
    private String khoaId;
    private String hocVi;
    private String chucVu;

    public Giangvien_Details(int userId, String khoaId, String hocVi, String chucVu) {
        this.userId = userId;
        this.khoaId = khoaId;
        this.hocVi = hocVi;
        this.chucVu = chucVu;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getKhoaId() {
        return khoaId;
    }

    public void setKhoaId(String khoaId) {
        this.khoaId = khoaId;
    }

    public String getHocVi() {
        return hocVi;
    }

    public void setHocVi(String hocVi) {
        this.hocVi = hocVi;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }
}
