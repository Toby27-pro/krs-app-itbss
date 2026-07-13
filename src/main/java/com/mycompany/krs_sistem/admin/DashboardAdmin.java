package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.ui.RoundedButton;
import com.mycompany.krs_sistem.auth.Login;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class DashboardAdmin extends JFrame {

    public DashboardAdmin() {
        setTitle("Dashboard Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(0, 153, 153));
        JLabel title = new JLabel("DASHBOARD ADMIN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        headerPanel.add(title);
        add(headerPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(4, 3, 20, 20));
        gridPanel.setBorder(new EmptyBorder(30, 50, 30, 50));
        gridPanel.setBackground(new Color(240, 244, 248));

        gridPanel.add(createMenuButton("Kelola User", () -> { new ListUser().setVisible(true); dispose(); }));
        gridPanel.add(createMenuButton("Kelola Dosen", () -> { new ListDosen().setVisible(true); dispose(); }));
        gridPanel.add(createMenuButton("Kelola Mahasiswa", () -> { new ListMahasiswa().setVisible(true); dispose(); }));
        gridPanel.add(createMenuButton("Kelola Prodi", () -> { new ListProdi().setVisible(true); dispose(); }));
        gridPanel.add(createMenuButton("Kelola Matakuliah", () -> { new ListMatakuliah().setVisible(true); dispose(); }));
        gridPanel.add(createMenuButton("Kelola Kelas", () -> { new ListKelas().setVisible(true); dispose(); }));
        gridPanel.add(createMenuButton("Kelola Jadwal", () -> { new ListJadwal().setVisible(true); dispose(); }));
        gridPanel.add(createMenuButton("Kelola Tahun Akademik", () -> { new ListTahunAkademik().setVisible(true); dispose(); }));
        gridPanel.add(createMenuButton("Kelola Ruangan", () -> { new ListRuangan().setVisible(true); dispose(); }));
        gridPanel.add(createMenuButton("Cetak Semua KRS", () -> { new CetakKRSAdmin().setVisible(true); dispose(); }));

        RoundedButton logoutBtn = new RoundedButton("Logout", 15);
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoutBtn.addActionListener(e -> { new Login().setVisible(true); dispose(); });
        gridPanel.add(logoutBtn);

        add(gridPanel, BorderLayout.CENTER);
    }

    private RoundedButton createMenuButton(String text, Runnable action) {
        RoundedButton btn = new RoundedButton(text, 15);
        btn.setNormalColor(Color.WHITE);
        btn.setHoverColor(new Color(230, 240, 245));
        btn.setForeground(new Color(0, 153, 153));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 153), 2));
        btn.addActionListener(e -> action.run());
        return btn;
    }
}
