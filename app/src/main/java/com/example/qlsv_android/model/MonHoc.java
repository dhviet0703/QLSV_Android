package com.example.qlsv_android.model;

public class MonHoc {
    private int id;
    private String tenMon;
    private int tinChi;
    private int ky;
    private String moTa;

    public MonHoc(int id, String tenMon, int tinChi, int ky, String moTa) {
        this.id = id;
        this.tenMon = tenMon;
        this.tinChi = tinChi;
        this.ky = ky;
        this.moTa = moTa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public int getTinChi() {
        return tinChi;
    }

    public void setTinChi(int tinChi) {
        this.tinChi = tinChi;
    }

    public int getKy() {
        return ky;
    }

    public void setKy(int ky) {
        this.ky = ky;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
}
