package com.unipath.controller;

import com.unipath.model.AvailabilitySlot;
import com.unipath.model.Calendar;
import com.unipath.model.Thesis;
import com.unipath.repository.ThesisRepository;
import com.unipath.dataBase.DBManager;

import java.util.List;

public class ManageThesisClass {

    private final ThesisRepository repository;

    public ManageThesisClass() {
        this.repository = new ThesisRepository();
        try {
            DBManager.getInstance().createTables();  // ✅ σωστό
        } catch (Exception e) {
            System.out.println("Σφάλμα δημιουργίας πινάκων: " + e.getMessage());
        }
    }
    // ── Δημοσίευση Διπλωματικής ─────────────────────────────────────
    public boolean publishThesis(int professorId, String title,
                                 String description, String prerequisites,
                                 int requiredECTS, int maxCandidates,
                                 String requiredSkills) {
        // Βήμα 1: Επικύρωση πεδίων
        if (!validateFields(title, description, prerequisites, requiredSkills)) {
            return false;
        }

        // Βήμα 2: Δημιουργία αντικειμένου Thesis
        Thesis thesis = new Thesis(professorId, title, description,
                prerequisites, requiredECTS,
                maxCandidates, requiredSkills);

        // Βήμα 3: Αποθήκευση στη βάση
        return repository.saveThesis(thesis);
    }

    // ── Αποθήκευση Slot Διαθεσιμότητας ──────────────────────────────
    public boolean setAvailability(int professorId, String dayOfWeek,
                                   String startTime, String endTime) {
        // Επικύρωση
        if (dayOfWeek == null || startTime == null || endTime == null) {
            return false;
        }

        AvailabilitySlot slot = new AvailabilitySlot(
                professorId, new java.util.Date(),
                dayOfWeek, startTime, endTime);

        return repository.saveAvailabilitySlot(slot);
    }

    // ── Ανάκτηση Ημερολογίου ────────────────────────────────────────
    public Calendar requestCalendar(int professorId) {
        return repository.getCalendarByProfessor(professorId);
    }

    // ── Επικύρωση Υποχρεωτικών Πεδίων ───────────────────────────────
    public boolean validateFields(String title, String description,
                                  String prerequisites, String requiredSkills) {
        if (title == null || title.isBlank()) return false;
        if (description == null || description.isBlank()) return false;
        if (prerequisites == null || prerequisites.isBlank()) return false;
        if (requiredSkills == null || requiredSkills.isBlank()) return false;
        return true;
    }
}