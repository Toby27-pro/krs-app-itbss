package com.mycompany.krs_sistem.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public abstract class BaseListFrame extends JFrame {
    protected JTable table;
    protected DefaultTableModel tableModel;
    protected RoundedButton btnTambah, btnEdit, btnHapus, btnDashboard;

    public BaseListFrame(String title, String[] columns) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 244, 248));
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 74, 107), getWidth(), 0, new Color(44, 125, 160));
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
        JLabel lblTitle = new JLabel("Data " + title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // Center Panel (Table + Buttons)
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        centerPanel.setOpaque(false);

        // Action Buttons Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionPanel.setOpaque(false);

        btnTambah = new RoundedButton("Tambah", 15);
        btnTambah.setBackground(new Color(44, 125, 160));
        btnTambah.setForeground(Color.WHITE);
        btnTambah.addActionListener(e -> onTambah());

        btnEdit = new RoundedButton("Edit", 15);
        btnEdit.setBackground(new Color(230, 126, 34));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.addActionListener(e -> onEdit());

        btnHapus = new RoundedButton("Hapus", 15);
        btnHapus.setBackground(new Color(231, 76, 60));
        btnHapus.setForeground(Color.WHITE);
        btnHapus.addActionListener(e -> onHapus());

        btnDashboard = new RoundedButton("Dashboard", 15);
        btnDashboard.setBackground(new Color(100, 100, 100));
        btnDashboard.setForeground(Color.WHITE);
        btnDashboard.addActionListener(e -> onDashboard());

        actionPanel.add(btnDashboard);
        actionPanel.add(btnTambah);
        actionPanel.add(btnEdit);
        actionPanel.add(btnHapus);

        centerPanel.add(actionPanel, BorderLayout.NORTH);

        // Prepended "No." column logic
        String[] newCols = new String[columns.length + 1];
        newCols[0] = "No.";
        System.arraycopy(columns, 0, newCols, 1, columns.length);

        // Table Setup
        tableModel = new DefaultTableModel(newCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public void addRow(Object[] rowData) {
                Object[] newRow = new Object[rowData.length + 1];
                newRow[0] = getRowCount() + 1;
                System.arraycopy(rowData, 0, newRow, 1, rowData.length);
                super.addRow(newRow);
            }
        };
        table = new JTable(tableModel);
        
        // Hide the ID column from the view
        table.removeColumn(table.getColumnModel().getColumn(1));
        
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(0).setMinWidth(50);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);

        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setGridColor(new Color(220, 220, 220));

        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(new Color(30, 74, 107));
        th.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        loadData();
    }

    protected abstract void loadData();
    protected abstract void onTambah();
    protected abstract void onEdit();
    protected abstract void onHapus();
    protected abstract void onDashboard();
    
    protected String getSelectedId(int columnIndex) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int modelRow = table.convertRowIndexToModel(row);
        return table.getModel().getValueAt(modelRow, columnIndex + 1).toString();
    }
}