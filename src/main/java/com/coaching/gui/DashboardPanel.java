package com.coaching.gui;

import com.coaching.service.ReportingService;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DashboardPanel extends JPanel {
    private final ReportingService reportingService = new ReportingService();
    private final JTextArea area = new JTextArea();

    public DashboardPanel() {
        setLayout(new BorderLayout(8, 8));
        area.setEditable(false);
        add(new JScrollPane(area), BorderLayout.CENTER);
        JButton refresh = new JButton("Refresh Dashboard");
        refresh.addActionListener(e -> refresh());
        add(refresh, BorderLayout.SOUTH);
        refresh();
    }

    public void refresh() {
        Map<String, String> data = reportingService.summary();
        StringBuilder sb = new StringBuilder("Coaching Management Dashboard\n\n");
        data.forEach((k, v) -> sb.append(k).append(": ").append(v).append("\n"));
        area.setText(sb.toString());
    }
}
