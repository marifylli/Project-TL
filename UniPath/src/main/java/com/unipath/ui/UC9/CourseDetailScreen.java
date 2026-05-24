package com.unipath.ui.UC9;

import com.unipath.controller.ManageProfCourseEdit;
import com.unipath.model.Course;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.IOException;

public class CourseDetailScreen {

    private ManageProfCourseEdit manageProfCourseEdit;
    private Course course;

    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private Label metaLabel;

    public CourseDetailScreen() {}

    public CourseDetailScreen(ManageProfCourseEdit manageProfCourseEdit, Course course) {
        this.manageProfCourseEdit = manageProfCourseEdit;
        this.course = course;
    }

    public void displayCourseDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Professor/course-detail-view.fxml"));
            Parent root = loader.load();

            CourseDetailScreen controller = loader.getController();
            if (controller != null) {
                controller.manageProfCourseEdit = this.manageProfCourseEdit;
                controller.course = this.course;

                if (controller.titleField != null && this.course != null) controller.titleField.setText(this.course.getTitle());
                if (controller.descriptionArea != null && this.course != null) controller.descriptionArea.setText(this.course.getDescription());
                if (controller.metaLabel != null && this.course != null) controller.metaLabel.setText(this.course.getECTS() + " ECTS · Εξάμηνο " + this.course.getSemester());
            }

            Stage stage = (Stage) javafx.stage.Window.getWindows().stream()
                    .filter(javafx.stage.Window::isShowing)
                    .findFirst()
                    .orElse(null);

            if (stage != null) {
                stage.getScene().setRoot(root);
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα φόρτωσης FXML: " + e.getMessage());
        }
    }

    public void editCourseFields() {
        if (titleField != null && descriptionArea != null && course != null) {
            course.setTitle(titleField.getText());
            course.setDescription(descriptionArea.getText());
        }
    }


    @FXML
    public void clicksRules() {
        if (manageProfCourseEdit != null) {
            String currentTitle = (titleField != null) ? titleField.getText() : "";
            String currentDesc = (descriptionArea != null) ? descriptionArea.getText() : "";

            // 1. Έλεγχος εγκυρότητας στον Controller
            boolean fieldsValid = manageProfCourseEdit.checkFields(currentTitle, currentDesc);

            if (fieldsValid) {
                editCourseFields(); // Ενημερώνουμε το αντικείμενο Course τοπικά

                // Ανοίγουμε τη RulesScreen περνώντας τον controller και το μάθημα
                RulesScreen rulesScreen = new RulesScreen(manageProfCourseEdit, course);
                rulesScreen.display();
            }
        }
    }
}