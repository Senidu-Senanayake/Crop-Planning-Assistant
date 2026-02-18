package com.smartfarming.ui;

import com.smartfarming.model.Market;
import com.smartfarming.model.RecommendationResult;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class MarketOptimizationPanel extends JPanel {

    public MarketOptimizationPanel(AppFrame frame, RecommendationResult result) {
        setLayout(new BorderLayout());
        setBackground(UIConstants.PALE_GREEN);

        add(new SidebarPanel(frame, AppFrame.CARD_MARKET), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UIConstants.PALE_GREEN);
        main.add(new HeaderBannerPanel("Market & Transport Optimization"), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UIConstants.PALE_GREEN);
        content.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        if (result != null) {
            // Map panel
            JPanel map = buildMapPanel(result.getMarkets());
            map.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(map);
            content.add(Box.createVerticalStrut(14));

            // Market table (show top 3)
            List<Market> top3 = result.getMarkets().subList(0, Math.min(3, result.getMarkets().size()));
            JScrollPane tableScroll = buildMarketTable(top3);
            tableScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
            tableScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
            content.add(tableScroll);
        } else {
            content.add(new JLabel("Run analysis to see market data.", SwingConstants.CENTER));
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(UIConstants.PALE_GREEN);
        main.add(scroll, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    private JPanel buildMapPanel(List<Market> markets) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background - simulated map
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 80, 30), getWidth(), getHeight(), new Color(60, 130, 60));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

                // Grid lines (map feel)
                g2.setColor(new Color(255, 255, 255, 20));
                for (int x = 0; x < getWidth(); x += 40) g2.drawLine(x, 0, x, getHeight());
                for (int y = 0; y < getHeight(); y += 40) g2.drawLine(0, y, getWidth(), y);

                // Title
                g2.setColor(UIConstants.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                g2.drawString("Market & Transport Optimization", 16, 28);
                g2.setFont(UIConstants.FONT_SMALL);
                g2.drawString(">>", getWidth() - 36, 28);

                // Node positions (Farm + markets)
                int[][] positions = {
                        {getWidth() / 2, 70},       // Farm
                        {80, 160},                  // Colombo
                        {getWidth() / 2, 200},      // Gampaha
                        {getWidth() - 110, 160}     // Kurunegala
                };
                String[] labels = {"Farm", "Colombo", "Gampaha", "Kurunegala"};

                // Draw edges (arrows)
                g2.setColor(new Color(180, 230, 100, 180));
                g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        0, new float[]{8, 5}, 0));
                drawArrow(g2, positions[0], positions[1]);
                drawArrow(g2, positions[0], positions[2]);
                drawArrow(g2, positions[0], positions[3]);
                drawArrow(g2, positions[1], positions[2]);
                drawArrow(g2, positions[2], positions[3]);

                // Draw nodes
                g2.setStroke(new BasicStroke(1));
                for (int i = 0; i < positions.length; i++) {
                    int x = positions[i][0], y = positions[i][1];
                    boolean isOptimal = labels[i].equals("Gampaha");
                    Color nodeColor = i == 0 ? new Color(100, 200, 100) :
                                     isOptimal ? new Color(50, 160, 50) : new Color(180, 150, 30);
                    g2.setColor(nodeColor);
                    g2.fillOval(x - 14, y - 14, 28, 28);
                    g2.setColor(UIConstants.WHITE);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawOval(x - 14, y - 14, 28, 28);

                    // Label
                    g2.setColor(UIConstants.WHITE);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    g2.setStroke(new BasicStroke(1));
                    // Label background
                    FontMetrics fm = g2.getFontMetrics();
                    int lw = fm.stringWidth(labels[i]);
                    g2.setColor(new Color(0, 0, 0, 140));
                    g2.fillRoundRect(x - lw / 2 - 4, y + 14, lw + 8, 18, 8, 8);
                    g2.setColor(UIConstants.WHITE);
                    g2.drawString(labels[i], x - lw / 2, y + 26);
                }
            }
        };
        panel.setPreferredSize(new Dimension(0, 260));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
        return panel;
    }

    private void drawArrow(Graphics2D g2, int[] from, int[] to) {
        g2.drawLine(from[0], from[1], to[0], to[1]);
        // Arrowhead
        double dx = to[0] - from[0], dy = to[1] - from[1];
        double len = Math.sqrt(dx * dx + dy * dy);
        double nx = dx / len, ny = dy / len;
        int ax = (int) (to[0] - 14 * nx), ay = (int) (to[1] - 14 * ny);
        int[] xs = {to[0], ax + (int)(6*ny), ax - (int)(6*ny)};
        int[] ys = {to[1], ay - (int)(6*nx), ay + (int)(6*nx)};
        g2.setStroke(new BasicStroke(1));
        g2.fillPolygon(xs, ys, 3);
    }

    private JScrollPane buildMarketTable(List<Market> markets) {
        String[] cols = {"Market", "Distance (km)", "Transport Cost", "Demand"};
        Object[][] data = new Object[markets.size()][4];
        for (int i = 0; i < markets.size(); i++) {
            Market m = markets.get(i);
            data[i][0] = m.getName();
            data[i][1] = (int) m.getDistanceKm();
            data[i][2] = "Rs " + (int) m.getTransportCost();
            data[i][3] = m.isOptimal() ? "✅ Optimal" : m.getDemand();
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(42);
        table.setFont(UIConstants.FONT_BODY);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 2));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(UIConstants.TABLE_HEADER_BG);
        header.setForeground(UIConstants.WHITE);
        header.setPreferredSize(new Dimension(0, 42));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                c.setBackground(row % 2 == 0 ? UIConstants.WHITE : UIConstants.TABLE_ROW_ALT);
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                // Highlight optimal row
                if (markets.get(row).isOptimal()) {
                    c.setBackground(new Color(200, 240, 200));
                }
                return c;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new UIConstants.RoundedBorder(UIConstants.LIGHT_GREEN, 14));
        return sp;
    }
}
