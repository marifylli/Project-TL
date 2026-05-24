package com.unipath.ui.UC11;

import javafx.fxml.FXML;
import javafx.scene.control.TableView; // ΠΡΟΣΘΕΣΕ ΑΥΤΟ
import com.unipath.controller.ManageThesisInterest;

public class ThesisBoardScreen {

 
    @FXML private ListView<String> thesisListView;

    private final ManageThesisInterest manager = new ManageThesisInterest();

    @FXML
    public void initialize() {
        if (thesisTableView != null) {
            // ... κώδικας ...
        } else {
            System.err.println("❌ Το thesisTableView είναι null!");
        }
    }

    @FXML
    public void handleBookAppointment() {
        System.out.println("Κλείσιμο ραντεβού...");
    }
}
