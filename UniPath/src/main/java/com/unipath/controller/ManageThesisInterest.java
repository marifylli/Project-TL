package com.unipath.controller;

import com.unipath.model.AvailabilitySlot;
import com.unipath.model.InterviewMeeting;
import com.unipath.model.Thesis;
import com.unipath.repository.InterviewRepository;
import com.unipath.repository.ThesisRepository;
import com.unipath.dataBase.DBManager;

import java.util.List;

public class ManageThesisInterest {

    private final ThesisRepository thesisRepository;
    private final InterviewRepository interviewRepository;

    public ManageThesisInterest() {
        this.thesisRepository = new ThesisRepository();
        this.interviewRepository = new InterviewRepository();
        try {
            DBManager.getInstance().createTables();
        } catch (Exception e) {
            System.out.println("Σφάλμα: " + e.getMessage());
        }
    }

    // ── Βήμα 2: Ανάκτηση διαθέσιμων διπλωματικών ───────────────────
    public List<Thesis> getAvailableTheses() {
        return thesisRepository.getAllTheses();
    }

    // ── Βήμα 4: Λεπτομέρειες θέματος ───────────────────────────────
    public Thesis getThesisDetails(int thesisId) {
        return thesisRepository.getThesisById(thesisId);
    }

    // ── Βήμα 6-7: Έλεγχος προϋποθέσεων ─────────────────────────────
    public boolean checkEligibility(int studentId, int thesisId) {
        return interviewRepository.checkStudentEligibility(studentId, thesisId);
    }

    // ── Βήμα 8: Ανάκτηση διαθέσιμων slots ──────────────────────────
    public List<AvailabilitySlot> getAvailableSlots(int professorId) {
        return interviewRepository.getAvailableSlots(professorId);
    }

    // ── Βήμα 10-11: Κράτηση ραντεβού ───────────────────────────────
    public boolean bookAppointment(int studentId, int professorId,
                                   int slotId, int thesisId) {
        return interviewRepository.saveInterviewMeeting(
                studentId, professorId, slotId, thesisId);
    }
}