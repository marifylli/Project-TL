package com.unipath.ui.UC10;

import com.unipath.controller.ManageThesisClass;
import com.unipath.model.AvailabilitySlot;
import com.unipath.model.Calendar;
import com.unipath.model.Thesis;
import com.unipath.ui.common.SuccessScreen;
import com.unipath.login.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
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

    private Thesis thesisToPublish;
    private Stage formStageReference;
    private final List<String[]> temporarySlotsData = new ArrayList<>();

    @FXML
    public void initialize() {
        dayComboBox.setItems(FXCollections.observableArrayList(
                "Δευτέρα", "Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή"
        ));
        slotListView.setItems(slots);
    }

    public void setThesisContext(Thesis thesis, Stage formStage) {
        this.thesisToPublish = thesis;
        this.formStageReference = formStage;
        loadExistingSlots();
    }

    @FXML
    private void onAddSlot() {
        if (dayComboBox.getValue() == null || startTimeField.getText().isEmpty() || endTimeField.getText().isEmpty()) {
            errorLabel.setText("Συμπληρώστε όλα τα πεδία του ραντεβού!");
            return;
        }
        String slot = dayComboBox.getValue() + " " + startTimeField.getText() + " - " + endTimeField.getText();
        slots.add(slot);
        temporarySlotsData.add(new String[]{dayComboBox.getValue(), startTimeField.getText(), endTimeField.getText()});

        startTimeField.clear();
        endTimeField.clear();
    }

    @FXML
    private void onPublishThesisClick() {
        if (thesisToPublish == null) {
            errorLabel.setText("Σφάλμα: Δεν βρέθηκαν δεδομένα διπλωματικής!");
            return;
        }

        int activeProfessorId = UserSession.getInstance().getUserId();

        // Βήμα 8: Αποθήκευση slots
        for (String[] slot : temporarySlotsData) {
            controller.setAvailability(activeProfessorId, slot[0], slot[1], slot[2]);
        }

        // Βήμα 10: Αποθήκευση αγγελίας
        boolean success = controller.publishThesis(
                activeProfessorId,
                thesisToPublish.getTitle(),
                thesisToPublish.getDescription(),
                thesisToPublish.getPrerequisites(),
                thesisToPublish.getRequiredECTS(),
                thesisToPublish.getMaxCandidates(),
                thesisToPublish.getRequiredSkills()
        );

        if (success) {
            // Κλείσιμο των προηγούμενων παραθύρων (Φόρμας & Ημερολογίου)
            if (formStageReference != null) formStageReference.close();
            ((Stage) slotListView.getScene().getWindow()).close();

            // Βήμα 11 & 12: Εμφάνιση επιτυχίας και μετάβαση στην Πινακίδα
            showSuccessAndOpenBoard();
        } else {
            errorLabel.setText("Αποτυχία αποθήκευσης στη Βάση.");
        }
    }

    private void loadExistingSlots() {
        int id = UserSession.getInstance().getUserId();
        Calendar cal = controller.requestCalendar(id);
        if (cal != null && cal.getAvailabilitySlots() != null) {
            for (AvailabilitySlot s : cal.getAvailabilitySlots()) {
                slots.add(s.getDayOfWeek() + " " + s.getStartTime() + " - " + s.getEndTime());
            }
        }
    }

    private void showSuccessAndOpenBoard() {
        try {
            // Βήμα 10: Άνοιγμα Πινακίδας (Θα φτιάξουμε τον controller της σύντομα)
            FXMLLoader boardLoader = new FXMLLoader(getClass().getResource("/fxml/Professor/theses-board-view.fxml"));
            Parent boardRoot = boardLoader.load();
            Stage boardStage = new Stage();
            boardStage.setTitle("Πινακίδα Διπλωματικών Εργασιών");
            boardStage.setScene(new Scene(boardRoot));
            boardStage.show();

            // Βήμα 11: Pop-up Επιτυχίας
            FXMLLoader successLoader = new FXMLLoader(getClass().getResource("/fxml/common/success-window-view.fxml"));
            Parent successRoot = successLoader.load();

            SuccessScreen successScreen = successLoader.getController();
            successScreen.setSuccessMessage("Η αγγελία δημοσιεύτηκε επιτυχώς!");

            Stage successStage = new Stage();
            successStage.initModality(Modality.APPLICATION_MODAL);
            successStage.setTitle("Επιβεβαίωση");
            successStage.setScene(new Scene(successRoot));
            successStage.showAndWait(); // Το βήμα 12 ολοκληρώνεται όταν ο χρήστης κλείσει το παράθυρο

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}