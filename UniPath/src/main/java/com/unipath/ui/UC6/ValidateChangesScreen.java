package com.unipath.ui.UC6;

import com.unipath.controller.ManageSecCourEditClass;
import com.unipath.model.Course;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ValidateChangesScreen {

    @FXML private Label titleLabel;
    @FXML private Label ectsLabel;
    @FXML private Label semesterLabel;
    @FXML private Label descriptionLabel;

    private ManageSecCourEditClass controller;
    private Course course;

    public ValidateChangesScreen() {}

    public ValidateChangesScreen(ManageSecCourEditClass controller, Course course) {
        this.controller = controller;
        this.course = course;
    }

    // --
    @FXML
    public void initialize() {
        populateLabels();
    }

    public void setContext(ManageSecCourEditClass controller, Course course) {
        this.controller = controller;
        this.course = course;
        populateLabels();
    }

    private void populateLabels() {
        // Έλεγχος ότι και το μάθημα υπάρχει, αλλά και ότι τα Labels έχουν προλάβει να φορτώσουν
        if (course == null || titleLabel == null) return;

        titleLabel.setText(course.getTitle());
        ectsLabel.setText(String.valueOf(course.getECTS()));
        semesterLabel.setText(String.valueOf(course.getSemester()));
        descriptionLabel.setText(course.getDescription() != null && !course.getDescription().trim().isEmpty()
                ? course.getDescription()
                : "-");
    }

    @FXML
    private void onConfirm() {
        controller.confirmChanges(course);
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancel() {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }

    public void display() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Secretary/validate-changes-view.fxml"));
            loader.setControllerFactory(c -> this);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 700, 500));
            stage.setTitle("UniPath - Επιβεβαίωση Αλλαγών");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}