package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseListFrame;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ListDosen extends BaseListFrame {

    public ListDosen() {
        super("Dosen", new String[]{"NIP", "Nama Lengkap", "Email", "Jabatan"});
    }

    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = connection.koneksi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT d.*, u.email FROM dosen d JOIN users u ON d.user_id = u.user_id")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("nip"),
                    rs.getString("nama_lengkap"),
                    rs.getString("email"),
                    rs.getString("jabatan")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    @Override
    protected void onTambah() {
        new TambahDosen().setVisible(true);
        this.dispose();
    }

    @Override
    protected void onEdit() {
        String id = getSelectedId(0);
        if (id != null) {
            new EditDosen(id).setVisible(true);
            this.dispose();
        }
    }

    @Override
    protected void onHapus() {
        String id = getSelectedId(0);
        String nama = getSelectedId(1);
        if (id != null && nama != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus dosen " + nama + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = connection.koneksi();
                     java.sql.PreparedStatement ps = conn.prepareStatement("DELETE FROM dosen WHERE nip=?")) {
                    ps.setString(1, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Data dosen " + nama + " berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
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
