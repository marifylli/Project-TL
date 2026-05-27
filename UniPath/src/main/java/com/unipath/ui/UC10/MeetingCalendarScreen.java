package com.unipath.ui.UC10;

import com.unipath.controller.ManageThesisClass;
import com.unipath.model.AvailabilitySlot;
import com.unipath.model.Thesis;
import com.unipath.repository.ThesisRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label; // Προσθήκη για την αναζήτηση του Label στο pop-up
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MeetingCalendarScreen implements Initializable {

    private final ManageThesisClass manager = new ManageThesisClass();

    @FXML private ComboBox<String> dayComboBox;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private ListView<String> slotListView;

    public MeetingCalendarScreen() {}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (dayComboBox != null) {
            dayComboBox.setItems(FXCollections.observableArrayList(
                    "Δευτέρα", "Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή"
            ));
        }
    }

    @FXML
    private void setAvailability() {
        if (dayComboBox != null && dayComboBox.getValue() != null &&
                startTimeField != null && !startTimeField.getText().trim().isEmpty() &&
                endTimeField != null && !endTimeField.getText().trim().isEmpty()) {

            String newSlot = dayComboBox.getValue() + " | " + startTimeField.getText().trim() + " - " + endTimeField.getText().trim();
            slotListView.getItems().add(newSlot);

            startTimeField.clear();
            endTimeField.clear();
        }
    }

    @FXML
    private void publishThesis() {

        if (slotListView != null) {
            slotListView.setStyle("");
        }

        String startTime = (startTimeField != null) ? startTimeField.getText().trim() : "";
        String endTime = (endTimeField != null) ? endTimeField.getText().trim() : "";

        boolean allFields = (slotListView != null && !slotListView.getItems().isEmpty()) ||
                (!startTime.isEmpty() && !endTime.isEmpty());

        if (allFields) {
            try {
                ThesisRepository repo = new ThesisRepository();

                AvailabilitySlot slot = new AvailabilitySlot();
                repo.setAvailabilitySlot(slot);

                Thesis thesis = new Thesis();
                repo.saveThesis(thesis);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/success-window-view.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("SuccessScreen");
                stage.setScene(new Scene(root));
                stage.show();

                System.out.println("[UC10] 🎉 Επιτυχής αποθήκευση και εμφάνιση SuccessScreen!");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {

            if (slotListView != null) {
                slotListView.setStyle("-fx-border-color: red; -fx-border-width: 1.5px; -fx-border-radius: 4;");
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/error-window-view.fxml"));
                Parent root = loader.load();


                for (javafx.scene.Node node : root.lookupAll("Label")) {
                    if (node instanceof Label && ((Label) node).getText().contains("Μήνυμα Σφάλματος")) {
                        ((Label) node).setText("Δεν έχουν προστεθεί διαθέσιμες ώρες (Slots)\nστο ημερολόγιο συναντήσεων.");
                        break;
                    }
                }

                Stage stage = new Stage();
                stage.setTitle("ErrorScreen");
                stage.setScene(new Scene(root));

                stage.showAndWait();
                System.out.println("[UI] highligthMissingFields(): Εμφανίστηκε η οθόνη σφάλματος.");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}