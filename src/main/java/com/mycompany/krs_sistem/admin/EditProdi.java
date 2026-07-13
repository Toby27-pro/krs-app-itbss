package com.mycompany.krs_sistem.admin;
import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseFormFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
public class EditProdi extends BaseFormFrame {
    private JTextField txtProdiId, txtKodeProdi, txtNamaProdi;
    private String prodiId;
    public EditProdi(String prodiId) {
        super("Edit Program Studi");
        this.prodiId = prodiId;
        txtKodeProdi = new JTextField(20);
        txtNamaProdi = new JTextField(20);
        addFormField("Kode Prodi", txtKodeProdi);
        addFormField("Nama Prodi", txtNamaProdi);
        loadData();
    }
    // backward compatibility
    public EditProdi(String a, String b, String c) {
        this(a);
    }
    private void loadData() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM prodi WHERE prodi_id=?")) {
            ps.setString(1, prodiId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtKodeProdi.setText(rs.getString("kode_prodi"));
                txtNamaProdi.setText(rs.getString("nama_prodi"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error load data: " + e.getMessage());
        }
    }
    @Override
    protected void onSimpan() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("UPDATE prodi SET kode_prodi=?, nama_prodi=? WHERE prodi_id=?")) {
            ps.setString(1, txtKodeProdi.getText());
            ps.setString(2, txtNamaProdi.getText());
            ps.setString(3, prodiId);
            ps.executeUpdate();
            String nama = txtNamaProdi.getText();
            JOptionPane.showMessageDialog(this, "Prodi " + nama + " berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            onBatal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    @Override
    protected void onBatal() {
        new ListProdi().setVisible(true);
        this.dispose();
    }
}
