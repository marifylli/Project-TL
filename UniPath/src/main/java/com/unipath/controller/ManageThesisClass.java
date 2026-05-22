package com.unipath.controller;

import com.unipath.model.*;
import com.unipath.repository.ThesisRepository;
import java.util.Date;
import com.unipath.model.*;
import com.unipath.repository.ThesisRepository;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Date;

public class ManageThesisClass {
    private final ThesisRepository repository = new ThesisRepository();

    // Βήμα 5: Μήνυμα requestCalendar(pId)
    public Calendar requestCalendar(int pId) {
        return repository.getCalendarByProfessor(pId);
    }

    // Βήμα 10: Μήνυμα setAvailableSlots(...)
    public boolean setAvailableSlots(int pId, String day, String start, String end) {
        return repository.saveAvailabilitySlot(new AvailabilitySlot(pId, new Date(), day, start, end));
    }

    // Βήμα 11: Μήνυμα saveNewThesis(...)
    public boolean saveNewThesis(int pId, String title, String desc, String pre, int ects, int max, String skills) {
        return repository.saveThesis(new Thesis(pId, title, desc, pre, ects, max, skills));
    }
    public void displaySuccessScreen() {
        try {
            // 1. Φορτώνουμε το κοινό FXML επιτυχίας από το common
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/success-window-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Επιτυχία Δημοσίευσης");
            stage.setScene(new Scene(root));
            stage.show();

            // 2. Βρίσκουμε δυναμικά το κουμπί "Κλείσιμο Μηνύματος" που ανήκει στη SuccessScreen
            javafx.scene.control.Button closeBtn = null;
            for (javafx.scene.Node node : root.lookupAll(".button")) {
                if (node instanceof javafx.scene.control.Button) {
                    javafx.scene.control.Button tempBtn = (javafx.scene.control.Button) node;
                    if (tempBtn.getText().contains("Κλείσιμο")) {
                        closeBtn = tempBtn;
                        break;
                    }
                }
            }

            // 3. Όταν ο χρήστης πατήσει "Κλείσιμο Μηνύματος" στο Pop-up
            if (closeBtn != null) {
                closeBtn.setOnAction(e -> {
                    stage.close(); // Κλείνει το μικρό pop-up

                    // 4. ΑΛΛΑΓΗ ΟΘΟΝΗΣ: Φορτώνουμε την κεντρική οθόνη του καθηγητή
                    try {
                        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/Professor/ProfessorMainScreen.fxml"));
                        Parent mainRoot = mainLoader.load();

                        // Βρίσκουμε το ενεργό κεντρικό παράθυρο της εφαρμογής
                        Stage mainStage = (Stage) javafx.stage.Window.getWindows().stream()
                                .filter(javafx.stage.Window::isShowing)
                                .findFirst()
                                .orElse(null);

                        if (mainStage != null) {
                            mainStage.getScene().setRoot(mainRoot); // Σε "πετάει" στην αρχική οθόνη!
                            System.out.println("[UI] Επιτυχής δημοσίευση UC10. Επιστροφή στην ProfessorMainScreen.");
                        }
                    } catch (IOException ex) {
                        System.err.println("Σφάλμα κατά την επιστροφή στην αρχική οθόνη καθηγητή: " + ex.getMessage());
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα φόρτωσης του success-window-view.fxml: " + e.getMessage());
        }
    }
}