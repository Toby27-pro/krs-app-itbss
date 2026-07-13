package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseFormFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class TambahProdi extends BaseFormFrame {

    private JTextField txtKodeProdi, txtNamaProdi;

    public TambahProdi() {
        super("Tambah Program Studi");
        
        txtKodeProdi = new JTextField(20);
        txtNamaProdi = new JTextField(20);
        
        addFormField("Kode Prodi", txtKodeProdi);
        addFormField("Nama Prodi", txtNamaProdi);
    }

    @Override
    protected void onSimpan() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO prodi (kode_prodi, nama_prodi) VALUES (?,?)")) {
            ps.setString(1, txtKodeProdi.getText());
            ps.setString(2, txtNamaProdi.getText());
            ps.executeUpdate();
            String nama = txtNamaProdi.getText();
            JOptionPane.showMessageDialog(this, "Prodi " + nama + " berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
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
