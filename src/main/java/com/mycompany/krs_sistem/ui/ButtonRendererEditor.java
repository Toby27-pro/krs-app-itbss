package com.mycompany.krs_sistem.ui;


import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class ButtonRendererEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    private final JButton button;
    private String label;
    private int selectedRow;

    public ButtonRendererEditor(JTable table) {
        button = new JButton();
        
        // --- KUSTOMISASI DESAIN TOMBOL (Modern Flat Design) ---
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false); // Hilangkan garis putus-putus saat diklik
        button.setBorderPainted(false); // Hilangkan border kaku bawaan Swing
        
        // Warna background (Toska/Teal disesuaikan dengan tema aplikasi Anda)
        button.setBackground(new Color(0, 153, 153)); 
        // Warna teks tulisan (Putih)
        button.setForeground(Color.WHITE); 
        // Desain Font (Bold, Ukuran 11 agar pas di sel)
        button.setFont(new Font("Segoe UI", Font.BOLD, 11)); 
        // Memberikan padding/jarak di dalam tombol agar tidak terlalu mepet atas-bawah
        button.setBorder(new EmptyBorder(4, 8, 4, 8)); 
        // Mengubah kursor menjadi bentuk tangan ketika mouse berada di atas tombol
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Aksi ketika tombol diklik
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped(); 
                
                String mataKuliahId = table.getValueAt(selectedRow, 0).toString();
                String namaMk = table.getValueAt(selectedRow, 2).toString();
                
                javax.swing.JOptionPane.showMessageDialog(table, 
                        "Anda memilih Mata Kuliah: " + namaMk + " (" + mataKuliahId + ")");
            }
        });
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        button.setText((value == null) ? "Detail" : value.toString());
        return button;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        label = (value == null) ? "Detail" : value.toString();
        button.setText(label);
        this.selectedRow = row; 
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }
}