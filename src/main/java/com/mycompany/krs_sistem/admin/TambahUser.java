package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseFormFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.sql.ResultSet;

public class TambahUser extends BaseFormFrame {

    private JTextField txtUsername, txtEmail;
    private javax.swing.JPasswordField txtPassword, txtConfirmPassword;
    private JComboBox<String> cbRole;

    public TambahUser() {
        super("Tambah User");
        
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

    @Override
    protected void onSimpan() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password, role_id, email) VALUES (?,?,?,?)")) {
            
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
            ps.executeUpdate();
            String username = txtUsername.getText();
            JOptionPane.showMessageDialog(this, "User " + username + " berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
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
