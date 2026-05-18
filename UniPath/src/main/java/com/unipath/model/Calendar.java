package com.unipath.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Calendar {

    private String calendarId;
    private Date lastUpdated;
    private String updatedBy;
    private List<AvailabilitySlot> availabilitySlot;

    public Calendar() {
        this.availabilitySlot = new ArrayList<>();
    }

    public Calendar(String calendarId, String updatedBy) {
        this.calendarId = calendarId;
        this.updatedBy = updatedBy;
        this.lastUpdated = new Date();
        this.availabilitySlot = new ArrayList<>();
    }

    public String getCalendarId()                        {
        return calendarId;
    }
    public Date getLastUpdated()                         {
        return lastUpdated;
    }
    public String getUpdatedBy()                         {
        return updatedBy;
    }
    public List<AvailabilitySlot> getAvailabilitySlots() {
        return availabilitySlot;
    }

    // Setters
    public void setCalendarId(String id)                          {
        this.calendarId = id;
    }
    public void setLastUpdated(Date date)                         {
        this.lastUpdated = date;
    }
    public void setUpdatedBy(String user)                         {
        this.updatedBy = user;
    }
    public void setAvailabilitySlots(List<AvailabilitySlot> slots){
        this.availabilitySlot = slots;
    }
    // Βοηθητική μέθοδος για προσθήκη slot
    public void addSlot(AvailabilitySlot slot) {
        this.availabilitySlot.add(slot);
    }

}