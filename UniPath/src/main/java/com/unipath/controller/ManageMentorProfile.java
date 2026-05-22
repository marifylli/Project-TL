package com.unipath.controller;

import com.unipath.model.HelpOffer;
import com.unipath.model.StudentProfile;
import com.unipath.repository.HelpOfferRepository;

public class ManageMentorProfile {

    private final HelpOfferRepository helpOfferRepository;

    // Constructor που αρχικοποιεί το Repository
    public ManageMentorProfile() {
        this.helpOfferRepository = new HelpOfferRepository();
    }

    /**
     * Υλοποίηση του Happy Path του SD7 για τη δημιουργία προσφοράς βοήθειας
     */
    public boolean createHelpOffer(StudentProfile profile, String courseId, String helpType, String notesFile, String meetingUrl) {

        // 1. Έλεγχος πεδίων (checkFields)
        if (!checkFields(courseId, helpType, notesFile, meetingUrl)) {
            System.out.println("[Controller] Σφάλμα: Τα πεδία δεν είναι έγκυρα ή είναι όλα κενά.");
            return false;
        }

        // 2. Δημιουργία του HelpOffer αντικειμένου μέσω της create() του Domain Model
        HelpOffer newOffer = HelpOffer.create(profile.getStudentId(), courseId, helpType, notesFile, meetingUrl);

        // 3. Αποθήκευση στη βάση δεδομένων μέσω του Repository
        boolean isSaved = helpOfferRepository.saveOffer(newOffer);

        if (isSaved) {
            // 4. Ενημέρωση του StudentProfile αντικειμένου στη μνήμη
            profile.updateActiveOffersList(newOffer);
            profile.updateLogHistory("Δημιουργήθηκε νέα προσφορά βοήθειας για το μάθημα: " + courseId);
            profile.updateprofile();

            System.out.println("[Controller] Η προσφορά αποθηκεύτηκε και το προφίλ ενημερώθηκε επιτυχώς!");
            return true;
        } else {
            System.out.println("[Controller] Αποτυχία αποθήκευσης της προσφοράς στη βάση δεδομένων.");
            return false;
        }
    }

    /**
     * Μέθοδος ελέγχου πεδίων (checkFields) από το SD7
     * Ελέγχει αν έχει επιλεγεί μάθημα και τύπος βοήθειας, και αν υπάρχει τουλάχιστον ένα URL ή αρχείο
     */
    public boolean checkFields(String courseId, String helpType, String notesFile, String meetingUrl) {
        if (courseId == null || courseId.isBlank()) return false;
        if (helpType == null || helpType.isBlank()) return false;

        // Πρέπει να έχει δώσει ή αρχείο σημειώσεων ή link τηλεδιάσκεψης (ή και τα δύο)
        boolean hasFile = (notesFile != null && !notesFile.isBlank());
        boolean hasMeeting = (meetingUrl != null && !meetingUrl.isBlank());

        return hasFile || hasMeeting;
    }
}