package com.smartfarming.ui;

import com.smartfarming.model.Crop;
import com.smartfarming.model.RecommendationResult;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class ProfitComparisonPanel extends JPanel {

    public ProfitComparisonPanel(AppFrame frame, RecommendationResult result) {
        setLayout(new BorderLayout());
        setOpaque(false);

        add(new SidebarPanel(frame, AppFrame.CARD_PROFIT), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(new HeaderBannerPanel("Profit Comparison"), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
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
            empty.setFont(new Font("Segoe UI", Font.BOLD, 18));
            empty.setForeground(Color.WHITE);
            content.add(empty);
        }

        main.add(content, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    private JPanel buildBarChart(List<Crop> crops) {
        JPanel chart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Glass background
                g2.setColor(new Color(255, 255, 255, 200));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                int w = getWidth(), h = getHeight();
                int bottomPad = 40, leftPad = 60, topPad = 30;
                int chartH = h - bottomPad - topPad;

                double maxVal = crops.stream()
                        .mapToDouble(c -> Math.max(c.getCost(), c.getExpectedIncome()))
                        .max().orElse(1);

                int barGroupWidth = (w - leftPad) / crops.size();
                int barW = Math.max(20, Math.min(60, barGroupWidth / 3));

                // Axis Lines
                g2.setColor(new Color(150, 150, 150, 100));
                for (int i = 0; i <= 4; i++) {
                    int y = topPad + chartH - (int) ((i / 4.0) * chartH);
                    g2.drawLine(leftPad, y, w - 20, y);
                    g2.setColor(UIConstants.DARK_TEXT);
                    g2.setFont(UIConstants.FONT_SMALL);
                    g2.drawString((int)((i / 4.0) * maxVal / 1000) + "k", 10, y + 5);
                    g2.setColor(new Color(150, 150, 150, 100));
                }

                // Bars
                for (int i = 0; i < crops.size(); i++) {
                    Crop crop = crops.get(i);
                    int groupX = leftPad + i * barGroupWidth + (barGroupWidth - (barW * 2 + 10)) / 2;

                    // Cost Bar (Red/Orange Gradient)
                    int costH = (int) ((crop.getCost() / maxVal) * chartH);
                    GradientPaint costPaint = new GradientPaint(0, topPad + chartH - costH, new Color(220, 80, 80), 0, topPad + chartH, new Color(150, 40, 40));
                    g2.setPaint(costPaint);
                    g2.fillRoundRect(groupX, topPad + chartH - costH, barW, costH, 8, 8);

                    // Income Bar (Green Gradient)
                    int incH = (int) ((crop.getExpectedIncome() / maxVal) * chartH);
                    GradientPaint incPaint = new GradientPaint(0, topPad + chartH - incH, new Color(80, 220, 100), 0, topPad + chartH, new Color(40, 150, 60));
                    g2.setPaint(incPaint);
                    g2.fillRoundRect(groupX + barW + 10, topPad + chartH - incH, barW, incH, 8, 8);

                    // Labels
                    g2.setColor(UIConstants.DARK_TEXT);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    FontMetrics fm = g2.getFontMetrics();
                    int textX = groupX + (barW * 2 + 10 - fm.stringWidth(crop.getName())) / 2;
                    g2.drawString(crop.getName(), textX, h - 15);
                }
                g2.dispose();
            }
        };
        chart.setOpaque(false);
        chart.setPreferredSize(new Dimension(800, 300));
        chart.setMaximumSize(new Dimension(1000, 350));
        return chart;
    }

    private JScrollPane buildTable(List<Crop> crops) {
        String[] cols = {"Crop", "Investment", "Expected Return", "Net Profit"};
        Object[][] data = new Object[crops.size()][4];
        for (int i = 0; i < crops.size(); i++) {
            Crop c = crops.get(i);
            data[i][0] = c.getName();
            data[i][1] = "Rs. " + String.format("%,.2f", c.getCost());
            data[i][2] = "Rs. " + String.format("%,.2f", c.getExpectedIncome());
            data[i][3] = "Rs. " + String.format("%,.2f", c.getProfit());
        }

        JTable table = new JTable(new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });

        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setOpaque(false);
        table.setShowGrid(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(20, 70, 30, 220));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 45));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? new Color(255, 255, 255, 200) : new Color(240, 250, 240, 200));
                }
                return c;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setBorder(BorderFactory.createLineBorder(new Color(255,255,255,100), 1, true));
        return sp;
    }
}