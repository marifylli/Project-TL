package com.unipath.ui.UC5;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;

public class ErrorScreen {

    public enum ErrorType {
        DUPLICATE_OR_EMPTY,
        MAX_TEACHING_LOAD
    }

    public void display() {
        display(ErrorType.DUPLICATE_OR_EMPTY);
    }

    public void display(ErrorType type) {
        String message = resolveMessage(type);
        showError(message);
    }

    private String resolveMessage(ErrorType type) {
        return switch (type) {
            case DUPLICATE_OR_EMPTY ->
                    "Τα στοιχεία του μαθήματος είναι ελλιπή ή το μάθημα υπάρχει ήδη στο σύστημα. " +
                            "Παρακαλώ ελέγξτε τα στοιχεία και δοκιμάστε ξανά.";
            case MAX_TEACHING_LOAD ->
                    "Ο επιλεγμένος καθηγητής έχει συμπληρώσει τον μέγιστο επιτρεπτό διδακτικό φόρτο (3 μαθήματα). " +
                            "Παρακαλώ επιλέξτε διαφορετικό διδάσκοντα.";
        };
    }

    private void showError(String message) {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/common/error-window-view.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/common/error-window-view.fxml");
            }
            if (fxmlUrl == null) {
                System.err.println(" Σφάλμα: Δεν βρέθηκε το error-window-view.fxml!");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // Χρησιμοποιούμε το fully qualified name για να αποφύγουμε σύγκρουση
            com.unipath.ui.common.ErrorScreen controller = loader.getController();
            controller.setErrorMessage(message);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("UniPath - Σφάλμα");
            stage.showAndWait();

        } catch (Exception e) {
            System.err.println(" Απέτυχε η φόρτωση της οθόνης σφάλματος:");
            e.printStackTrace();
        }
    }
}