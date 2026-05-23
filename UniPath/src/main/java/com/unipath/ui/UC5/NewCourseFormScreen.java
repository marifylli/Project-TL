package com.unipath.ui.UC5;

import com.unipath.controller.ManageCourseAddition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;


public class NewCourseFormScreen {

    @FXML private TextField courseIdField;
    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private TextField ectsField;
    @FXML private TextField semesterField;
    @FXML private Button btnSubmit;
    @FXML private Label errorLabel;

    private ManageCourseAddition controller;

    public NewCourseFormScreen() {}

    public NewCourseFormScreen(ManageCourseAddition controller) {
        this.controller = controller;
    }

    public void setController(ManageCourseAddition controller) {
        this.controller = controller;
    }

    public void display() {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/UC5/new-course-form-view.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/UC5/new-course-form-view.fxml");
            }
            if (fxmlUrl == null) {
                System.err.println("Σφάλμα: Δεν βρέθηκε το new-course-form-view.fxml!");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            loader.setController(this);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Προσθήκη Νέου Μαθήματος");
            stage.show();

        } catch (Exception e) {
            System.err.println("Απέτυχε η φόρτωση της φόρμας νέου μαθήματος:");
            e.printStackTrace();
        }
    }

    @FXML
    private void onSubmit() {
        String courseId = courseIdField.getText().trim();
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        String ectsText = ectsField.getText().trim();
        String semesterText = semesterField.getText().trim();

        // Βασικός έλεγχος κενών πεδίων στο UI
        if (courseId.isEmpty() || title.isEmpty() || ectsText.isEmpty() || semesterText.isEmpty()) {
            errorLabel.setText("Παρακαλώ συμπληρώστε όλα τα υποχρεωτικά πεδία.");
            errorLabel.setVisible(true);
            return;
        }

        int ects, semester;
        try {
            ects = Integer.parseInt(ectsText);
            semester = Integer.parseInt(semesterText);
        } catch (NumberFormatException e) {
            errorLabel.setText("Τα ECTS και το Εξάμηνο πρέπει να είναι αριθμοί.");
            errorLabel.setVisible(true);
            return;
        }

        errorLabel.setVisible(false);

        // Κλήση βήματος 3 & 4 του controller
        controller.submitCourseDetails(courseId, title, description, ects, semester);
    }
}
