package com.smartfarming.ui;

import com.smartfarming.model.Farm;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.io.File;

/**
 * InputFormPanel: UI panel for Smart Farming Crop Planning Assistant
 * Collects farm details: soil type, rainfall, season, location
 */
public class InputFormPanel extends JPanel {

    // UI components
    private JComboBox<String> soilCombo;
    private JComboBox<String> rainfallCombo;
    private JRadioButton yalaBtn;
    private JRadioButton mahaBtn;
    private JComboBox<String> locationField;

    // Background image
    private Image backgroundImage;
    private boolean imageLoaded = false;

    public InputFormPanel(AppFrame frame) {
        // Load background image with better error handling
        loadBackgroundImage();

        setLayout(new BorderLayout());

        /* ================= TOP HEADER ================= */
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIConstants.DARK_GREEN);

        // Left section with SCA Home
        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftHeader.setOpaque(false);
        JLabel homeLabel = new JLabel("SCA Home");
        homeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        homeLabel.setForeground(Color.WHITE);
        leftHeader.add(homeLabel);
        topPanel.add(leftHeader, BorderLayout.WEST);

        // Create center header using helper method
        JPanel centerHeader = createCenterHeader();
        topPanel.add(centerHeader, BorderLayout.CENTER);

        // Right section (empty for balance)
        JPanel rightHeader = new JPanel();
        rightHeader.setOpaque(false);
        rightHeader.setPreferredSize(new Dimension(100, 70));
        topPanel.add(rightHeader, BorderLayout.EAST);

        topPanel.setPreferredSize(new Dimension(0, 100));
        add(topPanel, BorderLayout.NORTH);

        /* ================= FORM PANEL ================= */
        // Initialize components
        soilCombo = new JComboBox<>(new String[]{"Select", "Loamy", "Clay", "Sandy"});
        rainfallCombo = new JComboBox<>(new String[]{"Select", "Low", "Medium", "High"});
        yalaBtn = new JRadioButton("Yala");
        mahaBtn = new JRadioButton("Maha");
        locationField = new JComboBox<>(new String[]{
                "Select", "Colombo", "Kandy", "Galle", "Jaffna",
                "Anuradhapura", "Batticaloa", "Kurunegala", "Matara"
        });

        // ✅ Frosted glass effect - semi-transparent panel so background image shows through
        JPanel formContainer = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 180)); // white with transparency
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // rounded corners
                g2d.dispose();
            }
        };
        formContainer.setOpaque(false); // ✅ transparent so background shows through
        formContainer.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Title: "Farm Input Details"
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel formTitle = new JLabel("Farm Input Details");
        formTitle.setFont(UIConstants.FONT_TITLE);
        formTitle.setForeground(UIConstants.DARK_GREEN);
        formContainer.add(formTitle, gbc);

        // Reset gridwidth
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Soil Type
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel soilLabel = new JLabel("Soil Type :");
        soilLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        soilLabel.setForeground(Color.BLACK);
        formContainer.add(soilLabel, gbc);

        gbc.gridx = 1;
        soilCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        soilCombo.setPreferredSize(new Dimension(200, 30));
        soilCombo.setBackground(Color.WHITE);
        formContainer.add(soilCombo, gbc);

        // Rainfall Level
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel rainfallLabel = new JLabel("Rainfall Level :");
        rainfallLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        rainfallLabel.setForeground(Color.BLACK);
        formContainer.add(rainfallLabel, gbc);

        gbc.gridx = 1;
        rainfallCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        rainfallCombo.setPreferredSize(new Dimension(200, 30));
        rainfallCombo.setBackground(Color.WHITE);
        formContainer.add(rainfallCombo, gbc);

        // Season
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel seasonLabel = new JLabel("Season :");
        seasonLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        seasonLabel.setForeground(Color.BLACK);
        formContainer.add(seasonLabel, gbc);

        gbc.gridx = 1;
        JPanel seasonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        seasonPanel.setOpaque(false);

        yalaBtn.setSelected(true);

        // Style radio buttons
        yalaBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        mahaBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        yalaBtn.setOpaque(false);
        mahaBtn.setOpaque(false);
        yalaBtn.setBackground(new Color(255, 255, 255, 0));
        mahaBtn.setBackground(new Color(255, 255, 255, 0));

        ButtonGroup bg = new ButtonGroup();
        bg.add(yalaBtn);
        bg.add(mahaBtn);

        seasonPanel.add(yalaBtn);
        seasonPanel.add(mahaBtn);
        formContainer.add(seasonPanel, gbc);

        // Farm Location
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel locationLabel = new JLabel("Farm Location :");
        locationLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        locationLabel.setForeground(Color.BLACK);
        formContainer.add(locationLabel, gbc);

        gbc.gridx = 1;
        locationField.setFont(new Font("Arial", Font.PLAIN, 14));
        locationField.setPreferredSize(new Dimension(200, 30));
        locationField.setBackground(Color.WHITE);
        formContainer.add(locationField, gbc);

        // Analyze button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);

        JButton analyzeBtn = new JButton("Analyze");
        analyzeBtn.setFont(new Font("Arial", Font.BOLD, 18));
        analyzeBtn.setForeground(Color.WHITE);
        analyzeBtn.setBackground(new Color(0, 120, 0));
        analyzeBtn.setPreferredSize(new Dimension(250, 40));
        analyzeBtn.setFocusPainted(false);
        analyzeBtn.setBorderPainted(false);

        // Add hover effect
        analyzeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                analyzeBtn.setBackground(new Color(0, 100, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                analyzeBtn.setBackground(new Color(0, 120, 0));
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(analyzeBtn);
        formContainer.add(buttonPanel, gbc);

        // Action listener
        analyzeBtn.addActionListener(e -> {
            String soil = Objects.requireNonNullElse((String) soilCombo.getSelectedItem(), "Select");
            String rain = Objects.requireNonNullElse((String) rainfallCombo.getSelectedItem(), "Select");
            String season = yalaBtn.isSelected() ? "Yala" : "Maha";
            String loc = Objects.requireNonNullElse((String) locationField.getSelectedItem(), "Select");

            if ("Select".equals(soil)) soil = "Not Specified";
            if ("Select".equals(rain)) rain = "Not Specified";
            if ("Select".equals(loc)) loc = "Unknown";

            frame.runAnalysis(new Farm(soil, rain, season, loc));
        });

        // ✅ Center panel transparent so background image shows through
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(formContainer);
        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Creates the centered header panel
     */
    private JPanel createCenterHeader() {
        JPanel centerHeader = new JPanel();
        centerHeader.setOpaque(false);
        centerHeader.setLayout(new BoxLayout(centerHeader, BoxLayout.Y_AXIS));

        JLabel mainTitle = new JLabel("Smart Farming");
        mainTitle.setFont(new Font("Arial", Font.BOLD, 28));
        mainTitle.setForeground(Color.WHITE);
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitle = new JLabel("Crop Assistant");
        subTitle.setFont(new Font("Arial", Font.PLAIN, 24));
        subTitle.setForeground(Color.WHITE);
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerHeader.add(mainTitle);
        centerHeader.add(subTitle);

        return centerHeader;
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

            // Subtle dark overlay for better form readability
            g2d.setColor(new Color(0, 0, 0, 40));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        } else {
            // Gradient fallback if image not found
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