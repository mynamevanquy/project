/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edusys.helper;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author lam
 */
public class MessageDialogHelper {
    //phương thức hộp thoại thông báo
    public static void showMessagerDialog(Component parent, String content,String title ){
        JOptionPane.showMessageDialog(parent, content, title, JOptionPane.INFORMATION_MESSAGE);
    }
    //phương thức hộp thoại thông báo lỗi
    public static void showErrorDialog(Component parent, String content,String title ){
        JOptionPane.showMessageDialog(parent,content, title,  JOptionPane.ERROR_MESSAGE);
    }
    //phương thwcsc hộp thoại  xác nhận thông tin
    public static int showConfirmDialog(Component parent, String content,String title ){
      int choose =  JOptionPane.showConfirmDialog(parent,content, title, 
              JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
      return choose;    
    }
}
