package com.unipath.ui.UC10;

import com.unipath.controller.ManageThesisClass;
import com.unipath.model.AvailabilitySlot;
import com.unipath.model.Calendar;
import com.unipath.model.Thesis;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class MeetingCalendarScreen {

    @FXML private ComboBox<String> dayComboBox;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private ListView<String> slotListView;
    @FXML private Label errorLabel;

    private final ManageThesisClass controller = new ManageThesisClass();
    private final ObservableList<String> slots = FXCollections.observableArrayList();
    private int professorId = 1;

    private Thesis thesisToPublish;
    private Stage formStageReference;
    private final List<String[]> temporarySlotsData = new ArrayList<>();

    @FXML
    public void initialize() {
        dayComboBox.setItems(FXCollections.observableArrayList(
                "Δευτέρα", "Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή"
        ));
        slotListView.setItems(slots);
        loadExistingSlots();
    }

    public void setThesisContext(Thesis thesis, Stage formStage) {
        this.thesisToPublish = thesis;
        this.formStageReference = formStage;
    }

    private void loadExistingSlots() {
        Calendar calendar = controller.requestCalendar(professorId);
        for (AvailabilitySlot slot : calendar.getAvailabilitySlots()) {
            slots.add(slot.getDayOfWeek() + " " + slot.getStartTime() + " - " + slot.getEndTime());
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
            errorLabel.setText("Συμπληρώστε ώρα έναρξης και λήξης!");
            return;
        }

        temporarySlotsData.add(new String[]{
                dayComboBox.getValue(),
                startTimeField.getText().trim(),
                endTimeField.getText().trim()
        });

        slots.add(dayComboBox.getValue() + " " + startTimeField.getText() + " - " + endTimeField.getText());

        startTimeField.clear();
        endTimeField.clear();
        dayComboBox.setValue(null);
    }

    @FXML
    private void onPublishThesisClick() {
        if (slots.isEmpty()) {
            errorLabel.setText("Πρέπει να προσθέσεις τουλάχιστον ένα slot!");
            return;
        }

        // Κλήση της validateFields με τις 4 κατάλληλες παραμέτρους
        if (!controller.validateFields(thesisToPublish.getTitle(), thesisToPublish.getDescription(),
                thesisToPublish.getPrerequisites(), thesisToPublish.getRequiredSkills())) {
            errorLabel.setText("Σφάλμα επικύρωσης δεδομένων της διπλωματικής.");
            return;
        }

        boolean slotsSaved = true;
        for (String[] slotData : temporarySlotsData) {
            boolean success = controller.setAvailability(professorId, slotData[0], slotData[1], slotData[2]);
            if (!success) slotsSaved = false;
        }

        // Κλήση της publishThesis με τις 7 παραμέτρους
        boolean thesisSaved = controller.publishThesis(
                thesisToPublish.getProfessorId(),
                thesisToPublish.getTitle(),
                thesisToPublish.getDescription(),
                thesisToPublish.getPrerequisites(),
                thesisToPublish.getRequiredECTS(),
                thesisToPublish.getMaxCandidates(),
                thesisToPublish.getRequiredSkills()
        );

        if (slotsSaved && thesisSaved) {
            // Κλήση της μεθόδου χωρίς ορίσματα ( found 0 )
            showSuccessWindow();

            if (formStageReference != null) {
                formStageReference.close();
            }
            ((Stage) slotListView.getScene().getWindow()).close();
        } else {
            errorLabel.setText("Σφάλμα κατά την αποθήκευση στη Βάση Δεδομένων.");
        }
    }

    private void showSuccessWindow() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/fxml/success-window-view.fxml"));
            javafx.scene.Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Οθόνη Επιβεβαίωσης");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            System.err.println("Σφάλμα φόρτωσης οθόνης επιτυχίας: " + e.getMessage());
        }
    }
}