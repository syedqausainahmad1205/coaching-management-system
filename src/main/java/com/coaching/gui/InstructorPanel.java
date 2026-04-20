package com.coaching.gui;

import com.coaching.model.Instructor;
import com.coaching.service.InstructorService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InstructorPanel extends JPanel {
    private final InstructorService service = new InstructorService();
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Phone", "Specialization", "Availability"}, 0);
    private final JTable table = new JTable(model);
    private final JTextField search = new JTextField();
    private final JTextField name = new JTextField();
    private final JTextField email = new JTextField();
    private final JTextField phone = new JTextField();
    private final JTextField specialization = new JTextField();
    private final JTextField availability = new JTextField();

    public InstructorPanel() {
        setLayout(new BorderLayout(8, 8));

        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.add(new JLabel("Search:"), BorderLayout.WEST);
        top.add(search, BorderLayout.CENTER);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> loadTable(search.getText()));
        top.add(searchBtn, BorderLayout.EAST);

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.add(new JLabel("Name")); form.add(name);
        form.add(new JLabel("Email")); form.add(email);
        form.add(new JLabel("Phone")); form.add(phone);
        form.add(new JLabel("Specialization")); form.add(specialization);
        form.add(new JLabel("Availability")); form.add(availability);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton refresh = new JButton("Refresh");
        actions.add(add); actions.add(update); actions.add(delete); actions.add(refresh);

        add.addActionListener(e -> perform(() -> service.create(name.getText(), email.getText(), phone.getText(), specialization.getText(), availability.getText())));
        update.addActionListener(e -> performWithSelectedId(id -> service.update(id, name.getText(), email.getText(), phone.getText(), specialization.getText(), availability.getText())));
        delete.addActionListener(e -> performWithSelectedId(service::delete));
        refresh.addActionListener(e -> loadTable(search.getText()));

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                name.setText(String.valueOf(model.getValueAt(row, 1)));
                email.setText(String.valueOf(model.getValueAt(row, 2)));
                phone.setText(String.valueOf(model.getValueAt(row, 3)));
                specialization.setText(String.valueOf(model.getValueAt(row, 4)));
                availability.setText(String.valueOf(model.getValueAt(row, 5)));
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
        for (Instructor i : service.findAll(keyword)) {
            model.addRow(new Object[]{i.id(), i.name(), i.email(), i.phone(), i.specialization(), i.availability()});
        }
    }
}
