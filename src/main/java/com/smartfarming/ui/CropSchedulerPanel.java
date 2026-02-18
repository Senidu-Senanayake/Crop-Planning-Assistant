package com.smartfarming.ui;

import com.smartfarming.engine.SchedulePlanner;
import com.smartfarming.model.RecommendationResult;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class CropSchedulerPanel extends JPanel {

    public CropSchedulerPanel(AppFrame frame, RecommendationResult result, String season) {
        setLayout(new BorderLayout());
        setBackground(UIConstants.PALE_GREEN);

        add(new SidebarPanel(frame, AppFrame.CARD_SCHEDULER), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UIConstants.PALE_GREEN);
        main.add(new HeaderBannerPanel("Crop Schedule Planner"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UIConstants.PALE_GREEN);
        content.setBorder(BorderFactory.createEmptyBorder(28, 30, 28, 30));

        if (result != null) {
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
            scroll.setBackground(UIConstants.WHITE);
            content.add(scroll, BorderLayout.CENTER);
        } else {
            content.add(placeholder(), BorderLayout.CENTER);
        }

        main.add(content, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    private JTable buildStyledTable(Object[][] data, String[] cols) {
        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(60);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 2));
        table.setBackground(UIConstants.WHITE);

        // Header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(UIConstants.TABLE_HEADER_BG);
        header.setForeground(UIConstants.WHITE);
        header.setPreferredSize(new Dimension(0, 52));

        // Row renderer
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                c.setBackground(row % 2 == 0 ? UIConstants.WHITE : UIConstants.TABLE_ROW_ALT);
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                ((JLabel) c).setFont(new Font("Segoe UI", Font.PLAIN, 15));
                return c;
            }
        });

        return table;
    }

    private JLabel placeholder() {
        JLabel l = new JLabel("Run analysis first to view the schedule.", SwingConstants.CENTER);
        l.setFont(UIConstants.FONT_BODY);
        return l;
    }
}
