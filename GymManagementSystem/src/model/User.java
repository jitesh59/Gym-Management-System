package model;

import java.io.Serializable;

/**
 * User.java
 * Represents an admin login account, including a security question
 * for file-based password recovery.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String securityQuestion;
    private String securityAnswer;

    public User() {
    }

    public User(String username, String password, String securityQuestion, String securityAnswer) {
        this.username = username;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getSecurityQuestion() { return securityQuestion; }
    public void setSecurityQuestion(String securityQuestion) { this.securityQuestion = securityQuestion; }

    public String getSecurityAnswer() { return securityAnswer; }
    public void setSecurityAnswer(String securityAnswer) { this.securityAnswer = securityAnswer; }
}
