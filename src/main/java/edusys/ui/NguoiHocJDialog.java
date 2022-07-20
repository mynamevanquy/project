/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edusys.ui;

import edusys.dao.NguoiHocDAO;
import edusys.entity.NguoiHoc;
import edusys.helper.DataValidator;
import edusys.utils.Auth;
import edusys.utils.MsgBox;
import edusys.utils.XDate;
import edusys.utils.XImage;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import static javax.print.attribute.Size2DSyntax.MM;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lam
 */
public class NguoiHocJDialog extends javax.swing.JDialog {

    NguoiHocDAO dao = new NguoiHocDAO();
    int row = 0;

    /**
     * Creates new form NguoiHocJDialog
     */
    public NguoiHocJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }

    void init() {
        setIconImage(XImage.getAppIcon());
        setLocationRelativeTo(null);
        setTitle("EDUSYS QUẢN LÝ NGƯỜI HỌC");
        fillTable();
       
      updateStatus();
        
    }

    void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblNguoiHoc.getModel();
        model.setRowCount(0);
        try {
            List<NguoiHoc> list = dao.selectAll();
            for (NguoiHoc nh : list) {
                Object[] row = {
                    nh.getMaNH(),
                    nh.getHoTen(),
                    nh.isGioiTinh() ? "Nam" : "Nữ",
                    XDate.toString(nh.getNgaySinh(), "dd-MM-yyyy"),
                    "0" + nh.getDienThoai(),
                    nh.getEmail(),
                    nh.getMaNV(),
                    XDate.toString(nh.getNgayDK(), "dd-M-yyyy")
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liêu");
        }
    }

    void edit() {
        try {
            String maNH = (String) tblNguoiHoc.getValueAt(this.row, 0);
            NguoiHoc model = dao.selectById(maNH);
            if (model != null) {
                setForm(model);
                updateStatus();
                tabs.setSelectedIndex(0);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    void setForm(NguoiHoc model) {
        txtMaNH.setText(model.getMaNH());
        txtHoTen.setText(model.getHoTen());
        rdoNam.setSelected(model.isGioiTinh());
          rdoNu.setSelected(!model.isGioiTinh());
        txtNgaySinh.setText(XDate.toString(model.getNgaySinh(), "dd-MM-yyyy"));
        txtDienThoai.setText("0" + model.getDienThoai());
        txtEmail.setText(model.getEmail());
        txaGhiChu.setText(model.getGhiChu());

    }
Date now = new Date();
    NguoiHoc getForm() {    
       Date date1 =  XDate.toDate(txtNgaySinh.getText(), "dd-MM-yyyy");
       String date2 = XDate.toString(date1, "yyyy-MM-dd");
       
        NguoiHoc model = new NguoiHoc();
        model.setMaNH(txtMaNH.getText());
        model.setHoTen(txtHoTen.getText());
        model.setGioiTinh(rdoNam.isSelected());
        model.setNgaySinh(XDate.toDate(date2, "yyyy-MM-dd"));
        model.setDienThoai(txtDienThoai.getText());
        model.setEmail(txtEmail.getText());
        
        model.setGhiChu(txaGhiChu.getText());
        model.setMaNV(Auth.user.getMaNV());
        model.setNgayDK(XDate.addDays(now,0));
        return model;
    }

    void updateStatus() {
        boolean edit = this.row >= 0;
        boolean first = this.row == 0;
        boolean last = this.row == tblNguoiHoc.getRowCount() - 1;
        txtMaNH.setEditable(!edit);
        // khi insert thì không update , delete
        btnThem.setEnabled(!edit);
        btnSua.setEnabled(edit);
        btnXoa.setEnabled(edit);

        btnFirst.setEnabled(edit && !first);
        btnPrev.setEnabled(edit && !first);
        btnNext.setEnabled(edit && !last);
        btnLast.setEnabled(edit && !last);
    }

    void clearForm() {
         txtHoTen.setText("");
        txtDienThoai.setText("");
        txtEmail.setText("");
        txtMaNH.setText("");
        txtNgaySinh.setText("");
        txaGhiChu.setText("");
        this.updateStatus();
        row = -1;
        updateStatus();
    }

    void insert() {
       
        StringBuilder sb = new StringBuilder();
        DataValidator.validateEmpty(txtMaNH, sb, "Vui lòng nhập mã người học!");
        DataValidator.validateEmpty(txtHoTen, sb, "Vui lòng nhập họ tên người học!");
        DataValidator.validateEmpty(txtNgaySinh, sb, "Vui lòng nhập ngày sinh người học!");
        DataValidator.validateEmpty(txtDienThoai, sb, "Vui lòng nhập điện thoại người học!");
        DataValidator.validateEmpty(txtEmail, sb, "Vui lòng nhập email người học!");
        DataValidator.checkEmail(txtEmail, sb);
        DataValidator.checkNumber(txtDienThoai, sb, "Vui lòng nhập số điện thoại là định dạng số!");
        DataValidator.isDateValid(txtNgaySinh,sb,"Vui lòng nhập đúng định dạng ngày sinh![dd-mm-yyyy]");
        if (sb.length() > 0) {
            MsgBox.alert(this, sb.toString());
            return;
            
        }   
        
        else{
        NguoiHoc model = getForm();
        try {        
            dao.insert(model);
            this.fillTable();
            this.clearForm();         
            MsgBox.alert(this, "Thêm mới thành công!");
        } catch (Exception e) {
            
            MsgBox.alert(this, "Thêm mới thất bại!");
            throw new RuntimeException(e);
        }
        }
    }

    
    void update(){
         StringBuilder sb = new StringBuilder();
        DataValidator.validateEmpty(txtMaNH, sb, "Vui lòng nhập mã người học!");
        DataValidator.validateEmpty(txtHoTen, sb, "Vui lòng nhập họ tên người học!");
        DataValidator.validateEmpty(txtNgaySinh, sb, "Vui lòng nhập ngày sinh người học!");
        DataValidator.validateEmpty(txtDienThoai, sb, "Vui lòng nhập điện thoại người học!");
        DataValidator.validateEmpty(txtEmail, sb, "Vui lòng nhập email người học!");
        DataValidator.checkEmail(txtEmail, sb);
        DataValidator.checkNumber(txtDienThoai, sb, "Vui lòng nhập số điện thoại là định dạng số!");
        DataValidator.isDateValid(txtNgaySinh,sb,"Vui lòng nhập đúng định dạng ngày sinh!");
        if (sb.length() > 0) {
            MsgBox.alert(this, sb.toString());
            return;
        }      
        else{
        NguoiHoc model = getForm();
        try {        
            dao.update(model);
            this.fillTable();
            this.clearForm();         
            MsgBox.alert(this, "Cập nhật thành công!");
        } catch (Exception e) {
            
            MsgBox.alert(this, "Cập nhật thất bại!");
            throw new RuntimeException(e);
        }
        }
    }
    
    void delete() {
        if (!Auth.isManager()) {
            MsgBox.alert(this, "Bạn không có quyền xoá chuyên đề");

        } else {
            if (MsgBox.confirm(this, "Bạn thực sự muốn xoá chuyên đề này?")) {
                String maNH = txtMaNH.getText();
                try {
                    dao.delete(maNH);
                    this.fillTable();
                    this.clearForm();
                    MsgBox.alert(this, "Xoá thành công!");
                } catch (Exception e) {
                    MsgBox.alert(this, "Xoá thất bại!");
                    throw new RuntimeException(e);
                }
            }
        }
    }
    
     void first() {
        row = 0;
        edit();
    }
//

    void prev() {
        if (row > 0) {
            row--;
            edit();
        }
    }

    void next() {
        if (row < tblNguoiHoc.getRowCount() - 1) {
            row++;
            edit();
        }
    }

    void last() {
        row = tblNguoiHoc.getRowCount() - 1;
        edit();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtNgaySinh = new javax.swing.JTextField();
        txtMaNH = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtDienThoai = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtHoTen = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaGhiChu = new javax.swing.JTextArea();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnMoi = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        rdoNam = new javax.swing.JRadioButton();
        rdoNu = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jTextField6 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblNguoiHoc = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(102, 153, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 153, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("QUẢN LÝ NGƯỜI HỌC");

        tabs.setBackground(new java.awt.Color(153, 255, 153));

        jPanel1.setBackground(new java.awt.Color(153, 255, 153));

        jLabel2.setText("Địa chỉ Email:");

        txtNgaySinh.setText("12/11/2001");

        jLabel3.setText("Ngày sinh:");

        txtEmail.setText("lam@gmail.com");

        jLabel4.setText("Mã người học:");

        txtDienThoai.setText("12321");

        jLabel5.setText("Điện thoại:");

        jLabel6.setText("Họ và tên:");

        txtHoTen.setText("a");

        jLabel7.setText("Giới tính:");

        jLabel8.setText("Ghi chú:");

        txaGhiChu.setColumns(20);
        txaGhiChu.setRows(5);
        jScrollPane1.setViewportView(txaGhiChu);

        btnThem.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnThem.setText("THÊM");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnSua.setText("SỬA");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnXoa.setText("XOÁ");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnMoi.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnMoi.setText("MỚI");
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        btnFirst.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnFirst.setText("|<");
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnPrev.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPrev.setText("<<");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnNext.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNext.setText(">>");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnLast.setText(">|");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoNam);
        rdoNam.setSelected(true);
        rdoNam.setText("Nam");
        rdoNam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rdoNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNamActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoNu);
        rdoNu.setText("Nữ");
        rdoNu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5)
                            .addComponent(txtDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(rdoNam, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(rdoNu, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtNgaySinh, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(txtMaNH, javax.swing.GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE)
                    .addComponent(txtHoTen)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addComponent(btnSua)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnXoa))
                            .addComponent(btnThem))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMoi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 253, Short.MAX_VALUE)
                        .addComponent(btnFirst)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrev)
                        .addGap(18, 18, 18)
                        .addComponent(btnNext)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLast)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtMaNH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel3))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdoNam)
                    .addComponent(rdoNu))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addGap(13, 13, 13)
                        .addComponent(txtDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnMoi)
                    .addComponent(btnXoa)
                    .addComponent(btnLast)
                    .addComponent(btnNext)
                    .addComponent(btnPrev)
                    .addComponent(btnFirst))
                .addContainerGap(70, Short.MAX_VALUE))
        );

        tabs.addTab("CẬP NHẬT", jPanel1);

        jPanel2.setBackground(new java.awt.Color(153, 255, 153));

        jLabel9.setText("Tìm kiếm");

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton1.setText("Tìm");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tblNguoiHoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ NH", "HỌ VÀ TÊN", "GIỚI TÍNH", "NGÀY SINH", "ĐIỆN THOẠI", "EMAIL", "MÃ NV", "NGÀY ĐK"
            }
        ));
        tblNguoiHoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblNguoiHocMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(tblNguoiHoc);
        if (tblNguoiHoc.getColumnModel().getColumnCount() > 0) {
            tblNguoiHoc.getColumnModel().getColumn(0).setResizable(false);
            tblNguoiHoc.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblNguoiHoc.getColumnModel().getColumn(1).setResizable(false);
            tblNguoiHoc.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblNguoiHoc.getColumnModel().getColumn(2).setResizable(false);
            tblNguoiHoc.getColumnModel().getColumn(2).setPreferredWidth(25);
            tblNguoiHoc.getColumnModel().getColumn(3).setResizable(false);
            tblNguoiHoc.getColumnModel().getColumn(3).setPreferredWidth(70);
            tblNguoiHoc.getColumnModel().getColumn(4).setResizable(false);
            tblNguoiHoc.getColumnModel().getColumn(4).setPreferredWidth(70);
            tblNguoiHoc.getColumnModel().getColumn(5).setResizable(false);
            tblNguoiHoc.getColumnModel().getColumn(5).setPreferredWidth(100);
            tblNguoiHoc.getColumnModel().getColumn(6).setResizable(false);
            tblNguoiHoc.getColumnModel().getColumn(6).setPreferredWidth(25);
            tblNguoiHoc.getColumnModel().getColumn(7).setResizable(false);
            tblNguoiHoc.getColumnModel().getColumn(7).setPreferredWidth(70);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71))
        );

        tabs.addTab("DANH SÁCH", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(tabs, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // btn thêm
        insert();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        //btn sửa
        update();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // btn xoá
        delete();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        //btn mới
        clearForm();
        txtHoTen.setBackground(Color.white);
        txtDienThoai.setBackground(Color.white);
        txtEmail.setBackground(Color.white);
        txtMaNH.setBackground(Color.white);
        txtNgaySinh.setBackground(Color.white);
        txaGhiChu.setBackground(Color.white);
    }//GEN-LAST:event_btnMoiActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // btn đầu tiên
        first();
       txtHoTen.setBackground(Color.white);
        txtDienThoai.setBackground(Color.white);
        txtEmail.setBackground(Color.white);
        txtMaNH.setBackground(Color.white);
        txtNgaySinh.setBackground(Color.white);
        txaGhiChu.setBackground(Color.white);
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // btn trước đó
        prev();
        txtHoTen.setBackground(Color.white);
        txtDienThoai.setBackground(Color.white);
        txtEmail.setBackground(Color.white);
        txtMaNH.setBackground(Color.white);
        txtNgaySinh.setBackground(Color.white);
        txaGhiChu.setBackground(Color.white);
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        //btn tiếp theo
        next();
        txtHoTen.setBackground(Color.white);
        txtDienThoai.setBackground(Color.white);
        txtEmail.setBackground(Color.white);
        txtMaNH.setBackground(Color.white);
        txtNgaySinh.setBackground(Color.white);
        txaGhiChu.setBackground(Color.white);
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // btn cuối cùng
        last();
        txtHoTen.setBackground(Color.white);
        txtDienThoai.setBackground(Color.white);
        txtEmail.setBackground(Color.white);
        txtMaNH.setBackground(Color.white);
        txtNgaySinh.setBackground(Color.white);
        txaGhiChu.setBackground(Color.white);
    }//GEN-LAST:event_btnLastActionPerformed

    private void tblNguoiHocMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNguoiHocMousePressed
        // click 2 lần vào bảng
        if (evt.getClickCount() == 2) {
            this.row = tblNguoiHoc.rowAtPoint(evt.getPoint());
            txtDienThoai.setBackground(Color.white);
            txtEmail.setBackground(Color.white);
            txtHoTen.setBackground(Color.white);
            txtMaNH.setBackground(Color.white);
            txtNgaySinh.setBackground(Color.white);
            edit();
        }

    }//GEN-LAST:event_tblNguoiHocMousePressed

    private void rdoNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoNamActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NguoiHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NguoiHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NguoiHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NguoiHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                NguoiHocJDialog dialog = new NguoiHocJDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblNguoiHoc;
    private javax.swing.JTextArea txaGhiChu;
    private javax.swing.JTextField txtDienThoai;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMaNH;
    private javax.swing.JTextField txtNgaySinh;
    // End of variables declaration//GEN-END:variables
}
