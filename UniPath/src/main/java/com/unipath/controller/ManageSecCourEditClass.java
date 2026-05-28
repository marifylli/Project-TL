package com.unipath.controller;

import com.unipath.model.Course;
import com.unipath.model.StudyPlan;
import com.unipath.repository.CourseRepository;
import com.unipath.repository.NotificationRepository;
import com.unipath.ui.UC6.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class ManageSecCourEditClass {

    private CourseRepository courseRepository;
    private NotificationRepository notificationRepository;

    public ManageSecCourEditClass() {
        this.courseRepository = new CourseRepository();
        this.notificationRepository = new NotificationRepository();

        List<Course> courseList = courseRepository.queryCourseList();

        if (courseList == null || courseList.isEmpty()) {
            // Αντικατάσταση του τοπικού ErrorScreen
            showCommonError("Παρουσιάστηκε σφάλμα. Παρακαλώ ελέγξτε τα στοιχεία σας.");
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
            // Αντικατάσταση του τοπικού ErrorScreen
            showCommonError("Παρουσιάστηκε σφάλμα. Παρακαλώ ελέγξτε τα στοιχεία σας.");

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
        // Αντικατάσταση του τοπικού SuccessScreen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/success-window-view.fxml"));
            Parent root = loader.load();

            com.unipath.ui.common.SuccessScreen controller = loader.getController();
            controller.setSuccessMessage("Οι αλλαγές αποθηκεύτηκαν επιτυχώς και οι φοιτητές ειδοποιήθηκαν.");

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Επιτυχία");
            stage.showAndWait();
        } catch (Exception e) {
            System.err.println("Απέτυχε η φόρτωση της κοινής οθόνης επιτυχίας στο UC6:");
            e.printStackTrace();
        }
    }

    private boolean checkForChanges(Course course) {
        return course.getTitle() == null || course.getTitle().trim().isEmpty() || course.getECTS() <= 0;
    }

    /**
     * Βοηθητική μέθοδος εντός του controller για την αποφυγή διπλότυπου κώδικα FXML φόρτωσης σφάλματος
     */
    private void showCommonError(String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/error-window-view.fxml"));
            Parent root = loader.load();

            com.unipath.ui.common.ErrorScreen controller = loader.getController();
            controller.setErrorMessage(message);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Σφάλμα");
            stage.showAndWait();
        } catch (Exception e) {
            System.err.println("Απέτυχε η φόρτωση της κοινής οθόνης σφάλματος στο UC6:");
            e.printStackTrace();
        }
    }
}