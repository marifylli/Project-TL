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
    private List<String> directions;
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
    public void getECTS() {

    }


}
