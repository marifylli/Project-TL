package com.unipath.model;

import java.util.Date;

public class InterviewMeeting {
    private int meetingId;
    private int studentId;
    private int professorId;
    private int slotId;
    private int diplomaticId;
    private Date date;
    private String startTime;
    private String status;
    private Date confirmedAt;
    private boolean notificationSent;

    public InterviewMeeting() {}

    public InterviewMeeting(int studentId, int professorId,
                            int slotId, int diplomaticId,
                            Date date, String startTime) {
        this.studentId = studentId;
        this.professorId = professorId;
        this.slotId = slotId;
        this.diplomaticId = diplomaticId;
        this.date = date;
        this.startTime = startTime;
        this.status = "PENDING";
        this.notificationSent = false;
    }

    // Getters
    public int getMeetingId()        {
        return meetingId;
    }
    public int getStudentId()        {
        return studentId;
    }
    public int getProfessorId()      {
        return professorId;
    }
    public int getSlotId()           {
        return slotId;
    }
    public int getDiplomaticId()     {
        return diplomaticId;
    }
    public Date getDate()            {
        return date;
    }
    public String getStartTime()     {
        return startTime;
    }
    public String getStatus()        {
        return status;
    }
    public Date getConfirmedAt()     {
        return confirmedAt;
    }
    public boolean isNotificationSent() {
        return notificationSent;
    }

    // Setters
    public void setMeetingId(int id)           {
        this.meetingId = id;
    }
    public void setStudentId(int id)           {
        this.studentId = id;
    }
    public void setProfessorId(int id)         {
        this.professorId = id;
    }
    public void setSlotId(int id)              {
        this.slotId = id;
    }
    public void setDiplomaticId(int id)        {
        this.diplomaticId = id;
    }
    public void setDate(Date date)             {
        this.date = date;
    }
    public void setStartTime(String time)      {
        this.startTime = time;
    }
    public void setStatus(String status)       {
        this.status = status;
    }
    public void setConfirmedAt(Date date)      {
        this.confirmedAt = date;
    }
    public void setNotificationSent(boolean b) {
        this.notificationSent = b;
    }

    public void bookApointment() {}
    public void addEvent() {}
}