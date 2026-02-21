package com.smartfarming.ui;

import com.smartfarming.model.Crop;
import com.smartfarming.model.RecommendationResult;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class CropRecommendationPanel extends JPanel {

    // Background image
    private Image backgroundImage;
    private boolean imageLoaded = false;

    public CropRecommendationPanel(AppFrame frame, RecommendationResult result) {
        // Load background image
        loadBackgroundImage();

        setLayout(new BorderLayout());

        // Layout: sidebar + main content
        JPanel sidebar = new SidebarPanel(frame, AppFrame.CARD_CROP_REC);
        add(sidebar, BorderLayout.WEST);

        // ✅ Main panel transparent so background shows through
        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(new HeaderBannerPanel("Crop Recommendation"), BorderLayout.NORTH);

        // ✅ Content area transparent
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        if (result != null) {
            List<Crop> crops = result.getRecommendedCrops();
            for (Crop crop : crops) {
                content.add(buildCropCard(crop));
                content.add(Box.createVerticalStrut(14));
            }

            // Soil tip card
            JPanel tipCard = buildTipCard(result.getSoilTip());
            content.add(Box.createVerticalStrut(6));
            content.add(tipCard);
            content.add(Box.createVerticalStrut(20));

            // Detailed report button
            JButton reportBtn = UIConstants.makeButton("Get Detailed Report  >");
            reportBtn.setPreferredSize(new Dimension(260, 48));
            reportBtn.setMaximumSize(new Dimension(280, 50));
            reportBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            reportBtn.addActionListener(e -> frame.showCard(AppFrame.CARD_FINAL));
            content.add(reportBtn);
        } else {
            JLabel placeholder = new JLabel("No data yet — fill in the form first.", SwingConstants.CENTER);
            placeholder.setFont(UIConstants.FONT_BODY);
            placeholder.setForeground(Color.WHITE);
            content.add(placeholder);
        }

        // ✅ Scroll pane transparent
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        main.add(scroll, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    private JPanel buildCropCard(Crop crop) {
        boolean high = crop.getSuitabilityLevel().equals("Highly Suitable");
        // ✅ Frosted glass effect matching InputFormPanel style
        Color bg = high ? new Color(220, 255, 220, 200) : new Color(240, 255, 240, 180);
        Color bar = high ? UIConstants.DARK_GREEN : UIConstants.MID_GREEN;
        int barWidth = high ? 260 : 160;

        JPanel card = new JPanel(new BorderLayout(14, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                // subtle border
                g2.setColor(new Color(100, 180, 100, 120));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Icon
        JLabel icon = new JLabel(high ? "🌾" : "🌽", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        // Text
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel nameLbl = new JLabel("<html><b>" + crop.getName() + "</b> - " + crop.getSuitabilityLevel() + "</html>");
        nameLbl.setFont(UIConstants.FONT_SUBHEAD);
        nameLbl.setForeground(UIConstants.DARK_GREEN);
        textPanel.add(nameLbl);

        // Bar
        JPanel barPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() { return new Dimension(barWidth, 12); }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Background track
                g2.setColor(new Color(180, 220, 180, 150));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                // Filled portion
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
                // ✅ Frosted glass matching style
                g2.setColor(new Color(220, 245, 210, 200));
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

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);
        row.add(leaf);
        row.add(tipLbl);

        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        wrapper.add(title);
        wrapper.add(Box.createVerticalStrut(4));
        wrapper.add(row);

        card.add(wrapper);
        return card;
    }

    private void loadBackgroundImage() {
        try {
            String[] possiblePaths = {
                    "/farm.jpeg"
            };

            for (String path : possiblePaths) {
                java.net.URL imgURL = getClass().getResource(path);
                if (imgURL != null) {
                    backgroundImage = javax.imageio.ImageIO.read(imgURL);
                    imageLoaded = true;
                    System.out.println("Background image loaded from: " + path);
                    return;
                }
            }

            // Fallback: try file system
            String userDir = System.getProperty("user.dir");
            String[] filePaths = {
                    userDir + "/src/main/resources/farm.jpeg",
                    userDir + "/resources/farm.jpeg",
                    userDir + "/farm.jpeg"
            };

            for (String filePath : filePaths) {
                File imageFile = new File(filePath);
                if (imageFile.exists()) {
                    backgroundImage = Toolkit.getDefaultToolkit().getImage(imageFile.getAbsolutePath());
                    imageLoaded = true;
                    System.out.println("Background image loaded from: " + filePath);
                    return;
                }
            }

        } catch (Exception e) {
            System.out.println("Could not load background image: " + e.getMessage());
        }

        imageLoaded = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        if (imageLoaded && backgroundImage != null) {
            // ✅ Draw background image stretched to fill entire panel
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

            // Subtle dark overlay for readability
            g2d.setColor(new Color(0, 0, 0, 40));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        } else {
            // Gradient fallback
            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(220, 240, 220),
                    getWidth(), getHeight(), new Color(180, 210, 180)
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(new Color(200, 230, 200, 50));
            for (int i = 0; i < 5; i++) {
                int x = (i * 200) % getWidth();
                int y = (i * 150) % getHeight();
                g2d.fillOval(x, y, 100, 100);
            }
        }

        g2d.dispose();
    }
}