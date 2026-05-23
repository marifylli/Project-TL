package com.unipath.ui.UC6;

import com.unipath.controller.ManageSecCourEditClass;
import com.unipath.model.Course;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CourseEditScreen {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private TextField ectsField;
    @FXML private TextField semesterField;
    @FXML private TextArea rulesField;
    @FXML private TextArea prerequisitesField;
    @FXML private Label errorLabel;

    private ManageSecCourEditClass controller;
    private Course course;

    public CourseEditScreen() {}

    public CourseEditScreen(ManageSecCourEditClass controller, Course course) {
        this.controller = controller;
        this.course = course;
    }

    public void setContext(ManageSecCourEditClass controller, Course course) {
        this.controller = controller;
        this.course = course;
        populateFields();
    }

    private void populateFields() {
        if (course == null) return;
        titleField.setText(course.getTitle() != null ? course.getTitle() : "");
        descriptionField.setText(course.getDescription() != null ? course.getDescription() : "");
        ectsField.setText(String.valueOf(course.getECTS()));
        semesterField.setText(String.valueOf(course.getSemester()));
        rulesField.setText(course.getRules() != null ? course.getRules() : "");
        prerequisitesField.setText(course.getPrerequisites() != null ? course.getPrerequisites() : "");
    }

    @FXML
    private void onSave() {
        errorLabel.setText("");
        String title = titleField.getText().trim();
        String ectsText = ectsField.getText().trim();
        String semesterText = semesterField.getText().trim();

        if (title.isEmpty()) {
            errorLabel.setText("⚠ Ο τίτλος είναι υποχρεωτικός.");
            return;
        }
        int ects, semester;
        try {
            ects = Integer.parseInt(ectsText);
            if (ects <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            errorLabel.setText("⚠ Τα ECTS πρέπει να είναι θετικός αριθμός.");
            return;
        }
        try {
            semester = Integer.parseInt(semesterText);
            if (semester <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            errorLabel.setText("⚠ Το εξάμηνο πρέπει να είναι θετικός αριθμός.");
            return;
        }

        course.setTitle(title);
        course.setDescription(descriptionField.getText().trim());
        course.setECTS(ects);
        course.setSemester(semester);
        course.setRules(rulesField.getText().trim());
        course.setPrerequisites(prerequisitesField.getText().trim());

        controller.saveChanges(course);
    }

    @FXML
    private void onBack() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }

    public void display() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Secretary/course-edit-view.fxml"));
            loader.setControllerFactory(c -> this);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Επεξεργασία Στοιχείων");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

