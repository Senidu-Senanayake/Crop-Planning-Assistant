package com.smartfarming.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Reusable banner header shown on all inner screens.
 * Glassmorphism style - semi-transparent frosted glass effect.
 */
public class HeaderBannerPanel extends JPanel {

    public HeaderBannerPanel(String screenTitle) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 170));
        setOpaque(false); // ✅ transparent so background image shows through

        // ✅ Glassmorphism wrapper panel
        JPanel glassPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Frosted glass background
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Subtle green tint overlay
                g2.setColor(new Color(20, 80, 20, 120));
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Bottom border glow line
                g2.setColor(new Color(255, 255, 255, 60));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);

                g2.dispose();
            }
        };
        glassPanel.setOpaque(false);

        // Top text area
        JPanel textArea = new JPanel();
        textArea.setOpaque(false);
        textArea.setLayout(new BoxLayout(textArea, BoxLayout.Y_AXIS));
        textArea.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));

        JLabel appTitle = new JLabel("Smart Farming Crop Planning Assistant", SwingConstants.CENTER);
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        appTitle.setForeground(UIConstants.WHITE);
        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ✅ Glowing separator
        JSeparator sep = new JSeparator() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(255, 255, 255, 0),
                        getWidth() / 2f, 0, new Color(255, 255, 255, 180),
                        false
                );
                // Mirror gradient for symmetry
                GradientPaint gp2 = new GradientPaint(
                        getWidth() / 2f, 0, new Color(255, 255, 255, 180),
                        getWidth(), 0, new Color(255, 255, 255, 0),
                        false
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth() / 2, 2);
                g2.setPaint(gp2);
                g2.fillRect(getWidth() / 2, 0, getWidth() / 2, 2);
                g2.dispose();
            }
        };
        sep.setMaximumSize(new Dimension(600, 2));

        JLabel subtitle = new JLabel("Optimize your crop yield with smart data driven decisions", SwingConstants.CENTER);
        subtitle.setFont(UIConstants.FONT_BODY);
        subtitle.setForeground(new Color(220, 240, 220));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        textArea.add(appTitle);
        textArea.add(Box.createVerticalStrut(4));
        textArea.add(sep);
        textArea.add(Box.createVerticalStrut(4));
        textArea.add(subtitle);

        // ✅ Screen title label with subtle glow text effect
        JLabel screenLabel = new JLabel(screenTitle, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                // Soft glow shadow behind text
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = fm.getAscent() + (getHeight() - fm.getHeight()) / 2;
                g2.setColor(new Color(100, 255, 100, 60));
                for (int i = 1; i <= 4; i++) {
                    g2.drawString(getText(), x - i, y);
                    g2.drawString(getText(), x + i, y);
                    g2.drawString(getText(), x, y - i);
                    g2.drawString(getText(), x, y + i);
                }
                // Actual text
                g2.setColor(UIConstants.WHITE);
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        screenLabel.setFont(UIConstants.FONT_TITLE);
        screenLabel.setForeground(UIConstants.WHITE);
        screenLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 14, 0));

        glassPanel.add(textArea, BorderLayout.CENTER);
        glassPanel.add(screenLabel, BorderLayout.SOUTH);

        add(glassPanel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // ✅ No solid background - fully transparent to show background image
    }
}