/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edusys.dao;

import edusys.entity.HocVien;
import edusys.utils.jdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lam
 */
public class HocVienDAO extends EdusysDAO<HocVien, String> {

    final String INSERT_SQL = "INSERT INTO HocVien ( MaKH , MaNH , Diem ) VALUES ( ? , ? , ? )";
    final String UPDATE_SQL = "UPDATE HocVien SET  MaKH  = ? ,  MaNH  = ? , Diem  = ?  WHERE MaHV = ?";
    final String DELETE_SQL = "DELETE FROM HocVien WHERE MaHV = ? ";
    final String SELECT_ALL_SQL = "SELECT * FROM HocVien";
    final String SELECT_BY_ID_SQL = "SELECT * FROM HocVien WHERE MaHV = ? ";
    final String SELECT_BY_KHOA_HOC_SQL = "SELECT * FROM HocVien WHERE MaKH = ? ";

    @Override
    public void insert(HocVien entity) {
        jdbcHelper.update(INSERT_SQL, entity.getMaKH(), entity.getMaNH(), entity.getDiem());
    }

    @Override
    public void update(HocVien entity) {
        jdbcHelper.update(UPDATE_SQL, entity.getMaKH(), entity.getMaNH(), entity.getDiem(), entity.getMaHV());
    }

    @Override
    public void delete(String id) {
        jdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public List<HocVien> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public HocVien selectById(String id) {
        List<HocVien> list = selectBySql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<HocVien> selectBySql(String sql, Object... args) {

        List<HocVien> list = new ArrayList<>();
        try {
            ResultSet rs = jdbcHelper.query(sql, args);
            while (rs.next()) {
                HocVien entity = new HocVien();
                entity.setMaHV(rs.getInt("MaHV"));
                entity.setMaKH(rs.getInt("MaKH"));
                entity.setMaNH(rs.getString("MaNH"));
                entity.setDiem(rs.getFloat("Diem"));
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<HocVien> selectByKhoaHoc(int MaKH) {
        return selectBySql(SELECT_BY_KHOA_HOC_SQL, MaKH);
    }
}
