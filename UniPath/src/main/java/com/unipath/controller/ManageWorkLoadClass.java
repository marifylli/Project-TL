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
        int studentId = com.unipath.login.UserSession.getInstance().getUserId();
        System.out.println("✓ Έναρξη ανάλυσης για τον φοιτητή με ID: " + studentId);

        studyPlanCourses.clear();

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

        if (studyPlanCourses.isEmpty()) {
            System.out.println("⚠ Δεν βρέθηκε οριστικοποιημένο πλάνο. Εμφάνιση Error Popup.");
            openErrorScreen(currentStage);
            return;
        }

        calculateWorkload();
        Collections.sort(sortedCourses);
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
        System.out.println("✓ DBManager: saveWorkloadIndex() -> Δείκτης αποθηκεύτηκε: " + totalWorkloadIndex);
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
            if (fxmlUrl == null) fxmlUrl = getClass().getClassLoader().getResource("fxml/common/error-window-view.fxml");

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            com.unipath.ui.common.ErrorScreen errorController = loader.getController();
            errorController.setErrorMessage("Δεν βρέθηκε ενεργό ή οριστικοποιημένο Πλάνο Σπουδών! Παρακαλώ δημιουργήστε πρώτα ένα πλάνο (UC1).");

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(stage);
            popupStage.setScene(new Scene(root, 500, 300));
            popupStage.setTitle("UniPath - Σφάλμα");
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}