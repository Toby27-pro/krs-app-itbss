package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseFormFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class EditMatakuliah extends BaseFormFrame {

    private JTextField txtKodeMk, txtNamaMk, txtSks, txtSemester;
    private JComboBox<String> cbProdi;
    private String mkId;

    public EditMatakuliah(String mkId) {
        super("Edit Mata Kuliah");
        this.mkId = mkId;
        
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
        
        loadData();
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

    public EditMatakuliah(String a, String b, String c, String d, String e) {
        this(a);
    }

    private void loadData() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("SELECT m.*, p.nama_prodi FROM mata_kuliah m LEFT JOIN prodi p ON m.prodi_id = p.prodi_id WHERE m.mk_id=?")) {
            ps.setString(1, mkId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtKodeMk.setText(rs.getString("kode_mk"));
                txtNamaMk.setText(rs.getString("nama_mk"));
                txtSks.setText(rs.getString("sks"));
                txtSemester.setText(rs.getString("semester"));
                String prodiId = rs.getString("prodi_id");
                String namaProdi = rs.getString("nama_prodi");
                if (prodiId != null) {
                    cbProdi.setSelectedItem(prodiId + " - " + namaProdi);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error load data: " + e.getMessage());
        }
    }

    @Override
    protected void onSimpan() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("UPDATE mata_kuliah SET kode_mk=?, nama_mk=?, sks=?, semester=?, prodi_id=? WHERE mk_id=?")) {
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
            
            ps.setString(6, mkId);
            ps.executeUpdate();
            String nama = txtNamaMk.getText();
            JOptionPane.showMessageDialog(this, "Mata Kuliah " + nama + " berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
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
