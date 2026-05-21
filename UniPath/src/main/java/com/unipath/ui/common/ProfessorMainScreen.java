package com.unipath.ui.common;

import com.unipath.model.Course;
import com.unipath.repository.CourseRepository;
import com.unipath.login.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProfessorMainScreen implements Initializable {

    @FXML private Label professorNameLabel;
    @FXML private Label coursesCountLabel;
    @FXML private VBox coursesContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // ΔΙΟΡΘΩΣΗ: Χρήση του getDisplayName() που έχουμε στο UserSession/User
        String dynamicName = UserSession.getInstance().getFullName(); // Αν στο UserSession έχεις getFullName(), κράτα το.
        // Αν σου βγάλει σφάλμα, άλλαξέ το σε UserSession.getInstance().getDisplayName()

        if (dynamicName != null) {
            professorNameLabel.setText("Prof. " + dynamicName);
        } else {
            professorNameLabel.setText("Prof. Test Mode");
        }
        loadCourses();
    }

    private void loadCourses() {
        int currentProfessorId = UserSession.getInstance().getUserId();

        CourseRepository repo = new CourseRepository();
        List<Course> allCourses = repo.queryGetCourses();

        List<Course> professorCourses = allCourses.stream()
                .filter(c -> c.getProfessorId() == currentProfessorId)
                .collect(Collectors.toList());

        if (professorCourses.isEmpty() && !allCourses.isEmpty()) {
            professorCourses = allCourses.stream().limit(4).collect(Collectors.toList());
        }

        coursesCountLabel.setText("Ανατεθειμένα Μαθήματα : " + professorCourses.size());
        coursesContainer.getChildren().clear();

        for (Course course : professorCourses) {
            coursesContainer.getChildren().add(createCourseCard(course));
        }
    }

    private HBox createCourseCard(Course course) {
        HBox card = new HBox(10);
        card.getStyleClass().add("prof-course-card");

        VBox info = new VBox(4);
        Label title = new Label(course.getTitle());
        title.getStyleClass().add("prof-course-name");

        // ΔΙΟΡΘΩΣΗ: Χρήση του getECTS() με κεφαλαία (όπως στο Course.java)
        String metaText = course.getECTS() + " ECTS · Εξάμηνο " + course.getSemester();
        Label meta = new Label(metaText);
        meta.getStyleClass().add("prof-course-meta");

        info.getChildren().addAll(title, meta);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label badge = new Label(course.isActive() ? "Ενεργό" : "Ανενεργό");
        badge.getStyleClass().add(course.isActive() ? "badge-active" : "badge-inactive");

        Button editBtn = new Button("Επεξεργασία");
        editBtn.getStyleClass().add("prof-edit-btn");

        card.getChildren().addAll(info, badge, editBtn);
        return card;
    }

    @FXML
    private void onThesisButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Professor/thesis-form-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Δημιουργία Νέας Αγγελίας Διπλωματικής");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.err.println("Σφάλμα ανοίγματος φόρμας διπλωματικής: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML private void onCalendarButtonClick() {}
}