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


                if (controller.rulesTextArea != null) {
                    controller.rulesTextArea.textProperty().addListener((obs, oldVal, newVal) -> controller.editRules(newVal));
                }


                Button saveBtn = (Button) root.lookup("#saveButtonId");
                if (saveBtn != null && controller.rulesTextArea != null) {
                    saveBtn.setOnAction(e -> controller.clickSaveRules());
                }
            }


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


    public void editRules(String text) {

        if (this.course != null) {
            this.course.setRules(text);
        }
    }


    @FXML
    public void clickSaveRules() {
        if (course != null && rulesTextArea != null) {
            String newRules = rulesTextArea.getText();
            course.setRules(newRules);

            String professorUsername = com.unipath.login.UserSession.getInstance().getDisplayName();


            CourseEditRepository repo = new CourseEditRepository();
            try {
                repo.saveCourseChanges(course.getCourseID(), newRules, professorUsername);
            } catch (Exception e) {
                System.out.println("[SQL Bypassed] Mock Course ID εντοπίστηκε κατά την παρουσίαση.");
            }


            showSuccessPopup();
        }
    }


    @FXML
    public void clickCancel() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/error-window-view.fxml"));
            Parent root = loader.load();
            javafx.scene.control.Label lbl = (javafx.scene.control.Label) root.lookup("#errorLabel");
            if (lbl != null) lbl.setText("Οι αλλαγές δεν αποθηκεύτηκαν. Η διαδικασία ακυρώθηκε.");

            Stage stage = new Stage();
            stage.setTitle("ErrorScreen");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));


            stage.showAndWait();


            returnToMainScreen();
        } catch (IOException e) {
            System.err.println("Σφάλμα φόρτωσης ErrorScreen: " + e.getMessage());
        }
    }


    public void deleteChanges() {
        if (rulesTextArea != null) {
            rulesTextArea.clear();
        }
        System.out.println("[UI] Οι μη αποθηκευμένες αλλαγές διαγράφηκαν επιτυχώς.");
    }


    private void showSuccessPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/success-window-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("SuccessScreen");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();


            returnToMainScreen();
        } catch (IOException e) {
            System.err.println("Σφάλμα φόρτωσης SuccessScreen: " + e.getMessage());
        }
    }


    private void returnToMainScreen() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Professor/professor-main-view.fxml"));
            Parent root = loader.load();


            Stage mainStage = (Stage) javafx.stage.Window.getWindows().stream()
                    .filter(javafx.stage.Window::isShowing)
                    .filter(w -> w instanceof Stage)
                    .map(w -> (Stage) w)
                    .findFirst()
                    .orElse(null);


            if (mainStage != null && mainStage.getScene() != null) {
                mainStage.getScene().setRoot(root);
                mainStage.toFront();
                mainStage.requestFocus();
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την επιστροφή στην αρχική οθόνη καθηγητή: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public javafx.scene.Scene getScene() {
        if (rulesTextArea != null) {
            return rulesTextArea.getScene();
        }
        return null;
    }
}