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
import java.net.URL;

import java.util.List;

public class CoursesToEvaluateScreen {

    @FXML private ListView<String> coursesListView;
    @FXML private Label errorLabel;

    // Σύνδεση με τον controller (όπως ορίζει το sd2)
    private final ManageEvaluation controller = new ManageEvaluation();
    private final ObservableList<String> courseList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Πρώτα φορτώνουμε τα δεδομένα στην ObservableList
        loadAttendedCourses();

        // 2. Μετά συνδέουμε τη λίστα με το ListView για να σιγουρέψουμε ότι θα τα σχεδιάσει
        if (coursesListView != null) {
            coursesListView.setItems(courseList);
            System.out.println("✓ [UI] Η ObservableList συνδέθηκε επιτυχώς με το coursesListView. Μέγεθος: " + courseList.size());
        } else {
            System.err.println("[UI Error] Το coursesListView είναι NULL! Ελέγξτε το fx:id στο FXML αρχείο.");
        }
    }

    private void loadAttendedCourses() {
        courseList.clear();

        // Αντί για το δυναμικό ID του session, βάζουμε καρφωτά το 41
        // για να τραβήξει τις εγγραφές που βάλαμε στο InsertTestData
        int studentId = 41;
        System.out.println("🔍 [UC2] Ανάκτηση μαθημάτων από τη βάση για τον test φοιτητή: " + studentId);

        List<com.unipath.model.Course> attendedCourses = controller.queryCoursesAttended(studentId);

        if (attendedCourses != null && !attendedCourses.isEmpty()) {
            for (com.unipath.model.Course c : attendedCourses) {
                courseList.add(c.getTitle() + " (" + c.getCourseID() + ")");
            }
            System.out.println("✓ [UI] Η λίστα γέμισε με " + courseList.size() + " πραγματικά μαθήματα από την SQLite.");
        } else {
            errorLabel.setText("Δεν βρέθηκαν εγγεγραμμένα μαθήματα για αυτόν τον φοιτητή στη βάση.");
            System.out.println("⚠️ [UI Warning] Το query επέστρεψε κενή λίστα από τη βάση.");
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
            URL fxmlUrl = getClass().getResource("/fxml/Student/evaluation-form-view.fxml");
            // Backup path για το Maven target environment
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/Student/courses-evaluate-view.fxml");
            }

            if (fxmlUrl == null) {
                System.err.println("[FXML Error] Δεν βρέθηκε το αρχείο courses-evaluate-view.fxml στον φάκελο fxml/Student/");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // Ανάκτηση του controller της φόρμας
            EvaluationFormScreen formScreen = loader.getController();

            Stage currentStage = (Stage) coursesListView.getScene().getWindow();
            formScreen.setEvaluationContext(courseId, currentStage);

            Stage stage = new Stage();
            stage.setTitle("Φόρμα Αξιολόγησης Μαθήματος");
            stage.setScene(new Scene(root));
            stage.show();

            System.out.println("✓ [UI Success] Η φόρμα αξιολόγησης άνοιξε επιτυχώς!");
        } catch (Exception e) {
            System.err.println("Σφάλμα κατά το άνοιγμα της φόρμας: " + e.getMessage());
            e.printStackTrace();
        }
    }


}

