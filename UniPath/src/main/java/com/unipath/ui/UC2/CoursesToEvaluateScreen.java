package com.unipath.ui.UC2;

import com.unipath.controller.ManageEvaluation;
import com.unipath.model.Course;
import java.util.List;

public class CoursesToEvaluateScreen {
    private ManageEvaluation manageEvaluation;
    private List<Course> courses;

    public CoursesToEvaluateScreen(ManageEvaluation manageEvaluation) {
        this.manageEvaluation = manageEvaluation;
    }

    public void displayCourses() {
        // show courses list
    }

    public void onCourseSelected(Course course) {
        manageEvaluation.onCourseSelected(course);
    }
}

