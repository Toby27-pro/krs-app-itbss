package com.mycompany.krs_sistem.auth;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.admin.DashboardAdmin;
import com.mycompany.krs_sistem.dosen.DashboardDosen;
import com.mycompany.krs_sistem.mahasiswa.DashboardMahasiswa;
import com.mycompany.krs_sistem.ui.RoundedButton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame {
    
    private JTextField emailField;
    private JPasswordField passwordField;

    public Login() {
        setTitle("LOGIN");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // (Logo and Branding)
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(240, 244, 248));
        GridBagConstraints gbcR = new GridBagConstraints();
        gbcR.gridx = 0; gbcR.gridy = 0; gbcR.insets = new Insets(10, 10, 10, 10);
        
        try {
            java.net.URL imgUrl = getClass().getResource("/Icon/logo.png");
            if (imgUrl != null) {
                javax.swing.ImageIcon icon = new javax.swing.ImageIcon(imgUrl);
                java.awt.Image image = icon.getImage(); 
                java.awt.Image newimg = image.getScaledInstance(150, 150,  java.awt.Image.SCALE_SMOOTH); 
                icon = new javax.swing.ImageIcon(newimg);
                JLabel logoImageLabel = new JLabel(icon);
                rightPanel.add(logoImageLabel, gbcR);
                gbcR.gridy++;
            }
        } catch (Exception ex) {
            // ignore if failed to load image
        }
        
        JLabel titleLabel = new JLabel("KRS ITBSS");
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 32));
        titleLabel.setForeground(new Color(0, 153, 153));
        rightPanel.add(titleLabel, gbcR);
        
        gbcR.gridy++;
        JLabel logoLabel = new JLabel("Sistem Informasi Akademik");
        logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        rightPanel.add(logoLabel, gbcR);

        // Left side (Login Form)
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.gridx = 0; gbcL.gridy = 0; gbcL.insets = new Insets(10, 10, 10, 10);
        gbcL.anchor = GridBagConstraints.WEST;

        JLabel loginLabel = new JLabel("LOGIN");
        loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        loginLabel.setForeground(new Color(0, 153, 153));
        leftPanel.add(loginLabel, gbcL);

        gbcL.gridy++;
        leftPanel.add(new JLabel("Email"), gbcL);

        gbcL.gridy++;
        emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(250, 35));
        leftPanel.add(emailField, gbcL);

        gbcL.gridy++;
        leftPanel.add(new JLabel("Password"), gbcL);

        gbcL.gridy++;
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(250, 35));
        leftPanel.add(passwordField, gbcL);

        gbcL.gridy++;
        RoundedButton btnLogin = new RoundedButton("Login", 15);
        btnLogin.setBackground(new Color(0, 153, 153));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(100, 35));
        btnLogin.addActionListener(e -> cekLogin());
        leftPanel.add(btnLogin, gbcL);

        add(rightPanel);
        add(leftPanel);
    }
    
    private void cekLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email dan Password tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("SELECT u.*, r.nama_role as role FROM users u JOIN roles r ON u.role_id = r.role_id WHERE u.email = ? AND u.password = ?")) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                String userId = rs.getString("user_id");
                
                // Store the current user ID in the session
                Session.setCurrentUserId(userId);
                
                JOptionPane.showMessageDialog(this, "Login Berhasil! Selamat datang, " + rs.getString("username") + "!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
                if ("Admin".equals(role)) {
                    new DashboardAdmin().setVisible(true);
                } else if ("Dosen".equals(role) || "Kaprodi".equals(role)) {
                    // Fetch Dosen nip and name
                    String nip = "";
                    String namaDosen = "Dosen";
                    try (PreparedStatement ps2 = conn.prepareStatement("SELECT nip, nama_lengkap FROM dosen WHERE user_id=?")) {
                        ps2.setString(1, userId);
                        ResultSet rs2 = ps2.executeQuery();
                        if (rs2.next()) {
                            nip = rs2.getString("nip");
                            namaDosen = rs2.getString("nama_lengkap");
                        }
                    }
                    new DashboardDosen(nip, namaDosen).setVisible(true);
                } else if ("Mahasiswa".equals(role)) {
                    new DashboardMahasiswa(userId).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Role tidak dikenali!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Email atau Password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error : " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}