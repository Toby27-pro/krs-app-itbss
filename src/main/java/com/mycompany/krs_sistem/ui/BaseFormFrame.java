package com.mycompany.krs_sistem.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public abstract class BaseFormFrame extends JFrame {
    
    protected JPanel formPanel;
    private GridBagConstraints gbc;
    private int currentRow = 0;

    public BaseFormFrame(String title) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
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
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // Center Panel (Form)
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        formPanel.setOpaque(false);
        
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        add(formPanel, BorderLayout.CENTER);

        // Bottom Panel (Buttons)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        bottomPanel.setOpaque(false);

        RoundedButton btnSimpan = new RoundedButton("Simpan", 15);
        btnSimpan.setBackground(new Color(44, 125, 160));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.addActionListener(e -> onSimpan());

        RoundedButton btnBatal = new RoundedButton("Batal", 15);
        btnBatal.setBackground(new Color(100, 100, 100));
        btnBatal.setForeground(Color.WHITE);
        btnBatal.addActionListener(e -> onBatal());

        bottomPanel.add(btnBatal);
        bottomPanel.add(btnSimpan);

        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    protected void addFormField(String labelText, JComponent inputComponent) {
        gbc.gridx = 0;
        gbc.gridy = currentRow;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(50, 50, 50));
        formPanel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = currentRow;
        gbc.weightx = 0.7;
        formPanel.add(inputComponent, gbc);
        
        currentRow++;
    }

    protected abstract void onSimpan();
    protected abstract void onBatal();
}
