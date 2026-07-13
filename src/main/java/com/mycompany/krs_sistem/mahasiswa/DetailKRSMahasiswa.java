package com.mycompany.krs_sistem.mahasiswa;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.RoundedButton;
import com.mycompany.krs_sistem.ui.RoundedPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DetailKRSMahasiswa extends JDialog {

    private String krsId;
    private String nimMahasiswa;
    private JTable krsTable;
    private DefaultTableModel krsTableModel;

    private final Font FONT_BOLD_18 = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FONT_BOLD_12 = new Font("Segoe UI", Font.BOLD, 12);
    private final Font FONT_BOLD_11 = new Font("Segoe UI", Font.BOLD, 11);
    private final Font FONT_PLAIN_11 = new Font("Segoe UI", Font.PLAIN, 11);

    public DetailKRSMahasiswa(JFrame parent, String krsId, String nimMahasiswa, String semesterName) {
        super(parent, "Detail KRS - " + semesterName, true);
        this.krsId = krsId;
        this.nimMahasiswa = nimMahasiswa;

        initComponents();
        loadDetailData();
    }

    private void initComponents() {
        setSize(850, 450);
        setLocationRelativeTo(getParent());
        getContentPane().setBackground(new Color(240, 244, 248));
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(30, 74, 107));
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel title = new JLabel("DETAIL MATA KULIAH");
        title.setFont(FONT_BOLD_18);
        title.setForeground(Color.WHITE);
        headerPanel.add(title);
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 244, 248));
        mainPanel.setBorder(new EmptyBorder(16, 20, 16, 20));
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        RoundedPanel tableContainer = new RoundedPanel(15);
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setLayout(new BorderLayout());

        String[] columns = {"Kode MK", "Mata Kuliah", "SKS", "Kelas", "Dosen", "Status", "Aksi", "jadwal_id"};
        krsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only Aksi column is editable (for button click)
            }
        };

        krsTable = new JTable(krsTableModel);
        krsTable.setRowHeight(30);
        krsTable.setFont(FONT_PLAIN_11);
        krsTable.setForeground(new Color(51, 78, 104));
        krsTable.setShowGrid(true);
        krsTable.setGridColor(new Color(200, 200, 200));
        krsTable.setIntercellSpacing(new Dimension(1, 1));
        krsTable.setFillsViewportHeight(true);

        JTableHeader header = krsTable.getTableHeader();
        header.setFont(FONT_BOLD_12);
        header.setBackground(new Color(44, 125, 160));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 30));

        krsTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        krsTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        krsTable.getColumnModel().getColumn(2).setPreferredWidth(40);
        krsTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        krsTable.getColumnModel().getColumn(4).setPreferredWidth(160);
        krsTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        krsTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        
        // Hide jadwal_id column
        krsTable.getColumnModel().getColumn(7).setMinWidth(0);
        krsTable.getColumnModel().getColumn(7).setMaxWidth(0);
        krsTable.getColumnModel().getColumn(7).setWidth(0);
        
        krsTable.getColumnModel().getColumn(6).setCellRenderer(new BatalkanRenderer());
        krsTable.getColumnModel().getColumn(6).setCellEditor(new BatalkanEditor());

        JScrollPane scrollTable = new JScrollPane(krsTable);
        scrollTable.setBorder(null);
        scrollTable.getViewport().setBackground(Color.WHITE);

        tableContainer.add(scrollTable, BorderLayout.CENTER);
        mainPanel.add(tableContainer, gbc);
        add(mainPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(new Color(240, 244, 248));
        footerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        RoundedButton closeBtn = new RoundedButton("Tutup", 20);
        closeBtn.setFont(FONT_BOLD_11);
        closeBtn.setBackground(new Color(100, 100, 100));
        closeBtn.setNormalColor(new Color(100, 100, 100));
        closeBtn.addActionListener(e -> dispose());
        footerPanel.add(closeBtn);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private void loadDetailData() {
        krsTableModel.setRowCount(0);
        String sql = "SELECT mk.kode_mk, mk.nama_mk, mk.sks, k.nama_kelas, d.nama_lengkap, kd.status as detail_status, kd.jadwal_id " +
                     "FROM krs_detail kd " +
                     "JOIN jadwal j ON kd.jadwal_id = j.jadwal_id " +
                     "JOIN kelas k ON j.kelas_id = k.kelas_id " +
                     "JOIN mata_kuliah mk ON k.mk_id = mk.mk_id " +
                     "JOIN dosen d ON k.nip = d.nip " +
                     "WHERE kd.krs_id = ?";
                     
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, krsId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    krsTableModel.addRow(new Object[]{
                        rs.getString("kode_mk"),
                        rs.getString("nama_mk"),
                        rs.getString("sks"),
                        rs.getString("nama_kelas"),
                        rs.getString("nama_lengkap"),
                        rs.getString("detail_status"),
                        "Batalkan",
                        rs.getString("jadwal_id")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Custom Renderer untuk tombol Batalkan
    class BatalkanRenderer extends JButton implements TableCellRenderer {
        public BatalkanRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.BOLD, 10));
            setBackground(new Color(220, 53, 69));
            setForeground(Color.WHITE);
            setBorderPainted(false);
            setFocusPainted(false);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            String status = (String) table.getValueAt(row, 5);
            if (!"Pending".equals(status)) {
                return new JLabel(""); // Sembunyikan tombol jika bukan Pending
            }
            setText((value == null) ? "Batalkan" : value.toString());
            return this;
        }
    }
    
    // Custom Editor untuk tombol Batalkan
    class BatalkanEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JButton button;
        private String jadwalId;
        private String kodeMk;
        private boolean isPushed;

        public BatalkanEditor() {
            button = new JButton("Batalkan");
            button.setOpaque(true);
            button.setFont(new Font("Segoe UI", Font.BOLD, 10));
            button.setBackground(new Color(220, 53, 69));
            button.setForeground(Color.WHITE);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.addActionListener(this);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            String status = (String) table.getValueAt(row, 5);
            if (!"Pending".equals(status)) {
                return new JLabel("");
            }
            jadwalId = table.getValueAt(row, 7).toString();
            kodeMk = table.getValueAt(row, 0).toString();
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isPushed) {
                isPushed = false;
                fireEditingStopped();
                int confirm = JOptionPane.showConfirmDialog(DetailKRSMahasiswa.this, "Apakah Anda yakin ingin membatalkan pengajuan mata kuliah " + kodeMk + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    batalkanMatkul(jadwalId);
                }
            } else {
                fireEditingStopped();
            }
        }
    }
    
    private void batalkanMatkul(String jadwalId) {
        try (Connection conn = connection.koneksi()) {
            conn.setAutoCommit(false);
            
            // 1. Kurangi kuota kelas
            String sqlUpdateKuota = "UPDATE kelas SET kuota_terisi = kuota_terisi - 1 WHERE kelas_id = (SELECT kelas_id FROM jadwal WHERE jadwal_id = ?)";
            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateKuota)) {
                psUpdate.setString(1, jadwalId);
                psUpdate.executeUpdate();
            }
            
            // 2. Hapus detail krs
            String sqlDeleteDetail = "DELETE FROM krs_detail WHERE krs_id = ? AND jadwal_id = ?";
            try (PreparedStatement psDel = conn.prepareStatement(sqlDeleteDetail)) {
                psDel.setString(1, krsId);
                psDel.setString(2, jadwalId);
                psDel.executeUpdate();
            }
            
            // 3. Cek apakah masih ada detail lain
            boolean hasOtherDetails = false;
            String sqlCheck = "SELECT COUNT(*) FROM krs_detail WHERE krs_id = ?";
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                psCheck.setString(1, krsId);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        hasOtherDetails = true;
                    }
                }
            }
            
            // 4. Jika kosong, hapus master krs
            if (!hasOtherDetails) {
                String sqlDeleteKrs = "DELETE FROM krs WHERE krs_id = ?";
                try (PreparedStatement psDelKrs = conn.prepareStatement(sqlDeleteKrs)) {
                    psDelKrs.setString(1, krsId);
                    psDelKrs.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "Mata kuliah dibatalkan. Karena ini adalah satu-satunya mata kuliah, pengajuan KRS ikut dihapus.");
                conn.commit();
                dispose(); // Tutup dialog jika krs master sudah hilang
                return;
            }
            
            conn.commit();
            JOptionPane.showMessageDialog(this, "Mata kuliah berhasil dibatalkan!");
            loadDetailData(); // refresh tabel
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal membatalkan mata kuliah: " + ex.getMessage());
        }
    }
}
