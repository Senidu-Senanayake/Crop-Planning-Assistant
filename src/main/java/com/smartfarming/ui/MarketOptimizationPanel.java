package com.smartfarming.ui;

import com.smartfarming.model.Market;
import com.smartfarming.model.RecommendationResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class MarketOptimizationPanel extends JPanel {

    private Image backgroundImage;
    private boolean imageLoaded = false;

    public MarketOptimizationPanel(AppFrame frame, RecommendationResult result) {
        loadBackgroundImage();
        setLayout(new BorderLayout());
        setOpaque(false);

        add(new SidebarPanel(frame, AppFrame.CARD_MARKET), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(new HeaderBannerPanel("Market & Transport Optimization"), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(true);
        content.setBackground(UIConstants.PALE_GREEN);
        content.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));

        if (result != null && result.getOptimalMarket() != null) {
            Market optimal = result.getOptimalMarket();
            List<Market> markets = result.getMarkets();

            JPanel cards = buildSummaryCards(optimal);
            cards.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(cards);
            content.add(Box.createVerticalStrut(16));

            JPanel pathPanel = buildPathPanel(optimal);
            pathPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(pathPanel);
            content.add(Box.createVerticalStrut(16));

            JScrollPane table = buildMarketTable(markets);
            table.setAlignmentX(Component.LEFT_ALIGNMENT);
            table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
            content.add(table);
        } else {
            JLabel ph = new JLabel("Run analysis to see market optimization results.", SwingConstants.CENTER);
            ph.setFont(UIConstants.FONT_BODY);
            content.add(ph);
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        main.add(scroll, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    // ── Summary Cards ────────────────────────────────────────────────────
    private JPanel buildSummaryCards(Market optimal) {
        JPanel row = new JPanel(new GridLayout(1, 4, 12, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        row.add(summaryCard("📍 Optimal Market",  optimal.getName(),
                new Color(56, 142, 60),  new Color(27, 94, 32)));
        row.add(summaryCard("📏 Distance",
                String.format("%.0f km", optimal.getDistanceKm()),
                new Color(25, 118, 210), new Color(13, 71, 161)));
        row.add(summaryCard("🚚 Transport Cost",
                String.format("Rs. %.0f", optimal.getTransportCost()),
                new Color(245, 124, 0),  new Color(191, 84, 0)));
        row.add(summaryCard("📈 Market Demand",   optimal.getDemand(),
                demandColor(optimal.getDemand()), demandDark(optimal.getDemand())));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JLabel heading = new JLabel("  Dijkstra's Shortest-Path Result");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 14));
        heading.setForeground(UIConstants.DARK_GREEN);
        heading.setBorder(new EmptyBorder(0, 0, 8, 0));
        wrapper.add(heading, BorderLayout.NORTH);
        wrapper.add(row, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel summaryCard(String label, String value, Color top, Color bottom) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, top, 0, getHeight(), bottom));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(255, 255, 255, 200));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 18));
        val.setForeground(Color.WHITE);
        val.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lbl);
        card.add(Box.createVerticalStrut(4));
        card.add(val);
        return card;
    }

    // ── Shortest Path Diagram ────────────────────────────────────────────
    private JPanel buildPathPanel(Market optimal) {
        List<String> path = optimal.getShortestPath();

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JLabel heading = new JLabel("  Shortest Route  →  " + optimal.getName());
        heading.setFont(new Font("Segoe UI", Font.BOLD, 14));
        heading.setForeground(UIConstants.DARK_GREEN);
        heading.setBorder(new EmptyBorder(0, 0, 8, 0));
        wrapper.add(heading, BorderLayout.NORTH);

        JPanel diagram = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2.setColor(new Color(235, 248, 235));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(UIConstants.LIGHT_GREEN);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);

                if (path == null || path.isEmpty()) return;

                int n       = path.size();
                int nodeR   = 22;
                int centerY = getHeight() / 2;
                int padding = 60;
                int spacing = n > 1 ? (getWidth() - 2 * padding) / (n - 1) : 0;

                // Compute node centres
                int[] cx = new int[n];
                int[] cy = new int[n];
                for (int i = 0; i < n; i++) {
                    cx[i] = padding + i * spacing;
                    cy[i] = centerY;
                }

                // ── Draw edges with distance label ──────────────────────
                for (int i = 0; i < n - 1; i++) {
                    boolean isMarketEdge = (i == n - 2);

                    // Edge colour & style
                    g2.setColor(isMarketEdge ? new Color(27, 94, 32) : UIConstants.MID_GREEN);
                    if (!isMarketEdge) {
                        // Dashed line for intermediate hops
                        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND,
                                BasicStroke.JOIN_ROUND, 0, new float[]{8, 5}, 0));
                    } else {
                        // Solid bold line for final hop to market
                        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    }

                    int x1 = cx[i] + nodeR;
                    int y1 = cy[i];
                    int x2 = cx[i + 1] - nodeR;
                    int y2 = cy[i + 1];
                    g2.drawLine(x1, y1, x2, y2);

                    // Arrowhead at destination end
                    g2.setStroke(new BasicStroke(1));
                    drawArrowHead(g2, x1, y1, x2, y2);

                    // Distance label centred on edge
                    int midX = (x1 + x2) / 2;
                    int midY = (y1 + y2) / 2 - 10;
                    // Show total distance on final edge, "via district" on intermediate edges
                    String distLabel = isMarketEdge
                            ? String.format("%.0f km total", optimal.getDistanceKm())
                            : "via " + path.get(i + 1);
                    g2.setFont(new Font("Segoe UI", Font.ITALIC, 10));
                    g2.setColor(new Color(60, 100, 60));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(distLabel, midX - fm.stringWidth(distLabel) / 2, midY);
                }

                // ── Draw nodes ──────────────────────────────────────────
                for (int i = 0; i < n; i++) {
                    boolean isFirst = (i == 0);
                    boolean isLast  = (i == n - 1);

                    Color fill = isFirst ? new Color(56, 142, 60)
                            : isLast  ? new Color(27, 94, 32)
                            :           new Color(100, 160, 100);

                    g2.setStroke(new BasicStroke(2));
                    g2.setColor(fill);
                    g2.fillOval(cx[i] - nodeR, cy[i] - nodeR, 2 * nodeR, 2 * nodeR);
                    g2.setColor(Color.WHITE);
                    g2.drawOval(cx[i] - nodeR, cy[i] - nodeR, 2 * nodeR, 2 * nodeR);

                    // Node icon (plain text — no emoji font dependency)
                    String icon = isFirst ? "F" : isLast ? "M" : "D";
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    g2.setColor(Color.WHITE);
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(icon, cx[i] - fm.stringWidth(icon) / 2, cy[i] + fm.getAscent() / 2 - 2);

                    // Node label below circle
                    g2.setColor(UIConstants.DARK_TEXT);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                    fm = g2.getFontMetrics();
                    String label = path.get(i);
                    // Shorten "Xxx Market" → "Xxx\nMkt" to save space
                    if (label.endsWith(" Market")) label = label.replace(" Market", "\nMkt");
                    String[] parts = label.split("\n");
                    for (int p = 0; p < parts.length; p++) {
                        int lw = fm.stringWidth(parts[p]);
                        g2.drawString(parts[p], cx[i] - lw / 2, cy[i] + nodeR + 16 + p * 14);
                    }
                }

                // ── Legend ──────────────────────────────────────────────
                g2.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                g2.setColor(new Color(100, 130, 100));
                g2.drawString("[F] Farm  -->  [D] Intermediate District  -->  [M] Wholesale Market",
                        10, getHeight() - 8);
            }
        };

        diagram.setPreferredSize(new Dimension(0, 140));
        diagram.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        diagram.setOpaque(false);

        wrapper.add(diagram, BorderLayout.CENTER);
        return wrapper;
    }

    /** Draws a filled arrowhead pointing from (x1,y1) toward (x2,y2). */
    private void drawArrowHead(Graphics2D g2, int x1, int y1, int x2, int y2) {
        double dx = x2 - x1, dy = y2 - y1;
        double len = Math.sqrt(dx * dx + dy * dy);
        if (len == 0) return;
        double nx = dx / len, ny = dy / len;
        int ax = (int) (x2 - 12 * nx), ay = (int) (y2 - 12 * ny);
        int[] xs = {x2, ax + (int) (6 * ny), ax - (int) (6 * ny)};
        int[] ys = {y2, ay - (int) (6 * nx), ay + (int) (6 * nx)};
        g2.fillPolygon(xs, ys, 3);
    }

    // ── Market Comparison Table ──────────────────────────────────────────
    private JScrollPane buildMarketTable(List<Market> markets) {
        String[] cols = {"Market", "Distance (km)", "Transport Cost", "Price/kg", "Demand", "Status"};
        Object[][] data = new Object[markets.size()][6];
        for (int i = 0; i < markets.size(); i++) {
            Market m = markets.get(i);
            data[i][0] = m.getName();
            data[i][1] = String.format("%.0f", m.getDistanceKm());
            data[i][2] = String.format("Rs. %.0f", m.getTransportCost());
            data[i][3] = m.getPricePerKg() != null ? m.getPricePerKg() : "-";
            data[i][4] = m.getDemand();
            data[i][5] = m.isOptimal() ? "✅ Optimal" : "";
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(38);
        table.setFont(UIConstants.FONT_BODY);
        table.setShowGrid(false);
        table.setBackground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(UIConstants.TABLE_HEADER_BG);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 40));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                                                           boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                boolean isOptimal = markets.get(row).isOptimal();
                if (isOptimal) {
                    c.setBackground(new Color(200, 240, 200));
                    c.setFont(new Font("Segoe UI", Font.BOLD, 13));
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : UIConstants.TABLE_ROW_ALT);
                    c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                }
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                if (col == 4) {
                    String d = String.valueOf(val);
                    c.setForeground("High".equals(d)   ? new Color(0, 100, 0)
                            : "Medium".equals(d) ? new Color(180, 100, 0)
                            :                      new Color(150, 0, 0));
                } else {
                    c.setForeground(UIConstants.DARK_TEXT);
                }
                return c;
            }
        });

        table.getColumnModel().getColumn(0).setPreferredWidth(160);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Color.WHITE);
        sp.setBorder(new UIConstants.RoundedBorder(UIConstants.LIGHT_GREEN, 14));
        return sp;
    }

    // ── Background image ─────────────────────────────────────────────────
    private void loadBackgroundImage() {
        try {
            ImageIcon icon = new ImageIcon("src/main/resources/farm.jpeg");
            if (icon.getIconWidth() > 0) {
                backgroundImage = icon.getImage();
                imageLoaded = true;
            }
        } catch (Exception e) {
            imageLoaded = false;
        }
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

    // ── Helpers ──────────────────────────────────────────────────────────
    private Color demandColor(String d) {
        return "High".equals(d)   ? new Color(56, 142, 60)
                : "Medium".equals(d) ? new Color(245, 124, 0)
                :                      new Color(198, 40, 40);
    }

    private Color demandDark(String d) {
        return "High".equals(d)   ? new Color(27, 94, 32)
                : "Medium".equals(d) ? new Color(191, 84, 0)
                :                      new Color(140, 20, 20);
    }
}