package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseFormFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.sql.ResultSet;

public class TambahJadwal extends BaseFormFrame {

    private JTextField txtJamMulai, txtJamSelesai;
    private JComboBox<String> cbKelas, cbRuangan, cbHari;

    public TambahJadwal() {
        super("Tambah Jadwal");
        
        txtJamMulai = new JTextField(20);
        txtJamSelesai = new JTextField(20);
        cbKelas = new JComboBox<>();
        cbRuangan = new JComboBox<>();
        cbHari = new JComboBox<>(new String[]{"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"});
        
        loadComboBoxes();
        
        addFormField("Kelas", cbKelas);
        addFormField("Ruangan", cbRuangan);
        addFormField("Hari", cbHari);
        addFormField("Jam Mulai (HH:MM)", txtJamMulai);
        addFormField("Jam Selesai (HH:MM)", txtJamSelesai);
    }

    private void loadComboBoxes() {
        try (Connection conn = connection.koneksi()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT k.kelas_id, m.nama_mk, k.nama_kelas FROM kelas k JOIN mata_kuliah m ON k.mk_id = m.mk_id");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) cbKelas.addItem(rs.getString("kelas_id") + " - " + rs.getString("nama_mk") + " (" + rs.getString("nama_kelas") + ")");
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT ruangan_id, nama_ruangan FROM ruangan");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) cbRuangan.addItem(rs.getString("ruangan_id") + " - " + rs.getString("nama_ruangan"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSimpan() {
        try (Connection conn = connection.koneksi()) {
            
            String kelas = (String) cbKelas.getSelectedItem();
            String kelasId = kelas != null ? kelas.split(" - ")[0].trim() : "";
            
            String ruangan = (String) cbRuangan.getSelectedItem();
            String ruanganId = ruangan != null ? ruangan.split(" - ")[0].trim() : "";
            
            String hari = cbHari.getSelectedItem().toString();
            String jamMulai = txtJamMulai.getText().trim();
            String jamSelesai = txtJamSelesai.getText().trim();
            
            if (jamMulai.isEmpty() || jamSelesai.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Jam mulai dan jam selesai tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Validasi 1: Cek bentrok dosen
            String sqlCekDosen = "SELECT COUNT(*) FROM jadwal j " +
                "JOIN kelas k ON j.kelas_id = k.kelas_id " +
                "WHERE k.nip = (SELECT nip FROM kelas WHERE kelas_id = ?) " +
                "AND j.hari = ? " +
                "AND j.jam_mulai < ? AND j.jam_selesai > ?";
            try (PreparedStatement psCek = conn.prepareStatement(sqlCekDosen)) {
                psCek.setString(1, kelasId);
                psCek.setString(2, hari);
                psCek.setString(3, jamSelesai);
                psCek.setString(4, jamMulai);
                ResultSet rsCek = psCek.executeQuery();
                if (rsCek.next() && rsCek.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "Dosen pada kelas ini sudah memiliki jadwal mengajar di waktu yang sama!\nHari: " + hari + ", Jam: " + jamMulai + " - " + jamSelesai, "Bentrok Dosen", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            // Validasi 2: Cek bentrok ruangan
            String sqlCekRuangan = "SELECT COUNT(*) FROM jadwal " +
                "WHERE ruangan_id = ? AND hari = ? " +
                "AND jam_mulai < ? AND jam_selesai > ?";
            try (PreparedStatement psCek = conn.prepareStatement(sqlCekRuangan)) {
                psCek.setString(1, ruanganId);
                psCek.setString(2, hari);
                psCek.setString(3, jamSelesai);
                psCek.setString(4, jamMulai);
                ResultSet rsCek = psCek.executeQuery();
                if (rsCek.next() && rsCek.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "Ruangan ini sudah digunakan di waktu yang sama!\nHari: " + hari + ", Jam: " + jamMulai + " - " + jamSelesai, "Bentrok Ruangan", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            // Semua validasi lolos, simpan jadwal
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO jadwal (kelas_id, ruangan_id, hari, jam_mulai, jam_selesai) VALUES (?,?,?,?,?)")) {
                ps.setString(1, kelasId);
                ps.setString(2, ruanganId);
                ps.setString(3, hari);
                ps.setString(4, jamMulai);
                ps.setString(5, jamSelesai);
                ps.executeUpdate();
            }
            
            String namaJadwal = kelas != null && kelas.contains(" - ") ? kelas.split(" - ")[1] : "terpilih";
            JOptionPane.showMessageDialog(this, "Jadwal kelas " + namaJadwal + " berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            onBatal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    @Override
    protected void onBatal() {
        new ListJadwal().setVisible(true);
        this.dispose();
    }
}
