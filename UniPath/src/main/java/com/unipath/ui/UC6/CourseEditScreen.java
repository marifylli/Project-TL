package com.unipath.ui.UC6;

import com.unipath.controller.ManageSecCourEditClass;
import com.unipath.model.Course;

public class CourseEditScreen {
    private ManageSecCourEditClass controller;
    private Course course;

    public CourseEditScreen(ManageSecCourEditClass controller, Course course) {
        this.controller = controller;
        this.course = course;
    }

    public void display() {
        System.out.println("CourseEditScreen: Φόρμα επεξεργασίας.");
    }

}
