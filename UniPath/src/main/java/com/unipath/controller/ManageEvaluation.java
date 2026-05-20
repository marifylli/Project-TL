package com.unipath.controller;

import com.unipath.model.Course;
import com.unipath.model.CourseEvaluation;
import com.unipath.ui.UC2.CoursesToEvaluateScreen;
import com.unipath.ui.UC2.EvaluationFormScreen;

import java.util.List;

public class ManageEvaluation {

    private CoursesToEvaluateScreen coursesToEvaluateScreen;
    private EvaluationFormScreen evaluationFormScreen;

    public void create() {
        coursesToEvaluateScreen = new CoursesToEvaluateScreen(this);
        coursesToEvaluateScreen.displayCourses();
    }

    public void onCourseSelected(Course course) {
        evaluationFormScreen = new EvaluationFormScreen(this, course);
        evaluationFormScreen.display();
    }

    public boolean checkEvaluationRight(int studentId, int courseId) {
        return false; // TODO
    }

    public boolean checkFields() {
        return false; // TODO
    }

    public void saveEvaluation(CourseEvaluation evaluation) {
        // TODO
    }

    public void updateCourseStats(int courseId) {
        // TODO
    }
}