package com.unipath.model;

import com.unipath.repository.InterviewRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Calendar {
    private String calendarId;
    private Date lastUpdated;
    private String updatedBy;
    private List<AvailabilitySlot> availabilitySlots;

    public Calendar() {
        this.availabilitySlots = new ArrayList<>();
    }

    public Calendar(String calendarId, String updatedBy) {
        this.calendarId = calendarId;
        this.updatedBy = updatedBy;
        this.lastUpdated = new Date();
        this.availabilitySlots = new ArrayList<>();
    }

    // Getters / Setters
    public String getCalendarId() { return calendarId; }
    public void setCalendarId(String id) { this.calendarId = id; }
    public List<AvailabilitySlot> getAvailabilitySlots() { return availabilitySlots; }
    public void setAvailabilitySlots(List<AvailabilitySlot> slots) { this.availabilitySlots = slots; }
    public void addSlot(AvailabilitySlot slot) { this.availabilitySlots.add(slot); }


    public static Calendar requestCalendar(int professorId) {
        com.unipath.repository.ThesisRepository repo = new com.unipath.repository.ThesisRepository();
        return repo.requestCalendar(professorId);
    }


    public static boolean addEvent(int studentId, int slotId, int diplomaticId) {
        InterviewRepository repo = new InterviewRepository();

        List<AvailabilitySlot> slots = repo.getAvailableSlots(41);
        int professorId = 1;

        return repo.saveInterviewMeeting(studentId, professorId, slotId, diplomaticId);
    }
}