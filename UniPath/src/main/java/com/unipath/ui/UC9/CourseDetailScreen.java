package com.unipath.ui.UC9;

import com.unipath.controller.ManageProfCourseEdit;
import com.unipath.model.Course;

public class CourseDetailScreen {

    private ManageProfCourseEdit manageProfCourseEdit;
    private Course course;

    public CourseDetailScreen(ManageProfCourseEdit manageProfCourseEdit, Course course) {
        this.manageProfCourseEdit = manageProfCourseEdit;
        this.course = course;
    }

    public void displayCourseDetails() {
        // render UI λεπτομερειών
    }

    public void editCourseFields() {
        manageProfCourseEdit.onEditCourseFields();
    }

    public void clicksRules() {
        manageProfCourseEdit.onRulesClicked();
    }

}
