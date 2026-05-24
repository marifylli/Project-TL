package com.unipath.model;

import com.unipath.repository.ThesisRepository;
import com.unipath.repository.InterviewRepository;
import java.util.Date;
import java.util.List;

public class AvailabilitySlot {
    private int slotId;
    private int professorId;
    private Date date;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private boolean isAvailable;

    public AvailabilitySlot() {}

    public AvailabilitySlot(int professorId, Date date, String dayOfWeek, String startTime, String endTime) {
        this.professorId = professorId;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = true;
    }


    public int getSlotId() { return slotId; }
    public void setSlotId(int id) { this.slotId = id; }
    public int getProfessorId() { return professorId; }
    public void setProfessorId(int id) { this.professorId = id; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String day) { this.dayOfWeek = day; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String time) { this.startTime = time; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String time) { this.endTime = time; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean avail) { this.isAvailable = avail; }


    public static boolean setAvailabilitySlot(AvailabilitySlot slot) {
        ThesisRepository repo = new ThesisRepository();
        return repo.setAvailabilitySlot(slot);
    }

    public static List<AvailabilitySlot> findAvailableSlots(int professorId) {
        InterviewRepository repo = new InterviewRepository();
        return repo.getAvailableSlots(professorId);
    }
}