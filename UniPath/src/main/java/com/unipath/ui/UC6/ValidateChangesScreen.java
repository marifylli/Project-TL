package com.unipath.ui.UC6;

import com.unipath.controller.ManageSecCourEditClass;
import com.unipath.model.Course;

public class ValidateChangesScreen {
    private ManageSecCourEditClass controller;
    private Course course;

    public ValidateChangesScreen(ManageSecCourEditClass controller, Course course) {
        this.controller = controller;
        this.course = course;
    }

    public void display() {
        System.out.println("ValidateChangesScreen: Επιβεβαίωση αλλαγών.");
    }

}
