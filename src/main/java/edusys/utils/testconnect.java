/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edusys.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author lam
 */
public class testconnect {
       
   public static void main(String[] args) {
       
       try {
           String driver= "com.microsoft.sqlserver.jdbc.SQLServerDriver";
       String dburl="jdbc:sqlserver://localhost;database=Edusys";
      String user = "sa";
    String pass = "12345";
           Class.forName(driver);       
       Connection connection = DriverManager.getConnection(dburl, user, pass);
       System.out.println("Connection is open");
     } catch (Exception e) {
           e.printStackTrace();
        
        
        
    }
}}
