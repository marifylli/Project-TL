package com.unipath.model;

// holds the criteria of the secretary in the AnalysisFilterScreen

public class FilterCriteria {
    private String academicYear;   // matches StudyPlan.academicYear nikou
    private String directionName;  // matches StudyPlan.directionName nikou
    private String courseId;       // matches Course.courseID nikou
    private String status;         // matches StudyPlan.status nikou


    public FilterCriteria() {}

    public FilterCriteria(String academicYear, String directionName,
                          String courseId, String status) {
        this.academicYear  = academicYear;
        this.directionName = directionName;
        this.courseId      = courseId;
        this.status        = status;
    }



    // enallaktiki roi : Used by ManageStatAnalysis to check if there is enough data for the filters.

    public boolean hasAtLeastOneFilter() {
        return (academicYear  != null && !academicYear.isBlank()) || (directionName != null && !directionName.isBlank()) || (courseId      != null && !courseId.isBlank()) || (status        != null && !status.isBlank());
    }

    public String getAcademicYear()
    { return academicYear; }
    public void setAcademicYear(String y)
    { this.academicYear = y; }

    public String getDirectionName()
    { return directionName; }
    public void setDirectionName(String d)
    { this.directionName = d; }

    public String getCourseId()
    { return courseId; }
    public void setCourseId(String c)
    { this.courseId = c; }

    public String getStatus()
    { return status; }
    public void setStatus(String s)
    { this.status = s; }

    @Override
    public String toString() {
        return "FilterCriteria{year=" + academicYear
                + ", direction=" + directionName
                + ", courseId=" + courseId
                + ", status=" + status + "}";
    }
}

