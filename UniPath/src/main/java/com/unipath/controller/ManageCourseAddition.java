package com.unipath.controller;

import com.unipath.model.Course;
import com.unipath.model.Professor;
import com.unipath.repository.CourseRepository;
import com.unipath.ui.UC5.NewCourseFormScreen;
import com.unipath.ui.UC5.AvailableProfessorsScreen;
import com.unipath.ui.common.SuccessScreen;
import com.unipath.ui.common.ErrorScreen; // Σωστό Import

import java.util.List;

public class ManageCourseAddition {

    private final CourseRepository dbManager;
    private final com.unipath.repository.NotificationRepository notificationRepository;

    public ManageCourseAddition() {
        this.dbManager = new CourseRepository();
        this.notificationRepository = new com.unipath.repository.NotificationRepository();

        NewCourseFormScreen newCourseFormScreen = new NewCourseFormScreen(this);
        newCourseFormScreen.display();
    }

    public void submitCourseDetails(String courseId, String title, String description, int ects, int semester) {

        // Έλεγχος για διπλότυπα ή κενά πεδία
        boolean isDuplicateOrEmpty = dbManager.queryCheckDuplicates(courseId)
                || courseId.trim().isEmpty()
                || title.trim().isEmpty();

        if (isDuplicateOrEmpty) {
            ErrorScreen.show(null, ErrorScreen.ErrorType.DUPLICATE_OR_EMPTY_COURSE);
            return;
        }

        // Αν όλα είναι εντάξει, τραβάμε τη λίστα καθηγητών
        List<Professor> professorList = dbManager.queryManageProfessors();

        AvailableProfessorsScreen availableProfessorsScreen = new AvailableProfessorsScreen(
                this, professorList, courseId, title, description, ects, semester
        );
        availableProfessorsScreen.displayProfessorList();
    }

    public void selectProfessor(Professor selectedProfessor, String courseId, String title, String description, int ects, int semester) {

        // Έλεγχος αν ο καθηγητής έχει ήδη 3 ή περισσότερα μαθήματα
        boolean isMaxLoad = validateProfessor(selectedProfessor);

        if (isMaxLoad) {
            // 🌟 Εδώ το MAX_TEACHING_LOAD είναι ήδη ολόσωστο
            ErrorScreen.show(null, ErrorScreen.ErrorType.MAX_TEACHING_LOAD);
            return;
        }

        // Δημιουργία του αντικειμένου Course
        Course course = new Course();
        course.setCourseID(courseId);
        course.setTitle(title);
        course.setDescription(description);
        course.setECTS(ects);
        course.setSemester(semester);

        // Αποθήκευση στη βάση δεδομένων
        boolean saved = dbManager.saveCourse(course, selectedProfessor.getProfessorId());

        if (saved) {
            // Χρησιμοποιούμε full package path για να μην υπάρχει καμία αμφιβολία στα imports
            com.unipath.ui.common.SuccessScreen.show(null, com.unipath.ui.common.SuccessScreen.SuccessType.COURSE_ADDED_SUCCESSFULLY);

            // Ειδοποίηση φοιτητών
            notifyAllStudents(title);
        }
    }

    // Εσωτερική μέθοδος επικύρωσης του Controller
    private boolean validateProfessor(Professor professor) {
        return professor.getCurrentTeachingLoad() >= 3;
    }

    private void notifyAllStudents(String courseTitle) {
        notificationRepository.notifyAllStudentsAboutCourse("Νέο Μάθημα: " + courseTitle);
    }
}