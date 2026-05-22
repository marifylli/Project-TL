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

    // Κατασκευαστής - Αντιστοιχεί στο "create" από το SecretaryMainScreen
    public ManageSecCourEditClass() {
        this.courseRepository = new CourseRepository();
        this.notificationRepository = new NotificationRepository();

        // Κλήση queryCourseList() προς DBManager
        List<Course> courseList = courseRepository.queryCourseList();

        // alt2: [There aren't Courses in the Data Base]
        if (courseList == null || courseList.isEmpty()) {
            ErrorScreen errorScreen = new ErrorScreen();
            errorScreen.display();
            return;
        }

        // [There are Courses in the Data Base] -> "create" & display CourseListScreen
        CourseListScreen courseListScreen = new CourseListScreen(this, courseList);
        courseListScreen.display();
    }

    // Βέλος: selectCourse()
    public void selectCourse(String courseId) {
        // Κλήση getCourseDetails() από το Entity Course
        Course course = courseRepository.getCourseDetails(courseId);

        // "create" & display CourseEditScreen
        CourseEditScreen courseEditScreen = new CourseEditScreen(this, course);
        courseEditScreen.display();
    }

    // Βέλος: modifyCourse()
    public void modifyCourse(Course modifiedCourse) {
        System.out.println("Λήψη τροποποιημένων στοιχείων στην CourseEditScreen...");
    }

    // Βέλος: saveChanges()
    public void saveChanges(Course modifiedCourse) {

        // Εσωτερική κλήση: checkForChanges (self-call στο διάγραμμα)
        boolean hasErrors = checkForChanges(modifiedCourse);

        // alt1: [Invalid Changes]
        if (hasErrors) {
            ErrorScreen errorScreen = new ErrorScreen();
            errorScreen.display();

            // Επιστροφή στην CourseEditScreen
            CourseEditScreen courseEditScreen = new CourseEditScreen(this, modifiedCourse);
            courseEditScreen.display();
            return;
        }

        // alt1: [Valid Changes] -> "create" & display ValidateChangesScreen
        ValidateChangesScreen validateChangesScreen = new ValidateChangesScreen(this, modifiedCourse);
        validateChangesScreen.display();
    }

    // Βέλος: confirmChanges()
    public void confirmChanges(Course course) {
        // 1. Κλήση saveEdits() στο Entity Course
        courseRepository.saveEdits(course);

        // 2. Χρήση του υφιστάμενου Model StudyPlan και κλήση refreshStudyPlans()
        StudyPlan studyPlanEntity = new StudyPlan();
        courseRepository.refreshStudyPlans(course.getCourseID());

        // 3. Κλήση sendNotification() στο Entity Notification
        notificationRepository.notifyAllStudentsAboutCourse("Επικαιροποιήθηκε το μάθημα: " + course.getTitle());

        // Αναμονή για confirmNotification()
        confirmNotification();
    }

    // Βέλος: confirmNotification()
    public void confirmNotification() {
        // "create" & display SuccessScreen
        SuccessScreen successScreen = new SuccessScreen();
        successScreen.display();
    }

    // Εσωτερική μέθοδος Controller: checkForChanges
    private boolean checkForChanges(Course course) {
        return course.getTitle() == null || course.getTitle().trim().isEmpty() || course.getEcts() <= 0;
    }

}
