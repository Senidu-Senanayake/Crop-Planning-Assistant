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
        setBackground(UIConstants.PALE_GREEN);

        add(new SidebarPanel(frame, AppFrame.CARD_PROFIT), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UIConstants.PALE_GREEN);
        main.add(new HeaderBannerPanel("Profit Comparison"), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UIConstants.PALE_GREEN);
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        if (result != null) {
            List<Crop> crops = result.getRecommendedCrops();

            // Chart
            JPanel chart = buildBarChart(crops);
            chart.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(chart);
            content.add(Box.createVerticalStrut(16));

            // Table
            JScrollPane tableScroll = buildTable(crops);
            tableScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
            tableScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
            content.add(tableScroll);
        } else {
            content.add(new JLabel("Run analysis first.", SwingConstants.CENTER));
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(UIConstants.PALE_GREEN);
        main.add(scroll, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    private JPanel buildBarChart(List<Crop> crops) {
        JPanel chartWrapper = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(230, 248, 230));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            }
        };
        chartWrapper.setOpaque(false);
        chartWrapper.setPreferredSize(new Dimension(0, 260));
        chartWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

        JPanel chart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                setBackground(new Color(230, 248, 230));

                int w = getWidth(), h = getHeight();
                int bottomPad = 40, leftPad = 60, topPad = 20;
                int chartH = h - bottomPad - topPad;

                // Find max value for scaling
                double maxVal = crops.stream()
                        .mapToDouble(c -> Math.max(c.getCost(), c.getExpectedIncome()))
                        .max().orElse(1);

                int barGroupWidth = (w - leftPad) / crops.size();
                int barW = Math.min(50, barGroupWidth / 3);

                // Y axis labels
                g2.setColor(UIConstants.DARK_TEXT);
                g2.setFont(UIConstants.FONT_SMALL);
                int[] yLabels = {0, 12, 21, 33, 94};
                for (int lv : yLabels) {
                    int y = topPad + chartH - (int) ((lv / 100.0) * chartH);
                    g2.drawString(String.valueOf(lv), 8, y + 4);
                    g2.setColor(new Color(180, 220, 180));
                    g2.drawLine(leftPad, y, w - 10, y);
                    g2.setColor(UIConstants.DARK_TEXT);
                }

                // Bars
                for (int i = 0; i < crops.size(); i++) {
                    Crop crop = crops.get(i);
                    int groupX = leftPad + i * barGroupWidth + barGroupWidth / 6;

                    // Cost bar (dark green)
                    int costH = (int) ((crop.getCost() / maxVal) * chartH * 0.9);
                    int costVal = (int) (crop.getCost() / 1000);
                    g2.setColor(UIConstants.DARK_GREEN);
                    g2.fillRoundRect(groupX, topPad + chartH - costH, barW, costH, 6, 6);
                    g2.setColor(UIConstants.DARK_TEXT);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    g2.drawString(String.valueOf(costVal), groupX + barW / 2 - 6, topPad + chartH - costH - 4);

                    // Income bar (light green)
                    int incH = (int) ((crop.getExpectedIncome() / maxVal) * chartH * 0.9);
                    int incVal = (int) (crop.getExpectedIncome() / 1000);
                    g2.setColor(UIConstants.ACCENT_GREEN);
                    g2.fillRoundRect(groupX + barW + 8, topPad + chartH - incH, barW, incH, 6, 6);
                    g2.setColor(UIConstants.DARK_TEXT);
                    g2.drawString(String.valueOf(incVal), groupX + barW + 8 + barW / 2 - 6, topPad + chartH - incH - 4);

                    // X label
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    g2.setColor(UIConstants.DARK_GREEN);
                    g2.drawString(crop.getName(), groupX, h - 10);
                }

                // Axis
                g2.setColor(UIConstants.MID_GREEN);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawLine(leftPad, topPad, leftPad, topPad + chartH);
                g2.drawLine(leftPad, topPad + chartH, w - 10, topPad + chartH);
            }
        };
        chart.setOpaque(false);
        chartWrapper.add(chart, BorderLayout.CENTER);
        return chartWrapper;
    }

    private JScrollPane buildTable(List<Crop> crops) {
        String[] cols = {"Crop", "Cost", "Expected Income", "Profit"};
        Object[][] data = new Object[crops.size()][4];
        for (int i = 0; i < crops.size(); i++) {
            Crop c = crops.get(i);
            data[i][0] = c.getName();
            data[i][1] = "Rs. " + (int) c.getCost();
            data[i][2] = "Rs. " + (int) c.getExpectedIncome();
            data[i][3] = "Rs. " + (int) c.getProfit();
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(44);
        table.setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 2));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(UIConstants.TABLE_HEADER_BG);
        header.setForeground(UIConstants.WHITE);
        header.setPreferredSize(new Dimension(0, 44));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                c.setBackground(row % 2 == 0 ? UIConstants.WHITE : UIConstants.TABLE_ROW_ALT);
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new UIConstants.RoundedBorder(UIConstants.LIGHT_GREEN, 14));
        return sp;
    }
}
