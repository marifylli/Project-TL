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


    public void submitCourseDetails(String courseId, String title, String description, int ects, int semester) {

        // Κλήση queryCheckDuplicates() στη βάση
        boolean isDuplicateOrEmpty = dbManager.queryCheckDuplicates(courseId) || courseId.trim().isEmpty() || title.trim().isEmpty();


        if (isDuplicateOrEmpty) {
            ErrorScreen errorScreen = new ErrorScreen();
            errorScreen.display(ErrorScreen.ErrorType.DUPLICATE_OR_EMPTY); // Ροή 1
            errorScreen.display(ErrorScreen.ErrorType.MAX_TEACHING_LOAD);  // Ροή 2
            return;
        }

        // [not duplicate not empty]
        List<Professor> professorList = dbManager.queryManageProfessors();


        AvailableProfessorsScreen availableProfessorsScreen = new AvailableProfessorsScreen(this, professorList, courseId, title, description, ects, semester);
        availableProfessorsScreen.displayProfessorList();
    }


    public void selectProfessor(Professor selectedProfessor, String courseId, String title, String description, int ects, int semester) {


        boolean isMaxLoad = validateProfessor(selectedProfessor);


        if (isMaxLoad) {
            ErrorScreen errorScreen = new ErrorScreen();
            errorScreen.display(); // Εμφάνιση οθόνης σφάλματος υπερφόρτωσης
            return;
        }


        Course course = new Course();
        course.setCourseID(courseId);
        course.setTitle(title);
        course.setDescription(description);
        course.setECTS(ects);
        course.setSemester(semester);


        boolean saved = dbManager.saveCourse(course, selectedProfessor.getProfessorId());

        if (saved) {

            SuccessScreen successScreen = new SuccessScreen();
            successScreen.display();


            notifyAllStudents(title);
        }
    }

    // Εσωτερική μέθοδος του Controller
    private boolean validateProfessor(Professor professor) {
        return professor.getCurrentTeachingLoad() >= 3;
    }


    private void notifyAllStudents(String courseTitle) {

        notificationRepository.notifyAllStudentsAboutCourse("Νέο Μάθημα: " + courseTitle);
    }
}