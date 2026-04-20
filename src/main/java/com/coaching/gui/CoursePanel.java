package com.coaching.gui;

import com.coaching.model.Course;
import com.coaching.service.CourseService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CoursePanel extends JPanel {
    private final CourseService service = new CourseService();
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Description", "Duration", "Fee", "Instructor ID", "Capacity", "Enrolled"}, 0);
    private final JTable table = new JTable(model);
    private final JTextField search = new JTextField();
    private final JTextField name = new JTextField();
    private final JTextField description = new JTextField();
    private final JTextField duration = new JTextField();
    private final JTextField fee = new JTextField();
    private final JTextField instructorId = new JTextField();
    private final JTextField capacity = new JTextField();
    private final JTextField enrolled = new JTextField("0");

    public CoursePanel() {
        setLayout(new BorderLayout(8, 8));

        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.add(new JLabel("Search:"), BorderLayout.WEST);
        top.add(search, BorderLayout.CENTER);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> loadTable(search.getText()));
        top.add(searchBtn, BorderLayout.EAST);

        JPanel form = new JPanel(new GridLayout(7, 2, 8, 8));
        form.add(new JLabel("Name")); form.add(name);
        form.add(new JLabel("Description")); form.add(description);
        form.add(new JLabel("Duration")); form.add(duration);
        form.add(new JLabel("Fee")); form.add(fee);
        form.add(new JLabel("Instructor ID (optional)")); form.add(instructorId);
        form.add(new JLabel("Capacity")); form.add(capacity);
        form.add(new JLabel("Enrolled Students")); form.add(enrolled);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton refresh = new JButton("Refresh");
        actions.add(add); actions.add(update); actions.add(delete); actions.add(refresh);

        add.addActionListener(e -> perform(() -> service.create(name.getText(), description.getText(), duration.getText(), fee.getText(), instructorId.getText(), capacity.getText(), enrolled.getText())));
        update.addActionListener(e -> performWithSelectedId(id -> service.update(id, name.getText(), description.getText(), duration.getText(), fee.getText(), instructorId.getText(), capacity.getText(), enrolled.getText())));
        delete.addActionListener(e -> performWithSelectedId(service::delete));
        refresh.addActionListener(e -> loadTable(search.getText()));

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                name.setText(String.valueOf(model.getValueAt(row, 1)));
                description.setText(String.valueOf(model.getValueAt(row, 2)));
                duration.setText(String.valueOf(model.getValueAt(row, 3)));
                fee.setText(String.valueOf(model.getValueAt(row, 4)));
                Object instructorVal = model.getValueAt(row, 5);
                instructorId.setText(instructorVal == null ? "" : String.valueOf(instructorVal));
                capacity.setText(String.valueOf(model.getValueAt(row, 6)));
                enrolled.setText(String.valueOf(model.getValueAt(row, 7)));
            }
        });

        JPanel north = new JPanel(new BorderLayout(8, 8));
        north.add(top, BorderLayout.NORTH);
        north.add(form, BorderLayout.CENTER);
        north.add(actions, BorderLayout.SOUTH);

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadTable("");
    }

    private void perform(Runnable action) {
        try {
            action.run();
            loadTable(search.getText());
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

    private void loadTable(String keyword) {
        model.setRowCount(0);
        for (Course c : service.findAll(keyword)) {
            model.addRow(new Object[]{c.id(), c.name(), c.description(), c.duration(), c.fee(), c.instructorId(), c.capacity(), c.enrolledStudents()});
        }
    }
}
