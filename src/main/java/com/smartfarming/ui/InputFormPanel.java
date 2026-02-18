package com.smartfarming.ui;

import com.smartfarming.model.Farm;

import javax.swing.*;
import java.awt.*;

public class InputFormPanel extends JPanel {

    private final JComboBox<String> soilCombo;
    private final JComboBox<String> rainfallCombo;
    private final JRadioButton yalaBtn;
    private final JRadioButton mahaBtn;
    private final JTextField locationField;

    public InputFormPanel(AppFrame frame) {
        setLayout(new BorderLayout());
        setBackground(UIConstants.PALE_GREEN);

        // Banner
        JPanel banner = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 80, 20),
                        0, getHeight(), new Color(40, 120, 40));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        banner.setPreferredSize(new Dimension(0, 120));
        banner.setLayout(new BoxLayout(banner, BoxLayout.Y_AXIS));
        banner.setBorder(BorderFactory.createEmptyBorder(14, 0, 10, 0));

        JLabel appLbl = new JLabel("Smart Farming Crop Planning Assistant", SwingConstants.CENTER);
        appLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        appLbl.setForeground(UIConstants.WHITE);
        appLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLbl = new JLabel("Optimize your crop yield with smart data driven decisions", SwingConstants.CENTER);
        subLbl.setFont(UIConstants.FONT_SMALL);
        subLbl.setForeground(new Color(200, 240, 200));
        subLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        banner.add(appLbl);
        banner.add(Box.createVerticalStrut(4));
        banner.add(subLbl);
        banner.add(Box.createVerticalGlue());

        // Form title bar
        JPanel titleBar = new JPanel();
        titleBar.setBackground(UIConstants.MID_GREEN);
        titleBar.setPreferredSize(new Dimension(0, 38));
        JLabel formTitle = new JLabel("Farm Input Details", SwingConstants.CENTER);
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(UIConstants.WHITE);
        titleBar.add(formTitle);

        // Combine banner + title
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.add(banner, BorderLayout.CENTER);
        topSection.add(titleBar, BorderLayout.SOUTH);
        add(topSection, BorderLayout.NORTH);

        // Form body
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIConstants.PALE_GREEN);
        form.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        GridBagConstraints lc = new GridBagConstraints();
        lc.anchor = GridBagConstraints.EAST;
        lc.insets = new Insets(14, 10, 14, 20);

        GridBagConstraints fc = new GridBagConstraints();
        fc.fill = GridBagConstraints.HORIZONTAL;
        fc.weightx = 1.0;
        fc.insets = new Insets(14, 0, 14, 10);

        // Soil Type
        soilCombo = styledCombo("Loamy", "Clay", "Sandy");
        addFormRow(form, "Soil Type :", soilCombo, lc, fc, 0);

        // Rainfall
        rainfallCombo = styledCombo("Low", "Medium", "High");
        addFormRow(form, "Rainfall Level :", rainfallCombo, lc, fc, 1);

        // Season
        yalaBtn = new JRadioButton("Yala");
        mahaBtn = new JRadioButton("Maha");
        yalaBtn.setSelected(true);
        yalaBtn.setBackground(UIConstants.PALE_GREEN);
        mahaBtn.setBackground(UIConstants.PALE_GREEN);
        yalaBtn.setFont(UIConstants.FONT_BODY);
        mahaBtn.setFont(UIConstants.FONT_BODY);
        ButtonGroup bg = new ButtonGroup();
        bg.add(yalaBtn); bg.add(mahaBtn);
        JPanel seasonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        seasonPanel.setOpaque(false);
        styleSeasonBtn(yalaBtn, seasonPanel);
        styleSeasonBtn(mahaBtn, seasonPanel);
        addFormRow(form, "Season :", seasonPanel, lc, fc, 2);

        // Location
        locationField = new JTextField("Type Here");
        locationField.setFont(UIConstants.FONT_BODY);
        locationField.setPreferredSize(new Dimension(0, 42));
        locationField.setBorder(BorderFactory.createCompoundBorder(
                new UIConstants.RoundedBorder(UIConstants.LIGHT_GREEN, 20),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)));
        locationField.setBackground(UIConstants.WHITE);
        locationField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (locationField.getText().equals("Type Here")) locationField.setText("");
            }
        });
        addFormRow(form, "Farm Location :", locationField, lc, fc, 3);

        // Analyze button
        JButton analyzeBtn = UIConstants.makeButton("Analyze");
        analyzeBtn.setPreferredSize(new Dimension(200, 48));
        GridBagConstraints bc = new GridBagConstraints();
        bc.gridx = 0; bc.gridy = 4; bc.gridwidth = 2;
        bc.insets = new Insets(24, 0, 0, 0);
        form.add(analyzeBtn, bc);

        analyzeBtn.addActionListener(e -> {
            String soil = (String) soilCombo.getSelectedItem();
            String rain = (String) rainfallCombo.getSelectedItem();
            String season = yalaBtn.isSelected() ? "Yala" : "Maha";
            String loc = locationField.getText().trim();
            if (loc.isEmpty() || loc.equals("Type Here")) loc = "Unknown";
            frame.runAnalysis(new Farm(soil, rain, season, loc));
        });

        add(form, BorderLayout.CENTER);
    }

    private JComboBox<String> styledCombo(String... items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cb.setPreferredSize(new Dimension(0, 42));
        cb.setBackground(UIConstants.WHITE);
        cb.setBorder(BorderFactory.createCompoundBorder(
                new UIConstants.RoundedBorder(UIConstants.LIGHT_GREEN, 20),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return cb;
    }

    private void styleSeasonBtn(JRadioButton btn, JPanel parent) {
        btn.setFont(UIConstants.FONT_BODY);
        btn.setBackground(UIConstants.PALE_GREEN);
        btn.setForeground(UIConstants.DARK_TEXT);
        parent.add(btn);
    }

    private void addFormRow(JPanel form, String label, JComponent field,
                             GridBagConstraints lc, GridBagConstraints fc, int row) {
        lc.gridx = 0; lc.gridy = row;
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_BODY);
        form.add(lbl, lc);
        fc.gridx = 1; fc.gridy = row;
        form.add(field, fc);
    }
}
