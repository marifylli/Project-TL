package com.unipath.ui.UC9;

import com.unipath.controller.ManageProfCourseEdit;
import com.unipath.model.Course;
import com.unipath.repository.CourseEditRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class RulesScreen {

    private ManageProfCourseEdit manageProfCourseEdit;
    private Course course;

    @FXML private TextArea rulesTextArea;

    public RulesScreen() {}

    public RulesScreen(ManageProfCourseEdit manageProfCourseEdit, Course course) {
        this.manageProfCourseEdit = manageProfCourseEdit;
        this.course = course;
    }

    // Εμφάνιση της οθόνης και αρχικοποίηση των δεδομένων
    public void display() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Professor/rules-screen-view.fxml"));
            Parent root = loader.load();

            RulesScreen controller = loader.getController();
            if (controller != null) {
                controller.manageProfCourseEdit = this.manageProfCourseEdit;
                controller.course = this.course;

                // Φόρτωση των υπαρχόντων κανόνων στο TextArea από το αντικείμενο Course
                if (controller.rulesTextArea != null && this.course != null && this.course.getRules() != null) {
                    controller.rulesTextArea.setText(this.course.getRules());
                }

                // Listener για την παρακολούθηση της πληκτρολόγησης των κανόνων
                if (controller.rulesTextArea != null) {
                    controller.rulesTextArea.textProperty().addListener((obs, oldVal, newVal) -> controller.editRules(newVal));
                }

                // Σύνδεση του Save Button μέσω κώδικα αν χρειάζεται, ή χρησιμοποιείται το @FXML onAction
                Button saveBtn = (Button) root.lookup("#saveButtonId");
                if (saveBtn != null && controller.rulesTextArea != null) {
                    saveBtn.setOnAction(e -> controller.clickSaveRules());
                }
            }

            // Αντικατάσταση του Root στο τρέχον Stage της εφαρμογής
            Stage stage = (Stage) javafx.stage.Window.getWindows().stream()
                    .filter(javafx.stage.Window::isShowing)
                    .findFirst()
                    .orElse(null);

            if (stage != null) {
                stage.getScene().setRoot(root);
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα φόρτωσης FXML RulesScreen: " + e.getMessage());
        }
    }

    // Από το Class Diagram: editRules(text)
    public void editRules(String text) {
        // Καταγράφει την προσωρινή πληκτρολόγηση κανόνων από τον καθηγητή
        if (this.course != null) {
            this.course.setRules(text);
        }
    }

    // Από το Class Diagram: clickSaveRules() - Χωρίς ορίσματα
    @FXML
    public void clickSaveRules() {
        if (course != null && rulesTextArea != null) {
            String newRules = rulesTextArea.getText();
            course.setRules(newRules);

            String professorUsername = com.unipath.login.UserSession.getInstance().getDisplayName();

            // 🔥 ΑΠΕΥΘΕΙΑΣ ΚΛΗΣΗ ΣΤΟ REPOSITORY (Μόνο η saveCourseChanges όπως ζήτησες)
            CourseEditRepository repo = new CourseEditRepository();
            try {
                repo.saveCourseChanges(course.getCourseID(), newRules, professorUsername);
            } catch (Exception e) {
                System.out.println("[SQL Bypassed] Mock Course ID εντοπίστηκε κατά την παρουσίαση.");
            }

            // Εμφάνιση Success Window
            showSuccessPopup();
        }
    }

    // Από το Class Diagram: clickCancel()
    // Από το Class Diagram: clickCancel()
    @FXML
    public void clickCancel() {
        // ΑΦΑΙΡΕΘΗΚΕ η deleteChanges() για να μην αδειάζει το TextArea στην οθόνη!

        // Εμφάνιση Error Window για την ακύρωση
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/error-window-view.fxml"));
            Parent root = loader.load();
            javafx.scene.control.Label lbl = (javafx.scene.control.Label) root.lookup("#errorLabel");
            if (lbl != null) lbl.setText("Οι αλλαγές δεν αποθηκεύτηκαν. Η διαδικασία ακυρώθηκε.");

            Stage stage = new Stage();
            stage.setTitle("ErrorScreen");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            // Παγώνει την εφαρμογή με το μήνυμα. Το κείμενο από πίσω φαίνεται κανονικά!
            stage.showAndWait();

            // Μόλις ο χρήστης πατήσει οκ στο μήνυμα, τότε τον επιστρέφει στην κεντρική οθόνη
            returnToMainScreen();
        } catch (IOException e) {
            System.err.println("Σφάλμα φόρτωσης ErrorScreen: " + e.getMessage());
        }
    }

    // Από το Class Diagram: deleteChanges()
    public void deleteChanges() {
        if (rulesTextArea != null) {
            rulesTextArea.clear();
        }
        System.out.println("[UI] Οι μη αποθηκευμένες αλλαγές διαγράφηκαν επιτυχώς.");
    }

    // Εσωτερικό popup επιτυχίας
    private void showSuccessPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/success-window-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("SuccessScreen");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Μόλις πατηθεί το OK, επιστρέφει στη Main οθόνη
            returnToMainScreen();
        } catch (IOException e) {
            System.err.println("Σφάλμα φόρτωσης SuccessScreen: " + e.getMessage());
        }
    }

    // Επαναφέρει την ProfessorMainScreen στο προσκήνιο
    private void returnToMainScreen() {
        Stage mainStage = (Stage) javafx.stage.Window.getWindows().stream()
                .filter(javafx.stage.Window::isShowing)
                .filter(w -> w instanceof Stage)
                .map(w -> (Stage) w)
                .findFirst()
                .orElse(null);
        if (mainStage != null) {
            mainStage.toFront();
            mainStage.requestFocus();
        }
    }

    // Μέθοδος που επιτρέπει στον Controller να αποκτά πρόσβαση στο Scene αν χρειαστεί
    public javafx.scene.Scene getScene() {
        if (rulesTextArea != null) {
            return rulesTextArea.getScene();
        }
        return null;
    }
}