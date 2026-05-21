package com.unipath.ui.UC2;

import com.unipath.controller.ManageEvaluation;
import com.unipath.model.Course;
import com.unipath.model.CourseEvaluation;

public class EvaluationFormScreen {
    private ManageEvaluation manageEvaluation;
    private Course course;

    public EvaluationFormScreen(ManageEvaluation manageEvaluation, Course course) {
        this.manageEvaluation = manageEvaluation;
        this.course = course;
    }

    public void display() {
        // show evaluation form
    }

    public void fillForm() {
        // TODO
    }

    public void submitEvaluation(CourseEvaluation evaluation) {
        manageEvaluation.saveEvaluation(evaluation);
    }

    public void highlightMissingFields() {
        // TODO
    }
}

