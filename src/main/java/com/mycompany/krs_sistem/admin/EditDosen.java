package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseFormFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class EditDosen extends BaseFormFrame {

    private JTextField txtNama;
    private JComboBox<String> cbUser, cbJabatan;
    private String nip;

    public EditDosen(String nip) {
        super("Edit Dosen");
        this.nip = nip;
        
        txtNama = new JTextField(20);
        cbUser = new JComboBox<>();
        cbJabatan = new JComboBox<>(new String[]{"Dosen", "Kaprodi"});
        
        loadUsers();
        
        addFormField("Akun User", cbUser);
        addFormField("Nama Lengkap", txtNama);
        addFormField("Jabatan", cbJabatan);
        
        loadData();
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

    public EditDosen(String a, String b, String c, String d) {
        this(a);
    }

    private void loadData() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("SELECT d.*, u.username FROM dosen d LEFT JOIN users u ON d.user_id = u.user_id WHERE d.nip=?")) {
            ps.setString(1, nip);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String userId = rs.getString("user_id");
                String username = rs.getString("username");
                if (userId != null && username != null) {
                    cbUser.setSelectedItem(userId + " - " + username);
                }
                
                txtNama.setText(rs.getString("nama_lengkap"));
                cbJabatan.setSelectedItem(rs.getString("jabatan"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error load data: " + e.getMessage());
        }
    }

    @Override
    protected void onSimpan() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("UPDATE dosen SET user_id=?, nama_lengkap=?, jabatan=? WHERE nip=?")) {
            
            String selectedUser = (String) cbUser.getSelectedItem();
            if (selectedUser != null && selectedUser.contains(" - ")) {
                ps.setString(1, selectedUser.split(" - ")[0]);
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            
            ps.setString(2, txtNama.getText());
            ps.setString(3, cbJabatan.getSelectedItem().toString());
            ps.setString(4, nip);
            ps.executeUpdate();
            
            String nama = txtNama.getText();
            JOptionPane.showMessageDialog(this, "Dosen " + nama + " berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
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
