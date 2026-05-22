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

    // Τα νέα fx:id που βάλαμε στο FXML
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private Label metaLabel;

    public CourseDetailScreen() {
    }

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

                // Γεμίζουμε τα επεξεργάσιμα πεδία με τις τρέχουσες τιμές
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
                System.out.println("[UI] Εμφάνιση λεπτομερειών μαθήματος: " + course.getTitle());
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα φόρτωσης FXML: " + e.getMessage());
        }
    }

    // ΒΗΜΑ 5: Ο καθηγητής τροποποιεί τα στοιχεία στην οθόνη
    public void editCourseFields() {
        if (titleField != null && descriptionArea != null && course != null) {
            course.setTitle(titleField.getText());
            course.setDescription(descriptionArea.getText());
        }
        if (manageProfCourseEdit != null) {
            manageProfCourseEdit.onEditCourseFields();
        }
    }

    @FXML
    public void clicksRules() {
        boolean isTitleEmpty = titleField != null && titleField.getText().trim().isEmpty();
        boolean isDescriptionEmpty = descriptionArea != null && descriptionArea.getText().trim().isEmpty();

        // Έλεγχος αν κάποιο από τα υποχρεωτικά πεδία είναι κενό
        if (isTitleEmpty || isDescriptionEmpty) {
            String warningMessage = "Προσοχή! ";

            if (isTitleEmpty && isDescriptionEmpty) {
                warningMessage += "Τα πεδία 'Τίτλος' και 'Περιγραφή' είναι κενά.";
            } else if (isTitleEmpty) {
                warningMessage += "Το πεδίο 'Τίτλος Μαθήματος' είναι κενό.";
            } else {
                warningMessage += "Το πεδίο 'Περιγραφή' είναι κενό.";
            }

            System.out.println("[Εναλλακτική Ροή 2] " + warningMessage);

            if (manageProfCourseEdit != null) {
                // Καλούμε τον Controller περνώντας του το ακριβές μήνυμα σφάλματος
                manageProfCourseEdit.onCancelRules(warningMessage);
            }
            return; // Διακοπή της ροής
        }

        // Αν όλα είναι συμπληρωμένα, συνεχίζουμε στη Βασική Ροή
        editCourseFields();
        if (manageProfCourseEdit != null) {
            manageProfCourseEdit.onRulesClicked();
        }
    }
}