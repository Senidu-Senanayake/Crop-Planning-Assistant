package com.smartfarming.ui;

import javax.swing.*;
import java.awt.*;

public class HeaderBannerPanel extends JPanel {

    public HeaderBannerPanel(String screenTitle) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 160));
        setOpaque(false);

        JPanel glassPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Smoother, deeper glass background
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Premium Gradient Overlay
                GradientPaint bgGradient = new GradientPaint(0, 0, new Color(10, 50, 20, 160), getWidth(), getHeight(), new Color(30, 90, 40, 120));
                g2.setPaint(bgGradient);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Bottom border highlight
                g2.setColor(new Color(255, 255, 255, 80));
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2.dispose();
            }
        };
        glassPanel.setOpaque(false);
        glassPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // Top text area
        JPanel textArea = new JPanel();
        textArea.setOpaque(false);
        textArea.setLayout(new BoxLayout(textArea, BoxLayout.Y_AXIS));

        JLabel appTitle = new JLabel("Smart Farming Crop Planning Assistant", SwingConstants.CENTER);
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        appTitle.setForeground(UIConstants.WHITE);
        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 100));
        sep.setMaximumSize(new Dimension(450, 1));

        JLabel subtitle = new JLabel("Optimize your crop yield with smart, data-driven decisions", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(new Color(220, 240, 220));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        textArea.add(appTitle);
        textArea.add(Box.createVerticalStrut(8));
        textArea.add(sep);
        textArea.add(Box.createVerticalStrut(8));
        textArea.add(subtitle);

        // Screen title with optimized soft shadow
        JLabel screenLabel = new JLabel(screenTitle, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                FontMetrics fm = g2.getFontMetrics(getFont());
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = fm.getAscent() + (getHeight() - fm.getHeight()) / 2;

                // Soft shadow instead of heavy glow for a cleaner modern look
                g2.setColor(new Color(0, 0, 0, 150));
                g2.drawString(getText(), x + 2, y + 2);

                g2.setColor(UIConstants.WHITE);
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        screenLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        screenLabel.setForeground(UIConstants.WHITE);
        screenLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        glassPanel.add(textArea, BorderLayout.NORTH);
        glassPanel.add(screenLabel, BorderLayout.CENTER);

        add(glassPanel, BorderLayout.CENTER);
    }
}