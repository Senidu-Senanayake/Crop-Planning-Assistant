package com.smartfarming.ui;

import com.smartfarming.model.Crop;
import com.smartfarming.model.Market;
import com.smartfarming.model.RecommendationResult;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FinalRecommendationPanel extends JPanel {

    public FinalRecommendationPanel(AppFrame frame, RecommendationResult result) {
        setLayout(new BorderLayout());
        setBackground(UIConstants.PALE_GREEN);

        add(new SidebarPanel(frame, AppFrame.CARD_FINAL), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UIConstants.PALE_GREEN);
        main.add(new HeaderBannerPanel("Final Recommendation"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UIConstants.PALE_GREEN);
        content.setBorder(BorderFactory.createEmptyBorder(24, 28, 20, 28));

        if (result != null) {
            Crop best        = result.getBestCrop();
            Market optimal   = result.getOptimalMarket();
            String cropName  = best != null ? best.getName()              : "N/A";
            String profit    = best != null ? "Rs. " + (int)best.getProfit() : "N/A";
            String marketName= optimal != null ? optimal.getName()        : "N/A";
            String distance  = optimal != null ? String.format("%.0f km", optimal.getDistanceKm()) : "N/A";
            String cost      = optimal != null ? String.format("Rs. %.0f", optimal.getTransportCost()) : "N/A";
            String priceKg   = optimal != null && optimal.getPricePerKg() != null ? optimal.getPricePerKg() : "N/A";
            String risk      = result.getRiskLevel();

            // 4-card grid
            JPanel grid = new JPanel(new GridLayout(2, 4, 12, 12));
            grid.setOpaque(false);

            grid.add(infoCard("🌾", "Best Crop",        cropName,  new Color(56,142,60),  new Color(27,94,32)));
            grid.add(infoCard("📍", "Best Market",      marketName,new Color(25,118,210),  new Color(13,71,161)));
            grid.add(infoCard("💰", "Estimated Profit", profit,    new Color(40,110,40),   new Color(20,80,20)));
            grid.add(infoCard("⚙", "Risk Level",        risk,
                    risk.contains("High") ? new Color(198,40,40) : new Color(56,142,60),
                    risk.contains("High") ? new Color(140,20,20) : new Color(27,94,32)));
            grid.add(infoCard("📏", "Distance",         distance,  new Color(100,60,160),  new Color(70,30,120)));
            grid.add(infoCard("🚚", "Transport Cost",   cost,      new Color(245,124,0),   new Color(191,84,0)));
            grid.add(infoCard("💲", "Market Price/kg",  priceKg,   new Color(0,131,143),   new Color(0,96,100)));
            grid.add(infoCard("📊", "Market Demand",    optimal != null ? optimal.getDemand() : "N/A",
                    new Color(56,142,60), new Color(27,94,32)));

            content.add(grid, BorderLayout.CENTER);

            // Path summary strip
            if (optimal != null && optimal.getShortestPath() != null) {
                List<String> path = optimal.getShortestPath();
                JPanel pathStrip = buildPathStrip(path);
                content.add(pathStrip, BorderLayout.SOUTH);
            }

            // Download button
            JButton dlBtn = UIConstants.makeButton("⬇  Download Report");
            dlBtn.setPreferredSize(new Dimension(260, 48));
            dlBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(frame,
                        "=== Smart Farming Report ===\n\n" +
                                "Best Crop      : " + cropName + "\n" +
                                "Best Market    : " + marketName + "\n" +
                                "Distance       : " + distance + "\n" +
                                "Transport Cost : " + cost + "\n" +
                                "Profit         : " + profit + "\n" +
                                "Risk Level     : " + risk + "\n" +
                                "Soil Tip       : " + result.getSoilTip(),
                        "Report Summary", JOptionPane.INFORMATION_MESSAGE);
            });
            JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
            btnRow.setOpaque(false);
            btnRow.add(dlBtn);

            JPanel south = new JPanel(new BorderLayout());
            south.setOpaque(false);
            if (optimal != null && optimal.getShortestPath() != null) {
                south.add(buildPathStrip(optimal.getShortestPath()), BorderLayout.NORTH);
            }
            south.add(btnRow, BorderLayout.SOUTH);
            content.add(south, BorderLayout.SOUTH);

        } else {
            content.add(new JLabel("Run analysis to see final recommendation.", SwingConstants.CENTER));
        }

        main.add(content, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    private JPanel infoCard(String icon, String label, String value, Color top, Color bottom) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0,0,top,0,getHeight(),bottom));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),18,18);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(12,14,12,14));

        JLabel iconLbl = new JLabel(icon + "  " + label);
        iconLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        iconLbl.setForeground(new Color(255,255,255,200));
        iconLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valLbl = new JLabel("<html>" + value + "</html>");
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valLbl.setForeground(Color.WHITE);
        valLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(iconLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(valLbl);
        return card;
    }

    private JPanel buildPathStrip(List<String> path) {
        JPanel strip = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 6));
        strip.setOpaque(false);
        strip.setBorder(BorderFactory.createEmptyBorder(10, 0, 4, 0));

        JLabel pathLbl = new JLabel("Shortest Route: ");
        pathLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pathLbl.setForeground(UIConstants.DARK_GREEN);
        strip.add(pathLbl);

        for (int i = 0; i < path.size(); i++) {
            JLabel node = new JLabel(path.get(i));
            node.setFont(new Font("Segoe UI", Font.BOLD, 12));
            node.setForeground(i == path.size()-1 ? UIConstants.DARK_GREEN : UIConstants.DARK_TEXT);
            node.setBorder(BorderFactory.createCompoundBorder(
                    new UIConstants.RoundedBorder(UIConstants.LIGHT_GREEN, 10),
                    BorderFactory.createEmptyBorder(2, 7, 2, 7)));
            node.setBackground(i == path.size()-1 ? UIConstants.LIGHT_GREEN : UIConstants.PALE_GREEN);
            node.setOpaque(true);
            strip.add(node);
            if (i < path.size()-1) {
                JLabel arrow = new JLabel(" → ");
                arrow.setFont(new Font("Segoe UI", Font.BOLD, 13));
                arrow.setForeground(UIConstants.MID_GREEN);
                strip.add(arrow);
            }
        }
        return strip;
    }
}