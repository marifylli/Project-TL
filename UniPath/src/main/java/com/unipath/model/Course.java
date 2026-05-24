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

    //GETTERS & SETTERS
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

    // --- ΠΡΟΣΘΗΚΗ GETTER/SETTER ΓΙΑ ΤΟΝ ΚΑΘΗΓΗΤΗ ---
    public int getProfessorId() { return professorId; }
    public void setProfessorId(int professorId) { this.professorId = professorId; }
}