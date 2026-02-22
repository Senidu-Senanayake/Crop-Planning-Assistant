package com.smartfarming.ui;

import com.smartfarming.datastructures.MarketGraph;
import com.smartfarming.model.Farm;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * InputFormPanel – collects Soil Type, Rainfall, Season, and District.
 * The district dropdown is populated from MarketGraph.getAllDistricts() so it
 * always stays in sync with the graph nodes.
 */
public class InputFormPanel extends JPanel {

    private JComboBox<String> soilCombo;
    private JComboBox<String> rainfallCombo;
    private JRadioButton yalaBtn;
    private JRadioButton mahaBtn;
    private JComboBox<String> districtCombo;

    private Image backgroundImage;
    private boolean imageLoaded = false;

    public InputFormPanel(AppFrame frame) {
        loadBackgroundImage();
        setLayout(new BorderLayout());

        /* ── TOP HEADER ────────────────────────────────────────── */
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIConstants.DARK_GREEN);
        topPanel.setPreferredSize(new Dimension(0, 100));

        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftHeader.setOpaque(false);
        // Removed the "SCA Home" label from here as requested

        topPanel.add(leftHeader, BorderLayout.WEST);
        topPanel.add(createCenterHeader(), BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        /* ── FORM ──────────────────────────────────────────────── */
        // Build district list: "Select" + all 25 districts
        List<String> districtList = MarketGraph.getAllDistricts();
        String[] districtItems = new String[districtList.size() + 1];
        districtItems[0] = "Select";
        for (int i = 0; i < districtList.size(); i++) districtItems[i + 1] = districtList.get(i);

        soilCombo     = styledCombo("Select", "Loamy", "Clay", "Sandy");
        rainfallCombo = styledCombo("Select", "Low", "Medium", "High");
        districtCombo = styledCombo(districtItems);
        yalaBtn = new JRadioButton("Yala ●");
        mahaBtn = new JRadioButton("Maha ●");
        yalaBtn.setSelected(true);

        // Frosted-glass form container
        JPanel formContainer = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 185));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2d.dispose();
            }
        };
        formContainer.setOpaque(false);
        formContainer.setBorder(BorderFactory.createEmptyBorder(28, 60, 28, 60));

        GridBagConstraints lc = new GridBagConstraints();
        lc.anchor = GridBagConstraints.EAST;
        lc.insets = new Insets(12, 10, 12, 20);

        GridBagConstraints fc = new GridBagConstraints();
        fc.fill = GridBagConstraints.HORIZONTAL;
        fc.weightx = 1.0;
        fc.insets = new Insets(12, 0, 12, 10);

        // Title
        GridBagConstraints tc = new GridBagConstraints();
        tc.gridx = 0; tc.gridy = 0; tc.gridwidth = 2;
        tc.insets = new Insets(0, 0, 18, 0);
        JLabel title = new JLabel("Farm Input Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(UIConstants.DARK_GREEN);
        formContainer.add(title, tc);

        // Soil Type
        addRow(formContainer, "Soil Type :", soilCombo, lc, fc, 1);

        // Rainfall
        addRow(formContainer, "Rainfall Level :", rainfallCombo, lc, fc, 2);

        // Season (radio)
        ButtonGroup bg = new ButtonGroup();
        bg.add(yalaBtn); bg.add(mahaBtn);
        styleRadio(yalaBtn); styleRadio(mahaBtn);
        JPanel seasonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        seasonPanel.setOpaque(false);
        seasonPanel.add(yalaBtn);
        seasonPanel.add(mahaBtn);
        addRow(formContainer, "Season :", seasonPanel, lc, fc, 3);

        // District (farm location → graph source node)
        addRow(formContainer, "Farm District :", districtCombo, lc, fc, 4);

        // Hint label under district
        GridBagConstraints hc = new GridBagConstraints();
        hc.gridx = 1; hc.gridy = 5; hc.anchor = GridBagConstraints.WEST;
        hc.insets = new Insets(-8, 0, 6, 0);
        JLabel hint = new JLabel("Used to find nearest wholesale market via shortest path");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(80, 120, 80));
        formContainer.add(hint, hc);

        // Analyze button
        GridBagConstraints bc = new GridBagConstraints();
        bc.gridx = 0; bc.gridy = 6; bc.gridwidth = 2;
        bc.insets = new Insets(22, 0, 0, 0);
        JButton analyzeBtn = buildAnalyzeButton();
        analyzeBtn.addActionListener(e -> {
            String soil   = getSelected(soilCombo);
            String rain   = getSelected(rainfallCombo);
            String season = yalaBtn.isSelected() ? "Yala" : "Maha";
            String dist   = getSelected(districtCombo);

            // Validate
            if ("Not Specified".equals(soil) || "Not Specified".equals(rain) || "Unknown".equals(dist)) {
                JOptionPane.showMessageDialog(frame,
                        "Please select Soil Type, Rainfall Level and Farm District before analyzing.",
                        "Incomplete Input", JOptionPane.WARNING_MESSAGE);
                return;
            }
            frame.runAnalysis(new Farm(soil, rain, season, dist));
        });
        formContainer.add(analyzeBtn, bc);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(formContainer);
        add(centerPanel, BorderLayout.CENTER);
    }

    /* ── Helpers ──────────────────────────────────────────────────────── */
    private JComboBox<String> styledCombo(String... items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cb.setPreferredSize(new Dimension(280, 38));
        cb.setBackground(Color.WHITE);
        return cb;
    }

    private void styleRadio(JRadioButton btn) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setOpaque(false);
        btn.setForeground(UIConstants.DARK_GREEN);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new UIConstants.RoundedBorder(UIConstants.MID_GREEN, 16),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)));
    }

    private void addRow(JPanel form, String labelText, JComponent field,
                        GridBagConstraints lc, GridBagConstraints fc, int row) {
        lc.gridx = 0; lc.gridy = row;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lbl.setForeground(Color.DARK_GRAY);
        form.add(lbl, lc);
        fc.gridx = 1; fc.gridy = row;
        form.add(field, fc);
    }

    private String getSelected(JComboBox<String> combo) {
        String val = Objects.requireNonNullElse((String) combo.getSelectedItem(), "Select");
        return "Select".equals(val) ? "Not Specified" : val;
    }

    private JButton buildAnalyzeButton() {
        JButton btn = new JButton("Analyze") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, UIConstants.MID_GREEN, 0, getHeight(), UIConstants.DARK_GREEN);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(250, 48));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(UIConstants.DARK_GREEN); btn.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(UIConstants.MID_GREEN);  btn.repaint(); }
        });
        return btn;
    }

    // UPDATED: Now groups the logo and text horizontally
    private JPanel createCenterHeader() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        wrapper.setOpaque(false);

        // Logo setup
        JLabel logoLbl = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("src/main/resources/logo.png");
            if (icon.getIconWidth() > 0) {
                Image scaledImg = icon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
                logoLbl.setIcon(new ImageIcon(scaledImg));
            } else {
                logoLbl.setText("🌱");
                logoLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
                logoLbl.setForeground(Color.WHITE);
            }
        } catch (Exception e) {
            logoLbl.setText("🌱");
            logoLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
            logoLbl.setForeground(Color.WHITE);
        }

        // Text setup
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel t1 = new JLabel("Smart Farming");
        t1.setFont(new Font("Arial", Font.BOLD, 28));
        t1.setForeground(Color.WHITE);
        t1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel t2 = new JLabel("Crop Assistant");
        t2.setFont(new Font("Arial", Font.PLAIN, 22));
        t2.setForeground(Color.WHITE);
        t2.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(t1);
        textPanel.add(t2);

        // Add to wrapper
        wrapper.add(logoLbl);
        wrapper.add(textPanel);

        return wrapper;
    }

    private void loadBackgroundImage() {
        try {
            java.net.URL url = getClass().getResource("/farm.jpeg");
            if (url != null) {
                backgroundImage = javax.imageio.ImageIO.read(url);
                imageLoaded = true; return;
            }
            String base = System.getProperty("user.dir");
            for (String p : new String[]{"/src/main/resources/farm.jpeg", "/resources/farm.jpeg", "/farm.jpeg"}) {
                File f = new File(base + p);
                if (f.exists()) { backgroundImage = Toolkit.getDefaultToolkit().getImage(f.getAbsolutePath()); imageLoaded = true; return; }
            }
        } catch (Exception ignored) {}
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        if (imageLoaded && backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillRect(0, 0, getWidth(), getHeight());
        } else {
            GradientPaint gp = new GradientPaint(0, 0, new Color(200, 230, 200), getWidth(), getHeight(), new Color(160, 200, 160));
            g2.setPaint(gp); g2.fillRect(0, 0, getWidth(), getHeight());
        }
        g2.dispose();
    }
}