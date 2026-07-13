package com.mycompany.krs_sistem.dosen;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.RoundedButton;
import com.mycompany.krs_sistem.ui.RoundedPanel;
import com.mycompany.krs_sistem.utils.PDFGenerator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class CetakKRSBimbingan extends JFrame {

    private String nipDosen;
    private String namaDosen;
    private JPanel navbarPanel;
    private JTable krsTable;
    private DefaultTableModel tableModel;

    private final Font FONT_BOLD_20 = new Font("Segoe UI", Font.BOLD, 20);
    private final Font FONT_BOLD_18 = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FONT_BOLD_13 = new Font("Segoe UI", Font.BOLD, 13);
    private final Font FONT_BOLD_12 = new Font("Segoe UI", Font.BOLD, 12);
    private final Font FONT_BOLD_11 = new Font("Segoe UI", Font.BOLD, 11);
    private final Font FONT_PLAIN_11 = new Font("Segoe UI", Font.PLAIN, 11);

    public CetakKRSBimbingan(String nip, String nama) {
        this.nipDosen = nip;
        this.namaDosen = nama;
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Cetak KRS Bimbingan - " + namaDosen);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 650);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 244, 248));
        setLayout(new BorderLayout());

        // Navbar
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
        JLabel logoText = new JLabel("Cetak KRS Bimbingan");
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

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 244, 248));
        mainPanel.setBorder(new EmptyBorder(20, 24, 20, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 12, 0);
        gbc.weightx = 1.0;

        // Title + Cetak PDF button
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Daftar Semua KRS Mahasiswa Bimbingan");
        title.setFont(FONT_BOLD_18);
        title.setForeground(new Color(30, 74, 107));
        titlePanel.add(title, BorderLayout.WEST);

        RoundedButton cetakBtn = new RoundedButton("Cetak PDF", 20);
        cetakBtn.setFont(FONT_BOLD_11);
        cetakBtn.setBackground(new Color(220, 53, 69));
        cetakBtn.setNormalColor(new Color(220, 53, 69));
        cetakBtn.setForeground(Color.WHITE);
        cetakBtn.setPreferredSize(new Dimension(120, 35));
        cetakBtn.addActionListener(e -> cetakPDF());
        titlePanel.add(cetakBtn, BorderLayout.EAST);

        gbc.gridy = 0;
        mainPanel.add(titlePanel, gbc);

        // Table
        RoundedPanel tableContainer = new RoundedPanel(15);
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setLayout(new BorderLayout());

        String[] columns = {"NIM", "Nama Mahasiswa", "Tahun Ajaran", "Semester", "Total SKS", "Status", "Tanggal KRS"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        krsTable = new JTable(tableModel);
        krsTable.setRowHeight(35);
        krsTable.setFont(FONT_PLAIN_11);
        krsTable.setForeground(new Color(51, 78, 104));
        krsTable.setShowGrid(true);
        krsTable.setGridColor(new Color(200, 200, 200));
        krsTable.setIntercellSpacing(new Dimension(1, 1));
        krsTable.setFillsViewportHeight(true);

        JTableHeader header = krsTable.getTableHeader();
        header.setFont(FONT_BOLD_12);
        header.setBackground(new Color(44, 125, 160));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 30));

        krsTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        krsTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        krsTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        krsTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        krsTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        krsTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        krsTable.getColumnModel().getColumn(6).setPreferredWidth(100);

        JScrollPane scrollTable = new JScrollPane(krsTable);
        scrollTable.setBorder(null);
        scrollTable.getViewport().setBackground(Color.WHITE);

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

    private void loadData() {
        tableModel.setRowCount(0);
        String sql = "SELECT k.krs_id, k.nim, m.nama_lengkap, ta.tahun_ajaran, ta.semester, k.tanggal_krs, k.status, " +
                     "COALESCE(SUM(CASE WHEN kd.status != 'Rejected' THEN mk.sks ELSE 0 END), 0) as total_sks " +
                     "FROM krs k " +
                     "JOIN mahasiswa m ON k.nim = m.nim " +
                     "LEFT JOIN tahun_akademik ta ON k.tahun_id = ta.tahun_id " +
                     "LEFT JOIN krs_detail kd ON k.krs_id = kd.krs_id " +
                     "LEFT JOIN jadwal j ON kd.jadwal_id = j.jadwal_id " +
                     "LEFT JOIN kelas kl ON j.kelas_id = kl.kelas_id " +
                     "LEFT JOIN mata_kuliah mk ON kl.mk_id = mk.mk_id " +
                     "WHERE m.dosen_pa = ? " +
                     "GROUP BY k.krs_id, k.nim, m.nama_lengkap, ta.tahun_ajaran, ta.semester, k.tanggal_krs, k.status " +
                     "ORDER BY m.nim, k.krs_id";

        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nipDosen);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getString("nim"),
                        rs.getString("nama_lengkap"),
                        rs.getString("tahun_ajaran") != null ? rs.getString("tahun_ajaran") : "-",
                        rs.getString("semester") != null ? rs.getString("semester") : "-",
                        rs.getInt("total_sks"),
                        rs.getString("status"),
                        rs.getString("tanggal_krs") != null ? rs.getString("tanggal_krs") : "-"
                    });
                }
            }

            if (tableModel.getRowCount() == 0) {
                tableModel.addRow(new Object[]{"-", "Tidak ada data KRS", "-", "-", "-", "-", "-"});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        }
    }

    private void cetakPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Rekap KRS Bimbingan");
        fileChooser.setSelectedFile(new File("Rekap_KRS_Bimbingan_" + nipDosen + ".pdf"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String path = fileToSave.getAbsolutePath();
            if (!path.toLowerCase().endsWith(".pdf")) {
                path += ".pdf";
            }

            boolean success = PDFGenerator.generateRekapKRSBimbingan(nipDosen, namaDosen, path);
            if (success) {
                JOptionPane.showMessageDialog(this, "PDF berhasil disimpan di:\n" + path, "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan PDF!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
