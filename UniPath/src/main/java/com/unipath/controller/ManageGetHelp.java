package com.unipath.controller;

import com.unipath.model.HelpOffer;
import com.unipath.model.StudentProfile;
import com.unipath.model.StudyPlan;
import com.unipath.repository.HelpOfferRepository;
import java.util.List;
import java.util.Collections;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javafx.scene.control.Alert;

public class ManageGetHelp {

    private final HelpOfferRepository helpOfferRepository;
    private HelpOffer selectedOffer;

    // Constructor
    public ManageGetHelp() {
        this.helpOfferRepository = new HelpOfferRepository();
    }


    public StudyPlan getStudyPlan(StudentProfile profile) {
        System.out.println("[Controller-UC8] 1. Κλήση getStudyPlan() προς το StudentProfile.");
        return profile.getStudyPlan();
    }


    public List<HelpOffer> getAvailableOffers(String courseId) {
        System.out.println("[Controller-UC8] 2. Κλήση getAvailableOffers() για το μάθημα: " + courseId);
        if (courseId == null || courseId.isBlank()) {
            return Collections.emptyList();
        }


        List<HelpOffer> offers = helpOfferRepository.getActiveOffers(courseId);
        System.out.println("[Controller-UC8] Επιστροφή λίστας προσφορών. Βρέθηκαν: " + offers.size());
        return offers;
    }


    public HelpOffer getOfferServices(HelpOffer offer) {
        System.out.println("[Controller-UC8] 3. Κλήση getOfferServices() για την προσφορά ID: " + offer.getOfferId());
        this.selectedOffer = offer;
        return this.selectedOffer; // Επιστρέφει το αντικείμενο με τις παροχές (fileUrl, meetingUrl)
    }


    public void executeAction(Stage stage) {
        System.out.println("[Controller-UC8] 4. Εκτέλεση ενέργειας executeAction().");
        if (this.selectedOffer != null) {
            System.out.println("[Controller-UC8] Εκκίνηση ενέργειας για τον τύπο: " + selectedOffer.getHelpType());


            if (selectedOffer.getNotesFile() != null && !selectedOffer.getNotesFile().isBlank()) {


                File sourceFile = new File(selectedOffer.getNotesFile());


                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Αποθήκευση Σημειώσεων");
                fileChooser.setInitialFileName(sourceFile.getName());


                String extension = "";
                int i = sourceFile.getName().lastIndexOf('.');
                if (i > 0) { extension = sourceFile.getName().substring(i+1); }
                if (!extension.isEmpty()) {
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extension.toUpperCase() + " Files", "*." + extension));
                }


                File destinationFile = fileChooser.showSaveDialog(stage);

                if (destinationFile != null) {
                    try {

                        Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);


                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Επιτυχία Λήψης");
                        alert.setHeaderText(null);
                        alert.setContentText("Το αρχείο αποθηκεύτηκε επιτυχώς στο:\n" + destinationFile.getAbsolutePath());
                        alert.showAndWait();

                    } catch (IOException e) {
                        System.err.println("Σφάλμα κατά την αντιγραφή του αρχείου: " + e.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Σφάλμα");
                        alert.setHeaderText("Αποτυχία λήψης αρχείου");
                        alert.setContentText("Δεν ήταν δυνατή η αποθήκευση. Ελέγξτε αν το αρχείο πηγής υπάρχει: " + selectedOffer.getNotesFile());
                        alert.showAndWait();
                    }
                }
            }

            if (selectedOffer.getMeetingUrl() != null) {
                System.out.println("[Controller-UC8] Ανακατεύθυνση στο meeting URL: " + selectedOffer.getMeetingUrl());

            }
        }
    }


    public void finalizeHelpSession(StudentProfile profile) {
        if (this.selectedOffer == null) return;

        System.out.println("[Controller-UC8] Έναρξη ενημέρωσης προφίλ μετά τη λήψη βοήθειας.");


        profile.updateLogHistory("Έλαβε ακαδημαϊκή βοήθεια για το μάθημα " +
                selectedOffer.getCourseId() +
                " από τον Mentor ID: " + selectedOffer.getMentorId());
        System.out.println("[Controller-UC8] 5. Εκτελέστηκε η updateLogHistory() στο StudentProfile.");


        profile.updateprofile();
        System.out.println("[Controller-UC8] 6. Εκτελέστηκε η updateprofile() στο StudentProfile.");
    }
}