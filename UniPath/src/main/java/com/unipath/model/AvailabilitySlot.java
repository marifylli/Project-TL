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


    public void setAvailableSlots() {}
    public List<AvailabilitySlot> findAvailableSlots() { return null; }
}