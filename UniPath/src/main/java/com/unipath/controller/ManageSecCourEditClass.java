package com.unipath.controller;

import com.unipath.model.Course;
import com.unipath.model.StudyPlan;
import com.unipath.repository.CourseRepository;
import com.unipath.repository.NotificationRepository;
import com.unipath.ui.UC6.*;
import java.util.List;

public class ManageSecCourEditClass {

    private CourseRepository courseRepository;
    private NotificationRepository notificationRepository;

    public ManageSecCourEditClass() {
        this.courseRepository = new CourseRepository();
        this.notificationRepository = new NotificationRepository();

        List<Course> courseList = courseRepository.queryCourseList();

        if (courseList == null || courseList.isEmpty()) {
            ErrorScreen errorScreen = new ErrorScreen();
            errorScreen.display();
            return;
        }

        CourseListScreen courseListScreen = new CourseListScreen(this, courseList);
        courseListScreen.display();
    }

    public void selectCourse(String courseId) {
        Course course = courseRepository.getCourseDetails(courseId);
        CourseEditScreen courseEditScreen = new CourseEditScreen(this, course);
        courseEditScreen.display();
    }

    public void modifyCourse(Course modifiedCourse) {
        System.out.println("Λήψη τροποποιημένων στοιχείων στην CourseEditScreen...");
    }

    public void saveChanges(Course modifiedCourse) {
        boolean hasErrors = checkForChanges(modifiedCourse);

        if (hasErrors) {
            ErrorScreen errorScreen = new ErrorScreen();
            errorScreen.display();

            CourseEditScreen courseEditScreen = new CourseEditScreen(this, modifiedCourse);
            courseEditScreen.display();
            return;
        }

        ValidateChangesScreen validateChangesScreen = new ValidateChangesScreen(this, modifiedCourse);
        validateChangesScreen.display();
    }

    public void confirmChanges(Course course) {
        courseRepository.saveEdits(course);

        StudyPlan studyPlanEntity = new StudyPlan();
        courseRepository.refreshStudyPlans(course.getCourseID());

        notificationRepository.notifyAllStudentsAboutCourse("Επikαιροποιήθηκε το μάθημα: " + course.getTitle());
        confirmNotification();
    }

    public void confirmNotification() {
        SuccessScreen successScreen = new SuccessScreen();
        successScreen.display();
    }

    private boolean checkForChanges(Course course) {
        return course.getTitle() == null || course.getTitle().trim().isEmpty() || course.getECTS() <= 0;
    }
}