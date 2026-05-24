package com.unipath.model;
import java.util.List;
import java.util.Date;

public class StudentProfile {
    private int profileId;
    private int studentId;
    private List<StudyPlan> savedPlans;
    private Date lastUpdated;


    public StudentProfile(){

    }
    public void addPlanToProfile(){

    }
    private List<HelpOffer> activeOffers = new java.util.ArrayList<>();

    public void updateActiveOffersList(HelpOffer newOffer) {
        if (this.activeOffers == null) {
            this.activeOffers = new java.util.ArrayList<>();
        }
        this.activeOffers.add(newOffer);
        System.out.println("Η νέα προσφορά για το μάθημα " + newOffer.getCourseId() + " προστέθηκε στο προφίλ του φοιτητή " + this.studentId);
    }

    public void updateLogHistory(String action) {
        System.out.println("[LOG - " + new java.util.Date() + "] Φοιτητής ID " + this.studentId + ": " + action);
    }

    public void updateprofile() {
        this.lastUpdated = new java.util.Date();
        System.out.println("Το προφίλ του φοιτητή " + this.studentId + " ενημερώθηκε επιτυχώς στις " + this.lastUpdated);
    }

    public StudyPlan getStudyPlan() {
        System.out.println("[Model] Κλήση της getStudyPlan() για τον φοιτητή " + this.studentId);
        if (this.savedPlans != null && !this.savedPlans.isEmpty()) {
            return this.savedPlans.get(0);
        }
        // Αν η λίστα είναι άδεια, επιστρέφουμε ένα mock αντικείμενο για να μην κρασάρει η εφαρμογή
        return new StudyPlan();
    }

    public int getStudentId() {
        return studentId;
    }
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<HelpOffer> getActiveOffers() {
        return activeOffers;
    }
    public void setActiveOffers(List<HelpOffer> activeOffers) {
        this.activeOffers = activeOffers;
    }
}
