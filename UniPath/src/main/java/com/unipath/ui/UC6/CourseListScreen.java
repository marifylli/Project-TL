package com.unipath.ui.UC6;

import com.unipath.controller.ManageSecCourEditClass;
import com.unipath.model.Course;
import java.util.List;

public class CourseListScreen {
    private ManageSecCourEditClass controller;
    private List<Course> courses;

    public CourseListScreen(ManageSecCourEditClass controller, List<Course> courses) {
        this.controller = controller;
        this.courses = courses;
    }

    public void display() {
        System.out.println("CourseListScreen: Εμφάνιση λίστας μαθημάτων.");
    }
}
