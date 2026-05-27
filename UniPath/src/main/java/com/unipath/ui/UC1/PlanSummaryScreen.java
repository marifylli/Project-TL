package com.unipath.ui.UC1;

import com.unipath.controller.ManageStudyPlan;
import com.unipath.model.Course;
import com.unipath.model.Scenario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.net.URL;
import java.util.List;

public class PlanSummaryScreen {

    private ManageStudyPlan manageStudyPlan;
    private Scenario scenario;
    private List<Course> selectedCourses;

    @FXML
    private Label scenarioTitleLabel;
    @FXML
    private Label totalEctsLabel;

    @FXML
    private VBox boxMainA;
    @FXML
    private VBox boxMainB;
    @FXML
    private VBox boxOtherA;
    @FXML
    private VBox boxElectives;

    @FXML
    private Label ectsALabel;
    @FXML
    private Label ectsBLabel;
    @FXML
    private Label lblOtherDirectionsTitle;
    @FXML
    private Label lblElectivesTitle;

    @FXML
    private ListView<String> summaryGroupAListView;
    @FXML
    private ListView<String> summaryGroupBListView;
    @FXML
    private ListView<String> otherDirectionsListView;
    @FXML
    private ListView<String> electivesListView;

    public void setSummaryData(ManageStudyPlan controller, Scenario scenario, List<Course> courses) {
        this.manageStudyPlan = controller;
        this.scenario = scenario;
        this.selectedCourses = courses;

        summaryGroupAListView.getItems().clear();
        summaryGroupBListView.getItems().clear();
        otherDirectionsListView.getItems().clear();
        electivesListView.getItems().clear();

        int scenarioId = (scenario != null) ? scenario.getScenarioId() : 1;

        if (scenarioTitleLabel != null && scenario != null) {
            scenarioTitleLabel.setText("Επιλεγμένο Σενάριο: " + scenario.getTitle());
        }


        String k1 = "K1";
        String k2 = "K2";


        if (controller != null) {
            try {
                java.lang.reflect.Field f1 = controller.getClass().getDeclaredField("activeDirection1");
                java.lang.reflect.Field f2 = controller.getClass().getDeclaredField("activeDirection2");
                f1.setAccessible(true);
                f2.setAccessible(true);
                k1 = (String) f1.get(controller);
                k2 = (String) f2.get(controller);
            } catch (Exception ignored) {
            }
        }

        int totalEcts = 0;


        if (scenarioId == 1) {
            boxMainA.setVisible(true);
            boxMainA.setManaged(true);
            boxMainB.setVisible(true);
            boxMainB.setManaged(true);
            boxOtherA.setVisible(true);
            boxOtherA.setManaged(true);
            boxElectives.setVisible(true);
            boxElectives.setManaged(true);

            ectsALabel.setText("1η Κύρια Κατεύθυνση (" + k1 + ") - Ομάδα Α:");
            ectsBLabel.setText("1η Κύρια Κατεύθυνση (" + k1 + ") - Ομάδα Β:");
            lblOtherDirectionsTitle.setText("Λοιπές Κατευθύνσεις - Ομάδα Α:");
            lblElectivesTitle.setText("Ελεύθερα / Άλλα Τμήματα / Erasmus:");

            if (courses != null) {
                for (Course c : courses) {
                    String id = c.getCourseID().toUpperCase();
                    String displayString = c.getTitle() + " (" + c.getECTS() + " ECTS)";
                    String dirs = (c.getDirections() != null) ? c.getDirections().toUpperCase() : "";
                    totalEcts += c.getECTS();

                    boolean isExternal = id.contains("_ΓΠ") || id.contains("_ERA") || id.equals("CEID_E90E") || id.equals("CEID_AE2") || id.equals("CEID_ΔΕΖ");

                    if (isExternal) {
                        electivesListView.getItems().add(displayString);
                    } else if (c.belongsToDirection(k1)) {
                        if (c.isGroupAForDirection(k1)) summaryGroupAListView.getItems().add(displayString);
                        else summaryGroupBListView.getItems().add(displayString);
                    } else {
                        if (dirs.contains(":A")) otherDirectionsListView.getItems().add(displayString);
                        else if (dirs.contains(":B")) electivesListView.getItems().add(displayString);
                    }
                }
            }
        }

        else if (scenarioId == 2) {
            boxMainA.setVisible(true);
            boxMainA.setManaged(true);
            boxMainB.setVisible(true);
            boxMainB.setManaged(true);
            boxOtherA.setVisible(true);
            boxOtherA.setManaged(true);
            boxElectives.setVisible(true);
            boxElectives.setManaged(true);

            ectsALabel.setText("1η Κύρια Κατεύθυνση (" + k1 + ") - Ομάδα Α:");
            ectsBLabel.setText("1η Κύρια Κατεύθυνση (" + k1 + ") - Ομάδα Β:");
            lblOtherDirectionsTitle.setText("2η Κύρια Κατεύθυνση (" + k2 + ") - Ομάδα Α:");
            lblElectivesTitle.setText("2η Κύρια Κατεύθυνση (" + k2 + ") - Ομάδα Β + Λοιπά Ελεύθερα:");

            if (courses != null) {
                for (Course c : courses) {
                    String id = c.getCourseID().toUpperCase();
                    String displayString = c.getTitle() + " (" + c.getECTS() + " ECTS)";
                    totalEcts += c.getECTS();

                    boolean isExternal = id.contains("_ΓΠ") || id.contains("_ERA") || id.equals("CEID_E90E") || id.equals("CEID_AE2") || id.equals("CEID_ΔΕΖ");

                    if (isExternal) {
                        electivesListView.getItems().add(displayString);
                    } else if (c.belongsToDirection(k1)) {

                        if (c.isGroupAForDirection(k1)) summaryGroupAListView.getItems().add(displayString);
                        else summaryGroupBListView.getItems().add(displayString);
                    } else if (c.belongsToDirection(k2)) {

                        if (c.isGroupAForDirection(k2)) otherDirectionsListView.getItems().add(displayString);
                        else electivesListView.getItems().add(displayString);
                    } else {

                        electivesListView.getItems().add(displayString);
                    }
                }
            }
        }

        else if (scenarioId == 3) {
            boxMainA.setVisible(true);
            boxMainA.setManaged(true);
            boxMainB.setVisible(true);
            boxMainB.setManaged(true);
            boxOtherA.setVisible(false);
            boxOtherA.setManaged(false);
            boxElectives.setVisible(false);
            boxElectives.setManaged(false);

            ectsALabel.setText("Γενικό Πλάνο - Μαθήματα Ομάδας Α:");
            ectsBLabel.setText("Γενικό Πλάνο - Μαθήματα Ομάδας Β & Εξωτερικά:");

            if (courses != null) {
                for (Course c : courses) {
                    String id = c.getCourseID().toUpperCase();
                    String displayString = c.getTitle() + " (" + c.getECTS() + " ECTS)";
                    String dirs = (c.getDirections() != null) ? c.getDirections().toUpperCase() : "";
                    totalEcts += c.getECTS();

                    boolean isExternal = id.contains("_ΓΠ") || id.contains("_ERA") || id.equals("CEID_E90E") || id.equals("CEID_AE2") || id.equals("CEID_ΔΕΖ");

                    if (isExternal) {
                        summaryGroupBListView.getItems().add(displayString);
                    } else if (dirs.contains(":A")) {
                        // Προτεραιότητα στην Ομάδα Α για να μην διπλοεμφανίζεται
                        summaryGroupAListView.getItems().add(displayString);
                    } else if (dirs.contains(":B")) {
                        summaryGroupBListView.getItems().add(displayString);
                    } else {
                        summaryGroupBListView.getItems().add(displayString);
                    }
                }
            }
        }

        if (totalEctsLabel != null && courses != null) {
            totalEctsLabel.setText("Συνολικά ECTS Πλάνου Επιλογής: " + totalEcts + " / 85 ECTS  (Μαθήματα: " + courses.size() + " / 17)");
        }
    }

    @FXML
    private void handleConfirm() {
        if (manageStudyPlan != null) {
            System.out.println("✓ [UI Action]: Οριστική υποβολή πλάνου στην SQLite.");
            manageStudyPlan.onConfirmPlan(scenario, selectedCourses);

            try {

                Stage stage = (Stage) totalEctsLabel.getScene().getWindow();
                stage.close();
                System.out.println("✓ [UI Flow]: Το πλάνο υποβλήθηκε και το παράθυρο έκλεισε.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleCancel() {
        System.out.println("✓ [UI Action]: Ακύρωση και επιστροφή στην Κεντρική Οθόνη.");
        try {
            Stage stage = (Stage) totalEctsLabel.getScene().getWindow();
            stage.close(); // Απλό κλείσιμο
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClear() {
        if (manageStudyPlan != null) {
            System.out.println("✓ [UI Action]: Διαγραφή πλάνου.");
            manageStudyPlan.removeCourses();
            try {
                Stage stage = (Stage) totalEctsLabel.getScene().getWindow();
                stage.close(); // Απλό κλείσιμο
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}