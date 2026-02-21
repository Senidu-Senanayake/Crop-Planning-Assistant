package com.smartfarming.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Left sidebar navigation used by all inner result screens.
 * Glassmorphism style - semi-transparent frosted glass effect.
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
        setOpaque(false); // ✅ transparent so background image shows through
        setLayout(new BorderLayout());

        // ✅ Logo + brand area - glassmorphism
        JPanel logoArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Frosted glass white tint
                g2.setColor(new Color(255, 255, 255, 60));
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Bottom separator line
                g2.setColor(new Color(255, 255, 255, 80));
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(10, getHeight() - 1, getWidth() - 10, getHeight() - 1);
                g2.dispose();
            }
        };
        logoArea.setOpaque(false);
        logoArea.setLayout(new BoxLayout(logoArea, BoxLayout.Y_AXIS));
        logoArea.setBorder(BorderFactory.createEmptyBorder(18, 0, 18, 0));

        JLabel logo = new JLabel("🌿", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel brand1 = new JLabel("Smart Farming", SwingConstants.CENTER);
        brand1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        brand1.setForeground(Color.WHITE);
        brand1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel brand2 = new JLabel("Crop Assistant", SwingConstants.CENTER);
        brand2.setFont(UIConstants.FONT_SMALL);
        brand2.setForeground(new Color(220, 240, 220));
        brand2.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoArea.add(logo);
        logoArea.add(Box.createVerticalStrut(4));
        logoArea.add(brand1);
        logoArea.add(brand2);

        // ✅ Nav buttons panel
        JPanel navPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 40, 0, 80));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        navPanel.setOpaque(false);
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        for (String[] item : NAV_ITEMS) {
            String icon  = item[0];
            String label = item[1];
            String card  = item[2];
            boolean active = card.equals(activeCard);
            navPanel.add(buildNavItem(frame, icon, label, card, active));
        }

        // ✅ Back button area
        JButton backBtn = UIConstants.makeButton("Back <");
        backBtn.setPreferredSize(new Dimension(150, 38));
        backBtn.setMaximumSize(new Dimension(170, 38));
        backBtn.addActionListener(e -> frame.showCard(AppFrame.CARD_INPUT));

        JPanel backWrap = new JPanel(new FlowLayout(FlowLayout.CENTER)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Top border line
                g2.setColor(new Color(255, 255, 255, 80));
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(10, 0, getWidth() - 10, 0);
                // Subtle glass tint
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        backWrap.setOpaque(false);
        backWrap.add(backBtn);

        // Right border glass line for the whole sidebar
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(255, 255, 255, 60)));

        add(logoArea, BorderLayout.NORTH);
        add(navPanel, BorderLayout.CENTER);
        add(backWrap, BorderLayout.SOUTH);
    }

    private JPanel buildNavItem(AppFrame frame, String icon, String label, String card, boolean active) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (active) {
                    // ✅ Active - bright frosted glass with green tint + left accent bar
                    g2.setColor(new Color(255, 255, 255, 60));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(new Color(80, 200, 80, 120));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    // Left accent bar
                    g2.setColor(new Color(150, 255, 150, 220));
                    g2.fillRect(0, 0, 4, getHeight());
                } else {
                    // Subtle glass
                    g2.setColor(new Color(255, 255, 255, 10));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
                // Bottom divider line
                g2.setColor(new Color(255, 255, 255, 30));
                g2.drawLine(10, getHeight() - 1, getWidth() - 10, getHeight() - 1);
                g2.dispose();
            }
        };
        item.setOpaque(false);
        item.setMaximumSize(new Dimension(200, 56));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLbl.setForeground(Color.WHITE);

        // Multi-line label
        String[] parts = label.split("\n");
        JPanel textBox = new JPanel();
        textBox.setOpaque(false);
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
        for (String part : parts) {
            JLabel l = new JLabel(part);
            l.setFont(UIConstants.FONT_SMALL);
            l.setForeground(active ? Color.WHITE : new Color(220, 240, 220));
            textBox.add(l);
        }

        item.add(iconLbl);
        item.add(textBox);

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { frame.showCard(card); }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!active) item.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!active) item.repaint();
            }
        });

        return item;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // ✅ Main sidebar glass layer - dark green tint with transparency
        g2.setColor(new Color(10, 60, 10, 160));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
}