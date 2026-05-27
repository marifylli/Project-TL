package com.unipath.ui.UC1;

import com.unipath.controller.ManageStudyPlan;
import com.unipath.model.Scenario;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import java.util.List;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

public class ScenarioSelectionScreen {

    private ManageStudyPlan manageStudyPlan;

    @FXML private ListView<Scenario> scenarioListView;
    @FXML private Label errorLabel;

    public ScenarioSelectionScreen() {}

    public ScenarioSelectionScreen(ManageStudyPlan manageStudyPlan) {
        this.manageStudyPlan = manageStudyPlan;
    }

    @FXML
    public void initialize() {
        System.out.println("DEBUG UI: Η initialize() του FXML ολοκληρώθηκε.");
        if (manageStudyPlan != null) {
            displayScenarios();
        }
    }

    public void setManageStudyPlan(ManageStudyPlan manageStudyPlan) {
        this.manageStudyPlan = manageStudyPlan;
        System.out.println("DEBUG UI: Το manageStudyPlan συνδέθηκε με την οθόνη.");
        displayScenarios();
    }

    public void displayScenarios() {
        System.out.println("DEBUG UI: Έναρξη της displayScenarios()...");

        if (scenarioListView == null) {
            System.out.println(" ΣΦΑΛΜΑ: Το scenarioListView είναι NULL! Ελέγξτε το fx:id στο FXML.");
            return;
        }
        if (manageStudyPlan == null) {
            System.out.println("ΣΦΑΛΜΑ: Το manageStudyPlan είναι NULL.");
            return;
        }

        scenarioListView.getItems().clear();

        try {
            System.out.println("DEBUG UI: Κλήση της manageStudyPlan.queryGetScenarios()...");
            List<Scenario> scenarios = manageStudyPlan.queryGetScenarios();

            if (scenarios == null || scenarios.isEmpty()) {
                System.out.println("DEBUG UI: Η βάση επέστρεψε 0 σενάρια. Δημιουργία hardcoded σεναρίων βάσει Οδηγού Σπουδών...");
                scenarios = new java.util.ArrayList<>();

                Scenario s1 = new Scenario();
                s1.setScenarioId(1);
                s1.setTitle("Σενάριο 1: 1 Κύρια Κατεύθυνση Εμβάθυνσης");
                s1.setDescription("Επιλογή 17 μαθημάτων με εστίαση σε μία κύρια κατεύθυνση (Ομάδες Α & Β).");
                scenarios.add(s1);

                Scenario s2 = new Scenario();
                s2.setScenarioId(2);
                s2.setTitle("Σενάριο 2: Διπλή Κατεύθυνση Εμβάθυνσης");
                s2.setDescription("Επιλογή 17 μαθημάτων μοιρασμένων σε δύο κύριες κατευθύνσεις.");
                scenarios.add(s2);

                Scenario s3 = new Scenario();
                s3.setScenarioId(3);
                s3.setTitle("Σενάριο 3: Γενική Κατεύθυνση (Ελεύθερο Πλάνο)");
                s3.setDescription("Ελεύθερη επιλογή 17 μαθημάτων από τουλάχιστον 4 διαφορετικές κατευθύνσεις.");
                scenarios.add(s3);
            }

            int studentId = com.unipath.login.UserSession.getInstance().getUserId();
            List<Integer> completedScenarioIds = manageStudyPlan.queryGetFinalizedScenarioIds(studentId);
            System.out.println("DEBUG UI: Ο φοιτητής έχει ήδη ολοκληρώσει τις κατευθύνσεις με IDs: " + completedScenarioIds);

            scenarioListView.setCellFactory(lv -> new ListCell<Scenario>() {
                @Override
                protected void updateItem(Scenario item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setDisable(false);
                        setStyle("");
                    } else {
                        if (completedScenarioIds.contains(item.getScenarioId())) {
                            setText(item.getTitle() + " - (Έχει ήδη οριστικοποιηθεί πλάνο)");
                            setDisable(true);
                            setStyle("-fx-text-fill: #888888; -fx-font-style: italic;");
                        } else {
                            setText(item.getTitle());
                            setDisable(false);
                            setStyle("");
                        }
                    }
                }
            });

            scenarioListView.getItems().addAll(scenarios);

            boolean foundAvailable = false;
            for (Scenario s : scenarios) {
                if (!completedScenarioIds.contains(s.getScenarioId())) {
                    scenarioListView.getSelectionModel().select(s);
                    foundAvailable = true;
                    break;
                }
            }
            if (!foundAvailable) {
                scenarioListView.getSelectionModel().clearSelection();
            }

            System.out.println("DEBUG UI: Το ListView ενημερώθηκε επιτυχώς με τις κατευθύνσεις!");

        } catch (Exception e) {
            System.err.println("ΣΦΑΛΜΑ κατά τη φόρτωση στο UI:");
            e.printStackTrace();
        }
    }

    public void selectScenario(Scenario scenario) {
        manageStudyPlan.onScenarioSelected(scenario);
    }

    @FXML
    private void handleNextStep() {
        if (scenarioListView == null) return;

        Scenario selected = scenarioListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            if (errorLabel != null) {
                errorLabel.setText("Παρακαλώ επιλέξτε ένα διαθέσιμο σενάριο προτού συνεχίσετε.");
                errorLabel.setVisible(true);
            }
            return;
        }

        System.out.println("✓ [UI Action]: Επιλέχθηκε το " + selected.getTitle() + ". Προώθηση στον κεντρικό controller...");

        // 🌟 ΚΑΛΟΥΜΕ ΤΗ ΡΟΗ ΤΗΣ ΟΜΑΔΑΣ ΣΟΥ: Ο controller έχει το Stage,
        // θα αλλάξει την οθόνη και θα γεμίσει τα 74 μαθήματα αυτόματα!
        if (manageStudyPlan != null) {
            manageStudyPlan.onScenarioSelected(selected);
        }
    }

    @FXML
    private void handleBackToMain() {
        System.out.println("↩ [UI Action]: Κλείσιμο του Popup και επιστροφή στο ήδη ανοιχτό Κεντρικό Μενού.");
        try {
            Stage stage = (Stage) scenarioListView.getScene().getWindow();
            stage.close(); // Κλείνει το popup, η πίσω οθόνη εμφανίζεται αμέσως!
        } catch (Exception e) {
            System.err.println(" Σφάλμα κατά το κλείσιμο: " + e.getMessage());
        }
    }
}