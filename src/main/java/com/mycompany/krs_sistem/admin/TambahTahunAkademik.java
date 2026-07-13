package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseFormFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class TambahTahunAkademik extends BaseFormFrame {

    private JTextField txtTahunAjaran;
    private JComboBox<String> cbSemester;
    private JComboBox<String> cbStatusAktif;

    public TambahTahunAkademik() {
        super("Tambah Tahun Akademik");
        
        txtTahunAjaran = new JTextField(20);
        cbSemester = new JComboBox<>(new String[]{"Ganjil", "Genap"});
        cbStatusAktif = new JComboBox<>(new String[]{"Aktif", "Tidak Aktif"});
        
        addFormField("Tahun Ajaran", txtTahunAjaran);
        addFormField("Semester", cbSemester);
        addFormField("Status Aktif", cbStatusAktif);
    }

    @Override
    protected void onSimpan() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO tahun_akademik (tahun_ajaran, semester, status_aktif) VALUES (?,?,?)")) {
            ps.setString(1, txtTahunAjaran.getText());
            ps.setString(2, cbSemester.getSelectedItem().toString());
            ps.setBoolean(3, cbStatusAktif.getSelectedItem().toString().equals("Aktif"));
            ps.executeUpdate();
            String nama = txtTahunAjaran.getText();
            JOptionPane.showMessageDialog(this, "Tahun Akademik " + nama + " berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            onBatal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    @Override
    protected void onBatal() {
        new ListTahunAkademik().setVisible(true);
        this.dispose();
    }
}
