package com.smartfarming.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Left sidebar navigation used by all inner result screens.
 * Styled with a clean, flat design to match the rest of the application.
 */
public class SidebarPanel extends JPanel {

    private static final String[][] NAV_ITEMS = {
            {"🌱", "Crop\nRecommandation", AppFrame.CARD_CROP_REC},
            {"📅", "Crop\nScheduler",      AppFrame.CARD_SCHEDULER},
            {"📊", "Profit\nComparison",   AppFrame.CARD_PROFIT},
            {"📍", "Market\nOptimization", AppFrame.CARD_MARKET},
            {"✅", "Final\nRecommendation",AppFrame.CARD_FINAL}
    };

    public SidebarPanel(AppFrame frame, String activeCard) {
        setPreferredSize(new Dimension(200, 0));
        setBackground(UIConstants.SIDEBAR_BG);
        setLayout(new BorderLayout());

        // Logo + brand area (Solid White Background)
        JPanel logoArea = new JPanel();
        logoArea.setBackground(UIConstants.WHITE);
        logoArea.setLayout(new BoxLayout(logoArea, BoxLayout.Y_AXIS));
        logoArea.setBorder(BorderFactory.createEmptyBorder(18, 0, 18, 0));

        // Fixed JLabel Initialization
        JLabel logo = new JLabel("", SwingConstants.CENTER);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        try {
            // Loads the image directly from your resources folder
            java.net.URL imgUrl = getClass().getResource("/edited logo.png");
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                // Scales the image to 75x75 pixels so it fits perfectly in the sidebar
                Image scaledImg = icon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
                logo.setIcon(new ImageIcon(scaledImg));
            } else {
                // Fallback to emoji if the image file isn't found
                logo.setText("🌿");
                logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
            }
        } catch (Exception e) {
            logo.setText("🌿");
            e.printStackTrace();
        }

        JLabel brand1 = new JLabel("Smart Farming", SwingConstants.CENTER);
        brand1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        brand1.setForeground(UIConstants.MID_GREEN);
        brand1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel brand2 = new JLabel("Crop Assistant", SwingConstants.CENTER);
        brand2.setFont(UIConstants.FONT_SMALL);
        brand2.setForeground(UIConstants.DARK_TEXT);
        brand2.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoArea.add(logo);
        logoArea.add(Box.createVerticalStrut(4));
        logoArea.add(brand1);
        logoArea.add(brand2);

        // Nav buttons panel
        JPanel navPanel = new JPanel();
        navPanel.setBackground(UIConstants.SIDEBAR_BG);
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        for (String[] item : NAV_ITEMS) {
            String icon  = item[0];
            String label = item[1];
            String card  = item[2];
            boolean active = card.equals(activeCard);
            navPanel.add(buildNavItem(frame, icon, label, card, active));
        }

        // Back button
        JButton backBtn = UIConstants.makeButton("Back <");
        backBtn.setPreferredSize(new Dimension(150, 38));
        backBtn.setMaximumSize(new Dimension(170, 38));
        backBtn.addActionListener(e -> frame.showCard(AppFrame.CARD_INPUT));

        JPanel backWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backWrap.setOpaque(false);
        backWrap.add(backBtn);

        add(logoArea, BorderLayout.NORTH);
        add(navPanel, BorderLayout.CENTER);
        add(backWrap, BorderLayout.SOUTH);
    }

    private JPanel buildNavItem(AppFrame frame, String icon, String label, String card, boolean active) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8)) {
            @Override protected void paintComponent(Graphics g) {
                if (active) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(UIConstants.MID_GREEN);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    super.paintComponent(g);
                }
            }
        };
        item.setBackground(UIConstants.SIDEBAR_BG);
        item.setMaximumSize(new Dimension(200, 56));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLbl.setForeground(active ? UIConstants.WHITE : UIConstants.DARK_TEXT);

        // Multi-line label
        String[] parts = label.split("\n");
        JPanel textBox = new JPanel();
        textBox.setOpaque(false);
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
        for (String part : parts) {
            JLabel l = new JLabel(part);
            l.setFont(UIConstants.FONT_SMALL);
            l.setForeground(active ? UIConstants.WHITE : UIConstants.DARK_TEXT);
            textBox.add(l);
        }

        item.add(iconLbl);
        item.add(textBox);

        item.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { frame.showCard(card); }
            @Override public void mouseEntered(MouseEvent e) {
                if (!active) item.setBackground(UIConstants.LIGHT_GREEN);
            }
            @Override public void mouseExited(MouseEvent e) {
                if (!active) item.setBackground(UIConstants.SIDEBAR_BG);
            }
        });

        return item;
    }
}