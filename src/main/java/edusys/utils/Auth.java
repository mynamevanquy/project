/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edusys.utils;

import edusys.entity.NhanVien;

/**
 *
 * @author lam
 */
public class Auth {
    /*
    đối tượng này chứa thông tin người sử dụng sau khi đăng nhập
    */
    public static NhanVien user = null;
    /*
    Xoá thông tin của người dùng khi có yêu cầu đăng xuất
    */
    public static void clear(){
        Auth.user = null;
    }
    /*
    Kiểm tra xem đăng nhập hay chưa
    @return đăng nhập hay chưa
    */
    public static boolean isLogin(){
        return Auth.user != null;
    }
    public static boolean isManager(){
        return Auth.isLogin() && user.isVaiTro();
    }
}
