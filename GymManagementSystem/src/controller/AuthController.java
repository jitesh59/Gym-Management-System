package controller;

import model.User;
import utils.FileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * AuthController.java
 * Handles admin login, logout, session state, and forgot-password recovery.
 * Demonstrates simple file-based authentication instead of a database.
 */
public class AuthController {

    private static final String USERS_FILE = "users.dat";
    private List<User> users;
    private User currentUser; // holds the logged-in session user

    public AuthController() {
        users = FileManager.loadList(USERS_FILE);
        if (users.isEmpty()) {
            // Create a default admin account on first run
            User admin = new User("admin", "admin123", "What is your favorite color?", "blue");
            users.add(admin);
            FileManager.saveList(USERS_FILE, users);
        }
    }

    public boolean login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(password)) {
                currentUser = u;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Forgot password: verify the security question answer, then reset password.
     */
    public boolean resetPassword(String username, String securityAnswer, String newPassword) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)
                    && u.getSecurityAnswer().equalsIgnoreCase(securityAnswer)) {
                u.setPassword(newPassword);
                FileManager.saveList(USERS_FILE, users);
                return true;
            }
        }
        return false;
    }

    public String getSecurityQuestion(String username) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u.getSecurityQuestion();
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}
