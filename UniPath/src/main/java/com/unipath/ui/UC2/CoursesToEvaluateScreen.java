package com.unipath.ui.UC2;

import com.unipath.ui.UC2.EvaluationFormScreen;
import com.unipath.controller.ManageEvaluation;
import com.unipath.login.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

public class CoursesToEvaluateScreen {

    @FXML private ListView<String> coursesListView;
    @FXML private Label errorLabel;

    // Σύνδεση με τον controller (όπως ορίζει το sd2)
    private final ManageEvaluation controller = new ManageEvaluation();
    private final ObservableList<String> courseList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        coursesListView.setItems(courseList);
        loadAttendedCourses();
    }

    private void loadAttendedCourses() {
        // Παίρνουμε το ID του συνδεδεμένου φοιτητή από το Session
        int studentId = UserSession.getInstance().getUserId();

        // Βήμα 2 (sd2): queryCoursesAttended
        List<String> attendedCourses = controller.queryCoursesAttended(studentId);
        if (attendedCourses != null) {
            courseList.addAll(attendedCourses);
        }
    }

    @FXML
    private void onCourseSelected() {
        errorLabel.setText("");
        String selectedCourse = coursesListView.getSelectionModel().getSelectedItem();

        if (selectedCourse == null) {
            errorLabel.setText("Παρακαλώ επιλέξτε ένα μάθημα από τη λίστα!");
            return;
        }

        // Βήμα 4 (sd2): checkEvaluationRight
        boolean hasRight = controller.checkEvaluationRight(selectedCourse);

        if (hasRight) {
            // Βήμα 5 (sd2): Άνοιγμα του EvaluationFormScreen
            openEvaluationForm(selectedCourse);
        } else {
            // Εναλλακτική Ροή 1 (sd2): Ήδη υποβληθεί
            errorLabel.setText("Έχετε ήδη υποβάλει αξιολόγηση για αυτό το μάθημα!");
        }
    }

    private void openEvaluationForm(String courseId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UC2/evaluation-form-view.fxml"));
            Parent root = loader.load();

            // Πεντακάθαρη ανάκτηση του controller χάρη στο import της κορυφής!
            EvaluationFormScreen formScreen = loader.getController();

            // Περνάμε το context στην επόμενη οθόνη χωρίς κανένα error πλέον
            Stage currentStage = (Stage) coursesListView.getScene().getWindow();
            formScreen.setEvaluationContext(courseId, currentStage);

            Stage stage = new Stage();
            stage.setTitle("Φόρμα Αξιολόγησης Μαθήματος");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.err.println("Σφάλμα κατά το άνοιγμα της φόρμας: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

