package com.coaching.gui;

import com.coaching.model.Enrollment;
import com.coaching.service.EnrollmentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EnrollmentPanel extends JPanel {
    private final EnrollmentService service = new EnrollmentService();
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Student ID", "Course ID", "Enrollment Date", "Status"}, 0);
    private final JTable table = new JTable(model);
    private final JTextField studentId = new JTextField();
    private final JTextField courseId = new JTextField();
    private final JTextField status = new JTextField("ACTIVE");

    public EnrollmentPanel() {
        setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.add(new JLabel("Student ID")); form.add(studentId);
        form.add(new JLabel("Course ID")); form.add(courseId);
        form.add(new JLabel("Status")); form.add(status);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Enroll");
        JButton update = new JButton("Update Status");
        JButton delete = new JButton("Delete");
        JButton refresh = new JButton("Refresh");
        actions.add(add); actions.add(update); actions.add(delete); actions.add(refresh);

        add.addActionListener(e -> perform(() -> service.create(studentId.getText(), courseId.getText(), status.getText())));
        update.addActionListener(e -> performWithSelectedId(id -> service.updateStatus(id, status.getText())));
        delete.addActionListener(e -> performWithSelectedId(service::delete));
        refresh.addActionListener(e -> loadTable());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                studentId.setText(String.valueOf(model.getValueAt(row, 1)));
                courseId.setText(String.valueOf(model.getValueAt(row, 2)));
                status.setText(String.valueOf(model.getValueAt(row, 4)));
            }
        });

        JPanel north = new JPanel(new BorderLayout(8, 8));
        north.add(form, BorderLayout.CENTER);
        north.add(actions, BorderLayout.SOUTH);

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadTable();
    }

    private void perform(Runnable action) {
        try {
            action.run();
            loadTable();
        } catch (Exception ex) {
            UiUtil.showError(this, ex);
        }
    }

    private void performWithSelectedId(java.util.function.IntConsumer action) {
        int row = table.getSelectedRow();
        if (row < 0) {
            UiUtil.showInfo(this, "Please select a row first.");
            return;
        }
        int id = Integer.parseInt(String.valueOf(model.getValueAt(row, 0)));
        perform(() -> action.accept(id));
    }

    private void loadTable() {
        model.setRowCount(0);
        for (Enrollment e : service.findAll()) {
            model.addRow(new Object[]{e.id(), e.studentId(), e.courseId(), e.enrollmentDate(), e.status()});
        }
    }
}
