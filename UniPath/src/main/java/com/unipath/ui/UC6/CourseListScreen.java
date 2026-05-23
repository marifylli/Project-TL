package com.unipath.ui.UC6;

import com.unipath.controller.ManageSecCourEditClass;
import com.unipath.model.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class CourseListScreen {

    @FXML private ListView<String> coursesListView;
    @FXML private TextField searchField;
    @FXML private Label errorLabel;

    private ManageSecCourEditClass controller;
    private List<Course> courses;

    public CourseListScreen() {}

    public CourseListScreen(ManageSecCourEditClass controller, List<Course> courses) {
        this.controller = controller;
        this.courses = courses;
    }

    public void setContext(ManageSecCourEditClass controller, List<Course> courses) {
        this.controller = controller;
        this.courses = courses;
        loadCourses(courses);
    }

    private void loadCourses(List<Course> list) {
        ObservableList<String> items = FXCollections.observableArrayList(
                list.stream()
                        .map(c -> c.getCourseID() + " | " + c.getTitle() + " | " + c.getECTS() + " ECTS")
                        .collect(Collectors.toList())
        );
        coursesListView.setItems(items);
    }

    @FXML
    private void onSearch() {
        String query = searchField.getText().toLowerCase();
        List<Course> filtered = courses.stream()
                .filter(c -> c.getTitle().toLowerCase().contains(query) ||
                        c.getCourseID().toLowerCase().contains(query))
                .collect(Collectors.toList());
        loadCourses(filtered);
    }

    @FXML
    private void onCourseSelected() {
        String selected = coursesListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        String courseId = selected.split("\\|")[0].trim();
        controller.selectCourse(courseId);
    }

    public void display() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Secretary/course-list-view.fxml"));
            loader.setControllerFactory(c -> this);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Επεξεργασία Μαθήματος");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

