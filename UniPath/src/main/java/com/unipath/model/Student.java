package com.unipath.model;
import java.util.List;

public class Student extends User {
    private int studentId;
    private int registrationYear;
    private int currentSemester;
    private List<CourseEvaluation> evaluationCourses;
    private boolean notificationsEnabled;
    private StudentProfile profile;

    public Student(){

    }
}
