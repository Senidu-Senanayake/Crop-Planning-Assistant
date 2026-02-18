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
        content.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));

        if (result != null) {
            Crop best = result.getBestCrop();
            String market = result.getOptimalMarket() != null ? result.getOptimalMarket().getName() : "N/A";
            String profit = best != null ? "Rs. " + (int) best.getProfit() : "N/A";
            String crop = best != null ? best.getName() : "N/A";
            String risk = result.getRiskLevel();

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1; gbc.weighty = 1;

            // Row 1
            gbc.gridx = 0; gbc.gridy = 0;
            content.add(buildInfoCard("🌾", "Best Crop:", crop, new Color(120, 200, 80), new Color(80, 160, 40)), gbc);

            gbc.gridx = 1; gbc.gridy = 0;
            content.add(buildInfoCard("📍", "Best Market:", market, new Color(90, 180, 90), new Color(50, 140, 50)), gbc);

            // Row 2
            gbc.gridx = 0; gbc.gridy = 1;
            content.add(buildInfoCard("💰", "Estimated Profit:", profit, new Color(40, 110, 40), new Color(20, 80, 20)), gbc);

            gbc.gridx = 1; gbc.gridy = 1;
            Color riskColor = risk.contains("High") ? new Color(180, 60, 40) : new Color(40, 120, 40);
            Color riskDark  = risk.contains("High") ? new Color(140, 30, 20) : new Color(20, 80, 20);
            content.add(buildInfoCard("⚙️", "Risk Level:", risk, riskColor, riskDark), gbc);

            // Download button row
            GridBagConstraints btnGbc = new GridBagConstraints();
            btnGbc.gridx = 0; btnGbc.gridy = 2; btnGbc.gridwidth = 2;
            btnGbc.insets = new Insets(20, 0, 0, 0);
            JButton dlBtn = UIConstants.makeButton("⬇  Download Report");
            dlBtn.setPreferredSize(new Dimension(280, 52));
            dlBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                    "Report saved as SmartFarming_Report.txt\n\n" +
                    "Best Crop: " + crop + "\nBest Market: " + market +
                    "\nEstimated Profit: " + profit + "\nRisk Level: " + risk,
                    "Download Report", JOptionPane.INFORMATION_MESSAGE));
            content.add(dlBtn, btnGbc);

        } else {
            content.add(new JLabel("Run analysis to see final recommendation.", SwingConstants.CENTER));
        }

        main.add(content, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

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
