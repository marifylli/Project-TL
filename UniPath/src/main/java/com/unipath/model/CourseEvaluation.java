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


    public void create() {


    }


    public void saveEvaluation() {


    }


    public boolean checkEvaluationRight() {
        return false;
    }


}




