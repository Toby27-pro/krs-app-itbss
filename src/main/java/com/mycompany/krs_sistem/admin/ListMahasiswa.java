package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseListFrame;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ListMahasiswa extends BaseListFrame {

    public ListMahasiswa() {
        super("Mahasiswa", new String[]{"NIM", "Nama Lengkap", "Program Studi", "Angkatan", "Semester Aktif", "SKS Max", "Email", "Dosen PA"});
    }

    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = connection.koneksi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT m.*, p.nama_prodi, u.email FROM mahasiswa m LEFT JOIN prodi p ON m.prodi_id = p.prodi_id JOIN users u ON m.user_id = u.user_id")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("nim"),
                    rs.getString("nama_lengkap"),
                    rs.getString("nama_prodi"),
                    rs.getString("angkatan"),
                    rs.getString("semester_aktif"),
                    rs.getString("max_sks"),
                    rs.getString("email"),
                    rs.getString("dosen_pa")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    @Override
    protected void onTambah() {
        new TambahMahasiswa().setVisible(true);
        this.dispose();
    }

    @Override
    protected void onEdit() {
        String id = getSelectedId(0);
        if (id != null) {
            new EditMahasiswa(id).setVisible(true);
            this.dispose();
        }
    }

    @Override
    protected void onHapus() {
        String id = getSelectedId(0);
        String nama = getSelectedId(1);
        if (id != null && nama != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus mahasiswa " + nama + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = connection.koneksi();
                     java.sql.PreparedStatement ps = conn.prepareStatement("DELETE FROM mahasiswa WHERE nim=?")) {
                    ps.setString(1, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Data mahasiswa " + nama + " berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
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
