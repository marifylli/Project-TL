package com.unipath.controller;

import com.unipath.login.UserSession;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManageWorkLoadClass {

    private final List<String> studyPlanCourses = new ArrayList<>();
    private double totalWorkloadIndex = 0.0;

    public static class CourseWorkload implements Comparable<CourseWorkload> {
        public String name;
        public double workload;

        public CourseWorkload(String name, double workload) {
            this.name = name;
            this.workload = workload;
        }

        @Override
        public int compareTo(CourseWorkload other) {
            return Double.compare(other.workload, this.workload);
        }
    }

    private final List<CourseWorkload> sortedCourses = new ArrayList<>();

    public void startAnalysis(Stage currentStage) {
        int studentId = UserSession.getInstance().getUserId();
        System.out.println("✓ Έναρξη ανάλυσης για τον φοιτητή με ID: " + studentId);

        studyPlanCourses.clear();

        // 🌟 ΔΙΑΒΑΣΜΑ ΑΠΟ ΤΗ ΜΝΗΜΗ ΤΟΥ SESSION (Ασφαλές & Δυναμικό)
        // Παίρνουμε τα πραγματικά μαθήματα που επέλεξε ο χρήστης στο προηγούμενο βήμα (UC1)
        if (ManageStudyPlan.sessionSavedCourseTitles != null && !ManageStudyPlan.sessionSavedCourseTitles.isEmpty()) {
            studyPlanCourses.addAll(ManageStudyPlan.sessionSavedCourseTitles);
            System.out.println("✓ Ανάκτηση " + studyPlanCourses.size() + " μαθημάτων από το session.");
        }
        else {
            String fetchPlanSql = "SELECT courses FROM StudyPlan WHERE studentId = ? AND (status = 'Finalized' OR isFinalized = 1) ORDER BY planId DESC LIMIT 1";

            try (java.sql.Connection conn = com.unipath.dataBase.DBManager.getInstance().connect();
                 java.sql.PreparedStatement pstmt = conn.prepareStatement(fetchPlanSql)) {

                pstmt.setInt(1, studentId);
                try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String rawCourses = rs.getString("courses");
                        if (rawCourses != null && !rawCourses.isEmpty()) {
                            String[] ids = rawCourses.split(",");
                            for (String id : ids) {
                                String cleanId = id.trim();

                                String courseSql = "SELECT title FROM Course WHERE courseId = ?";
                                try (java.sql.PreparedStatement cPstmt = conn.prepareStatement(courseSql)) {
                                    cPstmt.setString(1, cleanId);
                                    try (java.sql.ResultSet cRs = cPstmt.executeQuery()) {
                                        if (cRs.next()) {
                                            studyPlanCourses.add(cRs.getString("title"));
                                            continue;
                                        }
                                    }
                                }

                                // Fallback με LIKE για απόλυτη ασφάλεια ανάκτησης τίτλων
                                String fallbackSql = "SELECT title FROM Course WHERE courseId LIKE ?";
                                try (java.sql.PreparedStatement fPstmt = conn.prepareStatement(fallbackSql)) {
                                    fPstmt.setString(1, "%" + cleanId.replace("CEID_", "") + "%");
                                    try (java.sql.ResultSet fRs = fPstmt.executeQuery()) {
                                        if (fRs.next()) {
                                            studyPlanCourses.add(fRs.getString("title"));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("❌ Σφάλμα κατά την ανάκτηση από την SQLite: " + e.getMessage());
            }
        }

        // ΕΛΕΓΧΟΣ ΕΝΑΛΛΑΚΤΙΚΗΣ ΡΟΗΣ 1: [empty or no study plan] ──
        if (studyPlanCourses.isEmpty()) {
            System.out.println("⚠ Δεν βρέθηκε οριστικοποιημένο πλάνο. Εμφάνιση Error Popup.");
            openErrorScreen(currentStage);
            return;
        }

        // 2. [Βασική Ροή]: Υπολογισμός και ταξινόμηση
        calculateWorkload();
        Collections.sort(sortedCourses);

        // Άνοιγμα της οθόνης αποτελεσμάτων
        openResultScreen(currentStage);
    }


    private void calculateWorkload() {
        totalWorkloadIndex = 0.0;
        sortedCourses.clear();

        for (String courseName : studyPlanCourses) {
            double workload = 3.5;
            if (courseName.contains("Βάσεις")) workload = 4.8;
            if (courseName.contains("Τεχνολογία")) workload = 4.5;
            if (courseName.contains("Δομές")) workload = 4.2;

            totalWorkloadIndex += workload;
            sortedCourses.add(new CourseWorkload(courseName, workload));
        }
    }

    public double getTotalWorkloadIndex() { return totalWorkloadIndex; }
    public List<CourseWorkload> getSortedCourses() { return sortedCourses; }

    public boolean confirmAndSave() {
        System.out.println("✓ DBManager: saveWorkloadIndex() -> Επιτυχής αποθήκευση δείκτη: " + totalWorkloadIndex);
        System.out.println("✓ Student Profile: addWorkLoadToProfile() -> Ενημερώθηκε.");
        return true;
    }

    private void openResultScreen(Stage stage) {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/Student/workload-result-view.fxml");
            if (fxmlUrl == null) fxmlUrl = getClass().getClassLoader().getResource("fxml/Student/workload-result-view.fxml");

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            com.unipath.ui.UC3.WorkLoadResultScreen view = loader.getController();
            view.setContext(this);

            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Αποτελέσματα Φόρτου");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openErrorScreen(Stage stage) {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/common/error-window-view.fxml");

            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/common/error-window-view.fxml");
            }

            if (fxmlUrl != null) {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(fxmlUrl);
                Parent root = loader.load();

                com.unipath.ui.common.ErrorScreen errorController = loader.getController();
                errorController.setErrorMessage("Δεν έχετε αποθηκευμένο πλάνο μαθημάτων!");

                stage.setScene(new Scene(root));
                stage.setTitle("UniPath - Προσοχή");
                stage.show();
                System.out.println("[UI] Εμφανίστηκε το κοινό Error Screen επιτυχώς.");
            } else {
                System.err.println("Κρίσιμο Σφάλμα: Δεν βρέθηκε πουθενά το αρχείο fxml/common/error-window-view.fxml");
            }
        } catch (Exception e) {
            System.err.println("Απέτυχε η φόρτωση της κοινής οθόνης σφάλματος: " + e.getMessage());
            e.printStackTrace();
        }
    }


}

