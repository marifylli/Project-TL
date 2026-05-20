package com.unipath.model;


import java.util.Date;
import java.util.List;

public class StudyPlan {
    private int planId;
    private int studentId;
    private int scenarioId;
    private Scenario scenario;
    private List<Course> courses; //courses
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


    // methodos gia usecase4 na sygkrinei to studyplan me ta filtra
    public boolean compareWithFilters(FilterCriteria filters) {
        if (filters == null) return true;

        // Filtrer: academiv year
        if (filters.getAcademicYear() != null
                && !filters.getAcademicYear().isBlank()
                && !this.academicYear.equals(filters.getAcademicYear())) {
            return false;
        }

        // kateuthinsi
        if (filters.getDirectionName() != null
                && !filters.getDirectionName().isBlank()
                && !this.directionName.equals(filters.getDirectionName())) {
            return false;
        }

        //  status of plan (p.χ. "FINALIZED")
        if (filters.getStatus() != null
                && !filters.getStatus().isBlank()
                && !this.status.equals(filters.getStatus())) {
            return false;
        }

        //  courseId — psaxnei sti lista courses me to courseID
        if (filters.getCourseId() != null
                && !filters.getCourseId().isBlank()) {
            if (this.courses == null || this.courses.isEmpty()) return false;
            boolean found = this.courses.stream()
                    .anyMatch(c -> filters.getCourseId()
                            .equals(String.valueOf(c.courseID)));
            if (!found) return false;
        }

        return true;
    }
}
