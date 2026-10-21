package com.resolvr.ui;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final JTextField username = new JTextField(18);
    private final JPasswordField password = new JPasswordField(18);

    // Temporary demo credentials only.
    // Replace with real authentication before deployment.
    private static final String DEMO_USERNAME = "admin";
    private static final String DEMO_PASSWORD = "admin123";

    public LoginFrame() {
        setTitle("RESOLVR - Staff Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(460, 440);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(new Color(239, 243, 249));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(220, 225, 233)),
                BorderFactory.createEmptyBorder(28, 32, 28, 32)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel title = new JLabel("RESOLVR");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(new Color(29, 42, 65));
        card.add(title, gbc);

        gbc.gridy++;
        JLabel subtitle = new JLabel("Staff Management Portal");
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setForeground(Color.GRAY);
        card.add(subtitle, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        card.add(new JLabel("Username"), gbc);

        gbc.gridx = 1;
        card.add(username, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        card.add(new JLabel("Password"), gbc);

        gbc.gridx = 1;
        card.add(password, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JButton login = new JButton("Staff Login");
        login.setFont(new Font("SansSerif", Font.BOLD, 15));
        login.setBackground(new Color(39, 85, 145));
        login.setForeground(Color.WHITE);
        login.setFocusPainted(false);
        card.add(login, gbc);

        gbc.gridy++;
        JLabel note = new JLabel("Demo credentials: admin / admin123");
        note.setHorizontalAlignment(SwingConstants.CENTER);
        note.setFont(new Font("SansSerif", Font.PLAIN, 11));
        note.setForeground(Color.GRAY);
        card.add(note, gbc);

        gbc.gridy++;
        JButton customerPortalBtn = new JButton("Looking to rent? Go to Customer Portal →");
        customerPortalBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        customerPortalBtn.setForeground(new Color(39, 85, 145));
        customerPortalBtn.setContentAreaFilled(false);
        customerPortalBtn.setBorderPainted(false);
        customerPortalBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        customerPortalBtn.addActionListener(e -> {
            dispose();
            new CustomerLoginFrame().setVisible(true);
        });
        card.add(customerPortalBtn, gbc);

        background.add(card);
        add(background);

        login.addActionListener(e -> login());
        getRootPane().setDefaultButton(login);
    }

    private void login() {
        String user = username.getText().trim();
        String pass = new String(password.getPassword());

        if (DEMO_USERNAME.equals(user)
                && DEMO_PASSWORD.equals(pass)) {
            dispose();
            new DashboardFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid demo username or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
            password.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Use the default Swing appearance.
            }

            new LoginFrame().setVisible(true);
        });
    }
}
