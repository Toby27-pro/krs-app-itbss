package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseListFrame;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ListJadwal extends BaseListFrame {

    public ListJadwal() {
        super("Jadwal", new String[]{"", "Mata Kuliah", "Kelas", "Ruangan", "Hari", "Jam Mulai", "Jam Selesai"});
    }

    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = connection.koneksi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT j.jadwal_id, m.nama_mk, k.nama_kelas, r.nama_ruangan, j.hari, j.jam_mulai, j.jam_selesai FROM jadwal j LEFT JOIN kelas k ON j.kelas_id = k.kelas_id LEFT JOIN mata_kuliah m ON k.mk_id = m.mk_id LEFT JOIN ruangan r ON j.ruangan_id = r.ruangan_id")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("jadwal_id"),
                    rs.getString("nama_mk"),
                    rs.getString("nama_kelas"),
                    rs.getString("nama_ruangan"),
                    rs.getString("hari"),
                    rs.getString("jam_mulai"),
                    rs.getString("jam_selesai")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    @Override
    protected void onTambah() {
        new TambahJadwal().setVisible(true);
        this.dispose();
    }

    @Override
    protected void onEdit() {
        String id = getSelectedId(0);
        if (id != null) {
            new EditJadwal(id).setVisible(true);
            this.dispose();
        }
    }

    @Override
    protected void onHapus() {
        String id = getSelectedId(0);
        String nama = getSelectedId(1);
        if (id != null && nama != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus jadwal " + nama + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = connection.koneksi();
                     java.sql.PreparedStatement ps = conn.prepareStatement("DELETE FROM jadwal WHERE jadwal_id=?")) {
                    ps.setString(1, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Data jadwal " + nama + " berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
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
