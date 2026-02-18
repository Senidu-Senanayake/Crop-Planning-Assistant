package com.smartfarming.ui;

import com.smartfarming.model.Crop;
import com.smartfarming.model.RecommendationResult;

import javax.swing.*;
import java.awt.*;

public class FinalRecommendationPanel extends JPanel {

    public FinalRecommendationPanel(AppFrame frame, RecommendationResult result) {
        setLayout(new BorderLayout());
        setBackground(UIConstants.PALE_GREEN);

        add(new SidebarPanel(frame, AppFrame.CARD_FINAL), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UIConstants.PALE_GREEN);
        main.add(new HeaderBannerPanel("Final Recommendation"), BorderLayout.NORTH);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(UIConstants.PALE_GREEN);
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        if (result != null) {
            Crop best = result.getBestCrop();
            String market = result.getOptimalMarket() != null ? result.getOptimalMarket().getName() : "N/A";
            String profit = best != null ? "Rs. " + (int) best.getProfit() : "N/A";
            String crop = best != null ? best.getName() : "N/A";
            String risk = result.getRiskLevel();

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 10, 8, 10);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1;
            gbc.weighty = 0; // will be overridden for rows that need expansion

            int gridy = 0;

            // ---- Crop Recommendation section ----
            gbc.gridx = 0;
            gbc.gridy = gridy++;
            gbc.gridwidth = 2;
            gbc.weighty = 0;
            content.add(createSectionHeader("Crop Recommendation"), gbc);

            // Crop & Market cards
            gbc.gridwidth = 1;
            gbc.weighty = 1; // cards can expand vertically
            gbc.gridx = 0;
            gbc.gridy = gridy;
            content.add(buildInfoCard("🌾", "Best Crop:", crop, new Color(120, 200, 80), new Color(80, 160, 40)), gbc);

            gbc.gridx = 1;
            gbc.gridy = gridy;
            content.add(buildInfoCard("📍", "Best Market:", market, new Color(90, 180, 90), new Color(50, 140, 50)), gbc);
            gridy++; // move to next row after cards

            // ---- Profit Comparison section ----
            gbc.gridx = 0;
            gbc.gridy = gridy++;
            gbc.gridwidth = 2;
            gbc.weighty = 0;
            content.add(createSectionHeader("Profit Comparison"), gbc);

            // Profit & Risk cards
            gbc.gridwidth = 1;
            gbc.weighty = 1;
            gbc.gridx = 0;
            gbc.gridy = gridy;
            content.add(buildInfoCard("💰", "Estimated Profit:", profit, new Color(40, 110, 40), new Color(20, 80, 20)), gbc);

            gbc.gridx = 1;
            gbc.gridy = gridy;
            Color riskColor = risk.contains("High") ? new Color(180, 60, 40) : new Color(40, 120, 40);
            Color riskDark  = risk.contains("High") ? new Color(140, 30, 20) : new Color(20, 80, 20);
            content.add(buildInfoCard("⚙️", "Risk Level:", risk, riskColor, riskDark), gbc);
            gridy++;

            // ---- Market Optimization section ----
            gbc.gridx = 0;
            gbc.gridy = gridy++;
            gbc.gridwidth = 2;
            gbc.weighty = 0;
            content.add(createSectionHeader("Market Optimization"), gbc);

            // Placeholder panel for market optimization (can be replaced later)
            JPanel marketPlaceholder = new JPanel(new GridBagLayout());
            marketPlaceholder.setBackground(UIConstants.PALE_GREEN);
            marketPlaceholder.setPreferredSize(new Dimension(400, 80));
            JLabel marketPlaceholderLabel = new JLabel("Market optimization details will appear here");
            marketPlaceholderLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            marketPlaceholderLabel.setForeground(new Color(80, 80, 80));
            marketPlaceholder.add(marketPlaceholderLabel);
            gbc.gridx = 0;
            gbc.gridy = gridy++;
            gbc.gridwidth = 2;
            gbc.weighty = 0.2; // small vertical space
            content.add(marketPlaceholder, gbc);

            // ---- Final Recommendation section & Download button ----
            gbc.gridx = 0;
            gbc.gridy = gridy++;
            gbc.gridwidth = 2;
            gbc.weighty = 0;
            content.add(createSectionHeader("Final Recommendation"), gbc);

            // Download button
            JButton dlBtn = UIConstants.makeButton("⬇  Download Report");
            dlBtn.setPreferredSize(new Dimension(280, 52));
            dlBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                    "Report saved as SmartFarming_Report.txt\n\n" +
                            "Best Crop: " + crop + "\nBest Market: " + market +
                            "\nEstimated Profit: " + profit + "\nRisk Level: " + risk,
                    "Download Report", JOptionPane.INFORMATION_MESSAGE));

            gbc.gridx = 0;
            gbc.gridy = gridy;
            gbc.gridwidth = 2;
            gbc.weighty = 0;
            gbc.insets = new Insets(20, 10, 10, 10);
            content.add(dlBtn, gbc);

        } else {
            content.add(new JLabel("Run analysis to see final recommendation.", SwingConstants.CENTER));
        }

        main.add(content, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    /** Creates a styled section header label */
    private JLabel createSectionHeader(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(30, 80, 30)); // dark green
        label.setBorder(BorderFactory.createEmptyBorder(15, 5, 5, 5));
        return label;
    }

    /** Builds a gradient info card (unchanged from original) */
    private JPanel buildInfoCard(String icon, String label, String value,
                                 Color colorTop, Color colorBottom) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, colorTop, 0, getHeight(), colorBottom);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setOpaque(false);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(200, 130));
        card.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel iconLbl = new JLabel(icon + "  " + label);
        iconLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        iconLbl.setForeground(UIConstants.WHITE);
        iconLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valLbl.setForeground(UIConstants.WHITE);
        valLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        inner.add(iconLbl);
        inner.add(Box.createVerticalStrut(10));
        inner.add(valLbl);

        card.add(inner);
        return card;
    }
}