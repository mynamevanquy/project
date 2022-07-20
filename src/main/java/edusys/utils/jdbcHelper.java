/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edusys.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author lam
 */
public class jdbcHelper {
        private static String driver= "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                                      
        private static String dburl="jdbc:sqlserver://localhost;database=EDUSYS";
                                    
        private static String user = "sa";
        private static String pass = "sa";
        static {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
        /**
         * Xây dựng PreparedStatement
         * 
         * @param sql   là câu lệnh sql có thể chứa tham số . Nó có thể là 1 lời
         * gọi thủ tục
         * @param args là danh sách các giá trị được cung cấp cho các tham số trong
         * các câu lệnh sql
         * @return PreparedStatement tạo được
         * @throw java,sql.SQLException lỗi sai cú pháp
         */
        public static PreparedStatement getStmt(String sql, Object ...args) throws SQLException  {
            Connection connection = DriverManager.getConnection(dburl, user, pass);
            PreparedStatement Pstmt = null;
            if(sql.trim().startsWith("{")){
                Pstmt = connection.prepareCall(sql);
            }else{
                Pstmt = connection.prepareStatement(sql);
            }
            for(int i=0 ; i<args.length;i++){
                Pstmt.setObject(i + 1, args[i]);
            }
            return Pstmt;
        }
        /**
         * Thực hiện câu lệnh SQL thao tác (INSERTM, UPDATE, DELETE) hoặc thủ tục
         * lưu thao tác dữ liệu
         * 
         * @@param sql   là câu lệnh sql có thể chứa tham số . Nó có thể là 1 lời
         * gọi thủ tục
         * @param args là danh sách các giá trị được cung cấp cho các tham số trong
         * các câu lệnh sql
         */
        public static int update(String sql, Object...args){
            try {
                PreparedStatement stmt= getStmt(sql, args);
                try {
                    return stmt.executeUpdate();
                    
                } finally {
                    stmt.getConnection().close();
                    
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        /**
         * Thực hiện câu lệnh SQL truy vấn (SELECT) hoặc thủ tục luu truy vẫn dữ lệu
     * @return 
         * @@param sql   là câu lệnh sql có thể chứa tham số . Nó có thể là 1 lời
         * gọi thủ tục
         * @param args là danh sách các giá trị được cung cấp cho các tham số trong
         * các câu lệnh sql
         */
        public static ResultSet query(String sql, Object...args){
            try {
                PreparedStatement stmt= getStmt(sql, args);
                return stmt.executeQuery();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        public static Object value(String sql, Object...args){
            try {
                ResultSet rs = query(sql, args);
                if(rs.next()){
                    return rs.getObject(0);
                }
                rs.getStatement().getConnection().close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        }
}
