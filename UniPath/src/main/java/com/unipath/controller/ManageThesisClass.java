package com.unipath.controller;

import com.unipath.model.AvailabilitySlot;
import com.unipath.model.Calendar;
import com.unipath.model.Thesis;
import com.unipath.repository.ThesisRepository;
import com.unipath.dataBase.DBManager;

public class ManageThesisClass {

    private final ThesisRepository repository;

    public ManageThesisClass() {
        this.repository = new ThesisRepository();
        try {
            DBManager.getInstance().createTables();
        } catch (Exception e) {
            System.out.println("Σφάλμα δημιουργίας πινάκων: " + e.getMessage());
        }
    }

    public boolean publishThesis(int professorId, String title,
                                 String description, String prerequisites,
                                 int requiredECTS, int maxCandidates,
                                 String requiredSkills) {
        if (!validateFields(title, description, prerequisites, requiredSkills)) {
            return false;
        }
        Thesis thesis = new Thesis(professorId, title, description,
                prerequisites, requiredECTS,
                maxCandidates, requiredSkills);
        return repository.saveThesis(thesis);
    }

    public boolean setAvailability(int professorId, String dayOfWeek,
                                   String startTime, String endTime) {
        if (dayOfWeek == null || startTime == null || endTime == null) {
            return false;
        }
        AvailabilitySlot slot = new AvailabilitySlot(
                professorId, new java.util.Date(),
                dayOfWeek, startTime, endTime);
        return repository.saveAvailabilitySlot(slot);
    }

    public Calendar requestCalendar(int professorId) {
        return repository.getCalendarByProfessor(professorId);
    }

    public boolean validateFields(String title, String description,
                                  String prerequisites, String requiredSkills) {
        if (title == null || title.isBlank()) return false;
        if (description == null || description.isBlank()) return false;
        if (prerequisites == null || prerequisites.isBlank()) return false;
        if (requiredSkills == null || requiredSkills.isBlank()) return false;
        return true;
    }
}