package com.coaching.gui;

import com.coaching.service.ReportingService;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ReportsPanel extends JPanel {
    private final ReportingService reportingService = new ReportingService();
    private final JTextArea reportArea = new JTextArea();

    public ReportsPanel() {
        setLayout(new BorderLayout(8, 8));
        reportArea.setEditable(false);
        add(new JScrollPane(reportArea), BorderLayout.CENTER);
        JButton generate = new JButton("Generate Report");
        generate.addActionListener(e -> refresh());
        add(generate, BorderLayout.SOUTH);
        refresh();
    }

    public void refresh() {
        Map<String, String> summary = reportingService.summary();
        StringBuilder report = new StringBuilder("Coaching Management Report\n==========================\n");
        summary.forEach((k, v) -> report.append(k).append(": ").append(v).append("\n"));
        reportArea.setText(report.toString());
    }
}
