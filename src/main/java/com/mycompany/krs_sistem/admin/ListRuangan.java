package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseListFrame;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ListRuangan extends BaseListFrame {

    public ListRuangan() {
        super("Ruangan", new String[]{"Ruangan ID", "Nama Ruangan", "Kapasitas"});
    }

    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = connection.koneksi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM ruangan")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("ruangan_id"),
                    rs.getString("nama_ruangan"),
                    rs.getString("kapasitas")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    @Override
    protected void onTambah() {
        new TambahRuangan().setVisible(true);
        this.dispose();
    }

    @Override
    protected void onEdit() {
        String id = getSelectedId(0);
        if (id != null) {
            new EditRuangan(id).setVisible(true);
            this.dispose();
        }
    }

    @Override
    protected void onHapus() {
        String id = getSelectedId(0);
        String nama = getSelectedId(1);
        if (id != null && nama != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus ruangan " + nama + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = connection.koneksi();
                     java.sql.PreparedStatement ps = conn.prepareStatement("DELETE FROM ruangan WHERE ruangan_id=?")) {
                    ps.setString(1, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Data ruangan " + nama + " berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
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
