package com.mycompany.krs_sistem.admin;
import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseFormFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
public class EditUser extends BaseFormFrame {
    private JTextField txtUsername, txtEmail;
    private javax.swing.JPasswordField txtPassword, txtConfirmPassword;
    private JComboBox<String> cbRole;
    private String userId;
    public EditUser(String userId) {
        super("Edit User");
        this.userId = userId;
        txtUsername = new JTextField(20);
        txtPassword = new javax.swing.JPasswordField(20);
        txtConfirmPassword = new javax.swing.JPasswordField(20);
        txtEmail = new JTextField(20);
        cbRole = new JComboBox<>();
        loadRoles();
        
        addFormField("Username", txtUsername);
        addFormField("Password", txtPassword);
        addFormField("Confirm Password", txtConfirmPassword);
        addFormField("Role", cbRole);
        addFormField("Email", txtEmail);
        loadData();
    }
    private void loadRoles() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("SELECT MIN(role_id) as role_id, nama_role FROM roles GROUP BY nama_role ORDER BY role_id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cbRole.addItem(rs.getString("role_id") + " - " + rs.getString("nama_role"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // For backwards compatibility with other calls
    public EditUser(String userId, String username, String password, String role, String email) {
        this(userId);
    }
    private void loadData() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("SELECT u.*, r.nama_role as role_name FROM users u JOIN roles r ON u.role_id = r.role_id WHERE u.user_id=?")) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtUsername.setText(rs.getString("username"));
                txtPassword.setText(rs.getString("password"));
                txtConfirmPassword.setText(rs.getString("password"));
                String roleId = rs.getString("role_id");
                String roleName = rs.getString("role_name");
                if (roleId != null) {
                    cbRole.setSelectedItem(roleId + " - " + roleName);
                }
                txtEmail.setText(rs.getString("email"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error load data: " + e.getMessage());
        }
    }
    @Override
    protected void onSimpan() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("UPDATE users SET username=?, password=?, role_id=?, email=? WHERE user_id=?")) {
            
            String pwd = new String(txtPassword.getPassword());
            String confirmPwd = new String(txtConfirmPassword.getPassword());
            if (!pwd.equals(confirmPwd)) {
                JOptionPane.showMessageDialog(this, "Password dan Confirm Password tidak cocok!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            ps.setString(1, txtUsername.getText());
            ps.setString(2, pwd);
            String selectedRole = (String) cbRole.getSelectedItem();
            if (selectedRole != null) {
                ps.setString(3, selectedRole.split(" - ")[0]);
            } else {
                ps.setString(3, "1");
            }
            ps.setString(4, txtEmail.getText());
            ps.setString(5, userId);
            ps.executeUpdate();
            String username = txtUsername.getText();
            JOptionPane.showMessageDialog(this, "User " + username + " berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            onBatal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    @Override
    protected void onBatal() {
        new ListUser().setVisible(true);
        this.dispose();
    }
}
