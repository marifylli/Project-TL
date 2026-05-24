package com.unipath.model;

import com.unipath.repository.ThesisRepository;
import com.unipath.repository.InterviewRepository;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;


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

    public Thesis() {}

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

    // Getters / Setters
    public int getDiplomaticId() { return diplomaticId; }
    public void setDiplomaticId(int id) { this.diplomaticId = id; }
    public int getProfessorId() { return professorId; }
    public void setProfessorId(int id) { this.professorId = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String desc) { this.description = desc; }
    public String getPrerequisites() { return prerequisites; }
    public void setPrerequisites(String prereq) { this.prerequisites = prereq; }
    public int getRequiredECTS() { return requiredECTS; }
    public void setRequiredECTS(int ects) { this.requiredECTS = ects; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean avail) { this.isAvailable = avail; }
    public int getMaxCandidates() { return maxCandidates; }
    public void setMaxCandidates(int max) { this.maxCandidates = max; }
    public int getInterestedStudents() { return interestedStudents; }
    public void setInterestedStudents(int count) { this.interestedStudents = count; }
    public Date getPublicationDate() { return publicationDate; }
    public void setPublicationDate(Date date) { this.publicationDate = date; }
    public String getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(String skills) { this.requiredSkills = skills; }
    public Date getLastModifiedDate() { return lastModifiedDate; }
    public void setLastModifiedDate(Date date) { this.lastModifiedDate = date; }


    public static boolean saveThesis(Thesis thesis) {
        ThesisRepository repo = new ThesisRepository();
        return repo.saveThesis(thesis);
    }

    public static List<Thesis> findAllThesis() {
        List<Thesis> theses = new ArrayList<>();
        String sql = "SELECT * FROM Thesis WHERE isAvailable = 1";

        try (java.sql.Connection conn = com.unipath.dataBase.DBManager.getInstance().connect();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Thesis t = new Thesis();
                t.setDiplomaticId(rs.getInt("thesisId"));
                t.setProfessorId(rs.getInt("professorId"));
                t.setTitle(rs.getString("title"));
                t.setDescription(rs.getString("description"));
                t.setPrerequisites(rs.getString("prerequisites"));
                t.setRequiredECTS(rs.getInt("requiredECTS"));
                t.setAvailable(rs.getInt("isAvailable") == 1);
                t.setMaxCandidates(rs.getInt("maxCandidates"));
                t.setRequiredSkills(rs.getString("requiredSkills"));
                theses.add(t);
            }
        } catch (java.sql.SQLException e) {
            System.err.println("❌ [Thesis Model]: Σφάλμα στο findAllThesis: " + e.getMessage());
        }
        return theses;
    }
    public static Thesis findThesisDetails(int thesisId) {
        String sql = "SELECT * FROM Thesis WHERE thesisId = ?";

        try (java.sql.Connection conn = com.unipath.dataBase.DBManager.getInstance().connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, thesisId);
            java.sql.ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Thesis t = new Thesis();
                t.setDiplomaticId(rs.getInt("thesisId"));
                t.setProfessorId(rs.getInt("professorId"));
                t.setTitle(rs.getString("title"));
                t.setDescription(rs.getString("description"));
                t.setPrerequisites(rs.getString("prerequisites"));
                t.setRequiredECTS(rs.getInt("requiredECTS"));
                t.setAvailable(rs.getInt("isAvailable") == 1);
                t.setMaxCandidates(rs.getInt("maxCandidates"));
                t.setRequiredSkills(rs.getString("requiredSkills"));
                return t;
            }
        } catch (java.sql.SQLException e) {
            System.err.println("❌ [Thesis Model]: Σφάλμα στο findThesisDetails: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return this.getTitle(); // Εμφανίζει τον τίτλο της διπλωματικής στη λίστα
    }
}