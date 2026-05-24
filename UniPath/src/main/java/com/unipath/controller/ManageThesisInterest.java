package com.unipath.controller;

import com.unipath.model.Calendar;
import com.unipath.model.Notification;
import com.unipath.model.AvailabilitySlot;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class ManageThesisInterest {

    public ManageThesisInterest() {}

    public boolean checkAcademicStatus(int studentId, int thesisId) {
        return true;
    }

    /**
     * Ανάκτηση διαθέσιμων ωρών από το Μοντέλο AvailabilitySlot
     */
    public List<AvailabilitySlot> getAvailableSlots(int professorId) {
        return AvailabilitySlot.findAvailableSlots(professorId);
    }

    public void selectConfirmApointment() {

        int studentId = 12;
        int slotId = 5;
        int diplomaticId = 2;
        int professorId = 41;


        Calendar.addEvent(studentId, slotId, diplomaticId);

        Notification.sendNotification(studentId, professorId, diplomaticId);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/success-window-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("SuccessScreen");
            stage.setScene(new Scene(root));
            stage.show();

            System.out.println("[UC11] 🎉 Η ροή του Sequence Diagram ολοκληρώθηκε πιστά!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}