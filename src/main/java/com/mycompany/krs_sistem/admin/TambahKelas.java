package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseFormFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.sql.ResultSet;

public class TambahKelas extends BaseFormFrame {

    private JTextField txtNamaKelas, txtKuota;
    private JComboBox<String> cbMk, cbDosen, cbTahun;

    public TambahKelas() {
        super("Tambah Kelas");
        
        txtNamaKelas = new JTextField(20);
        txtKuota = new JTextField(20);
        cbMk = new JComboBox<>();
        cbDosen = new JComboBox<>();
        cbTahun = new JComboBox<>();
        
        loadComboBoxes();
        
        addFormField("Mata Kuliah", cbMk);
        addFormField("Dosen", cbDosen);
        addFormField("Tahun Akademik", cbTahun);
        addFormField("Nama Kelas", txtNamaKelas);
        addFormField("Kuota", txtKuota);
    }

    private void loadComboBoxes() {
        try (Connection conn = connection.koneksi()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT mk_id, nama_mk FROM mata_kuliah");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) cbMk.addItem(rs.getString("mk_id") + " - " + rs.getString("nama_mk"));
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT nip, nama_lengkap FROM dosen");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) cbDosen.addItem(rs.getString("nip") + " - " + rs.getString("nama_lengkap"));
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT tahun_id, tahun_ajaran, semester FROM tahun_akademik");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) cbTahun.addItem(rs.getString("tahun_id") + " - " + rs.getString("tahun_ajaran") + " (" + rs.getString("semester") + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSimpan() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO kelas (mk_id, nip, tahun_id, nama_kelas, kuota, kuota_terisi) VALUES (?,?,?,?,?,0)")) {
            
            String mk = (String) cbMk.getSelectedItem();
            ps.setString(1, mk != null ? mk.split(" - ")[0] : "");
            
            String nip = (String) cbDosen.getSelectedItem();
            ps.setString(2, nip != null ? nip.split(" - ")[0] : "");
            
            String tahun = (String) cbTahun.getSelectedItem();
            ps.setString(3, tahun != null ? tahun.split(" - ")[0] : "");
            
            ps.setString(4, txtNamaKelas.getText());
            ps.setString(5, txtKuota.getText());
            
            ps.executeUpdate();
            String nama = txtNamaKelas.getText();
            JOptionPane.showMessageDialog(this, "Kelas " + nama + " berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            onBatal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    @Override
    protected void onBatal() {
        new ListKelas().setVisible(true);
        this.dispose();
    }
}
