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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
        loadExistingSlots();
    }

    public void setThesisContext(Thesis thesis, Stage formStage) {
        this.thesisToPublish = thesis;
        this.formStageReference = formStage;
    }

    @FXML
    private void onAddSlot() {
        errorLabel.setText("");
        if (dayComboBox.getValue() == null || startTimeField.getText().isBlank() || endTimeField.getText().isBlank()) {
            errorLabel.setText("Συμπληρώστε όλα τα στοιχεία του ραντεβού!");
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
            errorLabel.setText("Πρέπει να ορίσετε τουλάχιστον ένα slot συναντήσεων!");
            return;
        }

        if (!controller.validateFields(thesisToPublish.getTitle(), thesisToPublish.getDescription(), "Valid", "Valid")) {
            errorLabel.setText("Σφάλμα επικύρωσης δεδομένων.");
            return;
        }

        int activeProfessorId = UserSession.getInstance().getUserId();

        boolean slotsSaved = true;
        for (String[] slotData : temporarySlotsData) {
            boolean success = controller.setAvailability(activeProfessorId, slotData[0], slotData[1], slotData[2]);
            if (!success) slotsSaved = false;
        }

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
            if (formStageReference != null) formStageReference.close();
            ((Stage) slotListView.getScene().getWindow()).close();

            showSuccessAndOpenBoard();
        } else {
            errorLabel.setText("Σφάλμα κατά την εγγραφή στη Βάση Δεδομένων.");
        }
    }

    private void loadExistingSlots() {
        int activeProfessorId = UserSession.getInstance().getUserId();

        Calendar calendar = controller.requestCalendar(activeProfessorId);
        if (calendar != null && calendar.getAvailabilitySlots() != null) {
            for (AvailabilitySlot slot : calendar.getAvailabilitySlots()) {
                slots.add(slot.getDayOfWeek() + " " + slot.getStartTime() + " - " + slot.getEndTime());
            }
        }
    }

    private void showSuccessAndOpenBoard() {
        try {
            FXMLLoader boardLoader = new FXMLLoader(getClass().getResource("/fxml/Professor/theses-board-view.fxml"));
            Parent boardRoot = boardLoader.load();
            Stage boardStage = new Stage();
            boardStage.setTitle("Πινακίδα Διπλωματικών Εργασιών");
            boardStage.setScene(new Scene(boardRoot));
            boardStage.show();

            FXMLLoader successLoader = new FXMLLoader(getClass().getResource("/fxml/common/success-window-view.fxml"));
            Parent successRoot = successLoader.load();

            SuccessScreen successScreen = successLoader.getController();
            successScreen.setSuccessMessage("Η νέα Αγγελία Διπλωματικής και το Ημερολόγιο αποθηκεύτηκαν επιτυχώς!");

            Stage successStage = new Stage();
            successStage.setTitle("Επιβεβαίωση");
            successStage.setScene(new Scene(successRoot));
            successStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            successStage.showAndWait();
        } catch (Exception e) {
            System.err.println("Σφάλμα κατά την ολοκλήρωση: " + e.getMessage());
            e.printStackTrace();
        }
    }
}