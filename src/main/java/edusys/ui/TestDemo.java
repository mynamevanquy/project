/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edusys.ui;

import edusys.dao.NhanVienDAO;
import edusys.dao.ThongKeDAO;
import edusys.entity.NhanVien;
import java.util.List;

/**
 *
 * @author lam
 */
public class TestDemo {
    public static void main(String[] args) {
        ThongKeDAO tkDAO = new ThongKeDAO();
        List<Object[]> list = tkDAO.getDoanhThu(2020);
        for (Object[] obj : list) {
            System.out.println("==>"+obj[0]);
        }
//        test sp _bandiem
//        List<Object[]> list = tkDAO.getBangDiem(3);
//        for (Object[] obj : list) {
//            System.out.println("==>"+obj[0]+"-"+obj[1]+"-"+obj[2]);
//        }
//        NhanVienDAO dao = new NhanVienDAO();
//        dao.delete("Thaopn");
//        dao.insert(new NhanVien("Thaopn","12345",  "Phạm Ngọc Thảo" , true));
////        List<NhanVien> ls = dao.selectAll();
//        for (NhanVien nv : ls) {
//            System.out.println("==>"+nv.toString());
//        }
    }
}
