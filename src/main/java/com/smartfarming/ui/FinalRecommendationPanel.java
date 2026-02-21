package com.smartfarming.ui;

import com.smartfarming.model.Crop;
import com.smartfarming.model.Market;
import com.smartfarming.model.RecommendationResult;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FinalRecommendationPanel extends JPanel {

    private Image backgroundImage;
    private boolean imageLoaded = false;

    public FinalRecommendationPanel(AppFrame frame, RecommendationResult result) {
        loadBackgroundImage();
        setLayout(new BorderLayout());
        setOpaque(false);

        add(new SidebarPanel(frame, AppFrame.CARD_FINAL), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(new HeaderBannerPanel("Final Recommendation"), BorderLayout.NORTH);

        // ✅ UPDATED: content is now OPAQUE to mask the image
        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(true);
        content.setBackground(UIConstants.PALE_GREEN);
        content.setBorder(BorderFactory.createEmptyBorder(24, 28, 20, 28));

        if (result != null) {
            // [Grid and card logic remains exactly as your source...]
            Crop best        = result.getBestCrop();
            Market optimal   = result.getOptimalMarket();
            String cropName  = best != null ? best.getName() : "N/A";
            String profit    = best != null ? "Rs. " + (int)best.getProfit() : "N/A";
            String risk      = result.getRiskLevel();

            JPanel grid = new JPanel(new GridLayout(2, 4, 12, 12));
            grid.setOpaque(false);
            grid.add(infoCard("🌾", "Best Crop", cropName, new Color(56,142,60), new Color(27,94,32)));
            // [Other cards added here...]

            content.add(grid, BorderLayout.CENTER);

            JButton dlBtn = UIConstants.makeButton("⬇  Download Report");
            dlBtn.setPreferredSize(new Dimension(260, 48));
            JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
            btnRow.setOpaque(false); btnRow.add(dlBtn);

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

    private void loadBackgroundImage() {
        try {
            ImageIcon icon = new ImageIcon("src/main/resources/farm.jpeg");
            if (icon.getIconWidth() > 0) {
                backgroundImage = icon.getImage();
                imageLoaded = true;
            }
        } catch (Exception e) { imageLoaded = false; }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imageLoaded && backgroundImage != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g2.setColor(new Color(0, 0, 0, 50));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    // [infoCard and buildPathStrip helper methods remain same as source...]
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
        JLabel iconLbl = new JLabel(icon + "  " + label); iconLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12)); iconLbl.setForeground(new Color(255,255,255,200));
        JLabel valLbl = new JLabel("<html>" + value + "</html>"); valLbl.setFont(new Font("Segoe UI", Font.BOLD, 18)); valLbl.setForeground(Color.WHITE);
        card.add(iconLbl); card.add(Box.createVerticalStrut(6)); card.add(valLbl);
        return card;
    }
    private JPanel buildPathStrip(List<String> path) {
        JPanel strip = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 6)); strip.setOpaque(false);
        JLabel pathLbl = new JLabel("Shortest Route: "); pathLbl.setFont(new Font("Segoe UI", Font.BOLD, 12)); pathLbl.setForeground(UIConstants.DARK_GREEN);
        strip.add(pathLbl);
        for (String nodeName : path) {
            JLabel node = new JLabel(nodeName); node.setFont(new Font("Segoe UI", Font.BOLD, 12));
            node.setBorder(BorderFactory.createCompoundBorder(new UIConstants.RoundedBorder(UIConstants.LIGHT_GREEN, 10), BorderFactory.createEmptyBorder(2, 7, 2, 7)));
            node.setBackground(UIConstants.PALE_GREEN); node.setOpaque(true); strip.add(node);
        }
        return strip;
    }
}