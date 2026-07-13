package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseFormFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.sql.ResultSet;

public class TambahMatakuliah extends BaseFormFrame {

    private JTextField txtKodeMk, txtNamaMk, txtSks, txtSemester;
    private JComboBox<String> cbProdi;

    public TambahMatakuliah() {
        super("Tambah Mata Kuliah");
        
        txtKodeMk = new JTextField(20);
        txtNamaMk = new JTextField(20);
        txtSks = new JTextField(20);
        txtSemester = new JTextField(20);
        cbProdi = new JComboBox<>();
        loadProdi();
        
        addFormField("Kode MK", txtKodeMk);
        addFormField("Nama Mata Kuliah", txtNamaMk);
        addFormField("SKS", txtSks);
        addFormField("Semester", txtSemester);
        addFormField("Program Studi", cbProdi);
    }

    private void loadProdi() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("SELECT prodi_id, nama_prodi FROM prodi");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cbProdi.addItem(rs.getString("prodi_id") + " - " + rs.getString("nama_prodi"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSimpan() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO mata_kuliah (kode_mk, nama_mk, sks, semester, prodi_id) VALUES (?,?,?,?,?)")) {
            ps.setString(1, txtKodeMk.getText());
            ps.setString(2, txtNamaMk.getText());
            ps.setString(3, txtSks.getText());
            ps.setString(4, txtSemester.getText());
            
            String selectedProdi = (String) cbProdi.getSelectedItem();
            if (selectedProdi != null) {
                ps.setString(5, selectedProdi.split(" - ")[0]);
            } else {
                ps.setString(5, "");
            }
            
            ps.executeUpdate();
            String nama = txtNamaMk.getText();
            JOptionPane.showMessageDialog(this, "Mata Kuliah " + nama + " berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            onBatal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    @Override
    protected void onBatal() {
        new ListMatakuliah().setVisible(true);
        this.dispose();
    }
}
