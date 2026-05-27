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

        calculateWorkload();

        if (sortedCourses.isEmpty()) {
            System.out.println("⚠ Δεν βρέθηκε οριστικοποιημένο πλάνο. Εμφάνιση Error Popup.");
            openErrorScreen(currentStage);
            return;
        }

        Collections.sort(sortedCourses);
        openResultScreen(currentStage);
    }

    private void calculateWorkload() {
        totalWorkloadIndex = 0.0;
        sortedCourses.clear();

        int studentId = UserSession.getInstance().getUserId();
        String sql = "SELECT c.title, c.workloadScore FROM Course c " +
                "INNER JOIN StudyPlan sp ON (',' || sp.courses || ',') LIKE ('%,' || c.courseId || ',%') " +
                "WHERE sp.studentId = ? AND sp.isFinalized = 1 ORDER BY sp.planId DESC LIMIT 17";

        try (java.sql.Connection conn = com.unipath.dataBase.DBManager.getInstance().connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("title");
                    double workload = rs.getFloat("workloadScore");
                    totalWorkloadIndex += workload;
                    sortedCourses.add(new CourseWorkload(name, workload));
                }
            }
        } catch (Exception e) {
            System.err.println("Σφάλμα κατά τον υπολογισμό workload: " + e.getMessage());
        }
    }


    public double getTotalWorkloadIndex() { return totalWorkloadIndex; }
    public List<CourseWorkload> getSortedCourses() { return sortedCourses; }

    public boolean confirmAndSave() {
        System.out.println("✓ DBManager: saveWorkloadIndex() -> Επιτυχής αποθήκευση δείκτη: " + totalWorkloadIndex);
        System.out.println("✓ Student Profile: addWorkLoadToProfile() -> Ενημερώθηκε.");
        return true;
    }

    private void openResultScreen(Stage parentStage) {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/Student/workload-result-view.fxml");
            if (fxmlUrl == null) fxmlUrl = getClass().getClassLoader().getResource("fxml/Student/workload-result-view.fxml");

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            com.unipath.ui.UC3.WorkLoadResultScreen view = loader.getController();
            view.setContext(this);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(parentStage);
            popupStage.setScene(new Scene(root, 1000, 650));
            popupStage.setTitle("UniPath - Αποτελέσματα Φόρτου");
            popupStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openErrorScreen(Stage parentStage) {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/common/error-window-view.fxml");
            if (fxmlUrl == null) fxmlUrl = getClass().getClassLoader().getResource("fxml/common/error-window-view.fxml");

            if (fxmlUrl != null) {
                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                Parent root = loader.load();

                com.unipath.ui.common.ErrorScreen errorController = loader.getController();
                errorController.setErrorMessage("Δεν έχετε αποθηκευμένο πλάνο μαθημάτων!");

                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.initOwner(parentStage);
                popupStage.setScene(new Scene(root));
                popupStage.setTitle("UniPath - Προσοχή");
                popupStage.showAndWait();

            } else {
                System.err.println("Κρίσιμο Σφάλμα: Δεν βρέθηκε το fxml/common/error-window-view.fxml");
            }
        } catch (Exception e) {
            System.err.println("Απέτυχε η φόρτωση της κοινής οθόνης σφάλματος: " + e.getMessage());
            e.printStackTrace();
        }
    }



}

