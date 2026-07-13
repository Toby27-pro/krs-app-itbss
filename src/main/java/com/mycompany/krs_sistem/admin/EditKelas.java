package com.mycompany.krs_sistem.admin;
import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.BaseFormFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
public class EditKelas extends BaseFormFrame {
    private JTextField txtKelasId, txtNamaKelas, txtKuota, txtKuotaTerisi;
    private JComboBox<String> cbMk, cbDosen, cbTahun;
    private String kelasId;
    public EditKelas(String kelasId) {
        super("Edit Kelas");
        this.kelasId = kelasId;
        txtNamaKelas = new JTextField(20);
        txtKuota = new JTextField(20);
        txtKuotaTerisi = new JTextField(20);
        cbMk = new JComboBox<>();
        cbDosen = new JComboBox<>();
        cbTahun = new JComboBox<>();
        loadComboBoxes();
        addFormField("Mata Kuliah", cbMk);
        addFormField("Dosen", cbDosen);
        addFormField("Tahun Akademik", cbTahun);
        addFormField("Nama Kelas", txtNamaKelas);
        addFormField("Kuota", txtKuota);
        addFormField("Kuota Terisi", txtKuotaTerisi);
        loadData();
    }
    private void loadComboBoxes() {
        try (Connection conn = connection.koneksi()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT mk_id, nama_mk FROM mata_kuliah");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) cbMk.addItem(rs.getString("mk_id") + " - " + rs.getString("nama_mk"));
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT nip, nama_lengkap FROM dosen");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) cbDosen.addItem(rs.getString("nip") + " - " + rs.getString("nama_lengkap"));
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT tahun_id, tahun_ajaran, semester FROM tahun_akademik");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) cbTahun.addItem(rs.getString("tahun_id") + " - " + rs.getString("tahun_ajaran") + " (" + rs.getString("semester") + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public EditKelas(String a, String b, String c, String d, String e) {
        this(a);
    }
    private void loadData() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("SELECT k.*, m.nama_mk, d.nama_lengkap, t.tahun_ajaran, t.semester FROM kelas k LEFT JOIN mata_kuliah m ON k.mk_id=m.mk_id LEFT JOIN dosen d ON k.nip=d.nip LEFT JOIN tahun_akademik t ON k.tahun_id=t.tahun_id WHERE k.kelas_id=?")) {
            ps.setString(1, kelasId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String mkId = rs.getString("mk_id");
                String namaMk = rs.getString("nama_mk");
                if (mkId != null) cbMk.setSelectedItem(mkId + " - " + namaMk);
                String nip = rs.getString("nip");
                String namaDosen = rs.getString("nama_lengkap");
                if (nip != null) cbDosen.setSelectedItem(nip + " - " + namaDosen);
                String tahunId = rs.getString("tahun_id");
                String tahunAjaran = rs.getString("tahun_ajaran");
                String semester = rs.getString("semester");
                if (tahunId != null) cbTahun.setSelectedItem(tahunId + " - " + tahunAjaran + " (" + semester + ")");
                txtNamaKelas.setText(rs.getString("nama_kelas"));
                txtKuota.setText(rs.getString("kuota"));
                txtKuotaTerisi.setText(rs.getString("kuota_terisi"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error load data: " + e.getMessage());
        }
    }
    @Override
    protected void onSimpan() {
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement("UPDATE kelas SET mk_id=?, nip=?, tahun_id=?, nama_kelas=?, kuota=?, kuota_terisi=? WHERE kelas_id=?")) {
            String mk = (String) cbMk.getSelectedItem();
            ps.setString(1, mk != null ? mk.split(" - ")[0] : "");
            String nip = (String) cbDosen.getSelectedItem();
            ps.setString(2, nip != null ? nip.split(" - ")[0] : "");
            String tahun = (String) cbTahun.getSelectedItem();
            ps.setString(3, tahun != null ? tahun.split(" - ")[0] : "");
            ps.setString(4, txtNamaKelas.getText());
            ps.setString(5, txtKuota.getText());
            ps.setString(6, txtKuotaTerisi.getText());
            ps.setString(7, kelasId);
            ps.executeUpdate();
            String nama = txtNamaKelas.getText();
            JOptionPane.showMessageDialog(this, "Kelas " + nama + " berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            onBatal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    @Override
    protected void onBatal() {
        new ListKelas().setVisible(true);
        this.dispose();
    }
}
