package com.unipath.model;

public class Secretary extends User {
    private int secretaryId;
    private String department;

    //  Constructor
    public Secretary() {
    }

    // Getters  Setters
    public int getSecretaryId() {
        return secretaryId;
    }

    public void setSecretaryId(int secretaryId) {
        this.secretaryId = secretaryId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}