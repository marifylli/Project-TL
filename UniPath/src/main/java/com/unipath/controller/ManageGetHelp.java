package com.unipath.controller;

import com.unipath.model.HelpOffer;
import com.unipath.model.StudentProfile;
import com.unipath.repository.HelpOfferRepository;
import java.util.List;

public class ManageGetHelp {

    private final HelpOfferRepository helpOfferRepository;

    // Constructor που αρχικοποιεί το Repository για την αναζήτηση
    public ManageGetHelp() {
        this.helpOfferRepository = new HelpOfferRepository();
    }

    /**
     * Αναζήτηση προσφορών βοήθειας με βάση το ID του μαθήματος (searchOffers στο SD8)
     */
    public List<HelpOffer> searchOffers(String courseId) {
        if (courseId == null || courseId.isBlank()) {
            System.out.println("[Controller-UC8] Άκυρο ID μαθήματος για αναζήτηση.");
            return java.util.Collections.emptyList();
        }

        System.out.println("[Controller-UC8] Αναζήτηση προσφορών στη βάση για το μάθημα: " + courseId);
        // Κλήση της μεθόδου του Repository που φτιάξαμε μαζί
        return helpOfferRepository.getActiveOffers(courseId);
    }

    /**
     * Επιλογή μιας προσφοράς από τον φοιτητή (selectOffer στο SD8)
     * Επιστρέφει την προσφορά και καταγράφει την ενέργεια στο ιστορικό του φοιτητή
     */
    public HelpOffer selectOffer(StudentProfile studentProfile, HelpOffer selectedOffer) {
        if (selectedOffer == null) {
            System.out.println("[Controller-UC8] Δεν επιλέχθηκε έγκυρη προσφορά.");
            return null;
        }

        System.out.println("[Controller-UC8] Ο φοιτητής " + studentProfile.getStudentId() +
                " επέλεξε την προσφορά ID: " + selectedOffer.getOfferId());

        // Ενημέρωση του ιστορικού (Log) του φοιτητή που λαμβάνει τη βοήθεια (από το SD8)
        studentProfile.updateLogHistory("Προβολή/Λήψη βοήθειας για το μάθημα " +
                selectedOffer.getCourseId() +
                " από τον Μέντορα " + selectedOffer.getMentorId());

        return selectedOffer;
    }
}