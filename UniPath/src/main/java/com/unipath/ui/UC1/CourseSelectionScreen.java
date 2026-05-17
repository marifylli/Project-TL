package com.unipath.ui.UC1;

import com.unipath.controller.ManageStudyPlan;
import com.unipath.model.Course;
import com.unipath.model.Scenario;

import java.util.List;

public class CourseSelectionScreen {

    private ManageStudyPlan manageStudyPlan;
    private Scenario scenario;

    public CourseSelectionScreen(ManageStudyPlan manageStudyPlan, Scenario scenario) {
        this.manageStudyPlan = manageStudyPlan;
        this.scenario = scenario;
    }

    public void displayCourses() {
        // render UI
    }

    public void selectCourses(List<Course> courses) {

        manageStudyPlan.onCoursesSelected(scenario, courses);
    }
}
