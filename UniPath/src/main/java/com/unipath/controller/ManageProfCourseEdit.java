package com.unipath.controller;

import com.unipath.model.Course;
import com.unipath.repository.CourseRepository;
import com.unipath.ui.UC9.*;
import com.unipath.ui.common.SuccessScreen;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;


public class ManageProfCourseEdit {

    private MyCoursesScreen myCoursesScreen;
    private CourseDetailScreen courseDetailScreen;
    private RulesScreen rulesScreen;
    private CourseRepository courseRepository;

    private Course selectedCourse;
    private int currentProfessorId = 1;

    public ManageProfCourseEdit() {
        this.courseRepository = new CourseRepository();
    }

    public void startMyCourses(){
        List<Course> coursesList = courseRepository.queryGetProfessorCourses(currentProfessorId);

        // alt2: [Has Courses]
        if (coursesList != null && !coursesList.isEmpty()) {
            myCoursesScreen = new MyCoursesScreen(this, coursesList);
            myCoursesScreen.displayProfCourses();
        } else {
            System.out.println("Ο καθηγητής δεν έχει ανατεθειμένα μαθήματα.");
            displayErrorScreen();
        }
    }

    public void onCourseSelected(Course course) {
        this.selectedCourse = course;
        courseDetailScreen = new CourseDetailScreen(this, selectedCourse);
        courseDetailScreen.displayCourseDetails();
    }

    // Όταν πατηθεί το κουμπί επεξεργασίας πεδίων
    public void onEditCourseFields() {
        System.out.println("Επεξεργασία πεδίων για το μάθημα: " + selectedCourse.getTitle());

    }

    // Όταν πατηθεί το "Rules" (clicksRules)
    public void onRulesClicked() {
        rulesScreen = new RulesScreen(this, selectedCourse);
        rulesScreen.display();
    }


    public void onSaveRules(String updatedRules) {
        // Παίρνουμε αυτόματα το username από τη βάση δεδομένων
        String professorUsername = courseRepository.queryGetProfessorUsername(currentProfessorId);

        boolean success = courseRepository.queryUpdateCourseRules(
                selectedCourse.getCourseID(),
                updatedRules,
                professorUsername
        );

        if (success) {
            selectedCourse.setRules(updatedRules);
            diplaySuccessScreen();
        }else {
            displayErrorScreen();
        }

    }

    public void onCancelRules() {
        if (rulesScreen != null) {
            rulesScreen.deleteChanges();
            displayErrorScreen();
        }
    }


    private void diplaySuccessScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unipath/ui/common/SuccessScreen.fxml"));
            Parent root = loader.load();

            SuccessScreen successController = loader.getController();
            successController.setSuccessMessage("Οι κανόνες του μαθήματος αποθηκεύτηκαν επιτυχώς!");

            Stage stage = new Stage();
            stage.setTitle("Επιτυχία");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά τη φόρτωση της SuccessScreen: " + e.getMessage());
        }
    }

    private void displayErrorScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unipath/ui/common/ErrorScreen.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Σφάλμα");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά τη φόρτωση της ErrorScreen: " + e.getMessage());
        }
    }


}
