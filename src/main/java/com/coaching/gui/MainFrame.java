package com.coaching.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Coaching Management System");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        DashboardPanel dashboardPanel = new DashboardPanel();
        ReportsPanel reportsPanel = new ReportsPanel();

        tabs.addTab("Dashboard", dashboardPanel);
        tabs.addTab("Students", new StudentPanel());
        tabs.addTab("Courses", new CoursePanel());
        tabs.addTab("Instructors", new InstructorPanel());
        tabs.addTab("Enrollments", new EnrollmentPanel());
        tabs.addTab("Attendance", new AttendancePanel());
        tabs.addTab("Payments", new PaymentPanel());
        tabs.addTab("Reports", reportsPanel);

        tabs.addChangeListener(e -> {
            java.awt.Component selected = tabs.getSelectedComponent();
            if (selected == dashboardPanel) {
                dashboardPanel.refresh();
            } else if (selected == reportsPanel) {
                reportsPanel.refresh();
            }
        });

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
    }
}
