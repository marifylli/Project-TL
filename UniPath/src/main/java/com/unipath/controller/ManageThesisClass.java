package com.unipath.controller;

import com.unipath.model.*;
import com.unipath.repository.ThesisRepository;
import com.unipath.dataBase.DBManager;
import java.util.Date;

public class ManageThesisClass {
    private final ThesisRepository repository = new ThesisRepository();

    public ManageThesisClass() {
        try { DBManager.getInstance().createTables(); }
        catch (Exception e) { e.printStackTrace(); }
    }

    public boolean publishThesis(int pId, String title, String desc, String pre, int ects, int max, String skills) {
        return repository.saveThesis(new Thesis(pId, title, desc, pre, ects, max, skills));
    }

    public boolean setAvailability(int pId, String day, String start, String end) {
        return repository.saveAvailabilitySlot(new AvailabilitySlot(pId, new Date(), day, start, end));
    }

    public Calendar requestCalendar(int pId) {
        return repository.getCalendarByProfessor(pId);
    }
}