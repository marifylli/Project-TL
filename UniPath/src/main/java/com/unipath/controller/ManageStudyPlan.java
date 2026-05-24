package com.unipath.controller;
import com.unipath.model.Course;
import com.unipath.repository.ScenarioRepository;
import com.unipath.ui.UC1.ConfirmationScreen;
import com.unipath.ui.UC1.CourseSelectionScreen;
import com.unipath.ui.UC1.PlanSummaryScreen;
import com.unipath.model.Scenario;
import com.unipath.ui.UC1.ScenarioSelectionScreen;
import com.unipath.repository.CourseRepository;
import com.unipath.repository.ScenarioRepository;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManageStudyPlan {
    private ScenarioSelectionScreen scenarioSelectionScreen;
    private CourseSelectionScreen courseSelectionScreen;
    private PlanSummaryScreen planSummaryScreen;
    private ConfirmationScreen confirmationScreen;
    private CourseRepository courseRepository;
    private ScenarioRepository scenarioRepository;

    private javafx.stage.Stage mainStage;

    private Scenario selectedScenario;
    private List<Course> selectedCourses;

    public ManageStudyPlan() {
        this.scenarioRepository = new ScenarioRepository();
        this.courseRepository = new CourseRepository();
    }

    public void startCreatePlan() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/Student/scenario-selection-view.fxml"));
            javafx.scene.Parent root = loader.load();

            com.unipath.ui.UC1.ScenarioSelectionScreen scenarioScreen = loader.getController();
            scenarioScreen.setManageStudyPlan(this);

            this.mainStage = new javafx.stage.Stage();
            mainStage.setTitle("Επιλογή Σεναρίου - UniPath");
            mainStage.setScene(new javafx.scene.Scene(root, 600, 500));
            mainStage.show();
        } catch (java.io.IOException e) {
            System.err.println("Σφάλμα κατά το άνοιγμα της οθόνης επιλογής σεναρίου: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void onScenarioSelected(Scenario scenario) {
        this.selectedScenario = scenario; // Αποθηκεύουμε το σενάριο που επιλέχθηκε
        try {
            // 1. Φόρτωση του FXML για την επιλογή μαθημάτων
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/Student/course-selection-view.fxml"));
            javafx.scene.Parent root = loader.load();

            // 2. Σύνδεση δεδομένων με τον Controller της νέας οθόνης
            com.unipath.ui.UC1.CourseSelectionScreen selectionScreen = loader.getController();
            selectionScreen.setSelectionData(this, scenario);

            // 3. 💡 ΔΙΟΡΘΩΣΗ: Δημιουργία νέου Scene και εξαναγκασμός εμφάνισης
            if (mainStage != null) {
                // Φτιάχνουμε μια νέα σκηνή με τις διαστάσεις που ορίζει το FXML (650x550)
                javafx.scene.Scene newScene = new javafx.scene.Scene(root, 650, 550);

                // Προσθέτουμε το CSS αρχείο αν υπάρχει (προαιρετικό, αλλά βοηθάει αν χάνει τα styles)
                if (getClass().getResource("/css/styles.css") != null) {
                    newScene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                }

                mainStage.setTitle("Επιλογή Μαθημάτων - UniPath");
                mainStage.setScene(newScene);
                mainStage.sizeToScene();  // Προσαρμογή μεγέθους παραθύρου
                mainStage.show();         // Επανεμφάνιση
            }

        } catch (java.io.IOException e) {
            System.err.println("Σφάλμα κατά τη φόρτωση της οθόνης επιλογής μαθημάτων: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void onCoursesSelected(Scenario scenario, List<Course> courses) {
        this.selectedScenario = scenario; // Αποθηκεύουμε το σενάριο
        this.selectedCourses = courses;   // Αποθηκεύουμε τα μαθήματα

        // 1. 💡 Εκκίνηση των ελέγχων (validateRules) που έχεις ήδη έτοιμους στην κλάση!
        if (validateRules()) {
            System.out.println("DEBUG: Οι έλεγχοι πέτυχαν! Φόρτωση του PlanSummaryScreen...");

            try {
                // 2. Φόρτωση του FXML για τη σύνοψη
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/Student/plan-summary-view.fxml"));
                javafx.scene.Parent root = loader.load();

                // Παίρνουμε τον Controller της επόμενης οθόνης
                PlanSummaryScreen summaryScreen = loader.getController();

                // 💡 SOS: Καλείς τη μέθοδο αρχικοποίησης δεδομένων του PlanSummaryScreen.
                // Ανάλογα με το πώς την έχεις ονομάσει, της περνάς τα δεδομένα.
                summaryScreen.setSummaryData(this, scenario, courses);

                // 3. Μετάβαση της οθόνης στο ίδιο παράθυρο
                if (mainStage != null) {
                    javafx.scene.Scene scene = new javafx.scene.Scene(root, 650, 550);
                    if (getClass().getResource("/css/styles.css") != null) {
                        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                    }
                    mainStage.setTitle("Προεπισκόπηση Προγράμματος Σπουδών - UniPath");
                    mainStage.setScene(scene);
                    mainStage.sizeToScene();
                    mainStage.show();
                }

            } catch (java.io.IOException e) {
                System.err.println("Σφάλμα κατά τη φόρτωση της οθόνης προεπισκόπησης: " + e.getMessage());
                e.printStackTrace();
            }
        } else {

            System.out.println("❌ ΑΠΟΤΥΧΙΑ: Το πρόγραμμα σπουδών δεν πληροί τις προϋποθέσεις του σεναρίου!");

            // Εδώ, προαιρετικά, μπορείς να εμφανίσεις ένα alert ή να ανάψεις το errorLabel της προηγούμενης οθόνης.
        }
    }
    public void onConfirmPlan(Scenario scenario, List<Course> courses) {
        System.out.println("DEBUG: Ο χρήστης επιβεβαίωσε το πρόγραμμα σπουδών. Αποθήκευση και εμφάνιση Success Screen...");

        try {
            // 1. Φόρτωση του FXML για την οθόνη επιτυχίας από τον κοινό φάκελο (common)
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/common/success-window-view.fxml"));
            javafx.scene.Parent root = loader.load();

            // 2. Αν η κλάση SuccessScreen χρειάζεται κάποια αρχικοποίηση, την κάνεις εδώ.
            // Για παράδειγμα, αν έχει μέθοδο setManageStudyPlan:
            // com.unipath.ui.common.SuccessScreen successScreen = loader.getController();

            // 3. Αλλαγή της σκηνής στο mainStage για να φανεί το Success Screen
            if (mainStage != null) {
                javafx.scene.Scene scene = new javafx.scene.Scene(root, 650, 550);
                if (getClass().getResource("/css/styles.css") != null) {
                    scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                }
                mainStage.setTitle("Επιτυχής Υποβολή - UniPath");
                mainStage.setScene(scene);
                mainStage.sizeToScene();
                mainStage.show();
            }

        } catch (java.io.IOException e) {
            System.err.println("Σφάλμα κατά τη φόρτωση της οθόνης επιτυχίας: " + e.getMessage());
            e.printStackTrace();
        }
    }





    public int calculateEcts(){
        if (selectedCourses == null) return 0;
        int total = 0;
        for (Course c : selectedCourses) {
            total += c.getECTS();
        }
        return total;
    }


    public void removeCourses() {
        System.out.println("DEBUG: Εκτέλεση της removeCourses(). Καθαρισμός επιλογών και επανεκκίνηση...");


        this.selectedScenario = null;

        this.selectedCourses = null;


        startCreatePlan();
    }



    public boolean validateRules() {
        return true;
        /*if (selectedScenario == null || selectedCourses == null || selectedCourses.isEmpty()) return false;

        // total ECTS must be 270
        if (calculateEcts() != 270) return false;

        int scenarioId = selectedScenario.getScenarioId();

        if (scenarioId == 1) {
            return validateScenario1();
        } else if (scenarioId == 2) {
            return validateScenario2();
        } else if (scenarioId == 3) {
            return validateScenario3();
        }
        return false;*/
    }

// --------- Helpers ---------

    private boolean validateScenario1() {
        // You must get main direction from UI selection
        String mainDir = getMainDirection();
        if (mainDir == null || mainDir.isEmpty()) return false;

        int mainA = 0, mainB = 0;
        int otherA = 0;
        Set<String> otherADirections = new HashSet<>();
        int otherCourses = 0;
        Set<String> otherCourseDirs = new HashSet<>();

        for (Course c : selectedCourses) {
            Set<String> dirs = parseDirections(c.getDirections());
            boolean inMain = dirs.contains(mainDir);

            if (inMain) {
                if (c.isGroupA()) mainA++;
                if (c.isGroupB()) mainB++;
            } else {
                if (c.isGroupA()) {
                    otherA++;
                    otherADirections.addAll(dirs);
                }
                otherCourses++;
                otherCourseDirs.addAll(dirs);
            }
        }

        return selectedCourses.size() == 17
                && mainA >= 5
                && mainB >= 5
                && otherA >= 5
                && otherADirections.size() >= 3
                && otherCourses >= 2
                && otherCourseDirs.size() >= 2;
    }

    // ===== Scenario 2 =====
    private boolean validateScenario2() {
        String mainDir = getMainDirection();
        String secondDir = getSecondDirection();
        if (mainDir == null || secondDir == null) return false;

        int main1A = 0, main1B = 0;
        int main2A = 0, main2B = 0;
        int otherCourses = 0;
        Set<String> otherDirs = new HashSet<>();

        for (Course c : selectedCourses) {
            Set<String> dirs = parseDirections(c.getDirections());
            boolean inMain1 = dirs.contains(mainDir);
            boolean inMain2 = dirs.contains(secondDir);

            if (inMain1) {
                if (c.isGroupA()) main1A++;
                if (c.isGroupB()) main1B++;
            } else if (inMain2) {
                if (c.isGroupA()) main2A++;
                if (c.isGroupB()) main2B++;
            } else {
                otherCourses++;
                otherDirs.addAll(dirs);
            }
        }

        return selectedCourses.size() == 17
                && main1A >= 5 && main1B >= 2
                && main2A >= 5 && main2B >= 2
                && otherCourses >= 3
                && otherDirs.size() >= 2;
    }

    // ===== Scenario 3 =====
    private boolean validateScenario3() {
        int groupAcount = 0;
        int otherCount = 0;
        Set<String> dirs = new HashSet<>();

        for (Course c : selectedCourses) {
            if (c.isGroupA()) groupAcount++;
            else otherCount++;
            dirs.addAll(parseDirections(c.getDirections()));
        }

        return selectedCourses.size() == 17
                && groupAcount >= 10
                && otherCount >= 7
                && dirs.size() >= 4;
    }

    // ===== Helpers =====
    private Set<String> parseDirections(String raw) {
        Set<String> dirs = new HashSet<>();
        if (raw == null || raw.isEmpty()) return dirs;

        String[] parts = raw.split(",");
        for (String p : parts) {
            String[] pair = p.trim().split(":");
            if (pair.length > 0) {
                String dir = pair[0].trim();
                if (!dir.isEmpty()) dirs.add(dir);
            }
        }
        return dirs;
    }

    // TODO: replace these with real values from your UI
    private String getMainDirection() {
        return null;
    }

    private String getSecondDirection() {
        return null;
    }

    public List<Scenario> queryGetScenarios() {
        return scenarioRepository.queryGetScenarios();
    }

    public List<Course> queryGetCourses() {
        return courseRepository.queryGetCourses();
    }

}
