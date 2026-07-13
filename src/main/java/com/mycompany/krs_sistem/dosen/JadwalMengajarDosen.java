package com.mycompany.krs_sistem.dosen;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.RoundedButton;
import com.mycompany.krs_sistem.ui.RoundedPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class JadwalMengajarDosen extends JFrame {
    
    private String nipDosen;
    private String namaDosen;
    private JPanel navbarPanel;
    private JTable jadwalTable;
    private DefaultTableModel tableModel;
    
    private final Font FONT_BOLD_20 = new Font("Segoe UI", Font.BOLD, 20);
    private final Font FONT_BOLD_18 = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FONT_BOLD_13 = new Font("Segoe UI", Font.BOLD, 13);
    private final Font FONT_BOLD_12 = new Font("Segoe UI", Font.BOLD, 12);
    private final Font FONT_BOLD_11 = new Font("Segoe UI", Font.BOLD, 11);
    private final Font FONT_PLAIN_11 = new Font("Segoe UI", Font.PLAIN, 11);
    
    public JadwalMengajarDosen(String nip, String nama) {
        this.nipDosen = nip;
        this.namaDosen = nama;
        initComponents();
        loadJadwal();
    }
    
    private void initComponents() {
        setTitle("Jadwal Mengajar - " + namaDosen);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 244, 248));
        setLayout(new BorderLayout());
        
        // navbar
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
        JLabel logoText = new JLabel("Jadwal Mengajar");
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
        
        // panel utama
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 244, 248));
        mainPanel.setBorder(new EmptyBorder(20, 24, 20, 24));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 12, 0);
        gbc.weightx = 1.0;
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel title = new JLabel("Jadwal Mengajar Semester Ini");
        title.setFont(FONT_BOLD_18);
        title.setForeground(new Color(30, 74, 107));
        titlePanel.add(title, BorderLayout.WEST);
        
        gbc.gridy = 0;
        mainPanel.add(titlePanel, gbc);
        
        // tabel jadwal
        RoundedPanel tableContainer = new RoundedPanel(15);
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setLayout(new BorderLayout());
        
        String[] columns = {"Hari", "Jam Mulai", "Jam Selesai", "Kode MK", "Mata Kuliah", "Kelas", "Ruangan", "jadwal_id"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        jadwalTable = new JTable(tableModel);
        jadwalTable.setRowHeight(35);
        jadwalTable.setFont(FONT_PLAIN_11);
        jadwalTable.setForeground(new Color(51, 78, 104));
        jadwalTable.setShowGrid(true);
        jadwalTable.setGridColor(new Color(200, 200, 200));
        
        JTableHeader header = jadwalTable.getTableHeader();
        header.setFont(FONT_BOLD_12);
        header.setBackground(new Color(44, 125, 160));
        header.setForeground(Color.WHITE);
        
        jadwalTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        jadwalTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        jadwalTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        jadwalTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        jadwalTable.getColumnModel().getColumn(4).setPreferredWidth(200);
        jadwalTable.getColumnModel().getColumn(5).setPreferredWidth(60);
        jadwalTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        
        // Sembunyikan jadwal_id
        jadwalTable.getColumnModel().getColumn(7).setMinWidth(0);
        jadwalTable.getColumnModel().getColumn(7).setMaxWidth(0);
        jadwalTable.getColumnModel().getColumn(7).setWidth(0);
        
        JScrollPane scrollTable = new JScrollPane(jadwalTable);
        scrollTable.setBorder(null);
        
        tableContainer.add(scrollTable, BorderLayout.CENTER);
        
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(tableContainer, gbc);
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        
        add(navbarPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void loadJadwal() {
        String sql = "SELECT j.jadwal_id, j.hari, j.jam_mulai, j.jam_selesai, mk.kode_mk, mk.nama_mk, k.nama_kelas, r.nama_ruangan " +
                     "FROM jadwal j " +
                     "JOIN kelas k ON j.kelas_id = k.kelas_id " +
                     "JOIN mata_kuliah mk ON k.mk_id = mk.mk_id " +
                     "LEFT JOIN ruangan r ON j.ruangan_id = r.ruangan_id " +
                     "WHERE k.nip = ? " +
                     "ORDER BY FIELD(j.hari, 'Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat'), j.jam_mulai";
                     
        try (Connection conn = connection.koneksi();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, nipDosen);
            try (ResultSet rs = stmt.executeQuery()) {
                tableModel.setRowCount(0);
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("hari"),
                        rs.getString("jam_mulai"),
                        rs.getString("jam_selesai"),
                        rs.getString("kode_mk"),
                        rs.getString("nama_mk"),
                        rs.getString("nama_kelas"),
                        rs.getString("nama_ruangan"),
                        rs.getString("jadwal_id")
                    };
                    tableModel.addRow(row);
                }
                
                if (tableModel.getRowCount() == 0) {
                    tableModel.addRow(new Object[]{"-", "-", "-", "-", "Tidak ada jadwal", "-", "-", "-"});
                }
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat jadwal: " + e.getMessage());
        }
    }
    

}