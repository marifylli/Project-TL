package com.unipath.model;

import java.util.Date;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


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
    public static List<AvailabilitySlot> findAvailableSlots(int professorId) {

        List<AvailabilitySlot> slots = new ArrayList<>();
        String URL = "jdbc:sqlite:unipath.db";

        // SQL ερώτημα που φέρνει τις ελεύθερες ώρες (όπου δεν έχει κλειστεί ακόμα ραντεβού)
        String sql = "SELECT * FROM AvailabilitySlot WHERE professorId = ? " +
                "AND slotId NOT IN (SELECT slotId FROM Appointment WHERE status != 'REJECTED')";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, professorId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                AvailabilitySlot slot = new AvailabilitySlot();
                slot.setSlotId(rs.getInt("slotId"));
                slot.setProfessorId(rs.getInt("professorId"));
                slot.setDayOfWeek(rs.getString("dayOfWeek"));
                slot.setStartTime(rs.getString("startTime"));
                slot.setEndTime(rs.getString("endTime"));

                slots.add(slot);
            }
        } catch (SQLException e) {
            System.err.println("❌ [AvailabilitySlot Model]: Σφάλμα κατά την εκτέλεση του findAvailableSlots: " + e.getMessage());
        }

        return slots;
    }
}