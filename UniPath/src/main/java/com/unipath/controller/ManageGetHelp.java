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
        return this.selectedOffer; // Επιστρέφει το αντικείμενο με τις παροχές (fileUrl, meetingUrl)
    }


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