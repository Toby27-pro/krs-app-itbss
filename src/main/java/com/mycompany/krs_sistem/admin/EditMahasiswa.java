package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseFormFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class EditMahasiswa extends BaseFormFrame {

    private JTextField txtNama, txtTglLahir, txtAngkatan, txtSemesterAktif, txtMaxSks;
    private JComboBox<String> cbProdi, cbUser, cbDosenPa;
    private String nim;

    public EditMahasiswa(String nim) {
        super("Edit Mahasiswa");
        this.nim = nim;
        
        cbUser = new JComboBox<>();
        txtNama = new JTextField(20);
        cbProdi = new JComboBox<>();
        txtTglLahir = new JTextField(20);
        txtAngkatan = new JTextField(20);
        txtSemesterAktif = new JTextField(20);
        txtMaxSks = new JTextField(20);
        cbDosenPa = new JComboBox<>();
        
        loadComboBoxes();
        
        addFormField("Akun User", cbUser);
        addFormField("Nama Lengkap", txtNama);
        addFormField("Program Studi", cbProdi);
        addFormField("Tanggal Lahir (YYYY-MM-DD)", txtTglLahir);
        addFormField("Angkatan", txtAngkatan);
        addFormField("Semester Aktif", txtSemesterAktif);
        addFormField("Max SKS", txtMaxSks);
        addFormField("Dosen Pembimbing", cbDosenPa);
        
        loadData();
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

    public EditMahasiswa(String a, String b, String c, String d, String e, String f, String g, String h, String i) {
        this(a);
    }

    private void loadData() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("SELECT m.*, p.nama_prodi, u.username, d.nama_lengkap as nama_dosen FROM mahasiswa m LEFT JOIN prodi p ON m.prodi_id = p.prodi_id LEFT JOIN users u ON m.user_id = u.user_id LEFT JOIN dosen d ON m.dosen_pa = d.nip WHERE m.nim=?")) {
            ps.setString(1, nim);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String userId = rs.getString("user_id");
                String username = rs.getString("username");
                if (userId != null && username != null) {
                    cbUser.setSelectedItem(userId + " - " + username);
                }
                
                txtNama.setText(rs.getString("nama_lengkap"));
                
                String prodiId = rs.getString("prodi_id");
                String namaProdi = rs.getString("nama_prodi");
                if (prodiId != null) cbProdi.setSelectedItem(prodiId + " - " + namaProdi);
                
                txtTglLahir.setText(rs.getString("tanggal_lahir"));
                txtAngkatan.setText(rs.getString("angkatan"));
                txtSemesterAktif.setText(rs.getString("semester_aktif"));
                txtMaxSks.setText(rs.getString("max_sks"));
                
                String dosenPa = rs.getString("dosen_pa");
                String namaDosen = rs.getString("nama_dosen");
                if (dosenPa != null && namaDosen != null) {
                    cbDosenPa.setSelectedItem(dosenPa + " - " + namaDosen);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error load data: " + e.getMessage());
        }
    }

    @Override
    protected void onSimpan() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("UPDATE mahasiswa SET user_id=?, nama_lengkap=?, prodi_id=?, tanggal_lahir=?, angkatan=?, semester_aktif=?, max_sks=?, dosen_pa=? WHERE nim=?")) {
            
            String selectedUser = (String) cbUser.getSelectedItem();
            if (selectedUser != null && selectedUser.contains(" - ")) {
                ps.setString(1, selectedUser.split(" - ")[0]);
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            
            ps.setString(2, txtNama.getText());
            
            String selectedProdi = (String) cbProdi.getSelectedItem();
            ps.setString(3, selectedProdi != null ? selectedProdi.split(" - ")[0] : "");
            
            ps.setString(4, txtTglLahir.getText());
            ps.setString(5, txtAngkatan.getText());
            ps.setString(6, txtSemesterAktif.getText());
            ps.setString(7, txtMaxSks.getText());
            
            String selectedDosen = (String) cbDosenPa.getSelectedItem();
            if (selectedDosen != null && selectedDosen.contains(" - ")) {
                ps.setString(8, selectedDosen.split(" - ")[0]);
            } else {
                ps.setString(8, null);
            }
            
            ps.setString(9, nim);
            ps.executeUpdate();
            
            String nama = txtNama.getText();
            JOptionPane.showMessageDialog(this, "Mahasiswa " + nama + " berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
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
