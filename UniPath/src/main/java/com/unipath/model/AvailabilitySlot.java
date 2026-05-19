package com.unipath.model;

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

    public AvailabilitySlot(int professorId, Date date, String dayOfWeek,
                            String startTime, String endTime) {
        this.professorId = professorId;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = true;
    }

    public int getSlotId()       {
        return slotId;
    }
    public int getProfessorId()  {
        return professorId;
    }
    public Date getDate()        {
        return date;
    }
    public String getDayOfWeek() {
        return dayOfWeek;
    }
    public String getStartTime() {
        return startTime;
    }
    public String getEndTime()   {
        return endTime;
    }
    public boolean isAvailable() {
        return isAvailable;
    }


    // Setters
    public void setSlotId(int id)           {
        this.slotId = id;
    }
    public void setProfessorId(int id)      {
        this.professorId = id;
    }
    public void setDate(Date date)          {
        this.date = date;
    }
    public void setDayOfWeek(String day)    {
        this.dayOfWeek = day;
    }
    public void setStartTime(String time)   {
        this.startTime = time;
    }
    public void setEndTime(String time)     {
        this.endTime = time;
    }
    public void setAvailable(boolean avail) {
        this.isAvailable = avail;
    }




    public void setAvailableSlots() {}
    public List<AvailabilitySlot> findAvailableSlots() { return null; }
}