package com.unipath.ui.UC9;

import com.unipath.controller.ManageProfCourseEdit;
import com.unipath.model.Course;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.IOException;

public class RulesScreen {

    private ManageProfCourseEdit manageProfCourseEdit;
    private Course course;

    @FXML private TextArea rulesTextArea;

    public RulesScreen() {
    }

    public RulesScreen(ManageProfCourseEdit manageProfCourseEdit, Course course) {
        this.manageProfCourseEdit = manageProfCourseEdit;
        this.course = course;
    }

    public void display() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Professor/rules-screen-view.fxml"));
            Parent root = loader.load();

            RulesScreen controller = loader.getController();
            if (controller != null) {
                controller.manageProfCourseEdit = this.manageProfCourseEdit;
                controller.course = this.course;

                if (controller.rulesTextArea != null && this.course != null && this.course.getRules() != null) {
                    controller.rulesTextArea.setText(this.course.getRules());
                }

                // Σύνδεση του Listener για το βέλος editsRules() όταν αλλάζει το κείμενο
                if (controller.rulesTextArea != null) {
                    controller.rulesTextArea.textProperty().addListener((obs, oldVal, newVal) -> controller.editsRules(newVal));
                }

                // Σύνδεση του saveButton "στο χέρι" για να καλεί απευθείας την clickSaveRules χωρίς ενδιάμεσες μεθόδους
                Button saveBtn = (Button) root.lookup("#saveButtonId");
                if (saveBtn != null && controller.rulesTextArea != null) {
                    saveBtn.setOnAction(e -> controller.clickSaveRules(controller.rulesTextArea.getText()));
                }
            }

            Stage stage = (Stage) javafx.stage.Window.getWindows().stream()
                    .filter(javafx.stage.Window::isShowing)
                    .findFirst()
                    .orElse(null);

            if (stage != null) {
                stage.getScene().setRoot(root);
                System.out.println("[UI] Εμφάνιση οθόνης κανόνων.");
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα φόρτωσης FXML: " + e.getMessage());
        }
    }

    // Από το βέλος editsRules() του χρήστη στην οθόνη
    public void editsRules(String text) {
        // Προσωρινή αποθήκευση αλλαγών
    }

    // Από το βέλος clickSaveRules() του χρήστη
    @FXML
    public void clickSaveRules(String newRulesText) {
        if (manageProfCourseEdit != null) {
            manageProfCourseEdit.onSaveRules(newRulesText);
        }
    }

    // Από το βέλος clickCancel() του χρήστη
    @FXML

    public void clickCancel() {
        if (manageProfCourseEdit != null) {
            // ΔΙΟΡΘΩΣΗ: Της περνάμε το μήνυμα ακύρωσης για να ξέρει ο controller τι να δείξει στο Error Window
            manageProfCourseEdit.onCancelRules("Οι αλλαγές δεν αποθηκεύτηκαν.");
        }
    }
    // Από το εσωτερικό βέλος deleteChanges() που στέλνει ο Controller στην οθόνη
    public void deleteChanges() {
        if (rulesTextArea != null) {
            rulesTextArea.clear();
        }
        System.out.println("Οι αλλαγές απορρίφθηκαν.");
    }
}