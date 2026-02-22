package com.smartfarming.ui;

import com.smartfarming.model.Crop;
import com.smartfarming.model.RecommendationResult;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class CropRecommendationPanel extends JPanel {

    private Image backgroundImage;
    private boolean imageLoaded = false;

    public CropRecommendationPanel(AppFrame frame, RecommendationResult result) {
        loadBackgroundImage();
        setLayout(new BorderLayout());
        setOpaque(false); // Base is transparent for the background image

        add(new SidebarPanel(frame, AppFrame.CARD_CROP_REC), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(new HeaderBannerPanel("Crop Recommendation"), BorderLayout.NORTH);

        // ✅ UPDATED: content is now OPAQUE to mask the image in the detail area
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(true);
        content.setBackground(UIConstants.PALE_GREEN);
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        if (result != null) {
            List<Crop> crops = result.getRecommendedCrops();
            for (Crop crop : crops) {
                content.add(buildCropCard(crop));
                content.add(Box.createVerticalStrut(14));
            }

            JPanel tipCard = buildTipCard(result.getSoilTip());
            content.add(Box.createVerticalStrut(6));
            content.add(tipCard);
            content.add(Box.createVerticalStrut(20));

            JButton reportBtn = UIConstants.makeButton("Get Detailed Report  >");
            reportBtn.setPreferredSize(new Dimension(260, 48));
            reportBtn.setMaximumSize(new Dimension(280, 50));
            reportBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            reportBtn.addActionListener(e -> frame.showCard(AppFrame.CARD_FINAL));
            content.add(reportBtn);
        } else {
            JLabel placeholder = new JLabel("No data yet — fill in the form first.", SwingConstants.CENTER);
            placeholder.setFont(UIConstants.FONT_BODY);
            placeholder.setForeground(UIConstants.DARK_TEXT);
            content.add(placeholder);
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        main.add(scroll, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    private JPanel buildCropCard(Crop crop) {
        boolean high = crop.getSuitabilityLevel().equals("Highly Suitable");
        // Keep cards slightly translucent for a modern feel against the solid background
        Color bg = high ? new Color(255, 255, 255, 220) : new Color(255, 255, 255, 180);
        Color bar = high ? UIConstants.DARK_GREEN : UIConstants.MID_GREEN;
        int barWidth = high ? 260 : 160;

        JPanel card = new JPanel(new BorderLayout(14, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(100, 180, 100, 120));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel icon = new JLabel(high ? "🌾" : "🌽", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel nameLbl = new JLabel("<html><b>" + crop.getName() + "</b> - " + crop.getSuitabilityLevel() + "</html>");
        nameLbl.setFont(UIConstants.FONT_SUBHEAD);
        nameLbl.setForeground(UIConstants.DARK_GREEN);
        textPanel.add(nameLbl);

        JPanel barPanel = new JPanel() {
            @Override public Dimension getPreferredSize() { return new Dimension(barWidth, 12); }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(180, 220, 180, 150));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(bar);
                g2.fillRoundRect(0, 0, barWidth, getHeight(), 8, 8);
                g2.dispose();
            }
        };
        barPanel.setOpaque(false);
        textPanel.add(Box.createVerticalStrut(6));
        textPanel.add(barPanel);

        card.add(icon, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(card);
        return wrapper;
    }

    private JPanel buildTipCard(String tip) {
        JPanel card = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 12)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(new Color(100, 180, 100, 150));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel title = new JLabel("<html><b>Soil Improvement Tips:</b></html>");
        title.setFont(UIConstants.FONT_SUBHEAD);
        title.setForeground(UIConstants.DARK_GREEN);

        JLabel leaf = new JLabel("🍃");
        leaf.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));

        JLabel tipLbl = new JLabel("<html>" + tip + "</html>");
        tipLbl.setFont(UIConstants.FONT_BODY);
        tipLbl.setForeground(Color.DARK_GRAY);

        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        wrapper.add(title);
        wrapper.add(Box.createVerticalStrut(4));

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);
        row.add(leaf); row.add(tipLbl);
        wrapper.add(row);

        card.add(wrapper);
        return card;
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
}