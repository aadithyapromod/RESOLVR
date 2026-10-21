
package com.resolvr;

import com.resolvr.ui.LoginFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception e) {
                System.err.println(
                        "Could not apply system look and feel."
                );
            }

            new LoginFrame().setVisible(true);
        });
    }
}
