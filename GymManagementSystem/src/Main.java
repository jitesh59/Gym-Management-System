import controller.AuthController;
import view.LoginView;

import javax.swing.*;

/**
 * Main.java
 * Application entry point. Launches the Login screen.
 * Run this class to start the Gym Management System.
 */
public class Main {
    public static void main(String[] args) {
        // Use the system look and feel for a more native, professional appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // fall back to default look and feel silently
        }

        SwingUtilities.invokeLater(() -> {
            AuthController authController = new AuthController();
            LoginView loginView = new LoginView(authController);
            loginView.setVisible(true);
        });
    }
}
