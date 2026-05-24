package com.unipath.model;

import com.unipath.dataBase.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Thesis {

    private int diplomaticId;
    private int professorId;
    private String title;
    private String description;
    private String prerequisites;
    private int requiredECTS;
    private boolean isAvailable;
    private int maxCandidates;
    private int interestedStudents;
    private Date publicationDate;
    private String requiredSkills;
    private Date lastModifiedDate;

    public Thesis() {
    }

    public Thesis(int professorId, String title, String description,
                  String prerequisites, int requiredECTS,
                  int maxCandidates, String requiredSkills) {
        this.professorId = professorId;
        this.title = title;
        this.description = description;
        this.prerequisites = prerequisites;
        this.requiredECTS = requiredECTS;
        this.maxCandidates = maxCandidates;
        this.requiredSkills = requiredSkills;
        this.isAvailable = true;
        this.publicationDate = new Date();
        this.lastModifiedDate = new Date();
    }

    // Βοηθητική μέθοδος για τη σύνδεση με τη βάση δεδομένων
    private static Connection getConnection() throws SQLException {
        return DBManager.getInstance().connect();
    }

    // ── ΟΙ ΜΕΘΟΔΟΙ ΑΠΟ ΤΟ SEQUENCE DIAGRAM (SD) ───────────────────

    /**
     * [Sequence Diagram]: findAllThesis()
     * Επιστρέφει όλες τις διαθέσιμες διπλωματικές από τη βάση δεδομένων.
     */
    public static List<Thesis> findAllThesis() {
        String sql = "SELECT * FROM Thesis WHERE isAvailable = 1";
        List<Thesis> theses = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Thesis thesis = new Thesis();
                thesis.setDiplomaticId(rs.getInt("thesisId"));
                thesis.setProfessorId(rs.getInt("professorId"));
                thesis.setTitle(rs.getString("title"));
                thesis.setDescription(rs.getString("description"));
                thesis.setPrerequisites(rs.getString("prerequisites"));
                thesis.setRequiredECTS(rs.getInt("requiredECTS"));
                thesis.setAvailable(rs.getInt("isAvailable") == 1);
                thesis.setMaxCandidates(rs.getInt("maxCandidates"));
                thesis.setRequiredSkills(rs.getString("requiredSkills"));
                theses.add(thesis);
            }
        } catch (SQLException e) {
            System.out.println("Σφάλμα κατά την εκτέλεση της Thesis.findAllThesis: " + e.getMessage());
        }
        return theses;
    }

    /**
     * [Sequence Diagram]: findThesisDetails(thesisId)
     * Επιστρέφει τις πλήρεις λεπτομέρειες μιας συγκεκριμένης διπλωματικής.
     */
    public static Thesis findThesisDetails(int thesisId) {
        String sql = "SELECT * FROM Thesis WHERE thesisId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, thesisId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Thesis thesis = new Thesis();
                thesis.setDiplomaticId(rs.getInt("thesisId"));
                thesis.setProfessorId(rs.getInt("professorId"));
                thesis.setTitle(rs.getString("title"));
                thesis.setDescription(rs.getString("description"));
                thesis.setPrerequisites(rs.getString("prerequisites"));
                thesis.setRequiredECTS(rs.getInt("requiredECTS"));
                thesis.setAvailable(rs.getInt("isAvailable") == 1);
                thesis.setMaxCandidates(rs.getInt("maxCandidates"));
                thesis.setRequiredSkills(rs.getString("requiredSkills"));
                return thesis;
            }
        } catch (SQLException e) {
            System.out.println("Σφάλμα κατά την εκτέλεση της Thesis.findThesisDetails: " + e.getMessage());
        }
        return null;
    }
    // ─────────────────────────────────────────────────────────────

    // Getters
    public int getDiplomaticId()       { return diplomaticId; }
    public int getProfessorId()        { return professorId; }
    public String getTitle()           { return title; }
    public String getDescription()     { return description; }
    public String getPrerequisites()   { return prerequisites; }
    public int getRequiredECTS()       { return requiredECTS; }
    public boolean isAvailable()       { return isAvailable; }
    public int getMaxCandidates()      { return maxCandidates; }
    public int getInterestedStudents() { return interestedStudents; }
    public Date getPublicationDate()   { return publicationDate; }
    public String getRequiredSkills()  { return requiredSkills; }
    public Date getLastModifiedDate()  { return lastModifiedDate; }

    // Setters
    public void setDiplomaticId(int id)            { this.diplomaticId = id; }
    public void setProfessorId(int id)             { this.professorId = id; }
    public void setTitle(String title)             { this.title = title; }
    public void setDescription(String desc)        { this.description = desc; }
    public void setPrerequisites(String prereq)    { this.prerequisites = prereq; }
    public void setRequiredECTS(int ects)          { this.requiredECTS = ects; }
    public void setAvailable(boolean available)    { this.isAvailable = available; }
    public void setMaxCandidates(int max)          { this.maxCandidates = max; }
    public void setInterestedStudents(int count)   { this.interestedStudents = count; }
    public void setPublicationDate(Date date)      { this.publicationDate = date; }
    public void setRequiredSkills(String skills)   { this.requiredSkills = skills; }
    public void setLastModifiedDate(Date date)     { this.lastModifiedDate = date; }

    @Override
    public String toString() {
        if (this.title != null) {
            return this.title + " (Απαιτούμενα ECTS: " + this.requiredECTS + ")";
        }
        return "Διπλωματική Εργασία (ID: " + this.diplomaticId + ")";
    }
}