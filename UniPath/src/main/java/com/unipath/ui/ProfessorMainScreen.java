package com.unipath.ui.UC10;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProfessorMainScreen implements Initializable {

    @FXML private Label professorNameLabel;
    @FXML private Label coursesCountLabel;
    @FXML private VBox coursesContainer;

    // Mock δεδομένα προς το παρόν
    private static class CourseData {
        String title, meta;
        boolean isActive;
        CourseData(String title, String meta, boolean isActive) {
            this.title = title; this.meta = meta; this.isActive = isActive;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadProfessorName();
        loadCourses();
    }

    private void loadProfessorName() {
        // TODO: αντικατάστασε με δεδομένα από βάση
        professorNameLabel.setText("Prof. Ioannis Georgiou");
    }

    private void loadCourses() {
        // TODO: αντικατάστασε με δεδομένα από CourseRepository
        List<CourseData> courses = List.of(
                new CourseData("Μηχανική Μάθηση", "5 ECTS · Εξάμηνο 7 · Ομάδα Α", true),
                new CourseData("Κυβερνοασφάλεια", "5 ECTS · Εξάμηνο 8 · Ομάδα Α", true),
                new CourseData("Επιστημονικός Υπολογισμός", "5 ECTS · Εξάμηνο 8 · Ομάδα Α", false)
        );

        coursesCountLabel.setText("Ανατεθειμένα Μαθήματα : " + courses.size());
        coursesContainer.getChildren().clear();

        for (CourseData course : courses) {
            coursesContainer.getChildren().add(createCourseCard(course));
        }
    }

    private HBox createCourseCard(CourseData course) {
        HBox card = new HBox(10);
        card.getStyleClass().add("prof-course-card");

        VBox info = new VBox(4);
        Label title = new Label(course.title);
        title.getStyleClass().add("prof-course-name");
        Label meta = new Label(course.meta);
        meta.getStyleClass().add("prof-course-meta");
        info.getChildren().addAll(title, meta);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label badge = new Label(course.isActive ? "Ενεργό" : "Ανενεργό");
        badge.getStyleClass().add(course.isActive ? "badge-active" : "badge-inactive");

        Button editBtn = new Button("Επεξεργασία");
        editBtn.getStyleClass().add("prof-edit-btn");
        editBtn.setOnAction(e -> onEditCourse(course.title));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(info, badge, editBtn);
        return card;
    }

    @FXML
    private void onThesisButtonClick() {
        System.out.println("Άνοιγμα φόρμας διπλωματικής...");
        // TODO: άνοιγμα ThesisFormScreen
    }

    @FXML
    private void onCalendarButtonClick() {
        System.out.println("Άνοιγμα ημερολογίου...");
        // TODO: άνοιγμα MeetingCalendarScreen
    }

    private void onEditCourse(String courseTitle) {
        System.out.println("Επεξεργασία μαθήματος: " + courseTitle);
        // TODO: άνοιγμα οθόνης επεξεργασίας μαθήματος (UC9)
    }
}