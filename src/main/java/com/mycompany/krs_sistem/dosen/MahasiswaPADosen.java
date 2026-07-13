package com.mycompany.krs_sistem.dosen;

import com.mycompany.krs_sistem.config.*;
import com.mycompany.krs_sistem.ui.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.AbstractCellEditor;

public class MahasiswaPADosen extends JFrame {
    
    private String nipDosen;
    private String namaDosen;
    private JPanel navbarPanel;
    private JTable mahasiswaTable;
    private DefaultTableModel tableModel;
    
    private final Font FONT_BOLD_20 = new Font("Segoe UI", Font.BOLD, 20);
    private final Font FONT_BOLD_18 = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FONT_BOLD_16 = new Font("Segoe UI", Font.BOLD, 16);
    private final Font FONT_BOLD_14 = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FONT_BOLD_13 = new Font("Segoe UI", Font.BOLD, 13);
    private final Font FONT_BOLD_12 = new Font("Segoe UI", Font.BOLD, 12);
    private final Font FONT_BOLD_11 = new Font("Segoe UI", Font.BOLD, 11);
    private final Font FONT_PLAIN_12 = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font FONT_PLAIN_11 = new Font("Segoe UI", Font.PLAIN, 11);
    
    public MahasiswaPADosen(String nip, String nama) {
        this.nipDosen = nip;
        this.namaDosen = nama;
        initComponents();
        loadMahasiswa();
    }
    
    private void initComponents() {
        setTitle("Mahasiswa Bimbingan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 244, 248));
        setLayout(new BorderLayout());
        
        navbarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 74, 107), getWidth(), 0, new Color(44, 125, 160));
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        navbarPanel.setLayout(new BorderLayout());
        navbarPanel.setBorder(new EmptyBorder(12, 24, 12, 24));
        
        JPanel logoArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoArea.setOpaque(false);
        JLabel logoText = new JLabel("Mahasiswa Bimbingan");
        logoText.setFont(FONT_BOLD_20);
        logoText.setForeground(Color.WHITE);
        logoArea.add(logoText);
        
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        userInfo.setOpaque(false);
        JLabel userName = new JLabel(namaDosen);
        userName.setForeground(Color.WHITE);
        userName.setFont(FONT_BOLD_13);
        
        RoundedButton backBtn = new RoundedButton("Kembali", 25);
        backBtn.setFont(FONT_BOLD_11);
        backBtn.setBackground(new Color(100, 100, 100));
        backBtn.setNormalColor(new Color(100, 100, 100));
        backBtn.addActionListener(e -> {
            new DashboardDosen(nipDosen, namaDosen).setVisible(true);
            dispose();
        });
        
        userInfo.add(userName);
        userInfo.add(backBtn);
        
        navbarPanel.add(logoArea, BorderLayout.WEST);
        navbarPanel.add(userInfo, BorderLayout.EAST);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 244, 248));
        mainPanel.setBorder(new EmptyBorder(20, 24, 20, 24));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel title = new JLabel("Daftar Mahasiswa Bimbingan Akademik");
        title.setFont(FONT_BOLD_18);
        title.setForeground(new Color(30, 74, 107));
        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.setBorder(new EmptyBorder(0, 0, 12, 0));
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        RoundedPanel tableContainer = new RoundedPanel(15);
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setLayout(new BorderLayout());
        
        String[] columns = {"NIM", "Nama Mahasiswa", "Prodi", "Semester", "Aksi"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        
        mahasiswaTable = new JTable(tableModel);
        mahasiswaTable.setRowHeight(50);
        mahasiswaTable.setFont(FONT_PLAIN_11);
        mahasiswaTable.setForeground(new Color(51, 78, 104));
        mahasiswaTable.setShowGrid(true);
        mahasiswaTable.setGridColor(new Color(200, 200, 200));
        
        JTableHeader header = mahasiswaTable.getTableHeader();
        header.setFont(FONT_BOLD_12);
        header.setBackground(new Color(44, 125, 160));
        header.setForeground(Color.WHITE);
        
        mahasiswaTable.getColumnModel().getColumn(4).setCellRenderer(new ActionButtonRenderer());
        mahasiswaTable.getColumnModel().getColumn(4).setCellEditor(new ActionButtonEditor());
        
        JScrollPane scrollTable = new JScrollPane(mahasiswaTable);
        scrollTable.setBorder(null);
        
        tableContainer.add(scrollTable, BorderLayout.CENTER);
        
        mainPanel.add(tableContainer, BorderLayout.CENTER);
        
        add(navbarPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void loadMahasiswa() {
        String sql = "SELECT m.nim, m.nama_lengkap, p.nama_prodi, m.semester_aktif " +
                     "FROM mahasiswa m " +
                     "JOIN prodi p ON m.prodi_id = p.prodi_id " +
                     "WHERE m.dosen_pa = ?";
                     
        try (Connection conn = connection.koneksi();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, nipDosen);
            try (ResultSet rs = stmt.executeQuery()) {
                tableModel.setRowCount(0);
                
                while (rs.next()) {
                    String nim = rs.getString("nim");
                    String nama = rs.getString("nama_lengkap");
                    String prodi = rs.getString("nama_prodi");
                    String semester = rs.getString("semester_aktif");
                    
                    tableModel.addRow(new Object[]{nim, nama, prodi, semester, "buttons"});
                }
                
                if (tableModel.getRowCount() == 0) {
                    tableModel.addRow(new Object[]{"-", "Tidak ada mahasiswa bimbingan", "-", "-", ""});
                }
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        }
    }
    
    class ActionButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton cekBtn;
        
        public ActionButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 8, 8));
            setOpaque(true);
            
            cekBtn = new JButton("Cek KRS");
            cekBtn.setFont(FONT_BOLD_11);
            cekBtn.setBackground(new Color(44, 125, 160));
            cekBtn.setForeground(Color.WHITE);
            cekBtn.setFocusPainted(false);
            cekBtn.setBorderPainted(false);
            cekBtn.setPreferredSize(new Dimension(90, 30));
            
            add(cekBtn);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(Color.WHITE);
            }
            return this;
        }
    }
    
    class ActionButtonEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private JPanel panel;
        private JButton cekBtn;
        private String currentNim;
        private String currentNama;
        
        public ActionButtonEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
            panel.setOpaque(true);
            
            cekBtn = new JButton("Cek KRS");
            cekBtn.setFont(FONT_BOLD_11);
            cekBtn.setBackground(new Color(44, 125, 160));
            cekBtn.setForeground(Color.WHITE);
            cekBtn.setFocusPainted(false);
            cekBtn.setBorderPainted(false);
            cekBtn.setPreferredSize(new Dimension(90, 30));
            
            cekBtn.addActionListener(e -> {
                if (currentNim != null && !currentNim.equals("-")) {
                    fireEditingStopped();
                    new PersetujuanKRSDosen(nipDosen, namaDosen, currentNim).setVisible(true);
                    dispose();
                }
            });
            
            panel.add(cekBtn);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentNim = (String) table.getValueAt(row, 0);
            currentNama = (String) table.getValueAt(row, 1);
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "buttons";
        }
    }
    
}