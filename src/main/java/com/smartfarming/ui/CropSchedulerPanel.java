package com.smartfarming.ui;

import com.smartfarming.engine.SchedulePlanner;
import com.smartfarming.model.RecommendationResult;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class CropSchedulerPanel extends JPanel {

    private Image backgroundImage;
    private boolean imageLoaded = false;

    public CropSchedulerPanel(AppFrame frame, RecommendationResult result, String season) {
        loadBackgroundImage();
        setLayout(new BorderLayout());
        setOpaque(false); // Let the background image show through the base

        add(new SidebarPanel(frame, AppFrame.CARD_SCHEDULER), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(new HeaderBannerPanel("Crop Schedule Planner"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        // 🟢 THIS IS THE MAGIC TRICK: Makes the detail UI solid pale green, covering the image!
        content.setOpaque(true);
        content.setBackground(UIConstants.PALE_GREEN);
        content.setBorder(BorderFactory.createEmptyBorder(28, 30, 28, 30));

        if (result != null && result.getRecommendedCrops() != null) {
            SchedulePlanner planner = new SchedulePlanner();
            List<SchedulePlanner.ScheduleEntry> schedule = planner.buildSchedule(result.getRecommendedCrops(), season);

            String[] columns = {"Crop", "Planting Date", "Harvest Date", "Duration"};
            Object[][] data = new Object[schedule.size()][4];
            for (int i = 0; i < schedule.size(); i++) {
                SchedulePlanner.ScheduleEntry e = schedule.get(i);
                data[i][0] = e.cropName();
                data[i][1] = e.plantingDate();
                data[i][2] = e.harvestDate();
                data[i][3] = e.durationDays() + " Days";
            }

            JTable table = buildStyledTable(data, columns);
            JScrollPane scroll = new JScrollPane(table);
            scroll.setBorder(new UIConstants.RoundedBorder(UIConstants.LIGHT_GREEN, 16));
            scroll.getViewport().setBackground(Color.WHITE);
            content.add(scroll, BorderLayout.CENTER);
        } else {
            JLabel placeholder = new JLabel("Run analysis first to view the schedule.", SwingConstants.CENTER);
            placeholder.setFont(UIConstants.FONT_BODY);
            content.add(placeholder, BorderLayout.CENTER);
        }

        main.add(content, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    private JTable buildStyledTable(Object[][] data, String[] cols) {
        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(45);
        table.setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(UIConstants.TABLE_HEADER_BG);
        header.setForeground(UIConstants.WHITE);
        header.setPreferredSize(new Dimension(0, 45));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                c.setBackground(row % 2 == 0 ? UIConstants.WHITE : UIConstants.TABLE_ROW_ALT);
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        return table;
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