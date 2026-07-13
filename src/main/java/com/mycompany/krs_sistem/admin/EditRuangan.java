package com.mycompany.krs_sistem.admin;
import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseFormFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
public class EditRuangan extends BaseFormFrame {
    private JTextField txtRuanganId, txtNamaRuangan, txtKapasitas;
    private String ruanganId;
    public EditRuangan(String ruanganId) {
        super("Edit Ruangan");
        this.ruanganId = ruanganId;
        txtNamaRuangan = new JTextField(20);
        txtKapasitas = new JTextField(20);
        addFormField("Nama Ruangan", txtNamaRuangan);
        addFormField("Kapasitas", txtKapasitas);
        loadData();
    }
    private void loadData() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM ruangan WHERE ruangan_id=?")) {
            ps.setString(1, ruanganId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtNamaRuangan.setText(rs.getString("nama_ruangan"));
                txtKapasitas.setText(rs.getString("kapasitas"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error load data: " + e.getMessage());
        }
    }
    @Override
    protected void onSimpan() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("UPDATE ruangan SET nama_ruangan=?, kapasitas=? WHERE ruangan_id=?")) {
            ps.setString(1, txtNamaRuangan.getText());
            ps.setString(2, txtKapasitas.getText());
            ps.setString(3, ruanganId);
            ps.executeUpdate();
            String nama = txtNamaRuangan.getText();
            JOptionPane.showMessageDialog(this, "Ruangan " + nama + " berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            onBatal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    @Override
    protected void onBatal() {
        new ListRuangan().setVisible(true);
        this.dispose();
    }
}
