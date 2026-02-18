package com.smartfarming.ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UIConstants {

    // Colors
    public static final Color DARK_GREEN = new Color(27, 94, 32);
    public static final Color MID_GREEN  = new Color(56, 142, 60);
    public static final Color LIGHT_GREEN = new Color(165, 214, 167);
    public static final Color PALE_GREEN  = new Color(232, 245, 233);
    public static final Color ACCENT_GREEN = new Color(100, 180, 50);
    public static final Color WHITE = Color.WHITE;
    public static final Color DARK_TEXT = new Color(20, 20, 20);
    public static final Color SIDEBAR_BG = new Color(245, 250, 245);
    public static final Color TABLE_HEADER_BG = new Color(27, 68, 35);
    public static final Color TABLE_ROW_ALT = new Color(240, 249, 240);

    // Fonts
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBHEAD = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 12);

    // Rounded border helper
    public static Border roundedBorder(Color color, int radius) {
        return BorderFactory.createCompoundBorder(
                new RoundedBorder(color, radius),
                BorderFactory.createEmptyBorder(6, 12, 6, 12));
    }

    // Gradient button factory
    public static JButton makeButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, MID_GREEN, 0, getHeight(), DARK_GREEN);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(200, 44));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Inner rounded border class
    public static class RoundedBorder implements Border {
        private final Color color;
        private final int radius;

        public RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override public Insets getBorderInsets(Component c) { return new Insets(4, 4, 4, 4); }
        @Override public boolean isBorderOpaque() { return false; }
    }
}
