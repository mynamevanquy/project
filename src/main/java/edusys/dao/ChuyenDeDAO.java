/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edusys.dao;

import edusys.entity.ChuyenDe;
import edusys.utils.jdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lam
 */
public class ChuyenDeDAO extends EdusysDAO<ChuyenDe, String> {

    final String INSERT_SQL = "INSERT INTO ChuyenDe( MaCD , TenCD , HocPhi , ThoiLuong , Hinh , MoTa ) VALUES (?,?,?,?,?,?)";
    final String UPDATE_SQL = "UPDATE ChuyenDe SET TenCD  = ? , HocPhi  = ? , ThoiLuong  = ? , Hinh  = ? , MoTa  = ? WHERE  MaCD  = ? ";
    final String DELETE_SQL = "DELETE FROM ChuyenDe WHERE MaCD = ? ";
    final String SELECT_ALL_SQL = "SELECT * FROM ChuyenDe";
    final String SELECT_BY_ID_SQL = "SELECT * FROM ChuyenDe WHERE MaCD = ? ";
     final String SELECT_TEN_CD_BY_ID_SQL = "SELECT TenCD FROM ChuyenDe WHERE MaCD = ? ";
     


    @Override
    public void insert(ChuyenDe entity) {
        jdbcHelper.update(INSERT_SQL, entity.getMaCD(), entity.getTenCD(), entity.getHocPhi(), 
                        entity.getThoiLuong(), entity.getHinh(), entity.getMoTa());
    }

    @Override
    public void update(ChuyenDe entity) {
        jdbcHelper.update(UPDATE_SQL,  entity.getTenCD(), entity.getHocPhi(), 
                        entity.getThoiLuong(), entity.getHinh(), entity.getMoTa(), entity.getMaCD());
    }

    @Override
    public void delete(String id) {
        jdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public List<ChuyenDe> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public ChuyenDe selectById(String id) {
        List<ChuyenDe> list = selectBySql(SELECT_BY_ID_SQL, id);
        if(list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<ChuyenDe> selectBySql(String sql, Object... args) {
        List<ChuyenDe> list = new ArrayList<>();
        try {
            ResultSet rs = jdbcHelper.query(sql, args);
            while(rs.next()){
                ChuyenDe entity = new ChuyenDe();
                entity.setMaCD(rs.getString("MaCD"));
                entity.setTenCD(rs.getString("TenCD"));
                entity.setHocPhi(rs.getFloat("HocPhi"));
                entity.setThoiLuong(rs.getInt("ThoiLuong"));
                entity.setHinh(rs.getString("Hinh"));
                entity.setMoTa(rs.getString("MoTa"));
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    
    public ChuyenDe SelectMaCDById(String id) {
     List<ChuyenDe> list = selectBySql(SELECT_BY_ID_SQL, id);
        if(list.isEmpty()){
            return null;
        }
        return (ChuyenDe)  list.get(0);
    }
    
        
      
    }
    

 



