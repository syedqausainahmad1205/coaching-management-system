package com.coaching.gui;

import javax.swing.*;

public final class UiUtil {
    private UiUtil() {
    }

    public static void showError(java.awt.Component parent, Exception e) {
        JOptionPane.showMessageDialog(parent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfo(java.awt.Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
