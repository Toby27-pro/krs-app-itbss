package com.mycompany.krs_sistem.mahasiswa;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.RoundedButton;
import com.mycompany.krs_sistem.ui.RoundedPanel;

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
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class JadwalMahasiswa extends JFrame {

    private String userId;
    private String nimMahasiswa;
    private String namaMahasiswa;

    private JPanel navbarPanel;
    private JPanel mainPanel;
    private JTable jadwalTable;
    private DefaultTableModel jadwalTableModel;

    private final Font FONT_BOLD_20 = new Font("Segoe UI", Font.BOLD, 20);
    private final Font FONT_BOLD_16 = new Font("Segoe UI", Font.BOLD, 16);
    private final Font FONT_BOLD_12 = new Font("Segoe UI", Font.BOLD, 12);
    private final Font FONT_BOLD_11 = new Font("Segoe UI", Font.BOLD, 11);
    private final Font FONT_PLAIN_11 = new Font("Segoe UI", Font.PLAIN, 11);

    public JadwalMahasiswa(String userId, String nimMahasiswa, String namaMahasiswa) {
        this.userId = userId;
        this.nimMahasiswa = nimMahasiswa;
        this.namaMahasiswa = namaMahasiswa;

        initComponents();
        loadJadwal();
    }

    private void initComponents() {
        setTitle("Jadwal Kuliah Mahasiswa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 650);
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
        navbarPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel logoArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoArea.setOpaque(false);
        JLabel logoText = new JLabel("Jadwal Kuliah");
        logoText.setFont(FONT_BOLD_20);
        logoText.setForeground(Color.WHITE);
        logoArea.add(logoText);

        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        userInfo.setOpaque(false);
        JLabel userName = new JLabel(namaMahasiswa + " (NIM: " + nimMahasiswa + ")");
        userName.setForeground(Color.WHITE);
        userName.setFont(FONT_BOLD_12);

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

        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 244, 248));
        mainPanel.setBorder(new EmptyBorder(16, 20, 16, 20));
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 12, 0);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JPanel jadwalPanel = createJadwalColumn();
        mainPanel.add(jadwalPanel, gbc);

        add(navbarPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createJadwalColumn() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        RoundedPanel tableContainer = new RoundedPanel(15);
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setLayout(new BorderLayout());

        String[] columns = {"ID", "MK ID", "Kode MK", "Mata Kuliah", "Hari", "Waktu", "Ruangan", "Dosen", "Kelas"};
        jadwalTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jadwalTable = new JTable(jadwalTableModel);
        jadwalTable.setRowHeight(30);
        jadwalTable.setFont(FONT_PLAIN_11);
        jadwalTable.setForeground(new Color(51, 78, 104));
        jadwalTable.setShowGrid(true);
        jadwalTable.setGridColor(new Color(200, 200, 200));
        jadwalTable.setIntercellSpacing(new Dimension(1, 1));
        jadwalTable.setFillsViewportHeight(true);

        jadwalTable.removeColumn(jadwalTable.getColumnModel().getColumn(1));
        jadwalTable.removeColumn(jadwalTable.getColumnModel().getColumn(0));

        JTableHeader header = jadwalTable.getTableHeader();
        header.setFont(FONT_BOLD_12);
        header.setBackground(new Color(44, 125, 160));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 30));

        jadwalTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        jadwalTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        jadwalTable.getColumnModel().getColumn(2).setPreferredWidth(50);
        jadwalTable.getColumnModel().getColumn(3).setPreferredWidth(90);
        jadwalTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        jadwalTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        jadwalTable.getColumnModel().getColumn(6).setPreferredWidth(50);

        JScrollPane scrollTable = new JScrollPane(jadwalTable);
        scrollTable.setBorder(null);
        scrollTable.getViewport().setBackground(Color.WHITE);

        tableContainer.add(scrollTable, BorderLayout.CENTER);

        panel.add(tableContainer, gbc);
        return panel;
    }

    private void loadJadwal() {
        jadwalTableModel.setRowCount(0);
        String sql = "SELECT j.jadwal_id, mk.mk_id, mk.kode_mk, mk.nama_mk, j.hari, j.jam_mulai, j.jam_selesai, r.nama_ruangan, d.nama_lengkap, k.nama_kelas " +
                     "FROM krs " +
                     "JOIN krs_detail kd ON krs.krs_id = kd.krs_id " +
                     "JOIN jadwal j ON kd.jadwal_id = j.jadwal_id " +
                     "JOIN kelas k ON j.kelas_id = k.kelas_id " +
                     "JOIN mata_kuliah mk ON k.mk_id = mk.mk_id " +
                     "LEFT JOIN dosen d ON k.nip = d.nip " +
                     "LEFT JOIN ruangan r ON j.ruangan_id = r.ruangan_id " +
                     "WHERE krs.nim = ? AND krs.status = 'Approved'";
                     
        try (java.sql.Connection conn = connection.koneksi();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, nimMahasiswa);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String waktu = rs.getString("jam_mulai").substring(0, 5) + " - " + rs.getString("jam_selesai").substring(0, 5);
                    jadwalTableModel.addRow(new Object[]{
                        rs.getString("jadwal_id"),
                        rs.getString("mk_id"),
                        rs.getString("kode_mk"),
                        rs.getString("nama_mk"),
                        rs.getString("hari"),
                        waktu,
                        rs.getString("nama_ruangan"),
                        rs.getString("nama_lengkap"),
                        rs.getString("nama_kelas")
                    });
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
}
