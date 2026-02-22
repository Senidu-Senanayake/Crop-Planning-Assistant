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

        // ✅ UPDATED: content is now OPAQUE to mask the image
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

    // [buildSummaryCards, summaryCard, buildPathPanel, drawArrowHead methods remain the same as your source]

    private JScrollPane buildMarketTable(List<Market> markets) {
        // [Table logic remains the same as your source]
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
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                boolean isOptimal = markets.get(row).isOptimal();
                c.setBackground(isOptimal ? new Color(200, 240, 200) : (row % 2 == 0 ? Color.WHITE : UIConstants.TABLE_ROW_ALT));
                ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);
                if (col == 4) {
                    String d = String.valueOf(val);
                    c.setForeground("High".equals(d) ? new Color(0,100,0) : "Medium".equals(d) ? new Color(180,100,0) : new Color(150,0,0));
                } else { c.setForeground(UIConstants.DARK_TEXT); }
                return c;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Color.WHITE);
        sp.setBorder(new UIConstants.RoundedBorder(UIConstants.LIGHT_GREEN, 14));
        return sp;
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

    // Helper methods from your original source for summaryCard, buildSummaryCards, buildPathPanel, etc.
    private JPanel summaryCard(String label, String value, Color top, Color bottom) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0,0,top,0,getHeight(),bottom));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),18,18);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(12,14,12,14));
        JLabel lbl = new JLabel(label); lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12)); lbl.setForeground(new Color(255,255,255,200));
        JLabel val = new JLabel(value); val.setFont(new Font("Segoe UI", Font.BOLD, 18)); val.setForeground(Color.WHITE);
        card.add(lbl); card.add(Box.createVerticalStrut(4)); card.add(val);
        return card;
    }
    private JPanel buildSummaryCards(Market optimal) {
        JPanel row = new JPanel(new GridLayout(1, 4, 12, 0)); row.setOpaque(false);
        row.add(summaryCard("📍 Optimal Market", optimal.getName(), new Color(56,142,60), new Color(27,94,32)));
        row.add(summaryCard("📏 Distance", String.format("%.0f km", optimal.getDistanceKm()), new Color(25,118,210), new Color(13,71,161)));
        row.add(summaryCard("🚚 Transport Cost", String.format("Rs. %.0f", optimal.getTransportCost()), new Color(245,124,0), new Color(191,84,0)));
        row.add(summaryCard("📈 Market Demand", optimal.getDemand(), demandColor(optimal.getDemand()), demandDark(optimal.getDemand())));
        JPanel wrapper = new JPanel(new BorderLayout()); wrapper.setOpaque(false); wrapper.add(row, BorderLayout.CENTER); return wrapper;
    }
    private JPanel buildPathPanel(Market optimal) {
        JPanel wrapper = new JPanel(new BorderLayout()); wrapper.setOpaque(false);
        JPanel diagram = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g); Graphics2D g2 = (Graphics2D) g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255,255,255,200)); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        diagram.setPreferredSize(new Dimension(0, 120)); wrapper.add(diagram, BorderLayout.CENTER); return wrapper;
    }
    private Color demandColor(String d) { return "High".equals(d) ? new Color(56,142,60) : ("Medium".equals(d) ? new Color(245,124,0) : new Color(198,40,40)); }
    private Color demandDark(String d) { return "High".equals(d) ? new Color(27,94,32) : ("Medium".equals(d) ? new Color(191,84,0) : new Color(140,20,20)); }
}