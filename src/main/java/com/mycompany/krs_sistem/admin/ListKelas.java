package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseListFrame;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ListKelas extends BaseListFrame {

    public ListKelas() {
        super("Kelas", new String[]{"", "Mata Kuliah", "Dosen", "T. Akademik", "Nama Kelas", "Kuota", "Terisi"});
    }

    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = connection.koneksi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT k.kelas_id, m.nama_mk, d.nama_lengkap, t.tahun_ajaran, t.semester, k.nama_kelas, k.kuota, k.kuota_terisi FROM kelas k LEFT JOIN mata_kuliah m ON k.mk_id = m.mk_id LEFT JOIN dosen d ON k.nip = d.nip LEFT JOIN tahun_akademik t ON k.tahun_id = t.tahun_id")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("kelas_id"),
                    rs.getString("nama_mk"),
                    rs.getString("nama_lengkap"),
                    rs.getString("tahun_ajaran") + " - " + rs.getString("semester"),
                    rs.getString("nama_kelas"),
                    rs.getString("kuota"),
                    rs.getString("kuota_terisi")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    @Override
    protected void onTambah() {
        new TambahKelas().setVisible(true);
        this.dispose();
    }

    @Override
    protected void onEdit() {
        String id = getSelectedId(0);
        if (id != null) {
            new EditKelas(id).setVisible(true);
            this.dispose();
        }
    }

    @Override
    protected void onHapus() {
        String id = getSelectedId(0);
        String nama = getSelectedId(1);
        if (id != null && nama != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus kelas " + nama + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = connection.koneksi();
                     java.sql.PreparedStatement ps = conn.prepareStatement("DELETE FROM kelas WHERE kelas_id=?")) {
                    ps.setString(1, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Data kelas " + nama + " berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                }
            }
        }
    }

    @Override
    protected void onDashboard() {
        new DashboardAdmin().setVisible(true);
        this.dispose();
    }
}
