package com.unipath.ui.UC5;

import com.unipath.controller.ManageCourseAddition;
import com.unipath.model.Professor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;

public class AvailableProfessorsScreen {

    @FXML private ListView<String> professorsListView;
    @FXML private Button btnSelect;
    @FXML private Label courseInfoLabel;
    @FXML private Label errorLabel;

    private ManageCourseAddition controller;
    private List<Professor> professors;
    private String courseId, title, description;
    private int ects, semester;

    public AvailableProfessorsScreen() {}

    public AvailableProfessorsScreen(ManageCourseAddition controller, List<Professor> professors,
                                     String courseId, String title, String description, int ects, int semester) {
        this.controller = controller;
        this.professors = professors;
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.ects = ects;
        this.semester = semester;
    }

    public void displayProfessorList() {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/Secretary/available-professors-view.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/Secretary/available-professors-view.fxml");
            }
            if (fxmlUrl == null) {
                System.err.println("❌ Σφάλμα: Δεν βρέθηκε το available-professors-view.fxml!");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            loader.setController(this);
            Parent root = loader.load();

            // Βρίσκουμε το ανοιχτό stage από τυχόν υπάρχον παράθυρο
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Επιλογή Διδάσκοντα");
            stage.show();

        } catch (Exception e) {
            System.err.println("❌ Απέτυχε η φόρτωση της οθόνης καθηγητών:");
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Αρχικοποίηση λίστας καθηγητών μετά τη φόρτωση του FXML
        if (professors != null) {
            for (Professor p : professors) {
                String display = p.getFullName()
                        + "  |  Φόρτος: " + p.getCurrentTeachingLoad() + "/3";
                professorsListView.getItems().add(display);
            }
        }

        if (courseInfoLabel != null && title != null) {
            courseInfoLabel.setText("Μάθημα: " + title + " (" + courseId + ")  |  ECTS: " + ects + "  |  Εξάμηνο: " + semester);
        }
    }

    @FXML
    private void onSelectProfessor() {
        int selectedIndex = professorsListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex < 0) {
            errorLabel.setText("⚠ Παρακαλώ επιλέξτε έναν διδάσκοντα.");
            errorLabel.setVisible(true);
            return;
        }

        errorLabel.setVisible(false);
        Professor selectedProfessor = professors.get(selectedIndex);

        // Κλήση βήματος 6 & 7 του controller
        controller.selectProfessor(selectedProfessor, courseId, title, description, ects, semester);
    }
}