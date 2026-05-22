package com.unipath.ui.UC2;

import controller.ManageEvaluation;
import java.util.List;

public class CoursesToEvaluateScreen {
    private Object stage; // Χρησιμοποιούμε Object για να μην χτυπάει το Stage
    private ManageEvaluation controller;

    public CoursesToEvaluateScreen(Object stage, ManageEvaluation controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void display() {
        // Χρήση Fully Qualified Names για τα Controls
        javafx.scene.control.Label titleLabel = new javafx.scene.control.Label("UniPath - Μαθήματα προς Αξιολόγηση");
        titleLabel.setLayoutX(20);
        titleLabel.setLayoutY(20);

        javafx.scene.control.ListView<String> coursesListView = new javafx.scene.control.ListView<>();
        coursesListView.setLayoutX(20);
        coursesListView.setLayoutY(50);
        coursesListView.setPrefSize(360, 250);

        // Βήμα 2 (sd2): queryCoursesAttended() [cite: 87, 88]
        List<String> attendedCourses = controller.queryCoursesAttended();
        coursesListView.getItems().addAll(attendedCourses);

        javafx.scene.control.Button selectButton = new javafx.scene.control.Button("Επιλογή Μαθήματος");
        selectButton.setLayoutX(20);
        selectButton.setLayoutY(320);

        selectButton.setOnAction(e -> {
            String selectedCourse = coursesListView.getSelectionModel().getSelectedItem();
            if (selectedCourse != null) {
                // Βήμα 3 & 4 (sd2): checkEvaluationRight [cite: 90, 103]
                if (controller.checkEvaluationRight(selectedCourse)) {
                    // Βήμα 5 (sd2): Μετάβαση στη φόρμα [cite: 91, 92]
                    EvaluationFormScreen formScreen = new EvaluationFormScreen(stage, controller, selectedCourse);
                    formScreen.display();
                } else {
                    System.out.println("Εναλλακτική Ροή 1: Εμφάνιση ErrorScreen [cite: 104, 105]");
                }
            }
        });

        // Σύνδεση με το Scene (κεντρικό αντικείμενο JavaFX)
        javafx.scene.Group root = new javafx.scene.Group(titleLabel, coursesListView, selectButton);
        javafx.scene.Scene scene = new javafx.scene.Scene(root, 400, 400);

        // Cast του Object πίσω σε Stage κατά το runtime
        ((javafx.stage.Stage) this.stage).setScene(scene);
        ((javafx.stage.Stage) this.stage).show();
    }
}