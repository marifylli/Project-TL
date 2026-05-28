package com.unipath.ui.UC2;

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
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;
import java.util.List;

public class CoursesToEvaluateScreen {

    @FXML private ListView<String> coursesListView;
    @FXML private Label errorLabel;

    private final ManageEvaluation controller = new ManageEvaluation();
    private final ObservableList<String> courseList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadAttendedCourses();

        if (coursesListView != null) {
            coursesListView.setItems(courseList);
        } else {
            System.err.println("[UI Error] Το coursesListView είναι NULL!");
        }
    }

    private void loadAttendedCourses() {
        courseList.clear();

        int studentId = UserSession.getInstance().getUserId();
        System.out.println("🔍 [UC2] Ανάκτηση μαθημάτων για studentId: " + studentId);

        List<com.unipath.model.Course> attendedCourses = controller.queryCoursesAttended(studentId);

        if (attendedCourses != null && !attendedCourses.isEmpty()) {
            for (com.unipath.model.Course c : attendedCourses) {
                courseList.add(c.getTitle() + " (" + c.getCourseID() + ")");
            }
            System.out.println("✓ [UI] Λίστα με " + courseList.size() + " μαθήματα.");
        } else {
            errorLabel.setText("Δεν βρέθηκαν μαθήματα για αξιολόγηση.");
            System.out.println("⚠️ [UI Warning] Κενή λίστα από τη βάση.");
        }
    }

    @FXML
    private void onCourseSelected() {
        errorLabel.setText("");
        String selectedItem = coursesListView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            errorLabel.setText("Παρακαλώ επιλέξτε ένα μάθημα από τη λίστα!");
            return;
        }


        String courseId = "UNKNOWN";
        if (selectedItem.contains("(") && selectedItem.contains(")")) {
            courseId = selectedItem.substring(selectedItem.indexOf("(") + 1, selectedItem.indexOf(")"));
        }


        boolean hasRight = controller.checkEvaluationRight(courseId);

        if (hasRight) {
            openEvaluationForm(courseId); // Περνάμε το καθαρό courseId
        } else {
            errorLabel.setText("Έχετε ήδη υποβάλει αξιολόγηση για αυτό το μάθημα!");
        }
    }

    private void openEvaluationForm(String courseId) {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/Student/evaluation-form-view.fxml");
            if (fxmlUrl == null)
                fxmlUrl = getClass().getClassLoader().getResource("fxml/Student/evaluation-form-view.fxml");

            if (fxmlUrl == null) {
                System.err.println("[FXML Error] Δεν βρέθηκε το evaluation-form-view.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            EvaluationFormScreen formScreen = loader.getController();
            Stage currentStage = (Stage) coursesListView.getScene().getWindow();
            formScreen.setEvaluationContext(courseId, currentStage);

            Stage popupStage = new Stage();
            popupStage.setTitle("Φόρμα Αξιολόγησης Μαθήματος");
            popupStage.setScene(new Scene(root));
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(currentStage);
            popupStage.showAndWait();

        } catch (Exception e) {
            System.err.println("Σφάλμα κατά το άνοιγμα της φόρμας: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

