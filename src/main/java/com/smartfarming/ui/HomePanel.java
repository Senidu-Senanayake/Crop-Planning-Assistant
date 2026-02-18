package com.smartfarming.ui;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    public HomePanel(AppFrame frame) {
        setLayout(new BorderLayout());
        setBackground(UIConstants.DARK_GREEN);

        // Background gradient
        JPanel bg = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(15, 60, 15),
                        0, getHeight(), new Color(50, 120, 30));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Decorative leaf circles
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillOval(-80, -80, 400, 400);
                g2.fillOval(getWidth() - 200, getHeight() - 200, 450, 450);
            }
        };
        bg.setLayout(new GridBagLayout());

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        // Logo emoji
        JLabel logoLbl = new JLabel("🌱", SwingConstants.CENTER);
        logoLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 90));
        logoLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleBold = new JLabel("Smart Farming", SwingConstants.CENTER);
        titleBold.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleBold.setForeground(UIConstants.WHITE);
        titleBold.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleNorm = new JLabel("Crop Planning Assistant", SwingConstants.CENTER);
        titleNorm.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        titleNorm.setForeground(UIConstants.WHITE);
        titleNorm.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Divider
        JSeparator div = new JSeparator();
        div.setForeground(new Color(255, 255, 255, 120));
        div.setMaximumSize(new Dimension(600, 2));

        // Subtitle
        JLabel sub = new JLabel("Optimize your crop yield with smart data driven decisions", SwingConstants.CENTER);
        sub.setFont(UIConstants.FONT_BODY);
        sub.setForeground(new Color(200, 240, 200));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Start button
        JButton startBtn = UIConstants.makeButton("Start  >");
        startBtn.setPreferredSize(new Dimension(220, 52));
        startBtn.setMaximumSize(new Dimension(240, 52));
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.addActionListener(e -> frame.showCard(AppFrame.CARD_INPUT));

        center.add(logoLbl);
        center.add(Box.createVerticalStrut(10));
        center.add(titleBold);
        center.add(titleNorm);
        center.add(Box.createVerticalStrut(10));
        center.add(div);
        center.add(Box.createVerticalStrut(8));
        center.add(sub);
        center.add(Box.createVerticalStrut(32));
        center.add(startBtn);

        bg.add(center);
        add(bg, BorderLayout.CENTER);
    }
}
