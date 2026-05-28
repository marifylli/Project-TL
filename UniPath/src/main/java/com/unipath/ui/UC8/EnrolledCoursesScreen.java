package com.unipath.ui.UC8;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.unipath.controller.ManageGetHelp;
import com.unipath.model.Course;
import com.unipath.model.StudyPlan;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class EnrolledCoursesScreen {

    @FXML private ListView<String> coursesListView;
    @FXML private Button nextButton;

    private ManageGetHelp controller;
    private final Map<String, String> courseTitleToIdMap = new HashMap<>();

    @FXML
    public void initialize() {
        controller = new ManageGetHelp();
        System.out.println("[UI-UC8] Αρχικοποίηση EnrolledCoursesScreen και φόρτωση μαθημάτων.");


        com.unipath.controller.ManageMentorProfile mentorController = new com.unipath.controller.ManageMentorProfile();
        List<Course> availableCourses = mentorController.queryCourses();

        if (availableCourses != null && !availableCourses.isEmpty()) {
            for (Course course : availableCourses) {
                String courseTitle = course.getTitle();
                String courseId = course.getCourseID();

                coursesListView.getItems().add(courseTitle);
                courseTitleToIdMap.put(courseTitle, courseId);
            }
        }


        if (coursesListView.getItems().isEmpty()) {
            coursesListView.setPlaceholder(new Label("Δεν βρέθηκαν διαθέσιμα μαθήματα."));
        }
    }

    @FXML
    private void handleSearchOffers() {
        String selectedTitle = coursesListView.getSelectionModel().getSelectedItem();

        if (selectedTitle == null) {
            System.out.println("[UI-UC8] Σφάλμα: Δεν επιλέχθηκε μάθημα.");
            return;
        }

        String selectedCourseId = courseTitleToIdMap.get(selectedTitle);
        System.out.println("[UI-UC8] Επιλέχθηκε το μάθημα: " + selectedTitle + " με ID: " + selectedCourseId);

        try {
            java.net.URL fxmlUrl = getClass().getResource("/fxml/Student/available-offers-view.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/Student/available-offers-view.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            AvailableOffersScreen nextScreen = loader.getController();
            nextScreen.loadOffersForCourse(selectedCourseId, selectedTitle);

            Stage stage = (Stage) nextButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Διαθέσιμες Προσφορές");
        } catch (Exception e) {
            System.err.println(" Σφάλμα κατά τη μετάβαση στο AvailableOffersScreen:");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        try {
            java.net.URL fxmlUrl = getClass().getResource("/fxml/Student/student-main-screen.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/Student/student-main-screen.fxml");
            }
            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage) coursesListView.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Κεντρικό Μενού");
        } catch (Exception e) {
            System.err.println(" Σφάλμα κατά την επιστροφή: " + e.getMessage());
        }
    }
}