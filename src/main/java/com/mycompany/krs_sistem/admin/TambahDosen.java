package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseFormFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class TambahDosen extends BaseFormFrame {

    private JTextField txtNip, txtNama;
    private JComboBox<String> cbUser, cbJabatan;

    public TambahDosen() {
        super("Tambah Dosen");
        
        txtNip = new JTextField(20);
        cbUser = new JComboBox<>();
        txtNama = new JTextField(20);
        cbJabatan = new JComboBox<>(new String[]{"Dosen", "Kaprodi"});
        
        loadUsers();
        
        addFormField("NIP", txtNip);
        addFormField("Akun User", cbUser);
        addFormField("Nama Lengkap", txtNama);
        addFormField("Jabatan", cbJabatan);
    }

    private void loadUsers() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("SELECT user_id, username FROM users WHERE role_id = 3");
             ResultSet rs = ps.executeQuery()) {
            cbUser.addItem("");
            while (rs.next()) {
                cbUser.addItem(rs.getString("user_id") + " - " + rs.getString("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSimpan() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO dosen (nip, user_id, nama_lengkap, jabatan) VALUES (?,?,?,?)")) {
            ps.setString(1, txtNip.getText());
            
            String selectedUser = (String) cbUser.getSelectedItem();
            if (selectedUser != null && selectedUser.contains(" - ")) {
                ps.setString(2, selectedUser.split(" - ")[0]);
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            
            ps.setString(3, txtNama.getText());
            ps.setString(4, cbJabatan.getSelectedItem().toString());
            ps.executeUpdate();
            
            String nama = txtNama.getText();
            JOptionPane.showMessageDialog(this, "Dosen " + nama + " berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            onBatal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    @Override
    protected void onBatal() {
        new ListDosen().setVisible(true);
        this.dispose();
    }
}
