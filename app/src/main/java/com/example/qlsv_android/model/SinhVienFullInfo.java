package com.example.qlsv_android.model;

import java.io.Serializable;

public class SinhVienFullInfo implements Serializable {
    User user ;
    SinhVien_Details sinhVienDetails;

    public SinhVienFullInfo(User user, SinhVien_Details sinhVienDetails) {
        this.user = user;
        this.sinhVienDetails = sinhVienDetails;
    }

    public SinhVienFullInfo() {
    }

    public Serializable getSinhVienDetails() {
        return (Serializable) sinhVienDetails;
    }

    public void setSinhVienDetails(SinhVien_Details sinhVienDetails) {
        this.sinhVienDetails = sinhVienDetails;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
