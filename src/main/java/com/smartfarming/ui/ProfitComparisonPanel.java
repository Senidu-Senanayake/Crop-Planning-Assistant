package com.smartfarming.ui;

import com.smartfarming.model.Crop;
import com.smartfarming.model.RecommendationResult;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class ProfitComparisonPanel extends JPanel {

    private Image backgroundImage;
    private boolean imageLoaded = false;

    public ProfitComparisonPanel(AppFrame frame, RecommendationResult result) {
        loadBackgroundImage();
        setLayout(new BorderLayout());
        setOpaque(false); // Let the background image show through the base

        add(new SidebarPanel(frame, AppFrame.CARD_PROFIT), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(new HeaderBannerPanel("Profit Comparison"), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        // 🟢 THIS IS THE MAGIC TRICK: Makes the detail UI solid pale green, covering the image!
        content.setOpaque(true);
        content.setBackground(UIConstants.PALE_GREEN);
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        if (result != null && result.getRecommendedCrops() != null && !result.getRecommendedCrops().isEmpty()) {
            List<Crop> crops = result.getRecommendedCrops();

            JPanel chart = buildBarChart(crops);
            chart.setAlignmentX(Component.CENTER_ALIGNMENT);
            content.add(chart);
            content.add(Box.createVerticalStrut(20));

            JScrollPane tableScroll = buildTable(crops);
            tableScroll.setAlignmentX(Component.CENTER_ALIGNMENT);
            content.add(tableScroll);
        } else {
            JLabel empty = new JLabel("Run analysis first to view profit comparisons.", SwingConstants.CENTER);
            empty.setFont(UIConstants.FONT_BODY);
            content.add(empty);
        }

        main.add(content, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    private JPanel buildBarChart(List<Crop> crops) {
        JPanel chart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Solid white rounded background for chart
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                int w = getWidth(), h = getHeight();
                int bottomPad = 40, leftPad = 60, topPad = 30;
                int chartH = h - bottomPad - topPad;

                double maxVal = crops.stream()
                        .mapToDouble(c -> Math.max(c.getCost(), c.getExpectedIncome()))
                        .max().orElse(1);

                int barGroupWidth = (w - leftPad) / crops.size();
                int barW = Math.max(20, Math.min(50, barGroupWidth / 3));

                g2.setColor(new Color(220, 230, 220));
                for (int i = 0; i <= 4; i++) {
                    int y = topPad + chartH - (int) ((i / 4.0) * chartH);
                    g2.drawLine(leftPad, y, w - 20, y);
                    g2.setColor(UIConstants.DARK_TEXT);
                    g2.setFont(UIConstants.FONT_SMALL);
                    g2.drawString((int)((i / 4.0) * maxVal / 1000) + "k", 10, y + 5);
                    g2.setColor(new Color(220, 230, 220));
                }

                for (int i = 0; i < crops.size(); i++) {
                    Crop crop = crops.get(i);
                    int groupX = leftPad + i * barGroupWidth + (barGroupWidth - (barW * 2 + 10)) / 2;

                    // Cost Bar
                    int costH = (int) ((crop.getCost() / maxVal) * chartH);
                    g2.setColor(new Color(210, 100, 100));
                    g2.fillRoundRect(groupX, topPad + chartH - costH, barW, costH, 6, 6);

                    // Income Bar
                    int incH = (int) ((crop.getExpectedIncome() / maxVal) * chartH);
                    g2.setColor(UIConstants.MID_GREEN);
                    g2.fillRoundRect(groupX + barW + 10, topPad + chartH - incH, barW, incH, 6, 6);

                    g2.setColor(UIConstants.DARK_TEXT);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    FontMetrics fm = g2.getFontMetrics();
                    int textX = groupX + (barW * 2 + 10 - fm.stringWidth(crop.getName())) / 2;
                    g2.drawString(crop.getName(), textX, h - 15);
                }
            }

            @Override
            public String getToolTipText(MouseEvent e) {
                int x = e.getX();
                int w = getWidth();
                int leftPad = 60;
                if (x < leftPad || crops.isEmpty()) return null;
                int barGroupWidth = (w - leftPad) / crops.size();
                int index = (x - leftPad) / barGroupWidth;

                if (index >= 0 && index < crops.size()) {
                    Crop c = crops.get(index);
                    return String.format("<html><div style='margin: 5px;'><b>%s</b><br>Investment: Rs. %,.0f<br>Return: Rs. %,.0f<br><font color='green'><b>Net Profit: Rs. %,.0f</b></font></div></html>",
                            c.getName(), c.getCost(), c.getExpectedIncome(), c.getProfit());
                }
                return null;
            }
        };

        ToolTipManager.sharedInstance().registerComponent(chart);
        chart.setOpaque(false);
        chart.setPreferredSize(new Dimension(800, 280));
        chart.setMaximumSize(new Dimension(1000, 300));
        chart.setBorder(new UIConstants.RoundedBorder(UIConstants.LIGHT_GREEN, 16));
        return chart;
    }

    private JScrollPane buildTable(List<Crop> crops) {
        String[] cols = {"Crop", "Investment", "Expected Return", "Net Profit"};
        Object[][] data = new Object[crops.size()][4];
        for (int i = 0; i < crops.size(); i++) {
            Crop c = crops.get(i);
            data[i][0] = c.getName();
            data[i][1] = "Rs. " + String.format("%,.0f", c.getCost());
            data[i][2] = "Rs. " + String.format("%,.0f", c.getExpectedIncome());
            data[i][3] = "Rs. " + String.format("%,.0f", c.getProfit());
        }

        JTable table = new JTable(new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });

        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(UIConstants.TABLE_HEADER_BG);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 45));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                c.setBackground(row % 2 == 0 ? UIConstants.WHITE : UIConstants.TABLE_ROW_ALT);

                // Make Net Profit column text green
                if (col == 3) {
                    c.setForeground(UIConstants.MID_GREEN);
                } else {
                    c.setForeground(UIConstants.DARK_TEXT);
                }
                return c;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Color.WHITE);
        sp.setBorder(new UIConstants.RoundedBorder(UIConstants.LIGHT_GREEN, 16));
        return sp;
    }

    // --- Background Image Logic ---
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
        if (imageLoaded && backgroundImage != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            // Adds a dark tint to the image so the sidebar text remains highly readable
            g2.setColor(new Color(0, 0, 0, 50));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }
}