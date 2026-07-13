package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseListFrame;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ListMatakuliah extends BaseListFrame {

    public ListMatakuliah() {
        super("Mata Kuliah", new String[]{"MK ID", "Kode MK", "Nama Mata Kuliah", "SKS", "Semester", "Program Studi"});
    }

    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = connection.koneksi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT m.*, p.nama_prodi FROM mata_kuliah m LEFT JOIN prodi p ON m.prodi_id = p.prodi_id")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("mk_id"),
                    rs.getString("kode_mk"),
                    rs.getString("nama_mk"),
                    rs.getString("sks"),
                    rs.getString("semester"),
                    rs.getString("nama_prodi")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    @Override
    protected void onTambah() {
        new TambahMatakuliah().setVisible(true);
        this.dispose();
    }

    @Override
    protected void onEdit() {
        String id = getSelectedId(0);
        if (id != null) {
            new EditMatakuliah(id).setVisible(true);
            this.dispose();
        }
    }

    @Override
    protected void onHapus() {
        String id = getSelectedId(0);
        String nama = getSelectedId(1);
        if (id != null && nama != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus matakuliah " + nama + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = connection.koneksi();
                     java.sql.PreparedStatement ps = conn.prepareStatement("DELETE FROM mata_kuliah WHERE mk_id=?")) {
                    ps.setString(1, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Data matakuliah " + nama + " berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
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
