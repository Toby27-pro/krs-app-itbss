package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseListFrame;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ListTahunAkademik extends BaseListFrame {

    public ListTahunAkademik() {
        super("Tahun Akademik", new String[]{"Tahun ID", "Tahun Ajaran", "Semester", "Status Aktif"});
    }

    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = connection.koneksi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM tahun_akademik")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("tahun_id"),
                    rs.getString("tahun_ajaran"),
                    rs.getString("semester"),
                    rs.getBoolean("status_aktif") ? "Aktif" : "Tidak Aktif"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    @Override
    protected void onTambah() {
        new TambahTahunAkademik().setVisible(true);
        this.dispose();
    }

    @Override
    protected void onEdit() {
        String id = getSelectedId(0);
        if (id != null) {
            new EditTahunAkademik(id).setVisible(true);
            this.dispose();
        }
    }

    @Override
    protected void onHapus() {
        String id = getSelectedId(0);
        String nama = getSelectedId(1);
        if (id != null && nama != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus tahun akademik " + nama + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = connection.koneksi();
                     java.sql.PreparedStatement ps = conn.prepareStatement("DELETE FROM tahun_akademik WHERE ta_id=?")) {
                    ps.setString(1, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Data tahun akademik " + nama + " berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
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
