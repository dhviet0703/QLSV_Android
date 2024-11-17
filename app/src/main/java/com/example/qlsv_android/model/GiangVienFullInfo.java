package com.example.qlsv_android.model;

import java.io.Serializable;

public class GiangVienFullInfo implements Serializable {
    private User user;
    private Giangvien_Details giangVienDetails;

    public GiangVienFullInfo(User user, Giangvien_Details giangVienDetails) {
        this.user = user;
        this.giangVienDetails = giangVienDetails;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Serializable getGiangVienDetails() {
        return (Serializable) giangVienDetails;
    }

    public void setGiangVienDetails(Giangvien_Details giangVienDetails) {
        this.giangVienDetails = giangVienDetails;
    }

    /**
     * Provides a detailed string representation of the GiangVienFullInfo object.
     *
     * @return A string containing user and Giangvien_Details information.
     */
    @Override
    public String toString() {
        return "GiangVienFullInfo{" +
                "User=" + (user != null ? user.toString() : "null") +
                ", GiangVienDetails=" + (giangVienDetails != null ? giangVienDetails.toString() : "null") +
                '}';
    }

    /**
     * Checks if the user and giangVienDetails are properly initialized.
     *
     * @return True if both fields are non-null, false otherwise.
     */
    public boolean isComplete() {
        return user != null && giangVienDetails != null;
    }

    /**
     * Combines relevant user and giangVienDetails information into a display-friendly format.
     *
     * @return A formatted string for displaying in UI components.
     */
    public String getDisplayInfo() {
        String userInfo = (user != null) ?
                "Họ tên: " + user.getHoTen() + "\nEmail: " + user.getEmail() + "\nĐiện thoại: " + user.getDienThoai() :
                "Thông tin người dùng không khả dụng.";

        String detailsInfo = (giangVienDetails != null) ?
                "Khoa: " + giangVienDetails.getKhoaId() + "\nHọc vị: " + giangVienDetails.getHocVi() + "\nChức vụ: " + giangVienDetails.getChucVu() :
                "Thông tin giảng viên không khả dụng.";

        return userInfo + "\n" + detailsInfo;
    }
}
