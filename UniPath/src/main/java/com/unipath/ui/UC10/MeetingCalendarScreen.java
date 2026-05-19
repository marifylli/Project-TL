package com.unipath.ui.UC10;

import com.unipath.controller.ManageThesisClass;
import com.unipath.model.AvailabilitySlot;
import com.unipath.model.Calendar;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MeetingCalendarScreen {

    @FXML private ComboBox<String> dayComboBox;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private ListView<String> slotListView;
    @FXML private Label errorLabel;

    private final ManageThesisClass controller = new ManageThesisClass();
    private final ObservableList<String> slots = FXCollections.observableArrayList();
    private int professorId = 1; // TODO: παίρνει από το login

    @FXML
    public void initialize() {
        dayComboBox.setItems(FXCollections.observableArrayList(
                "Δευτέρα", "Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή"
        ));
        slotListView.setItems(slots);
        loadExistingSlots();
    }

    private void loadExistingSlots() {
        Calendar calendar = controller.requestCalendar(professorId);
        for (AvailabilitySlot slot : calendar.getAvailabilitySlots()) {
            slots.add(slot.getDayOfWeek() + " " +
                    slot.getStartTime() + " - " +
                    slot.getEndTime());
        }
    }

    @FXML
    private void onAddSlot() {
        errorLabel.setText("");

        if (dayComboBox.getValue() == null) {
            errorLabel.setText("Επέλεξε ημέρα!");
            return;
        }
        if (startTimeField.getText().isBlank() || endTimeField.getText().isBlank()) {
            errorLabel.setText("Συμπλήρωσε ώρα έναρξης και λήξης!");
            return;
        }

        boolean success = controller.setAvailability(
                professorId,
                dayComboBox.getValue(),
                startTimeField.getText().trim(),
                endTimeField.getText().trim()
        );

        if (success) {
            slots.add(dayComboBox.getValue() + " " +
                    startTimeField.getText() + " - " +
                    endTimeField.getText());
            startTimeField.clear();
            endTimeField.clear();
            dayComboBox.setValue(null);
        } else {
            errorLabel.setText("Σφάλμα αποθήκευσης slot!");
        }
    }

    @FXML
    private void onConfirm() {
        if (slots.isEmpty()) {
            errorLabel.setText("Πρέπει να προσθέσεις τουλάχιστον ένα slot!");
            return;
        }
        javafx.stage.Stage stage =
                (javafx.stage.Stage) slotListView.getScene().getWindow();
        stage.close();
    }
}