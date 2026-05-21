package com.unipath.ui.common;

import com.unipath.model.Course;
import com.unipath.repository.CourseRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadProfessorName();
        loadCourses();
    }

    private void loadProfessorName() {
        // Αντικατάστησε με το όνομα του loggedInUser αν το έχεις διαθέσιμο
        professorNameLabel.setText("Prof. Ioannis Georgiou");
    }

    private void loadCourses() {
        CourseRepository repo = new CourseRepository();
        List<Course> allCourses = repo.queryGetCourses();

        coursesCountLabel.setText("Ανατεθειμένα Μαθήματα : " + allCourses.size());
        coursesContainer.getChildren().clear();

        for (Course course : allCourses) {
            coursesContainer.getChildren().add(createCourseCard(course));
        }
    }

    private HBox createCourseCard(Course course) {
        HBox card = new HBox(10);
        card.getStyleClass().add("prof-course-card");

        VBox info = new VBox(4);
        Label title = new Label(course.getTitle());
        title.getStyleClass().add("prof-course-name");

        // Δημιουργία δυναμικού meta text από τα δεδομένα της βάσης
        String metaText = course.getEcts() + " ECTS · Εξάμηνο " + course.getSemester();
        Label meta = new Label(metaText);
        meta.getStyleClass().add("prof-course-meta");

        info.getChildren().addAll(title, meta);
        HBox.setHgrow(info, Priority.ALWAYS);

        // Χρήση της μεθόδου isActive() του μοντέλου Course
        Label badge = new Label(course.isActive() ? "Ενεργό" : "Ανενεργό");
        badge.getStyleClass().add(course.isActive() ? "badge-active" : "badge-inactive");

        Button editBtn = new Button("Επεξεργασία");
        editBtn.getStyleClass().add("prof-edit-btn");
        editBtn.setOnAction(e -> onEditCourse(course.getTitle()));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(info, badge, editBtn);
        return card;
    }

    @FXML
    private void onThesisButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Professor/thesis-form-view.fxml"));
            Parent root = loader.load();

            // Ανοίγουμε το παράθυρο για το UC10
            Stage stage = new Stage();
            stage.setTitle("Δημοσίευση Θέματος Διπλωματικής");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCalendarButtonClick() {
        System.out.println("Άνοιγμα ημερολογίου...");
    }

    private void onEditCourse(String courseTitle) {
        System.out.println("Επεξεργασία μαθήματος: " + courseTitle);
    }
}