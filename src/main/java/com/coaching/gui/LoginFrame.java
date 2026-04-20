package com.coaching.gui;

import com.coaching.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final AuthService authService = new AuthService();
    private final JComboBox<String> role = new JComboBox<>(new String[]{"Student", "Teacher"});
    private final JTextField email = new JTextField();
    private final JPasswordField password = new JPasswordField();

    public LoginFrame() {
        setTitle("Login - Coaching Management System");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(420, 230);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        form.add(new JLabel("Role"));
        form.add(role);
        form.add(new JLabel("Email"));
        form.add(email);
        form.add(new JLabel("Password"));
        form.add(password);

        JButton login = new JButton("Login");
        login.addActionListener(e -> performLogin());
        getRootPane().setDefaultButton(login);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setBorder(BorderFactory.createEmptyBorder(0, 8, 12, 16));
        actions.add(login);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
    }

    private void performLogin() {
        try {
            String selectedRole = String.valueOf(role.getSelectedItem());
            String enteredEmail = email.getText();
            String enteredPassword = new String(password.getPassword());
            boolean authenticated = "Student".equals(selectedRole)
                    ? authService.authenticateStudent(enteredEmail, enteredPassword)
                    : authService.authenticateTeacher(enteredEmail, enteredPassword);
            if (!authenticated) {
                UiUtil.showInfo(this, "Invalid credentials.");
                return;
            }
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
            dispose();
        } catch (Exception ex) {
            UiUtil.showError(this, ex);
        }
    }
}
