package com.example.qlsv_android.model;

public class LichHoc {
    private int id;
    private int lopId;
    private int monHocId;
    private int giangVienId;
    private String ngayHoc;
    private String gioBatDau;
    private String gioKetThuc;

    public LichHoc(int id, int lopId, int monHocId, int giangVienId, String ngayHoc, String gioBatDau, String gioKetThuc) {
        this.id = id;
        this.lopId = lopId;
        this.monHocId = monHocId;
        this.giangVienId = giangVienId;
        this.ngayHoc = ngayHoc;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLopId() {
        return lopId;
    }

    public void setLopId(int lopId) {
        this.lopId = lopId;
    }

    public int getMonHocId() {
        return monHocId;
    }

    public void setMonHocId(int monHocId) {
        this.monHocId = monHocId;
    }

    public int getGiangVienId() {
        return giangVienId;
    }

    public void setGiangVienId(int giangVienId) {
        this.giangVienId = giangVienId;
    }

    public String getNgayHoc() {
        return ngayHoc;
    }

    public void setNgayHoc(String ngayHoc) {
        this.ngayHoc = ngayHoc;
    }

    public String getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(String gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public String getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(String gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }
}
