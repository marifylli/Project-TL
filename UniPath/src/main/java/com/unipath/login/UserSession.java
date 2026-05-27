package com.unipath.login;

public class UserSession {
    private static UserSession instance;

    private int userId;
    private String email;
    private String displayName;
    private String role;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }


    public void startSession(int userId, String email, String displayName, String role) {
        this.userId = userId;
        this.email = email;
        this.displayName = displayName;
        this.role = role;
        System.out.println("[Session Started] Email αποθηκευμένο: " + this.email);
    }

    public void cleanSession() {
        this.userId = 0;
        this.email = null;
        this.displayName = null;
        this.role = null;
    }

    public int getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getDisplayName() { return displayName; }
    public String getRole() { return role; }
}