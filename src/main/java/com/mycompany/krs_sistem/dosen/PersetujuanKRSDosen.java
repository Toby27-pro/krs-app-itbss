package com.mycompany.krs_sistem.dosen;

import com.mycompany.krs_sistem.config.*;
import com.mycompany.krs_sistem.ui.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

public class PersetujuanKRSDosen extends JFrame {
    
    private String nipDosen;
    private String namaDosen;
    private JPanel navbarPanel;
    private JTable krsTable;
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
    
    private String filterNim;
    
    public PersetujuanKRSDosen(String nip, String nama) {
        this(nip, nama, null);
    }
    
    public PersetujuanKRSDosen(String nip, String nama, String filterNim) {
        this.nipDosen = nip;
        this.namaDosen = nama;
        this.filterNim = filterNim;
        initComponents();
        loadKRS();
    }
    
    private void initComponents() {
        setTitle("Persetujuan KRS - " + namaDosen);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
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
        JLabel logoText = new JLabel("Persetujuan KRS");
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
        JLabel title = new JLabel("Daftar Pengajuan KRS Menunggu Persetujuan");
        title.setFont(FONT_BOLD_18);
        title.setForeground(new Color(30, 74, 107));
        titlePanel.add(title, BorderLayout.WEST);
        
        gbc.gridy = 0;
        mainPanel.add(titlePanel, gbc);
        
        // tabel krs
        RoundedPanel tableContainer = new RoundedPanel(15);
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setLayout(new BorderLayout());
        
        String[] columns = {"ID KRS", "NIM", "Nama Mahasiswa", "Total SKS", "Tanggal", "Status", "Aksi"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Hanya kolom aksi yang bisa di-klik
            }
        };
        
        krsTable = new JTable(tableModel);
        krsTable.setRowHeight(40);
        krsTable.setFont(FONT_PLAIN_11);
        krsTable.setForeground(new Color(51, 78, 104));
        krsTable.setShowGrid(true);
        krsTable.setGridColor(new Color(200, 200, 200));
        
        JTableHeader header = krsTable.getTableHeader();
        header.setFont(FONT_BOLD_12);
        header.setBackground(new Color(44, 125, 160));
        header.setForeground(Color.WHITE);
        
        krsTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        krsTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        krsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        krsTable.getColumnModel().getColumn(3).setPreferredWidth(60);
        krsTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        krsTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        krsTable.getColumnModel().getColumn(6).setPreferredWidth(150);
        
        // tombol aksi di kolom terakhir
        krsTable.getColumn("Aksi").setCellRenderer(new ButtonRenderer());
        krsTable.getColumn("Aksi").setCellEditor(new ButtonEditor(new JButton()));
        
        JScrollPane scrollTable = new JScrollPane(krsTable);
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
    
    private void loadKRS() {
        try {
            Connection conn = connection.koneksi();
            String sql = "SELECT k.krs_id as id_krs, k.nim, m.nama_lengkap, SUM(mk.sks) as total_sks, k.tanggal_krs, k.status " +
                         "FROM krs k " +
                         "JOIN mahasiswa m ON k.nim = m.nim " +
                         "JOIN krs_detail kd ON k.krs_id = kd.krs_id " +
                         "JOIN jadwal j ON kd.jadwal_id = j.jadwal_id " +
                         "JOIN kelas kl ON j.kelas_id = kl.kelas_id " +
                         "JOIN mata_kuliah mk ON kl.mk_id = mk.mk_id " +
                         "WHERE k.status = 'Pending' AND m.dosen_pa = ? ";
            if (filterNim != null && !filterNim.trim().isEmpty()) {
                sql += "AND m.nim = ? ";
            }
            sql += "GROUP BY k.krs_id, k.nim, m.nama_lengkap, k.tanggal_krs, k.status";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nipDosen);
            if (filterNim != null && !filterNim.trim().isEmpty()) {
                stmt.setString(2, filterNim);
            }
            ResultSet rs = stmt.executeQuery();
            
            tableModel.setRowCount(0);
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("id_krs"),
                    rs.getString("nim"),
                    rs.getString("nama_lengkap"),
                    rs.getString("total_sks"),
                    rs.getString("tanggal_krs"),
                    rs.getString("status"),
                    "" // Kolom aksi tidak butuh teks
                };
                tableModel.addRow(row);
            }
            
            if (tableModel.getRowCount() == 0) {
                tableModel.addRow(new Object[]{"-", "-", "Tidak ada pengajuan KRS", "-", "-", "-", "-"});
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        }
    }
    
    private void showDetailKRS(String idKRS, String namaMhs) {
        javax.swing.JDialog detailDialog = new javax.swing.JDialog(this, "Detail KRS - " + namaMhs, true);
        detailDialog.setSize(900, 400);
        detailDialog.setLocationRelativeTo(this);
        detailDialog.setLayout(new BorderLayout());

        String[] detailCols = {"Detail ID", "Kode MK", "Nama Mata Kuliah", "SKS", "Kelas", "Jadwal", "Ruangan", "Status", "Aksi"};
        DefaultTableModel detailModel = new DefaultTableModel(detailCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8;
            }
        };

        JTable detailTable = new JTable(detailModel);
        detailTable.setRowHeight(35);
        detailTable.getColumnModel().getColumn(0).setMinWidth(0);
        detailTable.getColumnModel().getColumn(0).setMaxWidth(0);
        detailTable.getColumn("Aksi").setCellRenderer(new DetailButtonRenderer());
        detailTable.getColumn("Aksi").setCellEditor(new DetailButtonEditor(new JButton(), detailDialog, detailModel, idKRS));

        JScrollPane scrollPane = new JScrollPane(detailTable);
        detailDialog.add(scrollPane, BorderLayout.CENTER);

        // Load data
        try {
            Connection conn = connection.koneksi();
            String sql = "SELECT kd.detail_id, mk.kode_mk, mk.nama_mk, mk.sks, kl.nama_kelas, j.hari, j.jam_mulai, j.jam_selesai, r.nama_ruangan, kd.status " +
                         "FROM krs_detail kd " +
                         "JOIN jadwal j ON kd.jadwal_id = j.jadwal_id " +
                         "JOIN kelas kl ON j.kelas_id = kl.kelas_id " +
                         "JOIN mata_kuliah mk ON kl.mk_id = mk.mk_id " +
                         "LEFT JOIN ruangan r ON j.ruangan_id = r.ruangan_id " +
                         "WHERE kd.krs_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idKRS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                detailModel.addRow(new Object[]{
                    rs.getString("detail_id"),
                    rs.getString("kode_mk"),
                    rs.getString("nama_mk"),
                    rs.getString("sks"),
                    rs.getString("nama_kelas"),
                    rs.getString("hari") + " " + rs.getString("jam_mulai") + "-" + rs.getString("jam_selesai"),
                    rs.getString("nama_ruangan"),
                    rs.getString("status"),
                    ""
                });
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat detail KRS: " + e.getMessage());
        }

        detailDialog.setVisible(true);
        loadKRS(); // Refresh main table after dialog closed
    }

    class ButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private JButton btnDetail;
        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setOpaque(true);
            setBackground(Color.WHITE);
            btnDetail = new JButton("Lihat Detail");
            btnDetail.setBackground(new Color(44, 125, 160));
            btnDetail.setForeground(Color.WHITE);
            btnDetail.setFont(new Font("Segoe UI", Font.BOLD, 11));
            add(btnDetail);
        }
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends javax.swing.AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private JPanel panel;
        private JButton btnDetail;
        private String idKRS;
        private String namaMhs;

        public ButtonEditor(JButton dummy) {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setBackground(Color.WHITE);
            btnDetail = new JButton("Lihat Detail");
            btnDetail.setBackground(new Color(44, 125, 160));
            btnDetail.setForeground(Color.WHITE);
            btnDetail.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btnDetail.addActionListener(e -> {
                fireEditingStopped();
                showDetailKRS(idKRS, namaMhs);
            });
            panel.add(btnDetail);
        }
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            idKRS = table.getValueAt(row, 0).toString();
            namaMhs = table.getValueAt(row, 2).toString();
            return panel;
        }
        public Object getCellEditorValue() {
            return "";
        }
    }

    class DetailButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private JButton btnSetujui;
        private JButton btnTolak;
        public DetailButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setOpaque(true);
            setBackground(Color.WHITE);
            btnSetujui = new JButton("Setujui");
            btnSetujui.setBackground(new Color(40, 167, 69));
            btnSetujui.setForeground(Color.WHITE);
            btnSetujui.setFont(new Font("Segoe UI", Font.BOLD, 10));
            btnTolak = new JButton("Tolak");
            btnTolak.setBackground(new Color(220, 53, 69));
            btnTolak.setForeground(Color.WHITE);
            btnTolak.setFont(new Font("Segoe UI", Font.BOLD, 10));
            add(btnSetujui);
            add(btnTolak);
        }
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String status = (String) table.getValueAt(row, 7);
            boolean isPending = "Pending".equals(status);
            btnSetujui.setVisible(isPending);
            btnTolak.setVisible(isPending);
            return this;
        }
    }

    class DetailButtonEditor extends javax.swing.AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private JPanel panel;
        private JButton btnSetujui;
        private JButton btnTolak;
        private String detailId;
        private javax.swing.JDialog dialog;
        private DefaultTableModel model;
        private int currentRow;
        private String krsId;

        public DetailButtonEditor(JButton dummy, javax.swing.JDialog dialog, DefaultTableModel model, String krsId) {
            this.dialog = dialog;
            this.model = model;
            this.krsId = krsId;
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setBackground(Color.WHITE);

            btnSetujui = new JButton("Setujui");
            btnSetujui.setBackground(new Color(40, 167, 69));
            btnSetujui.setForeground(Color.WHITE);
            btnSetujui.setFont(new Font("Segoe UI", Font.BOLD, 10));
            btnSetujui.addActionListener(e -> {
                fireEditingStopped();
                approveDetail(detailId);
            });

            btnTolak = new JButton("Tolak");
            btnTolak.setBackground(new Color(220, 53, 69));
            btnTolak.setForeground(Color.WHITE);
            btnTolak.setFont(new Font("Segoe UI", Font.BOLD, 10));
            btnTolak.addActionListener(e -> {
                fireEditingStopped();
                rejectDetail(detailId);
            });

            panel.add(btnSetujui);
            panel.add(btnTolak);
        }

        public java.awt.Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.currentRow = row;
            this.detailId = table.getValueAt(row, 0).toString();
            String status = (String) table.getValueAt(row, 7);
            boolean isPending = "Pending".equals(status);
            btnSetujui.setVisible(isPending);
            btnTolak.setVisible(isPending);
            return panel;
        }
        public Object getCellEditorValue() {
            return "";
        }

        private void approveDetail(String dId) {
            Connection conn = null;
            try {
                conn = connection.koneksi();
                conn.setAutoCommit(false);
                String sql = "UPDATE krs_detail SET status = 'Approved' WHERE detail_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, dId);
                stmt.executeUpdate();
                stmt.close();
                
                checkAllProcessed(conn);
                conn.commit();
                conn.setAutoCommit(true);
                conn.close();
                model.setValueAt("Approved", currentRow, 7);
            } catch (SQLException e) {
                if (conn != null) {
                    try { conn.rollback(); } catch (SQLException ex) {}
                    try { conn.close(); } catch (SQLException ex) {}
                }
                JOptionPane.showMessageDialog(dialog, "Gagal menyetujui: " + e.getMessage());
            }
        }

        private void rejectDetail(String dId) {
            Connection conn = null;
            try {
                conn = connection.koneksi();
                conn.setAutoCommit(false);
                
                // 1. Get kelas_id
                String sqlGetKelas = "SELECT j.kelas_id FROM krs_detail kd JOIN jadwal j ON kd.jadwal_id = j.jadwal_id WHERE kd.detail_id = ?";
                PreparedStatement psGet = conn.prepareStatement(sqlGetKelas);
                psGet.setString(1, dId);
                ResultSet rs = psGet.executeQuery();
                if (rs.next()) {
                    String kelasId = rs.getString("kelas_id");
                    
                    // 2. Decrease kuota
                    String sqlDec = "UPDATE kelas SET kuota_terisi = kuota_terisi - 1 WHERE kelas_id = ? AND kuota_terisi > 0";
                    PreparedStatement psDec = conn.prepareStatement(sqlDec);
                    psDec.setString(1, kelasId);
                    psDec.executeUpdate();
                    psDec.close();
                }
                rs.close();
                psGet.close();
                
                // 3. Update status
                String sql = "UPDATE krs_detail SET status = 'Rejected' WHERE detail_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, dId);
                stmt.executeUpdate();
                stmt.close();
                
                checkAllProcessed(conn);
                conn.commit();
                conn.setAutoCommit(true);
                conn.close();
                model.setValueAt("Rejected", currentRow, 7);
            } catch (SQLException e) {
                if (conn != null) {
                    try { conn.rollback(); } catch (SQLException ex) {}
                    try { conn.close(); } catch (SQLException ex) {}
                }
                JOptionPane.showMessageDialog(dialog, "Gagal menolak: " + e.getMessage());
            }
        }
        
        private void checkAllProcessed(Connection conn) throws SQLException {
            String sqlCheck = "SELECT COUNT(*) FROM krs_detail WHERE krs_id = ? AND status = 'Pending'";
            PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
            psCheck.setString(1, krsId);
            ResultSet rs = psCheck.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                // Update master KRS if no pending items left
                String sqlUpd = "UPDATE krs SET status = 'Approved' WHERE krs_id = ?";
                PreparedStatement psUpd = conn.prepareStatement(sqlUpd);
                psUpd.setString(1, krsId);
                psUpd.executeUpdate();
                psUpd.close();
            }
            rs.close();
            psCheck.close();
        }
    }
}