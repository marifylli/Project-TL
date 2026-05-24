package com.unipath.model;

import java.util.ArrayList;
import java.util.List;

public class HelpOffer {
    private int offerId;
    private int mentorId; // int για συμβατότητα με το studentId του Student table
    private String courseId; // String για συμβατότητα με το courseId του Course table
    private String helpType;
    private String notesFile; // Αντιστοιχεί στο fileUrl της βάσης δεδομένων
    private String meetingUrl;
    private boolean isActive;

    // Default Constructor
    public HelpOffer() {
    }

    // Constructor για πλήρη αρχικοποίηση (χρήσιμος στο Repository κατά την ανάκτηση)
    public HelpOffer(int offerId, int mentorId, String courseId, String helpType, String notesFile, String meetingUrl, boolean isActive) {
        this.offerId = offerId;
        this.mentorId = mentorId;
        this.courseId = courseId;
        this.helpType = helpType;
        this.notesFile = notesFile;
        this.meetingUrl = meetingUrl;
        this.isActive = isActive;
    }

    // Η στατική μέθοδος create() που απαιτείται από το Domain (Happy Path του SD7)
    public static HelpOffer create(int mentorId, String courseId, String helpType, String notesFile, String meetingUrl) {
        HelpOffer offer = new HelpOffer();
        offer.setMentorId(mentorId);
        offer.setCourseId(courseId);
        offer.setHelpType(helpType);
        offer.setNotesFile(notesFile);
        offer.setMeetingUrl(meetingUrl);
        offer.setActive(true); // Οι νέες προσφορές είναι εξορισμού ενεργές
        return offer;
    }

    // Μέθοδος getOfferServices() που καλείται στο SD8
    public List<String> getOfferServices() {
        List<String> services = new ArrayList<>();
        if (notesFile != null && !notesFile.isBlank()) {
            services.add("Λήψη Αρχείου Σημειώσεων");
        }
        if (meetingUrl != null && !meetingUrl.isBlank()) {
            services.add("Σύνδεση σε URL Τηλεδιάσκεψης");
        }
        return services;
    }

    // Getters & Setters
    public int getOfferId() { return offerId; }
    public void setOfferId(int offerId) { this.offerId = offerId; }

    public int getMentorId() { return mentorId; }
    public void setMentorId(int mentorId) { this.mentorId = mentorId; }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getHelpType() { return helpType; }
    public void setHelpType(String helpType) { this.helpType = helpType; }

    public String getNotesFile() { return notesFile; }
    public void setNotesFile(String notesFile) { this.notesFile = notesFile; }

    public String getMeetingUrl() { return meetingUrl; }
    public void setMeetingUrl(String meetingUrl) { this.meetingUrl = meetingUrl; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
}
