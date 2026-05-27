package com.unipath.ui.UC1;

import com.unipath.controller.ManageStudyPlan;
import com.unipath.model.Course;
import com.unipath.model.Scenario;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CourseSelectionScreen {

    private ManageStudyPlan manageStudyPlan;
    private Scenario scenario;

    @FXML
    private Label scenarioTitleLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private ComboBox<String> directionComboBox1;
    @FXML
    private ComboBox<String> directionComboBox2;
    @FXML
    private TabPane selectionTabPane;
    @FXML
    private VBox dir1Box;
    @FXML
    private VBox dir2Box;

    // Tabs
    @FXML
    private Tab tabMainDirection1;
    @FXML
    private Tab tabMainDirection2;
    @FXML
    private Tab tabOtherDirections;
    @FXML
    private Tab tabGeneralPlan;
    @FXML
    private Tab tabElectives;

    // ListViews
    @FXML
    private ListView<String> main1GroupAListView;
    @FXML
    private ListView<String> main1GroupBListView;
    @FXML
    private ListView<String> main2GroupAListView;
    @FXML
    private ListView<String> main2GroupBListView;
    @FXML
    private ListView<String> otherDirectionsListView;
    @FXML
    private ListView<String> generalGroupAListView;
    @FXML
    private ListView<String> generalGroupBListView;
    @FXML
    private ListView<String> electivesListView;

    // Counter Labels
    @FXML
    private Label lblMain1A;
    @FXML
    private Label lblMain1B;
    @FXML
    private Label lblMain2A;
    @FXML
    private Label lblMain2B;
    @FXML
    private Label lblOtherA;
    @FXML
    private Label lblGenA;
    @FXML
    private Label lblGenB;
    @FXML
    private Label lblElectives;

    private final Map<String, Course> courseMap = new HashMap<>();
    private final Set<Course> globallySelectedCourses = new HashSet<>();
    private boolean isUpdatingSelection = false;

    public void setSelectionData(ManageStudyPlan manageStudyPlan, Scenario scenario) {
        this.manageStudyPlan = manageStudyPlan;
        this.scenario = scenario;

        if (scenarioTitleLabel != null && scenario != null) {
            scenarioTitleLabel.setText("Επιλεγμένο: " + scenario.getTitle());
        }

        configureListViews();
        setupDirectionComboBoxes();
        setupSelectionListeners();
        displayCourses();
    }

    private void configureListViews() {
        ListView[] lists = {main1GroupAListView, main1GroupBListView, main2GroupAListView,
                main2GroupBListView, otherDirectionsListView, generalGroupAListView,
                generalGroupBListView, electivesListView};
        for (ListView lv : lists) {
            if (lv != null) lv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }
    }

    private void setupDirectionComboBoxes() {
        if (directionComboBox1 == null || directionComboBox2 == null) return;

        directionComboBox1.getItems().clear();
        directionComboBox2.getItems().clear();

        String[] directions = {
                "K1: Αλγοριθμικές Θεμελιώσεις και Ευφυής Υπολογιστική",
                "K2: Δίκτυα και Επικοινωνίες",
                "K3: Τεχνολογία της Πληροφορίας",
                "K4: Τεχνολογία Υλικού, Αρχιτεκτονική Υπολογιστών",
                "K5: Τεχνολογία και Συστήματα Λογισμικού",
                "K6: Ευφυή Συστήματα και Ανάλυση Μεγάλων Δεδομένων"
        };

        directionComboBox1.getItems().addAll(directions);
        directionComboBox2.getItems().addAll(directions);

        directionComboBox1.getSelectionModel().select(0);
        directionComboBox2.getSelectionModel().select(1);

        int scenarioId = (scenario != null) ? scenario.getScenarioId() : 1;

        if (scenarioId == 1) {
            dir1Box.setVisible(true);
            dir2Box.setVisible(false);
            selectionTabPane.getTabs().removeAll(tabMainDirection2, tabGeneralPlan);
        } else if (scenarioId == 2) {
            dir1Box.setVisible(true);
            dir2Box.setVisible(true);
            selectionTabPane.getTabs().removeAll(tabOtherDirections, tabGeneralPlan);
        } else if (scenarioId == 3) {
            dir1Box.setVisible(false);
            dir2Box.setVisible(false);
            selectionTabPane.getTabs().removeAll(tabMainDirection1, tabMainDirection2, tabOtherDirections);
        }

        directionComboBox1.valueProperty().addListener((obs, old, newVal) -> {
            globallySelectedCourses.clear();
            displayCourses();
            updateDirectionContextInController();
        });
        directionComboBox2.valueProperty().addListener((obs, old, newVal) -> {
            globallySelectedCourses.clear();
            displayCourses();
            updateDirectionContextInController();
        });

        updateDirectionContextInController();
    }

    private void updateDirectionContextInController() {
        if (manageStudyPlan == null) return;
        String k1 = directionComboBox1.getValue() != null ? directionComboBox1.getValue().substring(0, 2).trim().toUpperCase().replace('Κ', 'K') : "K1";
        String k2 = directionComboBox2.getValue() != null ? directionComboBox2.getValue().substring(0, 2).trim().toUpperCase().replace('Κ', 'K') : "K2";
        manageStudyPlan.setDirectionContext(k1, k2);
    }

    public void displayCourses() {
        if (manageStudyPlan == null) return;

        isUpdatingSelection = true;

        ListView[] lists = {main1GroupAListView, main1GroupBListView, main2GroupAListView,
                main2GroupBListView, otherDirectionsListView, generalGroupAListView,
                generalGroupBListView, electivesListView};
        for (ListView lv : lists) {
            if (lv != null) lv.getItems().clear();
        }
        courseMap.clear();

        List<Course> allCourses = manageStudyPlan.queryGetCourses();
        if (allCourses == null || allCourses.isEmpty()) {
            isUpdatingSelection = false;
            return;
        }

        int scenarioId = (scenario != null) ? scenario.getScenarioId() : 1;
        String rawK1 = (directionComboBox1 != null && directionComboBox1.getValue() != null) ? directionComboBox1.getValue() : "K1";
        String rawK2 = (directionComboBox2 != null && directionComboBox2.getValue() != null) ? directionComboBox2.getValue() : "K2";

        String selectedK1 = rawK1.substring(0, 2).trim().toUpperCase().replace('Κ', 'K');
        String selectedK2 = rawK2.substring(0, 2).trim().toUpperCase().replace('Κ', 'K');

        for (Course course : allCourses) {
            String id = course.getCourseID().toUpperCase();
            String displayString = course.getTitle() + " (" + course.getECTS() + " ECTS)";
            courseMap.put(displayString, course);

            boolean belongsToK1 = course.belongsToDirection(selectedK1);
            boolean belongsToK2 = (scenarioId == 2) && course.belongsToDirection(selectedK2);

            boolean isExternalOrErasmus = id.contains("_ΓΠ") || id.contains("_ERA") ||
                    id.equals("CEID_E90E") || id.equals("CEID_AE2") || id.equals("CEID_ΔΕΖ");

            if (scenarioId == 1) {
                if (isExternalOrErasmus) {
                    electivesListView.getItems().add(displayString);
                } else if (belongsToK1) {
                    if (course.isGroupAForDirection(selectedK1)) main1GroupAListView.getItems().add(displayString);
                    else if (course.isGroupBForDirection(selectedK1)) main1GroupBListView.getItems().add(displayString);
                } else {
                    if (course.getDirections() != null && course.getDirections().toUpperCase().contains(":A")) {
                        otherDirectionsListView.getItems().add(displayString);
                    } else if (course.getDirections() != null && course.getDirections().toUpperCase().contains(":B")) {
                        electivesListView.getItems().add(displayString);
                    }
                }
            } else if (scenarioId == 2) {
                if (isExternalOrErasmus) {
                    electivesListView.getItems().add(displayString);
                } else if (belongsToK1) {
                    if (course.isGroupAForDirection(selectedK1)) main1GroupAListView.getItems().add(displayString);
                    else if (course.isGroupBForDirection(selectedK1)) main1GroupBListView.getItems().add(displayString);
                } else if (belongsToK2) {
                    if (course.isGroupAForDirection(selectedK2)) main2GroupAListView.getItems().add(displayString);
                    else if (course.isGroupBForDirection(selectedK2)) main2GroupBListView.getItems().add(displayString);
                } else {
                    electivesListView.getItems().add(displayString);
                }
            } else if (scenarioId == 3) {
                if (isExternalOrErasmus) {
                    electivesListView.getItems().add(displayString);
                } else if (course.getDirections() != null) {
                    String dirs = course.getDirections().toUpperCase();

                    if (dirs.contains(":A")) {
                        generalGroupAListView.getItems().add(displayString);
                    } else if (dirs.contains(":B")) {
                        generalGroupBListView.getItems().add(displayString);
                    }
                }
            }
        }

        for (ListView<String> lv : lists) {
            if (lv == null) continue;
            for (String itemText : lv.getItems()) {
                Course c = courseMap.get(itemText);
                if (c != null && globallySelectedCourses.contains(c)) {
                    lv.getSelectionModel().select(itemText);
                }
            }
        }

        isUpdatingSelection = false;
        updateCounterLabels();
    }

    private void setupSelectionListeners() {
        int scenarioId = (scenario != null) ? scenario.getScenarioId() : 1;

        int maxMain1B = (scenarioId == 2) ? 2 : 5;
        int maxMain2B = 2;
        int maxOtherA = 5;
        int maxGenA = 10;
        int maxGenB = 7;

        trackSelection(main1GroupAListView, 5);
        trackSelection(main1GroupBListView, maxMain1B);
        trackSelection(main2GroupAListView, 5);
        trackSelection(main2GroupBListView, maxMain2B);
        trackSelection(otherDirectionsListView, maxOtherA);
        trackSelection(generalGroupAListView, maxGenA);
        trackSelection(generalGroupBListView, maxGenB);


        if (electivesListView != null) {
            electivesListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<String>) change -> {
                if (isUpdatingSelection) return;

                while (change.next()) {
                    if (change.wasAdded()) {
                        for (String s : change.getAddedSubList()) {
                            Course c = courseMap.get(s);
                            if (c != null) globallySelectedCourses.add(c);
                        }
                    }
                    if (change.wasRemoved()) {
                        for (String s : change.getRemoved()) {
                            globallySelectedCourses.removeIf(c -> (c.getTitle() + " (" + c.getECTS() + " ECTS)").equals(s));
                        }
                    }
                }


                if (scenarioId == 1) {
                    int otherDirectionsBSelected = 0;
                    int externalDepartmentsSelected = 0;
                    int erasmusSelected = 0;

                    for (String item : electivesListView.getSelectionModel().getSelectedItems()) {
                        Course c = courseMap.get(item);
                        if (c != null) {
                            String id = c.getCourseID().toUpperCase();
                            if (id.contains("_ΓΠ") || id.equals("CEID_E90E") || id.equals("CEID_AE2") || id.equals("CEID_ΔΕΖ")) {
                                externalDepartmentsSelected++;
                            } else if (id.contains("_ERA")) {
                                erasmusSelected++;
                            } else {
                                otherDirectionsBSelected++;
                            }
                        }
                    }


                    if ((otherDirectionsBSelected > 0 && externalDepartmentsSelected > 0) ||
                            (otherDirectionsBSelected > 0 && erasmusSelected > 0) ||
                            (externalDepartmentsSelected > 0 && erasmusSelected > 0)) {

                        if (errorLabel != null) {
                            errorLabel.setText("⚠ Κανόνας Αμοιβαίου Αποκλεισμού: Επιτρέπονται 2 μαθήματα ΜΟΝΟ από μία κατηγορία (ή Λοιπές Β, ή Άλλα Τμήματα, ή Erasmus)!");
                            errorLabel.setVisible(true);
                        }
                    } else if (otherDirectionsBSelected > 2 || externalDepartmentsSelected > 2 || erasmusSelected > 2) {
                        if (errorLabel != null) {
                            errorLabel.setText("⚠ Αυστηρό Όριο: Επιτρέπονται το πολύ 2 μαθήματα επιλογής στο 3ο Tab!");
                            errorLabel.setVisible(true);
                        }
                    } else {
                        if (errorLabel != null) errorLabel.setVisible(false);
                    }
                }

                updateCounterLabels();
            });
        }
    }

    private void trackSelection(ListView<String> listView, int maxLimit) {
        if (listView == null) return;
        listView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<String>) change -> {
            if (isUpdatingSelection) return;

            while (change.next()) {
                if (change.wasAdded()) {
                    for (String itemText : change.getAddedSubList()) {
                        Course c = courseMap.get(itemText);
                        if (c != null) {
                            int currentListCount = listView.getSelectionModel().getSelectedItems().size();
                            if (currentListCount > maxLimit) {
                                isUpdatingSelection = true;
                                if (errorLabel != null) {
                                    errorLabel.setText("⚠ Αυστηρό Όριο Σεναρίου: Επιτρέπονται το πολύ " + maxLimit + " μαθήματα σε αυτή την κατηγορία!");
                                    errorLabel.setVisible(true);
                                }
                                final String toRemove = itemText;
                                Platform.runLater(() -> {
                                    listView.getSelectionModel().clearSelection(listView.getItems().indexOf(toRemove));
                                    isUpdatingSelection = false;
                                });
                                return;
                            } else {
                                globallySelectedCourses.add(c);
                            }
                        }
                    }
                }
                if (change.wasRemoved()) {
                    for (String itemText : change.getRemoved()) {
                        globallySelectedCourses.removeIf(c -> (c.getTitle() + " (" + c.getECTS() + " ECTS)").equals(itemText));
                    }
                }
            }
            if (errorLabel != null && listView.getSelectionModel().getSelectedItems().size() <= maxLimit) {
                errorLabel.setVisible(false);
            }
            updateCounterLabels();
        });
    }

    private void updateCounterLabels() {
        int scenarioId = (scenario != null) ? scenario.getScenarioId() : 1;
        int maxMain1B = (scenarioId == 2) ? 2 : 5;


        int m1A = main1GroupAListView.getSelectionModel().getSelectedItems().size();
        int m1B = main1GroupBListView.getSelectionModel().getSelectedItems().size();
        int m2A = main2GroupAListView.getSelectionModel().getSelectedItems().size();
        int m2B = main2GroupBListView.getSelectionModel().getSelectedItems().size();
        int othA = otherDirectionsListView.getSelectionModel().getSelectedItems().size();

        int gA = generalGroupAListView.getSelectionModel().getSelectedItems().size();
        int gB = generalGroupBListView.getSelectionModel().getSelectedItems().size();
        int el = electivesListView.getSelectionModel().getSelectedItems().size();


        if (scenarioId == 3) {
            gA = generalGroupAListView.getSelectionModel().getSelectedItems().size();
            gB = generalGroupBListView.getSelectionModel().getSelectedItems().size();


            int extCount = electivesListView.getSelectionModel().getSelectedItems().size();
            gB += extCount;
        }


        int totalSelected = globallySelectedCourses.size();


        if (lblMain1A != null) lblMain1A.setText("Μαθήματα Ομάδας Α (Επιλεγμένα: " + m1A + " / 5)");
        if (lblMain1B != null) lblMain1B.setText("Μαθήματα Ομάδας Β (Επιλεγμένα: " + m1B + " / " + maxMain1B + ")");
        if (lblMain2A != null) lblMain2A.setText("Μαθήματα Ομάδας Α (Επιλεγμένα: " + m2A + " / 5)");
        if (lblMain2B != null) lblMain2B.setText("Μαθήματα Ομάδας Β (Επιλεγμένα: " + m2B + " / 2)");
        if (lblOtherA != null) lblOtherA.setText("Λοιπές Κατευθύνσεις (Ομάδα Α) (Επιλεγμένα: " + othA + " / 5)");

        if (lblGenA != null) lblGenA.setText("Μαθήματα Ομάδας Α (Επιλεγμένα: " + gA + " / 10)");
        if (lblGenB != null) lblGenB.setText("Μαθήματα Ομάδας Β (Επιλεγμένα: " + gB + " / 7)");

        if (lblElectives != null) {
            if (scenarioId == 1) {
                lblElectives.setText("Ελεύθερες Επιλογές (Επιλεγμένα: " + el + " / 2) [Σύνολο Πλάνου: " + totalSelected + " / 17]");
            } else if (scenarioId == 3) {
                lblElectives.setText("Ελεύθερα / Erasmus (Επιλεγμένα: " + el + ") [Σύνολο Πλάνου: " + totalSelected + " / 17]");
            } else {
                lblElectives.setText("Ελεύθερα / Άλλα Τμήματα / Erasmus [Σύνολο Πλάνου: " + totalSelected + " / 17]");
            }
        }
    }

    @FXML
    private void handleNextStep() {
        List<Course> finalizedList = new ArrayList<>(globallySelectedCourses);

        if (errorLabel == null) return;

        if (finalizedList.size() != 17) {
            errorLabel.setText(" ΣΦΑΛΜΑ: Το πρόγραμμα σπουδών πρέπει να περιλαμβάνει συνολικά ακριβώς 17 μαθήματα! (Έχετε επιλέξει: " + finalizedList.size() + ")");
            errorLabel.setVisible(true);
            return;
        }

        int scenarioId = (scenario != null) ? scenario.getScenarioId() : 1;
        if (scenarioId == 1) {
            int m1A = main1GroupAListView.getSelectionModel().getSelectedItems().size();
            int m1B = main1GroupBListView.getSelectionModel().getSelectedItems().size();
            int el = electivesListView.getSelectionModel().getSelectedItems().size();

            if (m1A != 5 || m1B != 5) {
                errorLabel.setText(" ΣΦΑΛΜΑ: Για το Σενάριο 1 απαιτούνται ακριβώς 5 μαθήματα Ομάδας Α και 5 Ομάδας Β της κύριας κατεύθυνσης!");
                errorLabel.setVisible(true);
                return;
            }
            if (el != 2) {
                errorLabel.setText("ΣΦΑΛΜΑ: Πρέπει να επιλέξετε ακριβώς 2 μαθήματα στο 3ο Tab!");
                errorLabel.setVisible(true);
                return;
            }
        } else if (scenarioId == 2) {
            int m1A = main1GroupAListView.getSelectionModel().getSelectedItems().size();
            int m1B = main1GroupBListView.getSelectionModel().getSelectedItems().size();
            int m2A = main2GroupAListView.getSelectionModel().getSelectedItems().size();
            int m2B = main2GroupBListView.getSelectionModel().getSelectedItems().size();

            if (m1A != 5 || m1B != 2 || m2A != 5 || m2B != 2) {
                errorLabel.setText("ΣΦΑΛΜΑ: Για το Σενάριο 2 απαιτούνται 5Α+2Β από την 1η κύρια κατεύθυνση και 5Α+2Β από τη 2η κύρια κατεύθυνση!");
                errorLabel.setVisible(true);
                return;
            }
        }

        errorLabel.setVisible(false);
        if (manageStudyPlan != null) {
            manageStudyPlan.onCoursesSelected(this.scenario, finalizedList);
        }
    }

    @FXML
    private void handleBack() {
        System.out.println("↩ [UI Action]: Επιστροφή από τα Tabs στο Βήμα 1 (Επιλογή Σεναρίου).");
        try {
            java.net.URL fxmlUrl = getClass().getClassLoader().getResource("fxml/Student/scenario-selection-view.fxml");
            if (fxmlUrl == null) fxmlUrl = getClass().getResource("/fxml/Student/scenario-selection-view.fxml");

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            ScenarioSelectionScreen scenarioScreen = loader.getController();
            scenarioScreen.setManageStudyPlan(this.manageStudyPlan);

            Stage stage = (Stage) scenarioTitleLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 650, 500));
            stage.setTitle("Επιλογή Σεναρίου - UniPath");
            stage.show();
        } catch (Exception e) {
            System.err.println("Απέτυχε η επιστροφή στην επιλογή σεναρίου: " + e.getMessage());
            e.printStackTrace();
        }
    }
}