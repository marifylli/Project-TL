package com.unipath.model;
import java.util.Date;

public class CourseEvaluation {
    private int evaluationId;
    private int studentID;
    private int courseId;
    private Date submissionDate;
    private boolean isSubmitted;
    private int rating;
    private String comments;
    private final boolean isAnonymous = true;

    public CourseEvaluation() {}

    public int getEvaluationId() { return evaluationId; }
    public void setEvaluationId(int evaluationId) { this.evaluationId = evaluationId; }

    public int getStudentID() { return studentID; }
    public void setStudentID(int studentID) { this.studentID = studentID; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public Date getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(Date submissionDate) { this.submissionDate = submissionDate; }

    public boolean isSubmitted() { return isSubmitted; }
    public void setSubmitted(boolean submitted) { this.isSubmitted = submitted; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public boolean isAnonymous() { return isAnonymous; }

    public void create() {

    }

    public void saveEvaluation() {

    }

    public boolean checkEvaluationRight() {
        return false;
    }

}
