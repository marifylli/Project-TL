package com.unipath.login;


public class User {
    private final String uid;
    private final String email;
    private final Role role;
    private final String displayName;

    public User(String uid, String email, Role role, String displayName) {
        this.uid = uid;
        this.email = email;
        this.role = role;
        this.displayName = displayName;
    }

    public String getUid()         { return uid; }
    public String getEmail()       { return email; }
    public Role   getRole()        { return role; }
    public String getDisplayName() { return displayName; }

    public String getRoleDisplayName() {
        return switch (role) {
            case STUDENT   -> "Φοιτητής";
            case PROFESSOR -> "Καθηγητής";
            case SECRETARY -> "Γραμματεία";
        };
    }
}