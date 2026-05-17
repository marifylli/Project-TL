package com.unipath.login;

public class FirestoreService {

    public User getUserByEmail(String email) throws Exception {
        Thread.sleep(600);

        Role role;
        if (email.equals("secretary@ceid.upatras.gr")) {
            role = Role.SECRETARY;
        } else if (email.matches("st\\d+@ceid\\.upatras\\.gr")) {
            role = Role.STUDENT;
        } else {
            role = Role.PROFESSOR;
        }

        return new User(email, email, role, email);
    }
}