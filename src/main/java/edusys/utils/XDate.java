/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edusys.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author lam
 */
public class XDate {
    static SimpleDateFormat formater = new SimpleDateFormat();
    /**
     * Chuyển đổi String sang date
     * 
     * @param date là String cần chuyển
     *  @pattern là định dạng thời gian
     * @return Date kết quả
     */
    public static Date toDate(String date, String pattern){
        try {
            formater.applyPattern(pattern);
            return formater.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Chuyển đổi từ date sang String
     * 
     * @param date là Date cần chuyển đổi
     * @param pattern là định dạng thời gian
     * @return String kết quả 
     */
    public static String toString(Date date , String pattern ){
        formater.applyPattern(pattern);
        return formater.format(date);
    }
    public static Date now(){
        return new Date();
    }
    /**
     * Bổ sung ngày vào thời gian
     * 
     * @param date thời gian hiện có
     * @param days số ngày cần bổ sung vào date
     * @return Date kết quả
     */
    public static Date addDays(Date date, long days){
        date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
        return date;
    }
}
