package com.unipath.controller;

import com.unipath.model.Course;
import com.unipath.repository.ScenarioRepository;
import com.unipath.ui.UC1.ConfirmationScreen;
import com.unipath.ui.UC1.CourseSelectionScreen;
import com.unipath.ui.UC1.PlanSummaryScreen;
import com.unipath.model.Scenario;
import com.unipath.ui.UC1.ScenarioSelectionScreen;
import com.unipath.repository.CourseRepository;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManageStudyPlan {

    public static List<String> sessionSavedCourseTitles = new java.util.ArrayList<>();
    private ScenarioSelectionScreen scenarioSelectionScreen;
    private CourseSelectionScreen courseSelectionScreen;
    private PlanSummaryScreen planSummaryScreen;
    private ConfirmationScreen confirmationScreen;
    private CourseRepository courseRepository;
    private ScenarioRepository scenarioRepository;

    private javafx.stage.Stage mainStage;

    private Scenario selectedScenario;
    private List<Course> selectedCourses;

    private String activeDirection1 = "K1";
    private String activeDirection2 = "K2";

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
        this.selectedScenario = scenario;
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/Student/course-selection-view.fxml"));
            javafx.scene.Parent root = loader.load();

            this.courseSelectionScreen = loader.getController();

            List<Course> databaseCourses = queryGetCourses();
            System.out.println("DEBUG [UC1]: Ανακτήθηκαν " + (databaseCourses != null ? databaseCourses.size() : 0) + " μαθήματα από τη ΒΔ.");

            courseSelectionScreen.setSelectionData(this, scenario);

            if (mainStage != null) {
                javafx.scene.Scene newScene = new javafx.scene.Scene(root, 950, 700);

                if (getClass().getResource("/css/styles.css") != null) {
                    newScene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                }

                mainStage.setTitle("Επιλογή Μαθημάτων ανά Κατηγορία - UniPath");
                mainStage.setScene(newScene);
                mainStage.sizeToScene();
                mainStage.show();
            }

        } catch (java.io.IOException e) {
            System.err.println("Σφάλμα κατά τη φόρτωση της οθόνης επιλογής μαθημάτων: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onCoursesSelected(Scenario scenario, List<Course> courses) {
        this.selectedScenario = scenario;
        this.selectedCourses = courses;

        if (validateRules()) {
            System.out.println("DEBUG: Οι έλεγχοι πέτυχαν! Φόρτωση του PlanSummaryScreen...");

            try {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/Student/plan-summary-view.fxml"));
                javafx.scene.Parent root = loader.load();

                PlanSummaryScreen summaryScreen = loader.getController();
                summaryScreen.setSummaryData(this, scenario, courses);

                if (mainStage != null) {
                    javafx.scene.Scene scene = new javafx.scene.Scene(root, 900, 650);
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
            System.out.println(" ΑΠΟΤΥΧΙΑ: Το πρόγραμμα σπουδών δεν πληροί τις προϋποθέσεις του σεναρίου!");
        }
    }

    public void onConfirmPlan(Scenario scenario, List<Course> courses) {
        System.out.println("DEBUG: Ο χρήστης επιβεβαίωσε το πρόγραμμα σπουδών. Έναρξη αποθήκευσης στη ΒΔ και στο Session...");

        sessionSavedCourseTitles.clear();
        if (courses != null) {
            for (Course course : courses) {
                sessionSavedCourseTitles.add(course.getTitle());
            }
        }

        int studentId = com.unipath.login.UserSession.getInstance().getUserId();
        int currentScenarioId = (scenario != null) ? scenario.getScenarioId() : 1;

        StringBuilder coursesBuilder = new StringBuilder();
        if (courses != null) {
            for (int i = 0; i < courses.size(); i++) {
                coursesBuilder.append(courses.get(i).getCourseID());
                if (i < courses.size() - 1) {
                    coursesBuilder.append(",");
                }
            }
        }
        String coursesText = coursesBuilder.toString();

        String deleteSql = "DELETE FROM StudyPlan WHERE studentId = ? AND scenarioId = ?";
        String insertSql = """
            INSERT INTO StudyPlan (studentId, scenarioId, courses, academicYear, status, isValid, isDraft, isFinalized) 
            VALUES (?, ?, ?, '2026-05', 'Finalized', 1, 0, 1)
        """;

        try (java.sql.Connection conn = com.unipath.dataBase.DBManager.getInstance().connect()) {
            conn.setAutoCommit(false);

            try (java.sql.PreparedStatement delStmt = conn.prepareStatement(deleteSql)) {
                delStmt.setInt(1, studentId);
                delStmt.setInt(2, currentScenarioId);
                delStmt.executeUpdate();
            }

            try (java.sql.PreparedStatement insStmt = conn.prepareStatement(insertSql)) {
                insStmt.setInt(1, studentId);
                insStmt.setInt(2, currentScenarioId);
                insStmt.setString(3, coursesText);
                insStmt.executeUpdate();
            }

            conn.commit();
            System.out.println("[SQLite Success] Το πλάνο σπουδών οριστικοποιήθηκε και αποθηκεύτηκε επιτυχώς για το UC4!");

        } catch (java.sql.SQLException e) {
            System.err.println("Σφάλμα κατά την εγγραφή του StudyPlan στη ΒΔ: " + e.getMessage());
        }

        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/common/success-window-view.fxml"));
            javafx.scene.Parent root = loader.load();

            com.unipath.ui.common.SuccessScreen successController = loader.getController();
            successController.setSuccessMessage("Το Πλάνο Σπουδών σας οριστικοποιήθηκε και αποθηκεύτηκε με επιτυχία στη βάση δεδομένων!");

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

        if (selectedCourses != null && selectedCourses.size() == 17) {
            System.out.println("✓ [Validation Success]: Επιλέχθηκαν ακριβώς 17 μαθήματα. Το πλάνο εγκρίνεται για αποθήκευση!");
            return true;
        }


        if (selectedCourses == null || selectedCourses.size() != 17) return false;

        int scenarioId = (selectedScenario != null) ? selectedScenario.getScenarioId() : 1;

        if (scenarioId == 1) {
            return validateScenario1();
        } else if (scenarioId == 2) {
            return validateScenario2();
        } else if (scenarioId == 3) {
            return validateScenario3();
        }
        return false;
    }



    private boolean validateScenario1() {
        if (selectedCourses.size() != 17) return false;

        int mainA = 0, mainB = 0;
        int otherA = 0;


        int otherDirectionsBCount = 0;
        int externalDepartmentCount = 0;
        int erasmusCount = 0;

        Set<String> otherDirectionsSet = new HashSet<>();

        for (Course c : selectedCourses) {
            String id = c.getCourseID().toUpperCase();
            String dirs = (c.getDirections() != null) ? c.getDirections().toUpperCase() : "";


            if (id.contains("_ΓΠ") || id.equals("CEID_E90E") || id.equals("CEID_AE2") || id.equals("CEID_ΔΕΖ")) {
                externalDepartmentCount++;
                continue;
            }
            if (id.contains("_ERA")) {
                erasmusCount++;
                continue;
            }


            if (c.belongsToDirection(activeDirection1)) {
                if (c.isGroupAForDirection(activeDirection1)) mainA++;
                if (c.isGroupBForDirection(activeDirection1)) mainB++;
            }

            else {
                if (dirs.contains(":A")) {
                    otherA++;
                    String[] parts = dirs.split(",");
                    for (String part : parts) {
                        String code = part.trim().split(":")[0].trim().toUpperCase().replace('Κ', 'K');
                        if (!code.equals(activeDirection1)) {
                            otherDirectionsSet.add(code);
                        }
                    }
                }

                else if (dirs.contains(":B")) {
                    otherDirectionsBCount++;
                }
            }
        }


        boolean validMutualExclusion = false;
        if (otherDirectionsBCount == 2 && externalDepartmentCount == 0 && erasmusCount == 0) validMutualExclusion = true;
        if (otherDirectionsBCount == 0 && externalDepartmentCount == 2 && erasmusCount == 0) validMutualExclusion = true;
        if (otherDirectionsBCount == 0 && externalDepartmentCount == 0 && erasmusCount == 2) validMutualExclusion = true;

        System.out.println("DEBUG [Scenario 1]: MainA=" + mainA + "/5, MainB=" + mainB + "/5, OtherDirectionsA=" + otherA + "/5 (Dirs: " + otherDirectionsSet.size() + ")");
        System.out.println("DEBUG [3ο Tab]: Άλλες_Β=" + otherDirectionsBCount + ", ΓΠ_Άλλα_Τμήματα=" + externalDepartmentCount + ", Erasmus=" + erasmusCount + " -> Έγκυρο Group 3: " + validMutualExclusion);


        return mainA == 5 && mainB == 5 && otherA == 5 && otherDirectionsSet.size() <= 3 && validMutualExclusion;
    }

    private boolean validateScenario2() {
        if (selectedCourses.size() != 17) return false;

        int main1A = 0, main1B = 0;
        int main2A = 0, main2B = 0;
        int extraCount = 0;
        Set<String> extraDirectionsSet = new HashSet<>();

        for (Course c : selectedCourses) {
            String id = c.getCourseID().toUpperCase();
            String dirs = (c.getDirections() != null) ? c.getDirections().toUpperCase() : "";


            boolean isExternal = id.contains("_ΓΠ") || id.equals("CEID_E90E") || id.equals("CEID_AE2") || id.equals("CEID_ΔΕΖ") || id.contains("_ERA");

            if (isExternal) {
                extraCount++;
                continue;
            }

            boolean isMain1 = c.belongsToDirection(activeDirection1);
            boolean isMain2 = c.belongsToDirection(activeDirection2);

            if (isMain1) {
                if (c.isGroupAForDirection(activeDirection1)) main1A++;
                if (c.isGroupBForDirection(activeDirection1)) main1B++;
            }
            if (isMain2) {
                if (c.isGroupAForDirection(activeDirection2)) main2A++;
                if (c.isGroupBForDirection(activeDirection2)) main2B++;
            }

            if (!isMain1 && !isMain2) {
                extraCount++;
                if (!dirs.isEmpty()) {
                    String[] parts = dirs.split(",");
                    for (String part : parts) {
                        extraDirectionsSet.add(part.trim().split(":")[0].trim().toUpperCase().replace('Κ', 'K'));
                    }
                }
            }
        }

        System.out.println("DEBUG [Scenario 2]: Main1A=" + main1A + "/5, Main1B=" + main1B + "/2 | Main2A=" + main2A + "/5, Main2B=" + main2B + "/2 | Extra=" + extraCount + "/3");
        return main1A == 5 && main1B == 2 && main2A == 5 && main2B == 2 && extraCount == 3 && extraDirectionsSet.size() <= 2;
    }

    private boolean validateScenario3() {
        if (selectedCourses.size() != 17) return false;

        int groupAcount = 0;
        int groupBcount = 0;
        int externalCount = 0;

        Set<String> directionsWithGroupA = new HashSet<>();
        Set<String> directionsWithGroupB = new HashSet<>();

        for (Course c : selectedCourses) {
            String id = c.getCourseID().toUpperCase();
            String dirs = (c.getDirections() != null) ? c.getDirections().toUpperCase() : "";

            // Ανίχνευση εξωτερικών μαθημάτων από το PDF
            if (id.contains("_ΓΠ") || id.equals("CEID_E90E") || id.equals("CEID_AE2") || id.equals("CEID_ΔΕΖ") || id.contains("_ERA")) {
                externalCount++;
                continue;
            }

            if (!dirs.isEmpty()) {
                String[] parts = dirs.split(",");
                for (String part : parts) {
                    String cleanPart = part.trim().toUpperCase().replace('Κ', 'K');
                    String dirCode = cleanPart.split(":")[0].trim();

                    if (cleanPart.contains(":A")) {
                        groupAcount++;
                        directionsWithGroupA.add(dirCode);
                    }
                    if (cleanPart.contains(":B")) {
                        groupBcount++;
                        directionsWithGroupB.add(dirCode);
                    }
                }
            }
        }

        System.out.println("DEBUG [Scenario 3]: GroupA=" + groupAcount + " (Dirs: " + directionsWithGroupA.size() + "), GroupB=" + groupBcount + " (Dirs: " + directionsWithGroupB.size() + "), External=" + externalCount);
        return groupAcount >= 10 && directionsWithGroupA.size() >= 4 && (groupBcount + externalCount) >= 7 && directionsWithGroupB.size() >= 4 && externalCount <= 2;
    }

    private Set<String> parseDirections(String raw) {
        Set<String> dirs = new HashSet<>();
        if (raw == null || raw.isEmpty()) return dirs;

        String[] parts = raw.split(",");
        for (String p : parts) {
            String[] pair = p.trim().split(":");
            if (pair.length > 0) {
                String dir = pair[0].trim().toUpperCase().replace('Κ', 'K');
                if (!dir.isEmpty()) dirs.add(dir);
            }
        }
        return dirs;
    }

    public void setDirectionContext(String k1, String k2) {
        this.activeDirection1 = k1;
        this.activeDirection2 = k2;
    }

    public List<Scenario> queryGetScenarios() {
        return scenarioRepository.queryGetScenarios();
    }

    public List<Course> queryGetCourses() {
        return courseRepository.queryGetCourses();
    }

    public List<Integer> queryGetFinalizedScenarioIds(int studentId) {
        List<Integer> completedIds = new ArrayList<>();
        String sql = "SELECT scenarioId FROM StudyPlan WHERE studentId = ? AND isFinalized = 1";

        try (java.sql.Connection conn = com.unipath.dataBase.DBManager.getInstance().connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    completedIds.add(rs.getInt("scenarioId"));
                }
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Σφάλμα στο queryGetFinalizedScenarioIds: " + e.getMessage());
        }
        return completedIds;
    }
}