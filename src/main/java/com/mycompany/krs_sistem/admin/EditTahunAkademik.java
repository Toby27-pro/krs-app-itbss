package com.mycompany.krs_sistem.admin;
import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseFormFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
public class EditTahunAkademik extends BaseFormFrame {
    private JTextField txtTahunId, txtTahunAjaran;
    private JComboBox<String> cbSemester;
    private JComboBox<String> cbStatusAktif;
    private String tahunId;
    public EditTahunAkademik(String tahunId) {
        super("Edit Tahun Akademik");
        this.tahunId = tahunId;
        txtTahunAjaran = new JTextField(20);
        cbSemester = new JComboBox<>(new String[]{"Ganjil", "Genap"});
        cbStatusAktif = new JComboBox<>(new String[]{"Aktif", "Tidak Aktif"});
        addFormField("Tahun Ajaran", txtTahunAjaran);
        addFormField("Semester", cbSemester);
        addFormField("Status Aktif", cbStatusAktif);
        loadData();
    }
    private void loadData() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM tahun_akademik WHERE tahun_id=?")) {
            ps.setString(1, tahunId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtTahunAjaran.setText(rs.getString("tahun_ajaran"));
                cbSemester.setSelectedItem(rs.getString("semester"));
                cbStatusAktif.setSelectedItem(rs.getBoolean("status_aktif") ? "Aktif" : "Tidak Aktif");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error load data: " + e.getMessage());
        }
    }
    @Override
    protected void onSimpan() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("UPDATE tahun_akademik SET tahun_ajaran=?, semester=?, status_aktif=? WHERE tahun_id=?")) {
            ps.setString(1, txtTahunAjaran.getText());
            ps.setString(2, cbSemester.getSelectedItem().toString());
            ps.setBoolean(3, cbStatusAktif.getSelectedItem().toString().equals("Aktif"));
            ps.setString(4, tahunId);
            ps.executeUpdate();
            String nama = txtTahunAjaran.getText();
            JOptionPane.showMessageDialog(this, "Tahun Akademik " + nama + " berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            onBatal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    @Override
    protected void onBatal() {
        new ListTahunAkademik().setVisible(true);
        this.dispose();
    }
}
