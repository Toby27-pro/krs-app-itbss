package com.mycompany.krs_sistem.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class RoundedButton extends JButton {
    
    private int cornerRadius = 30;
    private Color normalColor = new Color(44, 125, 160);
    private Color hoverColor = new Color(35, 107, 138);
    
    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(10, 20, 10, 20));
        setForeground(Color.WHITE);
        setFont(getFont().deriveFont(Font.BOLD, 12f));
        setBackground(normalColor);
        
        setFocusPainted(false);
        setFocusable(true);
        setOpaque(false);
    }
    
    public RoundedButton(String text, int radius) {
        this(text);
        this.cornerRadius = radius;
    }
    
    public void setNormalColor(Color color) {
        this.normalColor = color;
        setBackground(color);
    }
    
    public void setHoverColor(Color color) {
        this.hoverColor = color;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        setBorderPainted(false);
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (getModel().isPressed()) {
            g2.setColor(hoverColor.darker());
        } else if (getModel().isRollover()) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(normalColor);
        }
        
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
        
        super.paintComponent(g);
        g2.dispose();
    }
}
