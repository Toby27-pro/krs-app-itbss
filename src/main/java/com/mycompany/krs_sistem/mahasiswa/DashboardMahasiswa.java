package com.mycompany.krs_sistem.mahasiswa;

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
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class DashboardMahasiswa extends JFrame {
    
    // panel
    private JPanel navbarPanel;
    private JPanel mainPanel;
    
    // tulisan total sks
    private JLabel totalSKSValue;
    
    private String nimMahasiswa;
    private String namaMahasiswa;
    private String userId;
    private String namaDosenPA = "Belum ditentukan";
    
    // ukuran huruf
    private final Font FONT_BOLD_20 = new Font("Segoe UI", Font.BOLD, 20);
    private final Font FONT_BOLD_18 = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FONT_BOLD_16 = new Font("Segoe UI", Font.BOLD, 16);
    private final Font FONT_BOLD_12 = new Font("Segoe UI", Font.BOLD, 12);
    private final Font FONT_BOLD_11 = new Font("Segoe UI", Font.BOLD, 11);
    private final Font FONT_PLAIN_12 = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font FONT_PLAIN_11 = new Font("Segoe UI", Font.PLAIN, 11);
    
    public DashboardMahasiswa(String userId) {
        this.userId = userId;
        fetchMahasiswaData();
        initComponents();
        loadSKSData();
    }
    
    private void fetchMahasiswaData() {
        String sql = "SELECT m.nim, m.nama_lengkap, d.nama_lengkap AS nama_dosen " +
                     "FROM mahasiswa m " +
                     "LEFT JOIN dosen d ON m.dosen_pa = d.nip " +
                     "WHERE m.user_id = ?";
        try (java.sql.Connection conn = connection.koneksi();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, userId);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    this.nimMahasiswa = rs.getString("nim");
                    this.namaMahasiswa = rs.getString("nama_lengkap");
                    String dosenPA = rs.getString("nama_dosen");
                    if (dosenPA != null && !dosenPA.trim().isEmpty()) {
                        this.namaDosenPA = dosenPA;
                    }
                } else {
                    this.nimMahasiswa = "Unknown";
                    this.namaMahasiswa = "Unknown";
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void initComponents() {
        setTitle("Dashboard Mahasiswa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 650);
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
        
        JPanel logoArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoArea.setOpaque(false);
        JLabel logoText = new JLabel("Dashboard Mahasiswa");
        logoText.setFont(FONT_BOLD_20);
        logoText.setForeground(Color.WHITE);
        logoArea.add(logoText);
        
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        userInfo.setOpaque(false);
        JLabel userName = new JLabel(namaMahasiswa + " (NIM: " + nimMahasiswa + ")");
        userName.setForeground(Color.WHITE);
        userName.setFont(FONT_BOLD_12);
        
        RoundedButton logoutBtn = new RoundedButton("Logout", 20);
        logoutBtn.setFont(FONT_BOLD_11);
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setNormalColor(new Color(231, 76, 60));
        logoutBtn.addActionListener(e -> {
            new com.mycompany.krs_sistem.auth.Login().setVisible(true);
            dispose();
        });
        
        userInfo.add(userName);
        userInfo.add(logoutBtn);
        
        navbarPanel.add(logoArea, BorderLayout.WEST);
        navbarPanel.add(userInfo, BorderLayout.EAST);
        
        // panel utama
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 244, 248));
        mainPanel.setBorder(new EmptyBorder(16, 20, 16, 20));
        mainPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 12, 0);
        gbc.weightx = 1.0;
        
        // panel welcome
        RoundedPanel welcomePanel = new RoundedPanel(15);
        welcomePanel.setBackground(Color.WHITE);
        welcomePanel.setBorder(new EmptyBorder(12, 20, 12, 20));
        welcomePanel.setLayout(new BorderLayout());
        
        JLabel welcomeTitle = new JLabel("Selamat datang, " + namaMahasiswa + "!");
        welcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeTitle.setForeground(new Color(30, 74, 107));
        
        JLabel welcomeSub = new JLabel("Semester Genap 2025/2026 - Sistem dan Teknologi Informasi | Semester 4");
        welcomeSub.setFont(FONT_PLAIN_11);
        welcomeSub.setForeground(new Color(95, 127, 154));
        
        welcomePanel.add(welcomeTitle, BorderLayout.NORTH);
        welcomePanel.add(welcomeSub, BorderLayout.CENTER);
        
        gbc.gridy = 0;
        mainPanel.add(welcomePanel, gbc);
        
        // panel statistik
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 16, 0));
        statsPanel.setOpaque(false);
        
        totalSKSValue = new JLabel("0 SKS");
        totalSKSValue.setFont(FONT_BOLD_18);
        totalSKSValue.setForeground(Color.WHITE);
        
        statsPanel.add(createGradientStatCard("SKS Maksimal", "24 SKS", new Color(44, 125, 160), new Color(30, 74, 107)));
        statsPanel.add(createGradientStatCardForLabel("Total SKS Diambil", totalSKSValue, new Color(40, 167, 69), new Color(30, 120, 50)));
        statsPanel.add(createGradientStatCard("Dosen PA", namaDosenPA, new Color(100, 80, 160), new Color(70, 50, 130)));
        
        gbc.gridy = 1;
        mainPanel.add(statsPanel, gbc);
        
        // Menu Tombol Grid
        JPanel menuPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        menuPanel.add(createMenuButton("Pengajuan KRS", new Color(44, 125, 160), () -> {
            new PengajuanKRS(userId, nimMahasiswa, namaMahasiswa).setVisible(true);
            dispose();
        }));
        
        menuPanel.add(createMenuButton("History KRS", new Color(100, 80, 160), () -> {
            new HistoryKRSMahasiswa(userId, nimMahasiswa, namaMahasiswa).setVisible(true);
            dispose();
        }));
        
        menuPanel.add(createMenuButton("Jadwal Kuliah", new Color(40, 167, 69), () -> {
            new JadwalMahasiswa(userId, nimMahasiswa, namaMahasiswa).setVisible(true);
            dispose();
        }));
        
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(menuPanel, gbc);
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(240, 244, 248));
        
        add(navbarPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private RoundedButton createMenuButton(String text, Color baseColor, Runnable action) {
        RoundedButton btn = new RoundedButton(text, 15);
        btn.setBackground(baseColor);
        btn.setNormalColor(baseColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BOLD_16);
        btn.addActionListener(e -> action.run());
        return btn;
    }
    
    private JPanel createGradientStatCard(String title, String value, Color colorStart, Color colorEnd) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gradient = new GradientPaint(0, 0, colorStart, getWidth(), getHeight(), colorEnd);
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        card.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 0, 2, 0);
        gbc.weightx = 1.0;
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_PLAIN_12);
        titleLabel.setForeground(new Color(255, 255, 255, 200));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(FONT_BOLD_18);
        valueLabel.setForeground(Color.WHITE);
        
        gbc.gridy = 0;
        card.add(titleLabel, gbc);
        gbc.gridy = 1;
        card.add(valueLabel, gbc);
        
        return card;
    }
    
    private JPanel createGradientStatCardForLabel(String title, JLabel valueLabel, Color colorStart, Color colorEnd) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gradient = new GradientPaint(0, 0, colorStart, getWidth(), getHeight(), colorEnd);
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        card.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 0, 2, 0);
        gbc.weightx = 1.0;
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_PLAIN_12);
        titleLabel.setForeground(new Color(255, 255, 255, 200));
        
        gbc.gridy = 0;
        card.add(titleLabel, gbc);
        gbc.gridy = 1;
        card.add(valueLabel, gbc);
        
        return card;
    }
    
    private void loadSKSData() {
        int totalSKS = 0;
        String sql = "SELECT mk.sks, kd.status " +
                     "FROM krs " +
                     "JOIN krs_detail kd ON krs.krs_id = kd.krs_id " +
                     "JOIN jadwal j ON kd.jadwal_id = j.jadwal_id " +
                     "JOIN kelas k ON j.kelas_id = k.kelas_id " +
                     "JOIN mata_kuliah mk ON k.mk_id = mk.mk_id " +
                     "WHERE krs.nim = ?";
                     
        try (java.sql.Connection conn = connection.koneksi();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, nimMahasiswa);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String status = rs.getString("status");
                    if ("Approved".equals(status) || "Pending".equals(status)) {
                        totalSKS += rs.getInt("sks");
                    }
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        totalSKSValue.setText(totalSKS + " SKS");
    }
    
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new DashboardMahasiswa("001").setVisible(true); // example
        });
    }
}