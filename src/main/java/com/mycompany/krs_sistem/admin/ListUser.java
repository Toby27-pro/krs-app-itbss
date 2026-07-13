package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseListFrame;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ListUser extends BaseListFrame {

    public ListUser() {
        super("User", new String[]{"User ID", "Username", "Email", "Status"});
    }

    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = connection.koneksi();
             Statement stmt = conn.createStatement()) {
            
            String currentUserId = com.mycompany.krs_sistem.auth.Session.getCurrentUserId();
            String sql = "SELECT u.user_id, u.username, r.nama_role as role, u.email FROM users u JOIN roles r ON u.role_id = r.role_id";
            if (currentUserId != null) {
                sql += " WHERE u.user_id != " + currentUserId;
            }
            
            try (ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("user_id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("role"),
                });
            }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    @Override
    protected void onTambah() {
        new TambahUser().setVisible(true);
        this.dispose();
    }

    @Override
    protected void onEdit() {
        String id = getSelectedId(0);
        if (id != null) {
            new EditUser(id).setVisible(true);
            this.dispose();
        }
    }

    @Override
    protected void onHapus() {
        String id = getSelectedId(0);
        String username = getSelectedId(1);
        if (id != null && username != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus user " + username + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = connection.koneksi();
                     java.sql.PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE user_id=?")) {
                    ps.setString(1, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "User " + username + " berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
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
