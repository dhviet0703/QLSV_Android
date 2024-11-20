//package com.example.qlsv_android.helper;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.view.MenuItem;
//
//import androidx.appcompat.app.AlertDialog;
//
//import com.example.qlsv_android.InfoActivity;
//import com.example.qlsv_android.R;
//import com.example.qlsv_android.ResetPasswordActivity;
//import com.example.qlsv_android.TeacherActivity;
//
//public class NavigationHelper {
//    public static boolean handleNavigation(Context context, MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.nav_info) {
//            // Lấy userName từ context (nếu context là TeacherActivity)
//            String userName = null;
//            if (context instanceof TeacherActivity) {
//                userName = ((TeacherActivity) context).getUserName(); // Lấy userName từ TeacherActivity
//            }
//
//            Intent intentInfo = new Intent(context, InfoActivity.class);
//            // Truyền userName sang InfoActivity
//            if (userName != null) {
//                intentInfo.putExtra("userName", userName);
//            }
//            context.startActivity(intentInfo);
//        } else if (id == R.id.nav_password) {
//            Intent intentPassword = new Intent(context, ResetPasswordActivity.class);
//            context.startActivity(intentPassword);
//        } else if (id == R.id.nav_logout) {
//            new AlertDialog.Builder(context)
//                    .setTitle("Xác nhận đăng xuất")
//                    .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
//                    .setPositiveButton("Có", (dialog, which) -> {
//                        if (context instanceof Activity) {
//                            ((Activity) context).finish(); // Đóng Activity hiện tại
//                        }
//                    })
//                    .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
//                    .create()
//                    .show();
//        } else {
//            return false;
//        }
//        return true;
//    }
//}
