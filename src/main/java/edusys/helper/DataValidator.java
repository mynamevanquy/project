/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edusys.helper;

import java.awt.Color;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author lam
 */
public class DataValidator {

    final static String DATE_FORMAT = "dd-MM-yyyy";

    //phương thức kiểm tra: Dữ liệu truyền vào, đối tượng sb, mess lỗi chứa thông dideemp lỗi
    public static void validateEmpty(JTextField field, StringBuilder sb, String errorMessage) {
        //kiểm tra field nếu rông thì 
        if (field.getText().equals("")) {
            // bỏ sung thông điệp lỗi vào sb
            sb.append(errorMessage).append("\n");
            //đổi màu nền
            field.setBackground(Color.yellow);
            //con trỏ vào field
            field.requestFocus();
        } else {
            //k lỗi thì sẽ set bg trắng
            field.setBackground(Color.WHITE);
        }

    }

    public static void validateEmpty(JLabel label, StringBuilder sb, String errorMessage) {
        //kiểm tra field nếu rông thì 
        if (label.getIcon() == null) {
            // bỏ sung thông điệp lỗi vào sb
            sb.append(errorMessage).append("\n");
            //đổi màu nền
            label.setBackground(Color.yellow);
            //con trỏ vào field
            label.requestFocus();
        } else {
            //k lỗi thì sẽ set bg trắng
            label.setBackground(Color.WHITE);
        }

    }

    public static void validateEmpty(JTextArea field, StringBuilder sb, String errorMessage) {
        //kiểm tra field nếu rông thì 
        if (field.getText().equals("")) {
            // bỏ sung thông điệp lỗi vào sb
            sb.append(errorMessage).append("\n");
            //đổi màu nền
            field.setBackground(Color.yellow);
            //con trỏ vào field
            field.requestFocus();
        } else {
            //k lỗi thì sẽ set bg trắng
            field.setBackground(Color.WHITE);
        }

    }
//phương thức kiểm tra: mật khẩu truyền vào, đối tượng sb, mess lỗi chứa thông dideemp lỗi

    public static void validateEmpty(JPasswordField field, StringBuilder sb, String errorMessage) {
        //kiểm tra field nếu rông thì 
        String password = new String(field.getPassword());
        if (password.equals("")) {
            // bỏ sung thông điệp lỗi vào sb
            sb.append(errorMessage).append("\n");
            //đổi màu nền
            field.setBackground(Color.yellow);
            //con trỏ vào field
            field.requestFocus();
        } else {
            //k lỗi thì sẽ set bg trắng
            field.setBackground(Color.WHITE);
        }
    }

    public static boolean checkEmail(JTextField field, StringBuilder sb) {
        boolean ok = true;

        Pattern pattern = Pattern.compile("\\w+@\\w+\\.\\w+");
        Matcher matcher = pattern.matcher(field.getText());

        if (!matcher.find()) {
            sb.append("Email không hợp lệ [someone@example.com]\n");
            field.setBackground(Color.yellow);
            ok = false;
        }
        if (ok) {
            field.setBackground(Color.white);
        }
        return ok;
    }

    public static boolean checkNumber(JTextField field, StringBuilder sb, String errorMessage) {
        boolean ok = true;

        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(field.getText());

        if (!matcher.find()) {
            sb.append(errorMessage).append("\n");
            field.setBackground(Color.yellow);
            ok = false;
        }
        if (ok) {
            field.setBackground(Color.white);
        }
        return ok;

    }

    public static boolean isDateValid(JTextField field, StringBuilder sb, String errorMessage) {
        boolean ok = true;

        SimpleDateFormat sdf = new  SimpleDateFormat();
        sdf.applyPattern("dd-MM-yyyy");
      
        try {
            Date dob = sdf.parse(field.getText());
            field.setBackground(Color.white);
            ok = true;
        } catch (Exception e) {
            sb.append(errorMessage).append("\n");
            field.setBackground(Color.yellow);
            ok = false;
        }
        return ok;
    }
        
    }



