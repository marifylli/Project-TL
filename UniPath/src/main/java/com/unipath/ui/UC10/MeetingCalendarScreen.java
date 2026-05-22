package com.unipath.ui.UC10;

import com.unipath.controller.ManageThesisClass;
import com.unipath.model.Thesis;
import com.unipath.login.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;


public class MeetingCalendarScreen {

    @FXML private ComboBox<String> dayComboBox;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private ListView<String> slotListView;

    private final ManageThesisClass manager = new ManageThesisClass();
    private final ObservableList<String> slots = FXCollections.observableArrayList();
    private final List<String[]> temporarySlotsData = new ArrayList<>();

    private Thesis thesisToPublish;
    private Stage formStageReference;

    @FXML
    public void initialize() {
        dayComboBox.setItems(FXCollections.observableArrayList("Δευτέρα", "Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή"));
        slotListView.setItems(slots);
    }

    public void setThesisContext(Thesis thesis, Stage formStage) {
        this.thesisToPublish = thesis;
        this.formStageReference = formStage;
    }

    // Βήμα 7: setAvailability()
    @FXML
    private void setAvailability() {
        if (dayComboBox.getValue() == null || startTimeField.getText().isEmpty() || endTimeField.getText().isEmpty()) return;

        String slot = dayComboBox.getValue() + " " + startTimeField.getText() + " - " + endTimeField.getText();
        slots.add(slot);
        temporarySlotsData.add(new String[]{dayComboBox.getValue(), startTimeField.getText(), endTimeField.getText()});
        startTimeField.clear();
        endTimeField.clear();
    }

    // Βήμα 8: publishThesis() -> alt1 [allFields]
    // Βήμα 8: publishThesis() -> alt1 [allFields]
    @FXML
    private void publishThesis() {
        // 1. Παίρνουμε το ενεργό ID ως int απευθείας από το Session!
        int activeProfessorId = UserSession.getInstance().getUserId();

        System.out.println("[Session] Ενεργό Professor ID από το Login: " + activeProfessorId);

        // 2. Βήμα 10: setAvailableSlots() στον Controller
        for (String[] slot : temporarySlotsData) {
            manager.setAvailableSlots(activeProfessorId, slot[0], slot[1], slot[2]);
        }

        // 3. Βήμα 11: saveNewThesis() στον Controller
        boolean success = manager.saveNewThesis(
                activeProfessorId,
                thesisToPublish.getTitle(),
                thesisToPublish.getDescription(),
                thesisToPublish.getPrerequisites(),
                thesisToPublish.getRequiredECTS(),
                thesisToPublish.getMaxCandidates(),
                thesisToPublish.getRequiredSkills()
        );

        if (success) {
            if (formStageReference != null) formStageReference.close();

            if (slotListView.getScene() != null && slotListView.getScene().getWindow() != null) {
                ((Stage) slotListView.getScene().getWindow()).close();
            }

            // Βήμα 12: Εμφάνιση του Pop-up μέσω του Controller
            displaySuccess();
        }
    }


    private void displaySuccess() {
        // Καλεί τον κεντρικό Controller να δείξει το παράθυρο και να κάνει το navigation
        manager.displaySuccessScreen();
    }
}