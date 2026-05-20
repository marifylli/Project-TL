package com.unipath.model;

import java.util.Date;
import java.util.List;

public class Course {
    public String courseID;       // otan exoume conflict prepei na meinei string gia na tairiazei me ti vasi tis milenas
    private String title;
    private String description;
    private int ects;
    private int semester;
    private boolean groupA;
    private boolean groupB;
    private String directions; // format: "K1:A,K2:B"
    private boolean isActive;
    private float averageRating;
    private float workloadScore;
    private int workloadRank;
    private Date lastModifiedDate;
    private String lastModifiedBy;
    private String rules;
    private String prerequisites;



    public Course() {

    }
    public int getECTS() {

    return ects;}

    public void setEcts(int ects)
    { this.ects = ects; }

    public boolean isGroupA() {
        return groupA;
    }

    public boolean isGroupB() {
        return groupB;
    }

    public void setGroupA(boolean groupA)           { this.groupA = groupA; }

    public void setGroupB(boolean groupB)           { this.groupB = groupB; }

    public String getDirections() {
        return directions;
    }
    public void setDirections(String directions){
        this.directions=directions;
    }

    //uc4 used

    public String getTitle()                        { return title; }
    public void setTitle(String title)              { this.title = title; }

    public String getDescription()                  { return description; }
    public void setDescription(String desc)         { this.description = desc; }

    public int getSemester()                        { return semester; }
    public void setSemester(int semester)           { this.semester = semester; }

    public boolean isActive()                       { return isActive; }
    public void setActive(boolean active)           { this.isActive = active; }

    public float getAverageRating()                 { return averageRating; }
    public void setAverageRating(float rating)      { this.averageRating = rating; }

    public float getWorkloadScore()                 { return workloadScore; }
    public void setWorkloadScore(float score)       { this.workloadScore = score; }

    public int getWorkloadRank()                    { return workloadRank; }
    public void setWorkloadRank(int rank)           { this.workloadRank = rank; }

    public String getLastModifiedBy()               { return lastModifiedBy; }
    public void setLastModifiedBy(String user)      { this.lastModifiedBy = user; }

    public String getRules()                        { return rules; }
    public void setRules(String rules)              { this.rules = rules; }

    public String getPrerequisites()                { return prerequisites; }
    public void setPrerequisites(String prereq)     { this.prerequisites = prereq; }


}
