package com.smartfarming.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Reusable banner header shown on all inner screens.
 */
public class HeaderBannerPanel extends JPanel {

    public HeaderBannerPanel(String screenTitle) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 170));
        setBackground(UIConstants.DARK_GREEN);

        // Top text area
        JPanel textArea = new JPanel();
        textArea.setOpaque(false);
        textArea.setLayout(new BoxLayout(textArea, BoxLayout.Y_AXIS));
        textArea.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));

        JLabel appTitle = new JLabel("Smart Farming Crop Planning Assistant", SwingConstants.CENTER);
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        appTitle.setForeground(UIConstants.WHITE);
        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 100));
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

        // Screen title label at bottom
        JLabel screenLabel = new JLabel(screenTitle, SwingConstants.CENTER);
        screenLabel.setFont(UIConstants.FONT_TITLE);
        screenLabel.setForeground(UIConstants.WHITE);
        screenLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 14, 0));

        add(textArea, BorderLayout.CENTER);
        add(screenLabel, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // Farm field gradient overlay
        GradientPaint gp = new GradientPaint(0, 0, new Color(20, 80, 20), 0, getHeight(), new Color(40, 100, 40));
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
