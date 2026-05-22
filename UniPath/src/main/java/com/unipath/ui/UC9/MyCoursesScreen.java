package com.unipath.ui.UC9;

import com.unipath.controller.ManageProfCourseEdit;
import com.unipath.model.Course;

import java.util.List;

public class MyCoursesScreen {
    private ManageProfCourseEdit manageProfCourseEdit;
    private List<Course> courses;



    public MyCoursesScreen(ManageProfCourseEdit manageProfCourseEdit, List<Course> courses) {
        this.manageProfCourseEdit = manageProfCourseEdit;
        this.courses = courses;
    }

    public void displayProfCourses() {
        // render UI λίστα μαθημάτων
    }

    public void selectCourse(Course course) {
        manageProfCourseEdit.onCourseSelected(course);
    }

}
