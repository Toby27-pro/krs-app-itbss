package com.mycompany.krs_sistem.mahasiswa;

import com.mycompany.krs_sistem.config.*;
import com.mycompany.krs_sistem.ui.*;

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

public class PengajuanKRS extends JFrame {
    
    private String userId;
    private String nimMahasiswa;
    private String namaMahasiswa;
    private JPanel navbarPanel;
    private JTable mataKuliahTable;
    private DefaultTableModel tableModel;
    private JLabel totalSKSValue;
    private JScrollPane scrollTable;
    
    private final Font FONT_BOLD_20 = new Font("Segoe UI", Font.BOLD, 20);
    private final Font FONT_BOLD_18 = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FONT_BOLD_16 = new Font("Segoe UI", Font.BOLD, 16);
    private final Font FONT_BOLD_14 = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FONT_BOLD_12 = new Font("Segoe UI", Font.BOLD, 12);
    private final Font FONT_BOLD_11 = new Font("Segoe UI", Font.BOLD, 11);
    private final Font FONT_BOLD_10 = new Font("Segoe UI", Font.BOLD, 10);
    private final Font FONT_PLAIN_13 = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font FONT_PLAIN_12 = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font FONT_PLAIN_11 = new Font("Segoe UI", Font.PLAIN, 11);
    
    public PengajuanKRS(String userId, String nim, String nama) {
        this.userId = userId;
        this.nimMahasiswa = nim;
        this.namaMahasiswa = nama;
        initComponents();
        loadMataKuliah();
    }
    
    private void initComponents() {
        setTitle("Pengajuan KRS");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 550);
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
        navbarPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        // tulisan di navbar
        JPanel logoArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoArea.setOpaque(false);
        JLabel logoText = new JLabel("Pengajuan KRS");
        logoText.setFont(FONT_BOLD_20);
        logoText.setForeground(Color.WHITE);
        logoArea.add(logoText);
        
        // user info
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        userInfo.setOpaque(false);
        JLabel userName = new JLabel(namaMahasiswa + " (NIM: " + nimMahasiswa + ")");
        userName.setForeground(Color.WHITE);
        userName.setFont(FONT_BOLD_12);
        
        // tombol kembali
        RoundedButton backBtn = new RoundedButton("Kembali", 25);
        backBtn.setFont(FONT_BOLD_11);
        backBtn.setBackground(new Color(100, 100, 100));
        backBtn.setNormalColor(new Color(100, 100, 100));
        backBtn.addActionListener(e -> {
            new DashboardMahasiswa(userId).setVisible(true);
            dispose();
        });
        
        userInfo.add(userName);
        userInfo.add(backBtn);
        
        navbarPanel.add(logoArea, BorderLayout.WEST);
        navbarPanel.add(userInfo, BorderLayout.EAST);
        
        // panel utama
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 244, 248));
        mainPanel.setBorder(new EmptyBorder(16, 20, 16, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;
        
        // judul
        JLabel titleLabel = new JLabel("Form Pengajuan Kartu Rencana Studi (KRS)");
        titleLabel.setFont(FONT_BOLD_18);
        titleLabel.setForeground(new Color(30, 74, 107));
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);
        
        // tabel mata kuliah dengan kolom Kelas
        RoundedPanel tableContainer = new RoundedPanel(12);
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setLayout(new BorderLayout());
        
        String[] columns = {"Pilih", "Mata Kuliah", "SKS", "Semester", "Kelas", "Kuota Kelas", "Hari", "Jam", "Ruangan", "Dosen", "jadwal_id", "kelas_id", "kode_mk_raw"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) return Boolean.class;
                return String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };
        
        mataKuliahTable = new JTable(tableModel);
        mataKuliahTable.setRowHeight(32);
        mataKuliahTable.setFont(FONT_PLAIN_11);
        mataKuliahTable.setShowGrid(true);
        mataKuliahTable.setGridColor(new Color(200, 200, 200));
        mataKuliahTable.setFillsViewportHeight(false);
        
        JTableHeader header = mataKuliahTable.getTableHeader();
        header.setFont(FONT_BOLD_11);
        header.setBackground(new Color(44, 125, 160));
        header.setForeground(Color.WHITE);
        
        // Mengatur lebar kolom
        mataKuliahTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        mataKuliahTable.getColumnModel().getColumn(1).setPreferredWidth(230); // Mata Kuliah
        mataKuliahTable.getColumnModel().getColumn(2).setPreferredWidth(40); // SKS
        mataKuliahTable.getColumnModel().getColumn(3).setPreferredWidth(60); // Semester
        mataKuliahTable.getColumnModel().getColumn(4).setPreferredWidth(50); // Kelas
        mataKuliahTable.getColumnModel().getColumn(5).setPreferredWidth(60); // Kuota
        mataKuliahTable.getColumnModel().getColumn(6).setPreferredWidth(50); // Hari
        mataKuliahTable.getColumnModel().getColumn(7).setPreferredWidth(80); // Jam
        mataKuliahTable.getColumnModel().getColumn(8).setPreferredWidth(90); // Ruangan
        mataKuliahTable.getColumnModel().getColumn(9).setPreferredWidth(120); // Dosen
        
        mataKuliahTable.getColumnModel().getColumn(10).setMinWidth(0);
        mataKuliahTable.getColumnModel().getColumn(10).setMaxWidth(0);
        mataKuliahTable.getColumnModel().getColumn(10).setWidth(0);
        
        mataKuliahTable.getColumnModel().getColumn(11).setMinWidth(0);
        mataKuliahTable.getColumnModel().getColumn(11).setMaxWidth(0);
        mataKuliahTable.getColumnModel().getColumn(11).setWidth(0);
        
        mataKuliahTable.getColumnModel().getColumn(12).setMinWidth(0);
        mataKuliahTable.getColumnModel().getColumn(12).setMaxWidth(0);
        mataKuliahTable.getColumnModel().getColumn(12).setWidth(0);
        
        scrollTable = new JScrollPane(mataKuliahTable);
        scrollTable.setBorder(null);
        scrollTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        tableContainer.add(scrollTable, BorderLayout.CENTER);
        
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(tableContainer, gbc);
        
        // panel bawah (total sks dan tombol)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        
        // total sks
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.setOpaque(false);
        JLabel totalLabel = new JLabel("Total SKS yang dipilih: ");
        totalLabel.setFont(FONT_BOLD_12);
        totalLabel.setForeground(new Color(30, 74, 107));
        totalSKSValue = new JLabel("0");
        totalSKSValue.setFont(FONT_BOLD_14);
        totalSKSValue.setForeground(new Color(44, 125, 160));
        totalPanel.add(totalLabel);
        totalPanel.add(totalSKSValue);
        
        // tombol simpan dan batal
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton simpanBtn = new JButton("Simpan Pengajuan");
        simpanBtn.setFont(FONT_BOLD_12);
        simpanBtn.setBackground(new Color(44, 125, 160));
        simpanBtn.setForeground(Color.WHITE);
        simpanBtn.setBorderPainted(false);
        simpanBtn.setFocusPainted(false);
        simpanBtn.addActionListener(e -> simpanKRS());
        
        JButton batalBtn = new JButton("Batal");
        batalBtn.setFont(FONT_PLAIN_12);
        batalBtn.addActionListener(e -> {
            new DashboardMahasiswa(userId).setVisible(true);
            dispose();
        });
        
        buttonPanel.add(simpanBtn);
        buttonPanel.add(batalBtn);
        
        bottomPanel.add(totalPanel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        
        gbc.gridy = 2;
        gbc.weighty = 0;
        mainPanel.add(bottomPanel, gbc);
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(navbarPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // hitung total sks
        mataKuliahTable.getModel().addTableModelListener(e -> hitungTotalSKS());
    }
    
    private void loadMataKuliah() {
        try (Connection conn = connection.koneksi()) {
            
            // Ambil data semester aktif dan prodi mahasiswa
            String sqlMhs = "SELECT m.semester_aktif, p.nama_prodi FROM mahasiswa m LEFT JOIN prodi p ON m.prodi_id = p.prodi_id WHERE m.nim = ?";
            PreparedStatement psMhs = conn.prepareStatement(sqlMhs);
            psMhs.setString(1, nimMahasiswa);
            ResultSet rsMhs = psMhs.executeQuery();
            int semesterAktif = 1;
            String namaProdi = "";
            if (rsMhs.next()) {
                semesterAktif = rsMhs.getInt("semester_aktif");
                namaProdi = rsMhs.getString("nama_prodi");
                if (namaProdi == null) namaProdi = "";
            }
            rsMhs.close();
            psMhs.close();
            
            // Mengambil jadwal aktif untuk mahasiswa sesuai prodi/semester
            String sql = "SELECT j.jadwal_id, mk.kode_mk, mk.nama_mk, mk.sks, mk.semester, k.kelas_id, k.nama_kelas, k.kuota, k.kuota_terisi, j.hari, j.jam_mulai, j.jam_selesai, r.nama_ruangan, d.nama_lengkap as dosen " +
                         "FROM jadwal j " +
                         "JOIN kelas k ON j.kelas_id = k.kelas_id " +
                         "JOIN mata_kuliah mk ON k.mk_id = mk.mk_id " +
                         "LEFT JOIN dosen d ON k.nip = d.nip " +
                         "LEFT JOIN ruangan r ON j.ruangan_id = r.ruangan_id " +
                         "WHERE j.jadwal_id NOT IN (" +
                         "  SELECT kd.jadwal_id FROM krs_detail kd " +
                         "  JOIN krs kr ON kd.krs_id = kr.krs_id " +
                         "  WHERE kr.nim = ? AND kd.status != 'Rejected'" +
                         ") " +
                         "AND CAST(mk.semester AS UNSIGNED) >= ? " +
                         "AND CAST(mk.semester AS UNSIGNED) % 2 = ? ";
            
            if (namaProdi.toLowerCase().contains("sistem dan teknologi informasi") || namaProdi.toLowerCase().equals("sti")) {
                sql += " AND (mk.kode_mk LIKE 'ST%' OR mk.kode_mk LIKE 'STI%') ";
            }
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nimMahasiswa);
            stmt.setInt(2, semesterAktif);
            stmt.setInt(3, semesterAktif % 2);
            ResultSet rs = stmt.executeQuery();
            
            tableModel.setRowCount(0);
            
            while (rs.next()) {
                String kodeMk = rs.getString("kode_mk");
                String namaMk = rs.getString("nama_mk");
                String matkulGabungan = kodeMk + " - " + namaMk;
                String kuotaText = rs.getString("kuota_terisi") + "/" + rs.getString("kuota");

                tableModel.addRow(new Object[]{
                    false,
                    matkulGabungan,
                    rs.getString("sks"),
                    rs.getString("semester"),
                    rs.getString("nama_kelas"),
                    kuotaText,
                    rs.getString("hari"),
                    rs.getString("jam_mulai") + " - " + rs.getString("jam_selesai"),
                    rs.getString("nama_ruangan"),
                    rs.getString("dosen"),
                    rs.getString("jadwal_id"), // Hidden
                    rs.getString("kelas_id"),
                    kodeMk
                });
            }
            
            if (tableModel.getRowCount() == 0) {
                tableModel.addRow(new Object[]{false, "Tidak ada mata kuliah tersedia", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"});
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            // data dummy dengan berbagai kelas
            tableModel.setRowCount(0);
            tableModel.addRow(new Object[]{false, "MK1234 - Pemrograman Berorientasi Objek II", "3", "4", "A", "0/40", "Senin", "08:00 - 09:40", "Lab. Komputer A", "Fauzi, S.Kom., M.Kom.", "1", "1", "MK1234"});
            tableModel.addRow(new Object[]{false, "MK1234 - Pemrograman Berorientasi Objek II", "3", "4", "B", "0/40", "Senin", "10:00 - 11:40", "Lab. Komputer A", "Fauzi, S.Kom., M.Kom.", "1", "1", "MK1234"});
            tableModel.addRow(new Object[]{false, "MK1235 - Basis Data Lanjutan", "3", "4", "A", "0/40", "Selasa", "10:00 - 11:40", "Ruang 301", "Dr. Siti Aminah", "2", "2", "MK1235"});
            tableModel.addRow(new Object[]{false, "MK1235 - Basis Data Lanjutan", "3", "4", "C", "0/40", "Selasa", "13:00 - 14:40", "Ruang 302", "Dr. Siti Aminah", "2", "2", "MK1235"});
            tableModel.addRow(new Object[]{false, "MK1236 - Rekayasa Perangkat Lunak", "3", "6", "B", "0/40", "Rabu", "13:00 - 14:40", "Ruang 302", "Budi Santoso, M.Kom.", "3", "3", "MK1236"});
        }
        
        adjustTableHeight();
    }
    
    private void hitungTotalSKS() {
        int total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean selected = (Boolean) tableModel.getValueAt(i, 0);
            if (selected != null && selected) {
                String sksStr = (String) tableModel.getValueAt(i, 2);
                if (sksStr != null && !sksStr.equals("-")) {
                    try {
                        total += Integer.parseInt(sksStr);
                    } catch (NumberFormatException e) {
                        // ignore
                    }
                }
            }
        }
        totalSKSValue.setText(total + "");
    }
    
    private void adjustTableHeight() {
        int rowCount = tableModel.getRowCount();
        if (rowCount > 0 && rowCount <= 5) {
            int height = rowCount * 32 + mataKuliahTable.getTableHeader().getHeight() + 5;
            scrollTable.setPreferredSize(new Dimension(scrollTable.getWidth(), height));
            scrollTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollTable.revalidate();
        }
    }
    
    private void simpanKRS() {
        java.util.ArrayList<Object[]> selectedRows = new java.util.ArrayList<>();
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean selected = (Boolean) tableModel.getValueAt(i, 0);
            if (selected != null && selected) {
                String kodeMK = (String) tableModel.getValueAt(i, 12);
                String sks = (String) tableModel.getValueAt(i, 2);
                String namaKelas = (String) tableModel.getValueAt(i, 4); 
                String hari = (String) tableModel.getValueAt(i, 6);
                String jam = (String) tableModel.getValueAt(i, 7); 
                String jadwalId = (String) tableModel.getValueAt(i, 10);
                String kelasId = (String) tableModel.getValueAt(i, 11);
                
                if (!kodeMK.equals("-")) {
                    selectedRows.add(new Object[]{kodeMK, sks, kelasId, hari, jam, jadwalId});
                }
            }
        }
        
        if (selectedRows.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih minimal satu mata kuliah!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int konfirmasi = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin mengajukan " + selectedRows.size() + " mata kuliah?\nTotal SKS: " + totalSKSValue.getText(),
            "Konfirmasi Pengajuan", JOptionPane.YES_NO_OPTION);
        
        if (konfirmasi != JOptionPane.YES_OPTION) {
            return;
        }
        
        try (Connection conn = connection.koneksi()) {
            
            // Validasi 0: Batas SKS Mahasiswa
            int sksDiajukan = 0;
            try {
                sksDiajukan = Integer.parseInt(totalSKSValue.getText().trim());
            } catch (Exception e) {}
            
            String sqlMaxSks = "SELECT max_sks FROM mahasiswa WHERE nim = ?";
            PreparedStatement psMax = conn.prepareStatement(sqlMaxSks);
            psMax.setString(1, nimMahasiswa);
            ResultSet rsMax = psMax.executeQuery();
            int maxSks = 24;
            if (rsMax.next()) {
                maxSks = rsMax.getInt("max_sks");
            }
            rsMax.close();
            psMax.close();
            
            String sqlSksExisting = "SELECT SUM(mk.sks) FROM krs_detail kd " +
                                    "JOIN krs kr ON kd.krs_id = kr.krs_id " +
                                    "JOIN jadwal j ON kd.jadwal_id = j.jadwal_id " +
                                    "JOIN kelas kl ON j.kelas_id = kl.kelas_id " +
                                    "JOIN mata_kuliah mk ON kl.mk_id = mk.mk_id " +
                                    "WHERE kr.nim = ? AND kd.status != 'Rejected'";
            PreparedStatement psExisting = conn.prepareStatement(sqlSksExisting);
            psExisting.setString(1, nimMahasiswa);
            ResultSet rsExisting = psExisting.executeQuery();
            int sksExisting = 0;
            if (rsExisting.next()) {
                sksExisting = rsExisting.getInt(1);
            }
            rsExisting.close();
            psExisting.close();
            
            if ((sksDiajukan + sksExisting) > maxSks) {
                JOptionPane.showMessageDialog(this, "Total SKS yang Anda ambil (" + (sksDiajukan + sksExisting) + " SKS) melebihi jatah maksimum Anda (" + maxSks + " SKS)!\n\nSKS diajukan sekarang: " + sksDiajukan + "\nSKS sebelumnya: " + sksExisting, "Batas SKS Terlampaui", JOptionPane.ERROR_MESSAGE);
                conn.close();
                return;
            }
            
            // Validasi 1: Kuota Kelas
            for (Object[] row : selectedRows) {
                String kelasId = (String) row[2];
                String sqlCek = "SELECT kuota, kuota_terisi FROM kelas WHERE kelas_id = ?";
                PreparedStatement psCek = conn.prepareStatement(sqlCek);
                psCek.setString(1, kelasId);
                ResultSet rsCek = psCek.executeQuery();
                if (rsCek.next()) {
                    int kuota = rsCek.getInt("kuota");
                    int terisi = rsCek.getInt("kuota_terisi");
                    if (terisi >= kuota) {
                        JOptionPane.showMessageDialog(this, "Kelas sudah penuh (kuota: " + kuota + ")! Pengajuan ditolak.", "Kuota Penuh", JOptionPane.ERROR_MESSAGE);
                        rsCek.close();
                        psCek.close();
                        conn.close();
                        return;
                    }
                }
                rsCek.close();
                psCek.close();
            }
            
            // Validasi 2: Bentrok Jadwal Mahasiswa (antara yang dipilih)
            for (int i=0; i<selectedRows.size(); i++) {
                for (int j=i+1; j<selectedRows.size(); j++) {
                    String kodeMK1 = (String) selectedRows.get(i)[0];
                    String kodeMK2 = (String) selectedRows.get(j)[0];
                    String hari1 = (String) selectedRows.get(i)[3];
                    String jam1 = (String) selectedRows.get(i)[4];
                    String hari2 = (String) selectedRows.get(j)[3];
                    String jam2 = (String) selectedRows.get(j)[4];
                    
                    // Cek mata kuliah yang sama dipilih lebih dari sekali
                    if (kodeMK1.equals(kodeMK2)) {
                        JOptionPane.showMessageDialog(this, "Anda tidak boleh memilih mata kuliah yang sama lebih dari satu kelas (" + kodeMK1 + ")!", "Mata Kuliah Ganda", JOptionPane.ERROR_MESSAGE);
                        conn.close();
                        return;
                    }
                    
                    if (hari1.equals(hari2) && isTimeOverlap(jam1, jam2)) {
                        JOptionPane.showMessageDialog(this, "Terdapat jadwal bentrok (kombinasi kelas) pada hari " + hari1 + "!\n" + jam1 + " vs " + jam2, "Jadwal Bentrok", JOptionPane.ERROR_MESSAGE);
                        conn.close();
                        return;
                    }
                }
            }
            
            // Validasi 3: Bentrok dengan jadwal EXISTING & Mata Kuliah Ganda EXISTING
            for (Object[] row : selectedRows) {
                String kodeMK = (String) row[0];
                String jadwalId = (String) row[5];
                String hari = (String) row[3];
                String jam = (String) row[4];
                String[] parts = jam.split(" - ");
                
                // 3a. Cek apakah mata kuliah ini sudah pernah diambil (lintas pengajuan) dan tidak ditolak
                String sqlCekMK = "SELECT COUNT(*) FROM krs_detail kd " +
                    "JOIN krs kr ON kd.krs_id = kr.krs_id " +
                    "JOIN jadwal j ON kd.jadwal_id = j.jadwal_id " +
                    "JOIN kelas kl ON j.kelas_id = kl.kelas_id " +
                    "JOIN mata_kuliah mk ON kl.mk_id = mk.mk_id " +
                    "WHERE kr.nim = ? AND kd.status != 'Rejected' AND mk.kode_mk = ?";
                PreparedStatement psCekMK = conn.prepareStatement(sqlCekMK);
                psCekMK.setString(1, nimMahasiswa);
                psCekMK.setString(2, kodeMK);
                ResultSet rsCekMK = psCekMK.executeQuery();
                if (rsCekMK.next() && rsCekMK.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "Mata kuliah " + kodeMK + " sudah pernah diajukan sebelumnya dan berstatus Pending/Approved!", "Mata Kuliah Ganda", JOptionPane.ERROR_MESSAGE);
                    rsCekMK.close();
                    psCekMK.close();
                    conn.close();
                    return;
                }
                rsCekMK.close();
                psCekMK.close();
                
                // 3b. Cek bentrok waktu dengan jadwal EXISTING
                if (parts.length == 2) {
                    String sqlBentrok = "SELECT COUNT(*) FROM krs_detail kd " +
                        "JOIN krs kr ON kd.krs_id = kr.krs_id " +
                        "JOIN jadwal j ON kd.jadwal_id = j.jadwal_id " +
                        "WHERE kr.nim = ? AND kd.status != 'Rejected' " +
                        "AND j.hari = ? AND j.jam_mulai < ? AND j.jam_selesai > ?";
                    PreparedStatement psBentrok = conn.prepareStatement(sqlBentrok);
                    psBentrok.setString(1, nimMahasiswa);
                    psBentrok.setString(2, hari);
                    psBentrok.setString(3, parts[1].trim());
                    psBentrok.setString(4, parts[0].trim());
                    ResultSet rsBentrok = psBentrok.executeQuery();
                    if (rsBentrok.next() && rsBentrok.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(this, "Kombinasi jadwal pada hari " + hari + " jam " + jam + " bentrok dengan kelas yang sudah ada di pengajuan sebelumnya!", "Jadwal Bentrok", JOptionPane.ERROR_MESSAGE);
                        rsBentrok.close();
                        psBentrok.close();
                        conn.close();
                        return;
                    }
                    rsBentrok.close();
                    psBentrok.close();
                }
            }
            
            // Lakukan Insert ke KRS dan KRS Detail
            // Kita perlu mengambil jadwal_id berdasarkan kelas_id untuk disisipkan ke krs_detail
            conn.setAutoCommit(false);
            
            // Re-validasi Kuota Kelas dengan Lock FOR UPDATE untuk mencegah race condition
            for (Object[] row : selectedRows) {
                String kelasId = (String) row[2];
                String sqlCekLock = "SELECT kuota, kuota_terisi FROM kelas WHERE kelas_id = ? FOR UPDATE";
                try (PreparedStatement psCekLock = conn.prepareStatement(sqlCekLock)) {
                    psCekLock.setString(1, kelasId);
                    try (ResultSet rsCekLock = psCekLock.executeQuery()) {
                        if (rsCekLock.next()) {
                            int kuota = rsCekLock.getInt("kuota");
                            int terisi = rsCekLock.getInt("kuota_terisi");
                            if (terisi >= kuota) {
                                conn.rollback();
                                conn.setAutoCommit(true);
                                JOptionPane.showMessageDialog(this, "Kelas sudah penuh (kuota: " + kuota + ") pada saat diproses! Pengajuan dibatalkan.", "Kuota Penuh", JOptionPane.ERROR_MESSAGE);
                                conn.close();
                                return;
                            }
                        }
                    }
                }
            }
            
            // Ambil tahun akademik aktif
            int tahunId = 1; // Default
            try (PreparedStatement psTahun = conn.prepareStatement("SELECT tahun_id FROM tahun_akademik WHERE status_aktif = 1 LIMIT 1");
                 ResultSet rsTahun = psTahun.executeQuery()) {
                if (rsTahun.next()) tahunId = rsTahun.getInt("tahun_id");
            }
            
            int existingKrsId = -1;
            try (PreparedStatement psCekKrs = conn.prepareStatement("SELECT krs_id FROM krs WHERE nim = ? AND tahun_id = ? LIMIT 1")) {
                psCekKrs.setString(1, nimMahasiswa);
                psCekKrs.setInt(2, tahunId);
                try (ResultSet rsCekKrs = psCekKrs.executeQuery()) {
                    if (rsCekKrs.next()) existingKrsId = rsCekKrs.getInt("krs_id");
                }
            }
            
            int newKrsId = -1;
            PreparedStatement psKrs = null;
            if (existingKrsId != -1) {
                newKrsId = existingKrsId;
                String sqlUpdateKrs = "UPDATE krs SET status = 'Pending', tanggal_krs = CURDATE() WHERE krs_id = ?";
                psKrs = conn.prepareStatement(sqlUpdateKrs);
                psKrs.setInt(1, newKrsId);
                psKrs.executeUpdate();
            } else {
                String sqlInsertKrs = "INSERT INTO krs (nim, tahun_id, tanggal_krs, status, catatan) VALUES (?, ?, CURDATE(), 'Pending', '')";
                psKrs = conn.prepareStatement(sqlInsertKrs, PreparedStatement.RETURN_GENERATED_KEYS);
                psKrs.setString(1, nimMahasiswa);
                psKrs.setInt(2, tahunId);
                psKrs.executeUpdate();
                
                try (ResultSet rsKeys = psKrs.getGeneratedKeys()) {
                    if (rsKeys.next()) newKrsId = rsKeys.getInt(1);
                }
            }
            
            String sqlInsertDetail = "INSERT INTO krs_detail (krs_id, jadwal_id, status) VALUES (?, ?, 'Pending')";
            PreparedStatement psDetail = conn.prepareStatement(sqlInsertDetail);
            
            String sqlUpdateKuota = "UPDATE kelas SET kuota_terisi = kuota_terisi + 1 WHERE kelas_id = ?";
            PreparedStatement psUpdateKuota = conn.prepareStatement(sqlUpdateKuota);
            
            for (Object[] row : selectedRows) {
                String kelasId = (String) row[2];
                String jadwalId = (String) row[5];
                
                psDetail.setInt(1, newKrsId);
                psDetail.setString(2, jadwalId);
                psDetail.executeUpdate();
                
                // Update kuota
                psUpdateKuota.setString(1, kelasId);
                psUpdateKuota.executeUpdate();
            }
            
            conn.commit();
            conn.setAutoCommit(true);
            
            psKrs.close();
            psDetail.close();
            psUpdateKuota.close();
            
            JOptionPane.showMessageDialog(this, "Pengajuan KRS berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            
            // kembali ke dashboard
            new DashboardMahasiswa(userId).setVisible(true);
            dispose();
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan pengajuan: " + e.getMessage());
        }
    }
    
    /**
     * Cek apakah dua rentang waktu overlap.
     * Format jam: "HH:MM - HH:MM"
     */
    private boolean isTimeOverlap(String jam1, String jam2) {
        try {
            String[] parts1 = jam1.split(" - ");
            String[] parts2 = jam2.split(" - ");
            if (parts1.length != 2 || parts2.length != 2) return jam1.equals(jam2);
            
            int mulai1 = timeToMinutes(parts1[0].trim());
            int selesai1 = timeToMinutes(parts1[1].trim());
            int mulai2 = timeToMinutes(parts2[0].trim());
            int selesai2 = timeToMinutes(parts2[1].trim());
            
            return mulai1 < selesai2 && mulai2 < selesai1;
        } catch (Exception e) {
            return jam1.equals(jam2);
        }
    }
    
    private int timeToMinutes(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }
}