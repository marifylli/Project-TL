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
                    .anyMatch(c -> filters.getCourseId().equals(c.courseID));
            if (!found) return false;
        }

        return true;
    }

    // getters and setters for uc4 marifylli

    public int getPlanId()
    { return planId; }
    public void setPlanId(int planId)
    { this.planId = planId; }

    public int getStudentId()
    { return studentId; }
    public void setStudentId(int studentId)
    { this.studentId = studentId; }

    public int getScenarioId()
    { return scenarioId; }
    public void setScenarioId(int scenarioId)
    { this.scenarioId = scenarioId; }

    public Scenario getScenario()
    { return scenario; }
    public void setScenario(Scenario scenario)
    { this.scenario = scenario; }

    public List<Course> getCourses()
    { return courses; }
    public void setCourses(List<Course> courses)
    { this.courses = courses; }

    public String getAcademicYear()
    { return academicYear; }
    public void setAcademicYear(String academicYear)
    { this.academicYear = academicYear; }

    public String getDirectionName()
    { return directionName; }
    public void setDirectionName(String directionName)
    { this.directionName = directionName; }

    public int getTotalECTS()
    { return totalECTS; }
    public void setTotalECTS(int totalECTS)
    { this.totalECTS = totalECTS; }

    public String getStatus()
    { return status; }
    public void setStatus(String status)
    { this.status = status; }

    public boolean isValid()
    { return isValid; }
    public void setValid(boolean valid)
    { this.isValid = valid; }

    public int getIsDraft()
    { return isDraft; }
    public void setIsDraft(int isDraft)
    { this.isDraft = isDraft; }

    public boolean isFinalized()
    { return isFinalized; }
    public void setFinalized(boolean finalized)
    { this.isFinalized = finalized; }

    public float getWorkloadIndex()
    { return workloadIndex; }
    public void setWorkloadIndex(float workloadIndex)
    { this.workloadIndex = workloadIndex; }
}
