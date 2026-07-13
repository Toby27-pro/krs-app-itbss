package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseFormFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class TambahMahasiswa extends BaseFormFrame {

    private JTextField txtNim, txtNama, txtTglLahir, txtAngkatan, txtSemesterAktif, txtMaxSks;
    private JComboBox<String> cbProdi, cbUser, cbDosenPa;

    public TambahMahasiswa() {
        super("Tambah Mahasiswa");
        
        txtNim = new JTextField(20);
        cbUser = new JComboBox<>();
        txtNama = new JTextField(20);
        cbProdi = new JComboBox<>();
        txtTglLahir = new JTextField(20);
        txtAngkatan = new JTextField(20);
        txtSemesterAktif = new JTextField(20);
        txtMaxSks = new JTextField(20);
        cbDosenPa = new JComboBox<>();
        
        loadComboBoxes();
        
        addFormField("NIM", txtNim);
        addFormField("Akun User", cbUser);
        addFormField("Nama Lengkap", txtNama);
        addFormField("Program Studi", cbProdi);
        addFormField("Tanggal Lahir (YYYY-MM-DD)", txtTglLahir);
        addFormField("Angkatan", txtAngkatan);
        addFormField("Semester Aktif", txtSemesterAktif);
        addFormField("Max SKS", txtMaxSks);
        addFormField("Dosen Pembimbing", cbDosenPa);
    }

    private void loadComboBoxes() {
        try (Connection conn = connection.koneksi()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT prodi_id, nama_prodi FROM prodi");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) cbProdi.addItem(rs.getString("prodi_id") + " - " + rs.getString("nama_prodi"));
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT user_id, username FROM users WHERE role_id = 2");
                 ResultSet rs = ps.executeQuery()) {
                cbUser.addItem("");
                while (rs.next()) cbUser.addItem(rs.getString("user_id") + " - " + rs.getString("username"));
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT nip, nama_lengkap FROM dosen");
                 ResultSet rs = ps.executeQuery()) {
                cbDosenPa.addItem("");
                while (rs.next()) cbDosenPa.addItem(rs.getString("nip") + " - " + rs.getString("nama_lengkap"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSimpan() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO mahasiswa (nim, user_id, nama_lengkap, prodi_id, tanggal_lahir, angkatan, semester_aktif, max_sks, dosen_pa) VALUES (?,?,?,?,?,?,?,?,?)")) {
            
            ps.setString(1, txtNim.getText());
            
            String selectedUser = (String) cbUser.getSelectedItem();
            if (selectedUser != null && selectedUser.contains(" - ")) {
                ps.setString(2, selectedUser.split(" - ")[0]);
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            
            ps.setString(3, txtNama.getText());
            
            String selectedProdi = (String) cbProdi.getSelectedItem();
            ps.setString(4, selectedProdi != null ? selectedProdi.split(" - ")[0] : "");
            
            ps.setString(5, txtTglLahir.getText());
            ps.setString(6, txtAngkatan.getText());
            ps.setString(7, txtSemesterAktif.getText());
            ps.setString(8, txtMaxSks.getText());
            
            String selectedDosen = (String) cbDosenPa.getSelectedItem();
            if (selectedDosen != null && selectedDosen.contains(" - ")) {
                ps.setString(9, selectedDosen.split(" - ")[0]);
            } else {
                ps.setString(9, null);
            }
            
            ps.executeUpdate();
            
            String nama = txtNama.getText();
            JOptionPane.showMessageDialog(this, "Mahasiswa " + nama + " berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            onBatal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    @Override
    protected void onBatal() {
        new ListMahasiswa().setVisible(true);
        this.dispose();
    }
}
