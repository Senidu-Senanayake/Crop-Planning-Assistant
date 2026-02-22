package com.smartfarming.ui;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    public HomePanel(AppFrame frame) {
        setLayout(new BorderLayout());
        setBackground(UIConstants.DARK_GREEN);

        // --- 1. Rich Background Gradient & Shapes ---
        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Deep, premium gradient
                GradientPaint gp = new GradientPaint(0, 0, new Color(10, 45, 15),
                        getWidth(), getHeight(), new Color(30, 90, 40));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Modern decorative overlapping rings
                g2.setColor(new Color(255, 255, 255, 12));
                g2.fillOval(-150, -150, 600, 600);
                g2.setColor(new Color(255, 255, 255, 8));
                g2.fillOval(getWidth() - 350, getHeight() - 350, 700, 700);

                // Inner depth ring
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(new Color(0, 0, 0, 30));
                g2.drawOval(getWidth() - 380, getHeight() - 380, 760, 760);
            }
        };
        bg.setLayout(new GridBagLayout()); // Keeps the card perfectly centered

        // --- 2. Frosted Glass Card Container ---
        JPanel glassCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Glass fill
                g2.setColor(new Color(255, 255, 255, 25));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);

                // Soft glow border
                g2.setColor(new Color(255, 255, 255, 60));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);

                // Drop shadow line at the bottom for depth
                g2.setColor(new Color(0, 0, 0, 40));
                g2.drawLine(20, getHeight() - 1, getWidth() - 20, getHeight() - 1);

                g2.dispose();
            }
        };
        glassCard.setOpaque(false);
        glassCard.setLayout(new BoxLayout(glassCard, BoxLayout.Y_AXIS));
        glassCard.setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80)); // Padding inside the card

        // --- 3. Custom Logo Loading ---
        JLabel logoLbl = new JLabel("", SwingConstants.CENTER);
        logoLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon icon = new ImageIcon("src/main/resources/logo.png");
            if (icon.getIconWidth() > 0) {
                // Scaled larger (130x130) for the hero section
                Image scaledImg = icon.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
                logoLbl.setIcon(new ImageIcon(scaledImg));
            } else {
                logoLbl.setText("🌱");
                logoLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 90));
            }
        } catch (Exception e) {
            logoLbl.setText("🌱");
            logoLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 90));
        }

        // --- 4. Premium Typography with Drop Shadows ---
        JLabel titleBold = createShadowText("Smart Farming", new Font("Segoe UI", Font.BOLD, 46));
        JLabel titleNorm = createShadowText("Crop Planning Assistant", new Font("Segoe UI", Font.PLAIN, 36));

        // Divider
        JSeparator div = new JSeparator();
        div.setForeground(new Color(255, 255, 255, 120));
        div.setMaximumSize(new Dimension(500, 2));

        // Subtitle
        JLabel sub = new JLabel("Optimize your crop yield with smart data driven decisions", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub.setForeground(new Color(210, 245, 210));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Start button
        JButton startBtn = UIConstants.makeButton("Start  >");
        startBtn.setPreferredSize(new Dimension(240, 52));
        startBtn.setMaximumSize(new Dimension(260, 52));
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.addActionListener(e -> frame.showCard(AppFrame.CARD_INPUT));

        // --- Assemble the Card ---
        glassCard.add(logoLbl);
        glassCard.add(Box.createVerticalStrut(15));
        glassCard.add(titleBold);
        glassCard.add(titleNorm);
        glassCard.add(Box.createVerticalStrut(15));
        glassCard.add(div);
        glassCard.add(Box.createVerticalStrut(15));
        glassCard.add(sub);
        glassCard.add(Box.createVerticalStrut(35));
        glassCard.add(startBtn);

        bg.add(glassCard);
        add(bg, BorderLayout.CENTER);
    }

    // Helper method to create clean drop-shadow text
    private JLabel createShadowText(String text, Font font) {
        JLabel label = new JLabel(text, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                // Draw the shadow first
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = fm.getAscent() + (getHeight() - fm.getHeight()) / 2;

                g2.setColor(new Color(0, 0, 0, 100)); // Dark shadow color
                g2.drawString(getText(), x + 2, y + 2); // Offset by 2 pixels
                g2.dispose();

                // Let Java draw the main white text perfectly on top
                super.paintComponent(g);
            }
        };
        label.setFont(font);
        label.setForeground(UIConstants.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setOpaque(false); // Important to ensure the shadow shows through
        return label;
    }
}