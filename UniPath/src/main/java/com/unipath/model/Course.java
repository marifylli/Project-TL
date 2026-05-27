package com.unipath.model;

import java.util.Date;

public class Course {

    public String courseID;
    private String title;
    private String description;

    private int ects;
    private int semester;
    private boolean groupA;
    private boolean groupB;
    private String directions;
    private boolean isActive;
    private float averageRating;
    private float workloadScore;
    private int workloadRank;
    private Date lastModifiedDate;
    private String lastModifiedBy;
    private String rules;
    private String prerequisites;

    private int professorId;

    // Constructor
    public Course() {
    }

    // GETTERS & SETTERS
    public String getCourseID() { return courseID; }
    public void setCourseID(String courseID) { this.courseID = courseID; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getECTS() { return ects; }
    public void setECTS(int ects) { this.ects = ects; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public boolean isGroupA() { return groupA; }
    public void setGroupA(boolean groupA) { this.groupA = groupA; }

    public boolean isGroupB() { return groupB; }
    public void setGroupB(boolean groupB) { this.groupB = groupB; }

    public String getDirections() { return directions; }
    public void setDirections(String directions) { this.directions = directions; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean isActive) { this.isActive = isActive; }

    public float getAverageRating() { return averageRating; }
    public void setAverageRating(float averageRating) { this.averageRating = averageRating; }

    public float getWorkloadScore() { return workloadScore; }
    public void setWorkloadScore(float workloadScore) { this.workloadScore = workloadScore; }

    public int getWorkloadRank() { return workloadRank; }
    public void setWorkloadRank(int workloadRank) { this.workloadRank = workloadRank; }

    public Date getLastModifiedDate() { return lastModifiedDate; }
    public void setLastModifiedDate(Date lastModifiedDate) { this.lastModifiedDate = lastModifiedDate; }

    public String getLastModifiedBy() { return lastModifiedBy; }
    public void setLastModifiedBy(String lastModifiedBy) { this.lastModifiedBy = lastModifiedBy; }

    public String getRules() { return rules; }
    public void setRules(String rules) { this.rules = rules; }

    public String getPrerequisites() { return prerequisites; }
    public void setPrerequisites(String prerequisites) { this.prerequisites = prerequisites; }

    public int getProfessorId() { return professorId; }
    public void setProfessorId(int professorId) { this.professorId = professorId; }

    // 🌟 Έξυπνες μέθοδοι ανάγνωσης κατεύθυνσης και ομάδας από το String της SQLite
    public boolean belongsToDirection(String directionCode) {
        if (this.directions == null || this.directions.isEmpty() || directionCode == null) {
            return false;
        }
        String[] parts = this.directions.split(",");
        for (String part : parts) {
            String trimmedPart = part.trim();
            String currentDirCode = trimmedPart.split(":")[0].trim().toUpperCase();

            currentDirCode = currentDirCode.replace('Κ', 'K');
            String searchCode = directionCode.trim().toUpperCase().replace('Κ', 'K');

            if (currentDirCode.equals(searchCode)) {
                return true;
            }
        }
        return false;
    }

    public boolean isGroupAForDirection(String directionCode) {
        if (this.directions == null || this.directions.isEmpty() || directionCode == null) {
            return false;
        }
        String[] parts = this.directions.split(",");
        for (String part : parts) {
            String trimmedPart = part.trim().toUpperCase().replace('Κ', 'K');
            String searchCode = directionCode.trim().toUpperCase().replace('Κ', 'K');

            if (trimmedPart.startsWith(searchCode) && trimmedPart.contains(":A")) {
                return true;
            }
        }
        return this.groupA;
    }

    public boolean isGroupBForDirection(String directionCode) {
        if (this.directions == null || this.directions.isEmpty() || directionCode == null) {
            return false;
        }
        String[] parts = this.directions.split(",");
        for (String part : parts) {
            String trimmedPart = part.trim().toUpperCase().replace('Κ', 'K');
            String searchCode = directionCode.trim().toUpperCase().replace('Κ', 'K');

            if (trimmedPart.startsWith(searchCode) && trimmedPart.contains(":B")) {
                return true;
            }
        }
        return this.groupB;
    }

    @Override
    public String toString() {
        return this.getCourseID() + " - " + this.getTitle() + " (" + this.getECTS() + " ECTS)";
    }
} // Εδώ κλείνει σωστά η κλάση Course