package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseFormFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class EditJadwal extends BaseFormFrame {
    private JTextField txtJamMulai, txtJamSelesai;
    private JComboBox<String> cbKelas, cbRuangan, cbHari;
    private String jadwalId;

    public EditJadwal(String jadwalId) {
        super("Edit Jadwal");
        this.jadwalId = jadwalId;
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
        loadData();
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

    public EditJadwal(String a, String b, String c, String d, String e, String f, String g, String h) {
        this(a);
    }

    private void loadData() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("SELECT j.*, m.nama_mk, k.nama_kelas, r.nama_ruangan FROM jadwal j LEFT JOIN kelas k ON j.kelas_id=k.kelas_id LEFT JOIN mata_kuliah m ON k.mk_id=m.mk_id LEFT JOIN ruangan r ON j.ruangan_id=r.ruangan_id WHERE j.jadwal_id=?")) {
            ps.setString(1, jadwalId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String kelasId = rs.getString("kelas_id");
                String namaMk = rs.getString("nama_mk");
                String namaKelas = rs.getString("nama_kelas");
                if (kelasId != null) cbKelas.setSelectedItem(kelasId + " - " + namaMk + " (" + namaKelas + ")");
                String ruanganId = rs.getString("ruangan_id");
                String namaRuangan = rs.getString("nama_ruangan");
                if (ruanganId != null) cbRuangan.setSelectedItem(ruanganId + " - " + namaRuangan);
                cbHari.setSelectedItem(rs.getString("hari"));
                txtJamMulai.setText(rs.getString("jam_mulai"));
                txtJamSelesai.setText(rs.getString("jam_selesai"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error load data: " + e.getMessage());
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
            
            // Validasi 1: Cek bentrok dosen (kecuali jadwal ini sendiri)
            String sqlCekDosen = "SELECT COUNT(*) FROM jadwal j " +
                "JOIN kelas k ON j.kelas_id = k.kelas_id " +
                "WHERE k.nip = (SELECT nip FROM kelas WHERE kelas_id = ?) " +
                "AND j.hari = ? " +
                "AND j.jam_mulai < ? AND j.jam_selesai > ? " +
                "AND j.jadwal_id != ?";
            try (PreparedStatement psCek = conn.prepareStatement(sqlCekDosen)) {
                psCek.setString(1, kelasId);
                psCek.setString(2, hari);
                psCek.setString(3, jamSelesai);
                psCek.setString(4, jamMulai);
                psCek.setString(5, jadwalId);
                ResultSet rsCek = psCek.executeQuery();
                if (rsCek.next() && rsCek.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "Dosen pada kelas ini sudah memiliki jadwal mengajar di waktu yang sama!\nHari: " + hari + ", Jam: " + jamMulai + " - " + jamSelesai, "Bentrok Dosen", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            // Validasi 2: Cek bentrok ruangan (kecuali jadwal ini sendiri)
            String sqlCekRuangan = "SELECT COUNT(*) FROM jadwal " +
                "WHERE ruangan_id = ? AND hari = ? " +
                "AND jam_mulai < ? AND jam_selesai > ? " +
                "AND jadwal_id != ?";
            try (PreparedStatement psCek = conn.prepareStatement(sqlCekRuangan)) {
                psCek.setString(1, ruanganId);
                psCek.setString(2, hari);
                psCek.setString(3, jamSelesai);
                psCek.setString(4, jamMulai);
                psCek.setString(5, jadwalId);
                ResultSet rsCek = psCek.executeQuery();
                if (rsCek.next() && rsCek.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "Ruangan ini sudah digunakan di waktu yang sama!\nHari: " + hari + ", Jam: " + jamMulai + " - " + jamSelesai, "Bentrok Ruangan", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            // Semua validasi lolos, update jadwal
            try (PreparedStatement ps = conn.prepareStatement("UPDATE jadwal SET kelas_id=?, ruangan_id=?, hari=?, jam_mulai=?, jam_selesai=? WHERE jadwal_id=?")) {
                ps.setString(1, kelasId);
                ps.setString(2, ruanganId);
                ps.setString(3, hari);
                ps.setString(4, jamMulai);
                ps.setString(5, jamSelesai);
                ps.setString(6, jadwalId);
                ps.executeUpdate();
            }
            
            String namaJadwal = kelas != null && kelas.contains(" - ") ? kelas.split(" - ")[1] : "terpilih";
            JOptionPane.showMessageDialog(this, "Jadwal kelas " + namaJadwal + " berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
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
