package com.coaching;

import com.coaching.gui.MainFrame;
import com.coaching.util.DatabaseInitializer;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer.initialize();
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
