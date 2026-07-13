package com.mycompany.krs_sistem.dosen;

import com.mycompany.krs_sistem.auth.Login;
import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.RoundedButton;
import com.mycompany.krs_sistem.ui.RoundedPanel;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class DashboardDosen extends JFrame {

    // panel
    private JPanel navbarPanel;
    private JPanel mainPanel;
    
    // tulisan statistik
    private JLabel totalMahasiswaPAValue;
    private JLabel totalKelasValue;
    
    private String nip;
    private String namaDosen;
    
    // ukuran huruf
    private final Font FONT_BOLD_20 = new Font("Segoe UI", Font.BOLD, 20);
    private final Font FONT_BOLD_18 = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FONT_BOLD_16 = new Font("Segoe UI", Font.BOLD, 16);
    private final Font FONT_BOLD_12 = new Font("Segoe UI", Font.BOLD, 12);
    private final Font FONT_BOLD_11 = new Font("Segoe UI", Font.BOLD, 11);
    private final Font FONT_PLAIN_12 = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font FONT_PLAIN_11 = new Font("Segoe UI", Font.PLAIN, 11);

    public DashboardDosen(String nip, String namaDosen) {
        this.nip = nip;
        this.namaDosen = namaDosen;
        
        initComponents();
        loadStatistik();
    }
    
    private void initComponents() {
        setTitle("Dashboard Dosen");
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
        JLabel logoText = new JLabel("Dashboard Dosen");
        logoText.setFont(FONT_BOLD_20);
        logoText.setForeground(Color.WHITE);
        logoArea.add(logoText);
        
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        userInfo.setOpaque(false);
        JLabel userName = new JLabel(namaDosen + " (NIP: " + nip + ")");
        userName.setForeground(Color.WHITE);
        userName.setFont(FONT_BOLD_12);
        
        RoundedButton logoutBtn = new RoundedButton("Logout", 20);
        logoutBtn.setFont(FONT_BOLD_11);
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setNormalColor(new Color(231, 76, 60));
        logoutBtn.addActionListener(e -> {
            new Login().setVisible(true);
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
        
        JLabel welcomeTitle = new JLabel("Selamat datang, Bapak/Ibu " + namaDosen + "!");
        welcomeTitle.setFont(FONT_BOLD_18);
        welcomeTitle.setForeground(new Color(30, 74, 107));
        
        JLabel welcomeSub = new JLabel("Sistem Informasi Akademik Terpadu - Portal Dosen");
        welcomeSub.setFont(FONT_PLAIN_11);
        welcomeSub.setForeground(new Color(95, 127, 154));
        
        welcomePanel.add(welcomeTitle, BorderLayout.NORTH);
        welcomePanel.add(welcomeSub, BorderLayout.CENTER);
        
        gbc.gridy = 0;
        mainPanel.add(welcomePanel, gbc);
        
        // panel statistik
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 16, 0));
        statsPanel.setOpaque(false);
        
        totalMahasiswaPAValue = new JLabel("0 Mahasiswa");
        totalMahasiswaPAValue.setFont(FONT_BOLD_18);
        totalMahasiswaPAValue.setForeground(Color.WHITE);
        
        totalKelasValue = new JLabel("0 Kelas");
        totalKelasValue.setFont(FONT_BOLD_18);
        totalKelasValue.setForeground(Color.WHITE);
        
        statsPanel.add(createGradientStatCardForLabel("Mahasiswa Bimbingan Akademik (PA)", totalMahasiswaPAValue, new Color(44, 125, 160), new Color(30, 74, 107)));
        statsPanel.add(createGradientStatCardForLabel("Total Jadwal Mengajar (Kelas)", totalKelasValue, new Color(100, 80, 160), new Color(70, 50, 130)));
        
        gbc.gridy = 1;
        mainPanel.add(statsPanel, gbc);
        
        // Menu Tombol Grid
        JPanel menuPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        menuPanel.add(createMenuButton("Persetujuan KRS", new Color(44, 125, 160), () -> {
            new PersetujuanKRSDosen(nip, namaDosen).setVisible(true);
            dispose();
        }));
        
        menuPanel.add(createMenuButton("Mahasiswa Bimbingan", new Color(100, 80, 160), () -> {
            new MahasiswaPADosen(nip, namaDosen).setVisible(true);
            dispose();
        }));
        
        menuPanel.add(createMenuButton("Jadwal Mengajar", new Color(40, 167, 69), () -> {
            new JadwalMengajarDosen(nip, namaDosen).setVisible(true);
            dispose();
        }));
        
        menuPanel.add(createMenuButton("Cetak KRS Bimbingan", new Color(220, 53, 69), () -> {
            new CetakKRSBimbingan(nip, namaDosen).setVisible(true);
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
    
    private void loadStatistik() {
        int countPA = 0;
        int countKelas = 0;
        
        try (Connection conn = connection.koneksi()) {
            // Count Mahasiswa PA
            String sqlPA = "SELECT COUNT(*) FROM mahasiswa WHERE dosen_pa = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlPA)) {
                ps.setString(1, nip);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        countPA = rs.getInt(1);
                    }
                }
            }
            
            // Count Kelas (Jadwal)
            String sqlKelas = "SELECT COUNT(DISTINCT k.kelas_id) " +
                              "FROM kelas k " +
                              "JOIN jadwal j ON k.kelas_id = j.kelas_id " +
                              "WHERE k.nip = ?";
            try (PreparedStatement ps2 = conn.prepareStatement(sqlKelas)) {
                ps2.setString(1, nip);
                try (ResultSet rs2 = ps2.executeQuery()) {
                    if (rs2.next()) {
                        countKelas = rs2.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        totalMahasiswaPAValue.setText(countPA + " Mahasiswa");
        totalKelasValue.setText(countKelas + " Kelas");
    }
}
