package com.unipath.ui.UC11;

import com.unipath.controller.ManageThesisInterest;
import com.unipath.model.AvailabilitySlot;
import com.unipath.model.Thesis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class StudentMeetingCalendarScreen {

    @FXML private ListView<String> availableSlotsListView;

    private final ObservableList<String> displaySlots = FXCollections.observableArrayList();
    private ManageThesisInterest manager;
    private Thesis thesis;
    private List<AvailabilitySlot> rawSlots;
    private Stage parentStage;

    @FXML
    public void initialize() {
        availableSlotsListView.setItems(displaySlots);
    }

    public void setCalendarContext(ManageThesisInterest manager, Thesis thesis, List<AvailabilitySlot> slots, Stage parentStage) {
        this.manager = manager;
        this.thesis = thesis;
        this.rawSlots = slots;
        this.parentStage = parentStage;

        displaySlots.clear();
        for (AvailabilitySlot slot : slots) {
            displaySlots.add(slot.getDayOfWeek() + " | " + slot.getStartTime() + " - " + slot.getEndTime());
        }
    }

    @FXML
    private void selectConfirmAppointment() {
        int selectedIndex = availableSlotsListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0 || manager == null || thesis == null || rawSlots == null) return;

        AvailabilitySlot selectedSlot = rawSlots.get(selectedIndex);
        int currentStudentId = 41; // test.student1 (st1) από το InsertTestData

        // [Sequence Diagram] 1. selectConfirmAppointment(studentId, professorId, slotId, diplomaticId)
        boolean success = manager.selectConfirmAppointment(
                currentStudentId,
                thesis.getProfessorId(),
                selectedSlot.getSlotId(),
                thesis.getDiplomaticId()
        );

        if (success) {
            System.out.println("[UC11] [Sequence Step]: 1.1 Appointment & Notification Created via Controller/Repositories");

            // [Sequence Diagram] Εμφάνιση του Μηνύματος Κράτησης (showSuccessMessage) στο ίδιο Boundary
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Μήνυμα Κράτησης");
            alert.setHeaderText("🎉 Επιτυχής Προγραμματισμός Συνέντευξης!");
            alert.setContentText("Η κράτησή σας καταχωρήθηκε στη ΒΔ.\n\n" +
                    "Θέμα: " + thesis.getTitle() + "\n" +
                    "Ημέρα/Ώρα: " + selectedSlot.getDayOfWeek() + " " + selectedSlot.getStartTime() + " - " + selectedSlot.getEndTime());

            // Το σύστημα παγώνει μέχρι ο φοιτητής να πατήσει το "OK"
            alert.showAndWait();

            // [Sequence Diagram / Ροή]: Επιστροφή στην Οθόνη Κεντρικής Σελίδας Φοιτητή
            try {
                // Φορτώνουμε την υπάρχουσα Κεντρική Οθόνη Φοιτητή που ήδη έχεις στο project σου
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Student/student-main-screen.fxml"));
                Parent root = loader.load();

                // Αλλάζουμε το root της κεντρικής σκηνής (parentStage) ώστε να μην κλείσει η εφαρμογή
                if (parentStage != null) {
                    parentStage.getScene().setRoot(root);
                }

                // Κλείνουμε μόνο το έξτρα αναδυόμενο παράθυρο του ημερολογίου
                Stage calendarStage = (Stage) availableSlotsListView.getScene().getWindow();
                calendarStage.close();

                System.out.println("[UC11] [Sequence Step]: Επιστροφή στην Οθόνη Κεντρικής Σελίδας Φοιτητή. Η ροή ολοκληρώθηκε πιστά!");

            } catch (IOException e) {
                System.err.println("❌ Σφάλμα κατά την επιστροφή στην Κεντρική Οθόνη: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("❌ Αποτυχία καταχώρησης ραντεβού.");
        }
    }
}