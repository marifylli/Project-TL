package com.unipath.controller;

import com.unipath.model.*;
import com.unipath.repository.*;
import java.util.List;

public class ManageThesisInterest {
    private final ThesisRepository thesisRepository = new ThesisRepository();
    private final InterviewRepository interviewRepository = new InterviewRepository();

    // Αντιστοιχεί στο "findAllThesis()" του διαγράμματος
    public List<Thesis> findAllThesis() {
        return thesisRepository.getAllTheses();
    }

    // Αντιστοιχεί στο "findThesisDetails()" του διαγράμματος
    public Thesis findThesisDetails(int thesisId) {
        return thesisRepository.getThesisById(thesisId);
    }

    // Αντιστοιχεί στο "findAvailableSlots()" του διαγράμματος
    public List<AvailabilitySlot> findAvailableSlots(int professorId) {
        return interviewRepository.getAvailableSlots(professorId);
    }

    // Αντιστοιχεί στο "selectConfirmAppointment()" του διαγράμματος
    public boolean selectConfirmAppointment(int studentId, int professorId, int slotId, int thesisId) {
        return interviewRepository.saveInterviewMeeting(studentId, professorId, slotId, thesisId);
    }
}