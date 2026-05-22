package com.unipath.controller;

import com.unipath.model.*;
import com.unipath.repository.ThesisRepository;
import java.util.Date;

public class ManageThesisClass {
    private final ThesisRepository repository = new ThesisRepository();

    // Βήμα 5: Μήνυμα requestCalendar(pId)
    public Calendar requestCalendar(int pId) {
        return repository.getCalendarByProfessor(pId);
    }

    // Βήμα 10: Μήνυμα setAvailableSlots(...)
    public boolean setAvailableSlots(int pId, String day, String start, String end) {
        return repository.saveAvailabilitySlot(new AvailabilitySlot(pId, new Date(), day, start, end));
    }

    // Βήμα 11: Μήνυμα saveNewThesis(...)
    public boolean saveNewThesis(int pId, String title, String desc, String pre, int ects, int max, String skills) {
        return repository.saveThesis(new Thesis(pId, title, desc, pre, ects, max, skills));
    }
}