package com.unipath.login;

public class UserSession {
    private static UserSession instance;

    private int userId;
    private String username;
    private String fullName;
    private String role;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void startSession(int userId, String username, String fullName, String role) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }

    public void cleanSession() {
        this.userId = 0;
        this.username = null;
        this.fullName = null;
        this.role = null;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
}