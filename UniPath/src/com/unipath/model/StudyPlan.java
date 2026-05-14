package com.unipath.model;

import java.util.Date;
import java.util.List;

public class StudyPlan {
    private int planId;
    private int studentId;
    private int scenarioId;
    private Scenario scenario;
    private String academicYear;
    private String directionName;
    private int totalECTS;
    private String status;
    private boolean isValid;
    private int isDraft;
    private boolean isFinalized;
    private float workloadIndex;
    private Date workloadCalculationDate;
    private Date createdDate;
    private Date lastUpdatedDate;

    public StudyPlan() {
    }
}
