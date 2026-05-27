package com.unipath.model;

import java.util.ArrayList;
import java.util.List;

public class Professor {


    private int professorId;
    private String fullName;
    private String email;
    private String office;


    private List<String> thesisTopics; // Λίστα με τα θέματα που έχει αναρτήσει


    private static final int MAX_TEACHING_LOAD = 3;
    private int currentTeachingLoad;

    public Professor() {
        this.thesisTopics = new ArrayList<>();
    }

    // Constructor
    public Professor(int professorId, String fullName, String email, String office) {
        this.professorId = professorId;
        this.fullName = fullName;
        this.email = email;
        this.office = office;
        this.thesisTopics = new ArrayList<>();
    }

    // --- GETTERS & SETTERS ---

    public int getProfessorId() { return professorId; }
    public void setProfessorId(int professorId) { this.professorId = professorId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOffice() { return office; }
    public void setOffice(String office) { this.office = office; }

    public List<String> getThesisTopics() { return thesisTopics; }
    public void addThesisTopic(String topic) { this.thesisTopics.add(topic); }

    public int getCurrentTeachingLoad() { return currentTeachingLoad; }
    public void setCurrentTeachingLoad(int currentTeachingLoad) { this.currentTeachingLoad = currentTeachingLoad; }

    // Μέθοδος που θα μπορούσε να χρησιμοποιηθεί για validation στο UC10
    public boolean canAcceptMoreTheses() {
        return currentTeachingLoad < MAX_TEACHING_LOAD;
    }
}