/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edusys.dao;

import edusys.entity.KhoaHoc;
import edusys.utils.jdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lam
 */
public class KhoaHocDAO extends  EdusysDAO<KhoaHoc, String>{
    final String INSERT_SQL = "INSERT INTO KhoaHoc ( MaCD , HocPhi , ThoiLuong , NgayKG , GhiChu , MaNV , NgayTao ) VALUES ( ? , ? , ? , ? , ? , ? ,?)";
    final String UPDATE_SQL = "UPDATE KhoaHoc SET  MaCD  = ? , HocPhi  = ? , ThoiLuong  = ? , NgayKG  = ? , GhiChu  = ? , MaNV  = ? , NgayTao  = ? WHERE MaKH = ? ";
    final String DELETE_SQL = "DELETE FROM [dbo].[KhoaHoc] WHERE MaKH = 2";
    final String SELECT_ALL_SQL = "SELECT * FROM KhoaHoc";
    final String SELECT_BY_ID_SQL = "SELECT * FROM KhoaHoc WHERE MaKH = ? ";
    final String SELECT_BY_MA_CD_SQL = "SELECT * FROM KhoaHoc WHERE MaCD = ? ";
     final String SELECT_MAKH_BY_TENCD = "select a.MaKH from  KhoaHoc a  join  ChuyenDe b on a.MaCD=b.MaCD where TenCD = ? ";
    @Override
    public void insert(KhoaHoc entity) {
        jdbcHelper.update(INSERT_SQL, entity.getMaCD(), entity.getHocPhi(), entity.getThoiLuong(), entity.getNgayKG(),
                entity.getGhiChu(), entity.getMaNV(), entity.getNgayTao());
    }

    @Override
    public void update(KhoaHoc entity) {
          jdbcHelper.update(UPDATE_SQL, entity.getMaCD(), entity.getHocPhi(), entity.getThoiLuong(), entity.getNgayKG(),
                entity.getGhiChu(), entity.getMaNV(), entity.getNgayTao(), entity.getMaKH());
    }

    @Override
    public void delete(String id) {
        jdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public List<KhoaHoc> selectAll() {
         return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public KhoaHoc selectById(String id) {
         List<KhoaHoc> list = selectBySql(SELECT_BY_ID_SQL, id);
       if(list.isEmpty()){
           return null;
       }
       return list.get(0);
    }

    @Override
    public List<KhoaHoc> selectBySql(String sql, Object... args) {
        List<KhoaHoc> list = new ArrayList<>();
        try {
            ResultSet rs = jdbcHelper.query(sql, args);
            while(rs.next()){
                KhoaHoc entity = new KhoaHoc();
                entity.setMaKH(rs.getInt("MaKH"));
                entity.setMaCD(rs.getString("MaCD"));
                entity.setHocPhi(rs.getFloat("HocPhi"));
                entity.setThoiLuong(rs.getInt("ThoiLuong"));
                entity.setNgayKG(rs.getDate("NgayKG"));
                entity.setGhiChu(rs.getString("GhiChu"));
                entity.setMaNV(rs.getString("MaNV"));
                entity.setNgayTao(rs.getDate("NgayTao"));
                
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public List<KhoaHoc> selectKhoaHocByChuyenDe(String maCD) {
         return selectBySql(SELECT_BY_MA_CD_SQL, maCD);
    }
    
    public List<Integer> selectYear(){
        String sql = "SELECT DISTINCT year(NgayKG) Year FROM KhoaHoc ORDER BY Year DESC";
        List<Integer> list = new ArrayList<>();
        try {
            ResultSet rs = jdbcHelper.query(sql);
            while(rs.next()){
                list.add(rs.getInt(1));
             }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
     public KhoaHoc selectMaKHByTenCD(String id){
            return (KhoaHoc) selectBySql(SELECT_MAKH_BY_TENCD, id);
     }
}
