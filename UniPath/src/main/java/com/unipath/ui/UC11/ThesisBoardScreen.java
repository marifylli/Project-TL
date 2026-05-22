package com.unipath.ui.UC11;

import com.unipath.controller.ManageThesisInterest;
import com.unipath.model.Thesis;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;

public class ThesisBoardScreen {
    @FXML private ListView<Thesis> thesisListView;
    private final ManageThesisInterest manager = new ManageThesisInterest();

    @FXML
    public void initialize() {
        // Βήμα 2: Αναζήτηση όλων των θεμάτων
        thesisListView.setItems(FXCollections.observableArrayList(manager.findAllThesis()));
    }
}