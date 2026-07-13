package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseFormFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class TambahRuangan extends BaseFormFrame {

    private JTextField txtNamaRuangan, txtKapasitas;

    public TambahRuangan() {
        super("Tambah Ruangan");
        
        txtNamaRuangan = new JTextField(20);
        txtKapasitas = new JTextField(20);
        
        addFormField("Nama Ruangan", txtNamaRuangan);
        addFormField("Kapasitas", txtKapasitas);
    }

    @Override
    protected void onSimpan() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO ruangan (nama_ruangan, kapasitas) VALUES (?,?)")) {
            ps.setString(1, txtNamaRuangan.getText());
            ps.setString(2, txtKapasitas.getText());
            ps.executeUpdate();
            String nama = txtNamaRuangan.getText();
            JOptionPane.showMessageDialog(this, "Ruangan " + nama + " berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
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
