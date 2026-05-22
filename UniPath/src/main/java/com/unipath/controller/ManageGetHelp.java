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

    // Constructor που αρχικοποιεί το Repository της ομάδας
    public ManageGetHelp() {
        this.helpOfferRepository = new HelpOfferRepository();
    }

    /**
     * Βήμα 1 (SD8): getStudyPlan() -> Ανακτά το πλάνο σπουδών από το StudentProfile
     */
    public StudyPlan getStudyPlan(StudentProfile profile) {
        System.out.println("[Controller-UC8] 1. Κλήση getStudyPlan() προς το StudentProfile.");
        return profile.getStudyPlan();
    }

    /**
     * Βήμα 2 (SD8): getAvailableOffers() , Αναζητά τις ενεργές προσφορές για ένα μάθημα
     */
    public List<HelpOffer> getAvailableOffers(String courseId) {
        System.out.println("[Controller-UC8] 2. Κλήση getAvailableOffers() για το μάθημα: " + courseId);
        if (courseId == null || courseId.isBlank()) {
            return Collections.emptyList();
        }

        // Χρησιμοποιούμε τη μέθοδο getActiveOffers που έχει ήδη το Repository της ομάδας
        List<HelpOffer> offers = helpOfferRepository.getActiveOffers(courseId);
        System.out.println("[Controller-UC8] Επιστροφή λίστας προσφορών. Βρέθηκαν: " + offers.size());
        return offers;
    }

    /**
     * Βήμα 3 (SD8): getOfferServices(), Ανάκτηση των παροχών της επιλεγμένης προσφοράς
     */
    public HelpOffer getOfferServices(HelpOffer offer) {
        System.out.println("[Controller-UC8] 3. Κλήση getOfferServices() για την προσφορά ID: " + offer.getOfferId());
        this.selectedOffer = offer;
        return this.selectedOffer; // Επιστρέφει το αντικείμενο με τις παροχές (fileUrl, meetingUrl)
    }

    /**
     * Βήμα 4 (SD8): executeAction() ,Αναδρομική μέθοδος για την εκτέλεση της παροχής
     */
    public void executeAction() {
        System.out.println("[Controller-UC8] 4. Εκτέλεση εσωτερικής ενέργειας executeAction().");
        if (this.selectedOffer != null) {
            System.out.println("[Controller-UC8] Εκκίνηση ενέργειας για τον τύπο: " + selectedOffer.getHelpType());
            if (selectedOffer.getNotesFile() != null) {
                System.out.println("[Controller-UC8] Προσομοίωση download αρχείου: " + selectedOffer.getNotesFile());
            }
            if (selectedOffer.getMeetingUrl() != null) {
                System.out.println("[Controller-UC8] Ανακατεύθυνση στο meeting URL: " + selectedOffer.getMeetingUrl());
            }
        }
    }

    /**
     * Βήματα 5 & 6 (SD8): updateLogHistory() και updateprofile() ,Ενημέρωση του προφίλ του φοιτητή
     */
    public void finalizeHelpSession(StudentProfile profile) {
        if (this.selectedOffer == null) return;

        System.out.println("[Controller-UC8] Έναρξη ενημέρωσης προφίλ μετά τη λήψη βοήθειας.");

        // 5. updateLogHistory() στο StudentProfile
        profile.updateLogHistory("Έλαβε ακαδημαϊκή βοήθεια για το μάθημα " +
                selectedOffer.getCourseId() +
                " από τον Mentor ID: " + selectedOffer.getMentorId());
        System.out.println("[Controller-UC8] 5. Εκτελέστηκε η updateLogHistory() στο StudentProfile.");

        // 6. updateprofile() στο StudentProfile
        profile.updateprofile();
        System.out.println("[Controller-UC8] 6. Εκτελέστηκε η updateprofile() στο StudentProfile.");
    }
}