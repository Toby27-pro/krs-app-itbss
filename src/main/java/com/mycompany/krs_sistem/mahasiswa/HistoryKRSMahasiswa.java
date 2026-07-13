package com.mycompany.krs_sistem.mahasiswa;

import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.ui.RoundedButton;
import com.mycompany.krs_sistem.ui.RoundedPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class HistoryKRSMahasiswa extends JFrame {
    
    private String userId;
    private String nimMahasiswa;
    private String namaMahasiswa;
    
    private JPanel navbarPanel;
    private JPanel mainPanel;
    private JTable krsTable;
    private DefaultTableModel krsTableModel;
    
    private final Font FONT_BOLD_20 = new Font("Segoe UI", Font.BOLD, 20);
    private final Font FONT_BOLD_16 = new Font("Segoe UI", Font.BOLD, 16);
    private final Font FONT_BOLD_12 = new Font("Segoe UI", Font.BOLD, 12);
    private final Font FONT_BOLD_11 = new Font("Segoe UI", Font.BOLD, 11);
    private final Font FONT_PLAIN_11 = new Font("Segoe UI", Font.PLAIN, 11);

    public HistoryKRSMahasiswa(String userId, String nimMahasiswa, String namaMahasiswa) {
        this.userId = userId;
        this.nimMahasiswa = nimMahasiswa;
        this.namaMahasiswa = namaMahasiswa;
        
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("History KRS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 244, 248));
        setLayout(new BorderLayout());
        
        navbarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 74, 107), getWidth(), 0, new Color(44, 125, 160));
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        navbarPanel.setLayout(new BorderLayout());
        navbarPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        JPanel logoArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoArea.setOpaque(false);
        JLabel logoText = new JLabel("History KRS (Rekap Semester)");
        logoText.setFont(FONT_BOLD_20);
        logoText.setForeground(Color.WHITE);
        logoArea.add(logoText);
        
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        userInfo.setOpaque(false);
        JLabel userName = new JLabel(namaMahasiswa + " (NIM: " + nimMahasiswa + ")");
        userName.setForeground(Color.WHITE);
        userName.setFont(FONT_BOLD_12);
        
        RoundedButton backBtn = new RoundedButton("Kembali", 25);
        backBtn.setFont(FONT_BOLD_11);
        backBtn.setBackground(new Color(100, 100, 100));
        backBtn.setNormalColor(new Color(100, 100, 100));
        backBtn.addActionListener(e -> {
            new DashboardMahasiswa(userId).setVisible(true);
            dispose();
        });
        
        userInfo.add(userName);
        userInfo.add(backBtn);
        
        navbarPanel.add(logoArea, BorderLayout.WEST);
        navbarPanel.add(userInfo, BorderLayout.EAST);
        
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 244, 248));
        mainPanel.setBorder(new EmptyBorder(16, 20, 16, 20));
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 12, 0);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        JPanel krsPanel = createKRSColumn();
        mainPanel.add(krsPanel, gbc);
        
        add(navbarPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createKRSColumn() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        RoundedPanel tableContainer = new RoundedPanel(15);
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setLayout(new BorderLayout());
        
        String[] columns = {"ID KRS", "Tahun Ajaran", "Semester", "Tanggal", "Total SKS", "Status", "Aksi"};
        krsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Hanya kolom Aksi (Detail) yang dapat di-klik
            }
        };
        
        krsTable = new JTable(krsTableModel);
        krsTable.setRowHeight(36);
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
        
        krsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        krsTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        krsTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        krsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        krsTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        krsTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        krsTable.getColumnModel().getColumn(6).setPreferredWidth(90);
        
        // Button Renderer dan Editor
        krsTable.getColumnModel().getColumn(6).setCellRenderer(new DetailButtonRenderer());
        krsTable.getColumnModel().getColumn(6).setCellEditor(new DetailButtonEditor());
        
        JScrollPane scrollTable = new JScrollPane(krsTable);
        scrollTable.setBorder(null);
        scrollTable.getViewport().setBackground(Color.WHITE);
        
        tableContainer.add(scrollTable, BorderLayout.CENTER);
        panel.add(tableContainer, gbc);
        return panel;
    }
    
    private void loadData() {
        krsTableModel.setRowCount(0);
        String sql = "SELECT kr.krs_id, ta.tahun_ajaran, ta.semester, kr.tanggal_krs, kr.status, " +
                     "COALESCE(SUM(CASE WHEN kd.status != 'Rejected' THEN mk.sks ELSE 0 END), 0) as total_sks " +
                     "FROM krs kr " +
                     "LEFT JOIN tahun_akademik ta ON kr.tahun_id = ta.tahun_id " +
                     "LEFT JOIN krs_detail kd ON kr.krs_id = kd.krs_id " +
                     "LEFT JOIN jadwal j ON kd.jadwal_id = j.jadwal_id " +
                     "LEFT JOIN kelas k ON j.kelas_id = k.kelas_id " +
                     "LEFT JOIN mata_kuliah mk ON k.mk_id = mk.mk_id " +
                     "WHERE kr.nim = ? " +
                     "GROUP BY kr.krs_id, ta.tahun_ajaran, ta.semester, kr.tanggal_krs, kr.status " +
                     "ORDER BY kr.krs_id DESC";
                     
        try (Connection conn = connection.koneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, nimMahasiswa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String semesterStr = rs.getString("semester");
                    krsTableModel.addRow(new Object[]{
                        rs.getString("krs_id"),
                        rs.getString("tahun_ajaran"),
                        semesterStr,
                        rs.getString("tanggal_krs"),
                        rs.getString("total_sks"),
                        rs.getString("status"),
                        "actions"
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Custom Renderer untuk tombol Detail & PDF
    class DetailButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton btnDetail;
        private JButton btnPdf;
        
        public DetailButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setOpaque(true);
            
            btnDetail = new JButton("Detail");
            btnDetail.setFont(new Font("Segoe UI", Font.BOLD, 10));
            btnDetail.setBackground(new Color(40, 167, 69));
            btnDetail.setForeground(Color.WHITE);
            btnDetail.setBorderPainted(false);
            btnDetail.setFocusPainted(false);
            
            btnPdf = new JButton("Cetak PDF");
            btnPdf.setFont(new Font("Segoe UI", Font.BOLD, 10));
            btnPdf.setBackground(new Color(220, 53, 69));
            btnPdf.setForeground(Color.WHITE);
            btnPdf.setBorderPainted(false);
            btnPdf.setFocusPainted(false);
            
            add(btnDetail);
            add(btnPdf);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(Color.WHITE);
            }
            
            String status = (String) table.getValueAt(row, 5);
            btnPdf.setVisible("Approved".equals(status));
            
            return this;
        }
    }
    
    // Custom Editor untuk tombol Detail & PDF
    class DetailButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton btnDetail;
        private JButton btnPdf;
        private String krsId;
        private String semesterName;

        public DetailButtonEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(true);
            
            btnDetail = new JButton("Detail");
            btnDetail.setFont(new Font("Segoe UI", Font.BOLD, 10));
            btnDetail.setBackground(new Color(40, 167, 69));
            btnDetail.setForeground(Color.WHITE);
            btnDetail.setBorderPainted(false);
            btnDetail.setFocusPainted(false);
            
            btnPdf = new JButton("Cetak PDF");
            btnPdf.setFont(new Font("Segoe UI", Font.BOLD, 10));
            btnPdf.setBackground(new Color(220, 53, 69));
            btnPdf.setForeground(Color.WHITE);
            btnPdf.setBorderPainted(false);
            btnPdf.setFocusPainted(false);
            
            btnDetail.addActionListener(e -> {
                fireEditingStopped();
                DetailKRSMahasiswa dialog = new DetailKRSMahasiswa(HistoryKRSMahasiswa.this, krsId, nimMahasiswa, semesterName);
                dialog.setVisible(true);
            });
            
            btnPdf.addActionListener(e -> {
                fireEditingStopped();
                javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
                fileChooser.setDialogTitle("Simpan KRS PDF");
                fileChooser.setSelectedFile(new java.io.File("KRS_" + nimMahasiswa + "_" + semesterName.replaceAll("[^a-zA-Z0-9_-]", "") + ".pdf"));
                int userSelection = fileChooser.showSaveDialog(HistoryKRSMahasiswa.this);
                if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
                    java.io.File fileToSave = fileChooser.getSelectedFile();
                    boolean success = com.mycompany.krs_sistem.utils.PDFGenerator.generateKRSPdf(krsId, nimMahasiswa, fileToSave.getAbsolutePath());
                    if (success) {
                        javax.swing.JOptionPane.showMessageDialog(HistoryKRSMahasiswa.this, "PDF berhasil disimpan di: " + fileToSave.getAbsolutePath());
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(HistoryKRSMahasiswa.this, "Gagal menyimpan PDF!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            
            panel.add(btnDetail);
            panel.add(btnPdf);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            krsId = table.getValueAt(row, 0).toString();
            String ta = table.getValueAt(row, 1) != null ? table.getValueAt(row, 1).toString() : "";
            String sm = table.getValueAt(row, 2) != null ? table.getValueAt(row, 2).toString() : "";
            semesterName = "Semester " + sm + " (" + ta + ")";
            
            String status = (String) table.getValueAt(row, 5);
            btnPdf.setVisible("Approved".equals(status));
            
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "actions";
        }
    }
}
