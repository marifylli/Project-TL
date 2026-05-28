package com.unipath.controller;

import com.unipath.model.HelpOffer;
import com.unipath.model.StudentProfile;
import com.unipath.model.StudyPlan;
import com.unipath.repository.HelpOfferRepository;
import java.util.List;
import java.util.Collections;


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
        return this.selectedOffer;
    }


    public void executeAction(javafx.stage.Stage stage) {
        System.out.println("[Controller-UC8] Εκτέλεση πραγματικής ενέργειας λήψης.");
        if (this.selectedOffer != null && selectedOffer.getNotesFile() != null) {

            java.io.File sourceFile = new java.io.File(selectedOffer.getNotesFile());
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Αποθήκευση Σημειώσεων");
            fileChooser.setInitialFileName(sourceFile.getName().isEmpty() ? "shmeiwseis.txt" : sourceFile.getName());

            java.io.File destinationFile = fileChooser.showSaveDialog(stage);

            if (destinationFile != null) {
                try {

                    if (!sourceFile.exists()) {
                        sourceFile.createNewFile();
                        java.nio.file.Files.writeString(sourceFile.toPath(), "Αυτές είναι οι σημειώσεις του μαθήματος από τον Mentor σας!");
                    }


                    if (destinationFile.exists()) {
                        destinationFile.delete();
                    }


                    java.nio.file.Files.copy(sourceFile.toPath(), destinationFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                    alert.setTitle("Επιτυχία Λήψης");
                    alert.setHeaderText(null);
                    alert.setContentText("Το αρχείο αποθηκεύτηκε επιτυχώς στα Downloads σας!");
                    alert.showAndWait();

                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
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