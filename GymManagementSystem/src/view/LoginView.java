package view;

import controller.AuthController;
import utils.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * LoginView.java
 * Entry-point login screen: username/password authentication plus a
 * Forgot Password recovery flow based on a security question stored in file.
 */
public class LoginView extends JFrame {

    private final AuthController authController;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginView(AuthController authController) {
        this.authController = authController;
        setTitle("Gym Management System - Login");
        setSize(420, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.PRIMARY_DARK);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(40, 0, 20, 0));
        JLabel dumbbell = new JLabel("\uD83C\uDFCB Gym Management System");
        dumbbell.setFont(new Font("Segoe UI", Font.BOLD, 20));
        dumbbell.setForeground(Color.WHITE);
        dumbbell.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subtitle = new JLabel("Administrator Login");
        subtitle.setFont(UITheme.FONT_LABEL);
        subtitle.setForeground(new Color(180, 190, 210));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(dumbbell);
        header.add(Box.createVerticalStrut(6));
        header.add(subtitle);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UITheme.CARD_BG);
        card.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(UITheme.FONT_LABEL_BOLD);
        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        usernameField.setText("admin");

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(UITheme.FONT_LABEL_BOLD);
        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBackground(UITheme.PRIMARY);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(UITheme.FONT_LABEL_BOLD);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        loginBtn.addActionListener(this::onLogin);

        JButton forgotBtn = new JButton("Forgot Password?");
        forgotBtn.setBorderPainted(false);
        forgotBtn.setContentAreaFilled(false);
        forgotBtn.setForeground(UITheme.PRIMARY);
        forgotBtn.setFont(UITheme.FONT_LABEL);
        forgotBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        forgotBtn.addActionListener(this::onForgotPassword);

        JLabel hint = new JLabel("Default: admin / admin123");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(UITheme.TEXT_MUTED);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(userLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(16));
        card.add(passLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(22));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(forgotBtn);
        card.add(Box.createVerticalStrut(16));
        card.add(hint);

        JPanel cardWrapper = new JPanel(new GridBagLayout());
        cardWrapper.setOpaque(false);
        cardWrapper.add(card);

        root.add(header, BorderLayout.NORTH);
        root.add(cardWrapper, BorderLayout.CENTER);

        // Enter key triggers login
        getRootPane().setDefaultButton(loginBtn);

        setContentPane(root);
    }

    private void onLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (authController.login(username, password)) {
            dispose();
            SwingUtilities.invokeLater(() -> new MainFrame(authController).setVisible(true));
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    private void onForgotPassword(ActionEvent e) {
        String username = JOptionPane.showInputDialog(this, "Enter your username:");
        if (username == null || username.trim().isEmpty()) return;

        String question = authController.getSecurityQuestion(username.trim());
        if (question == null) {
            JOptionPane.showMessageDialog(this, "Username not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String answer = JOptionPane.showInputDialog(this, question);
        if (answer == null) return;

        String newPassword = JOptionPane.showInputDialog(this, "Enter your new password:");
        if (newPassword == null || newPassword.trim().isEmpty()) return;

        if (authController.resetPassword(username.trim(), answer.trim(), newPassword.trim())) {
            JOptionPane.showMessageDialog(this, "Password reset successful! Please login.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect answer to security question.",
                    "Recovery Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
