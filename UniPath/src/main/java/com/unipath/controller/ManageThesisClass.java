package com.unipath.controller;

import com.unipath.dataBase.DBManager;
import com.unipath.model.Calendar;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ManageThesisClass {

    public ManageThesisClass() {}


    public void requestCalendar() {

        Calendar calendar = Calendar.requestCalendar(41); // test professorId

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Professor/meeting-calendar-view.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Ημερολόγιο Συναντήσεων");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validateFields(String title, String ects, String maxCandidates) {
        if (title == null || title.isEmpty()) return false;
        try {
            Integer.parseInt(ects);
            Integer.parseInt(maxCandidates);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}