package com.unipath.ui.common;

import com.unipath.controller.ManageProfCourseEdit;
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

public class ProfessorMainScreen implements Initializable {

    @FXML private Label professorNameLabel;
    @FXML private Label coursesCountLabel;
    @FXML private VBox coursesContainer;

    // Αρχικοποίηση του Controller ως πεδίο της κλάσης
    private final ManageProfCourseEdit manageProfCourseEdit = new ManageProfCourseEdit();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String dynamicName = UserSession.getInstance().getDisplayName();

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
        List<Course> professorCourses = repo.queryGetProfessorCourses(currentProfessorId);

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

        String metaText = course.getECTS() + " ECTS · Εξάμηνο " + course.getSemester();
        Label meta = new Label(metaText);
        meta.getStyleClass().add("prof-course-meta");

        info.getChildren().addAll(title, meta);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label badge = new Label(course.isActive() ? "Ενεργό" : "Ανενεργό");
        badge.getStyleClass().add(course.isActive() ? "badge-active" : "badge-inactive");

        Button editBtn = new Button("Επεξεργασία");
        editBtn.getStyleClass().add("prof-edit-btn");

        // ΒΑΣΙΚΗ ΡΟΗ: Όταν πατηθεί η επεξεργασία, ανοίγει απευθείας η CourseDetailScreen
        editBtn.setOnAction(ActionEvent -> {
            // Δημιουργούμε τον manager για τα πεδία
            com.unipath.controller.ManageProfCourseEdit manager = new com.unipath.controller.ManageProfCourseEdit();

            // Ανοίγουμε την οθόνη λεπτομερειών περνώντας της τον manager και το επιλεγμένο course
            com.unipath.ui.UC9.CourseDetailScreen detailScreen = new com.unipath.ui.UC9.CourseDetailScreen(manager, course);
            detailScreen.displayCourseDetails();
        });
        card.getChildren().addAll(info, badge, editBtn);
        return card;
    }

    @FXML
    private void selectThesisManagement(ActionEvent event) {
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

    // Η ΠΑΛΙΑ ΜΕΘΟΔΟΣ clickMyCourses() ΑΦΑΙΡΕΘΗΚΕ ΠΛΗΡΩΣ
    // καθώς πλέον η επιλογή γίνεται απευθείας από την createCourseCard παραπάνω.
}