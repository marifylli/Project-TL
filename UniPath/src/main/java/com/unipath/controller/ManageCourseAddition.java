package com.unipath.controller;

import com.unipath.model.Course;
import com.unipath.model.Professor;
import com.unipath.repository.CourseRepository;
import com.unipath.ui.UC5.NewCourseFormScreen;
import com.unipath.ui.UC5.AvailableProfessorsScreen;
import com.unipath.ui.UC5.SuccessScreen;
import com.unipath.ui.UC5.ErrorScreen;

import java.util.List;

public class ManageCourseAddition {

    private CourseRepository dbManager;
    private com.unipath.repository.NotificationRepository notificationRepository;

    public ManageCourseAddition() {
        this.dbManager = new CourseRepository();
        this.notificationRepository = new com.unipath.repository.NotificationRepository(); // <--- ΝΕΟ

        NewCourseFormScreen newCourseFormScreen = new NewCourseFormScreen(this);
        newCourseFormScreen.display();
    }

    // Βήμα 3 & 4: submitCourseDetails()
    public void submitCourseDetails(String courseId, String title, String description, int ects, int semester) {

        // Κλήση queryCheckDuplicates() στη βάση
        boolean isDuplicateOrEmpty = dbManager.queryCheckDuplicates(courseId) || courseId.trim().isEmpty() || title.trim().isEmpty();

        // alt1: [duplicate entry / empty]
        if (isDuplicateOrEmpty) {
            ErrorScreen errorScreen = new ErrorScreen();
            errorScreen.display(); // Εμφάνιση οθόνης σφάλματος
            return;
        }

        // [not duplicate not empty] -> Βήμα 5: ΔΙΟΡΘΩΘΗΚΕ (Απλή ανάθεση σε τοπική μεταβλητή)
        List<Professor> professorList = dbManager.queryManageProfessors();

        // Βήμα 5 & 6: create & display AvailableProfessorsScreen (ΔΙΟΡΘΩΘΗΚΕ - Κρατάμε μόνο τη displayProfessorList)
        AvailableProfessorsScreen availableProfessorsScreen = new AvailableProfessorsScreen(this, professorList, courseId, title, description, ects, semester);
        availableProfessorsScreen.displayProfessorList();
    }

    // Βήμα 6 & 7: selectProfessor()
    public void selectProfessor(Professor selectedProfessor, String courseId, String title, String description, int ects, int semester) {

        // Εσωτερική κλήση: validateProfessor()
        boolean isMaxLoad = validateProfessor(selectedProfessor);

        // alt2: [max teaching load]
        if (isMaxLoad) {
            ErrorScreen errorScreen = new ErrorScreen();
            errorScreen.display(); // Εμφάνιση οθόνης σφάλματος υπερφόρτωσης
            return;
        }

        // alt2: [not max load]  Δημιουργία αντικειμένου Course ("create")
        Course course = new Course();
        course.setCourseID(courseId);
        course.setTitle(title);
        course.setDescription(description);
        course.setEcts(ects);
        course.setSemester(semester);

        // Βήμα 8: saveCourse() στη βάση
        boolean saved = dbManager.saveCourse(course, selectedProfessor.getProfessorId());

        if (saved) {
            // Βήμα 9: create & display SuccessScreen
            SuccessScreen successScreen = new SuccessScreen();
            successScreen.display();

            // Βήμα 10: notifyAllStudents()
            notifyAllStudents(title);
        }
    }

    // Εσωτερική μέθοδος του Controller
    private boolean validateProfessor(Professor professor) {
        return professor.getCurrentTeachingLoad() >= 3;
    }

    // Βήμα 10: notifyAllStudents()
    private void notifyAllStudents(String courseTitle) {
        // Κλήση του νέου απομονωμένου repository
        notificationRepository.notifyAllStudentsAboutCourse("Νέο Μάθημα: " + courseTitle);
    }
}