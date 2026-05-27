package com.unipath.ui.UC11;

import com.unipath.controller.ManageThesisInterest;
import com.unipath.model.AvailabilitySlot;
import com.unipath.model.Thesis;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class StudentMeetingCalendarScreen {

    private final ManageThesisInterest manager = new ManageThesisInterest();

    @FXML private ComboBox<String> timeSlotComboBox;
    @FXML private ListView<String> availableSlotsListView;

    private Thesis selectedThesis;
    private List<AvailabilitySlot> slots;
    private Stage currentStage;

    public StudentMeetingCalendarScreen() {}

    @FXML
    public void selectConfirmAppointment() {

        String selectedSlot = availableSlotsListView.getSelectionModel().getSelectedItem();

        if (selectedSlot == null) {
            System.out.println("Εναλλακτική ροή: Δεν επιλέχθηκε slot.");


            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Σφάλμα Επιλογής");
            alert.setHeaderText(null);
            alert.setContentText("Παρακαλώ επιλέξτε μια διαθέσιμη ώρα από τη λίστα για να προχωρήσετε.");
            alert.showAndWait();

            return;
        }

        try {
            manager.selectConfirmApointment();
            System.out.println(" Η ειδοποίηση στάλθηκε στον καθηγητή!");


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ειδοποίηση Συστήματος");
            alert.setHeaderText(null);
            alert.setContentText("Η επιλογή ώρας καταχωρήθηκε και η ειδοποίηση στάλθηκε επιτυχώς!");
            alert.showAndWait();

        } catch (Exception e) {
            System.out.println(" Σφάλμα κατά την καταχώρηση: " + e.getMessage());
        }
    }

    public void setCalendarContext(Thesis selectedThesis, List<AvailabilitySlot> slots, Stage currentStage) {
        this.selectedThesis = selectedThesis;
        this.slots = slots;
        this.currentStage = currentStage;

        if (slots != null) {
            for (AvailabilitySlot slot : slots) {
                availableSlotsListView.getItems().add(slot.getDayOfWeek() + " " + slot.getStartTime());
            }
        }
    }
}