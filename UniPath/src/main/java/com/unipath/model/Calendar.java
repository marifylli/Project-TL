package com.unipath.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Calendar {

    private String calendarId;
    private Date lastUpdated;
    private String updatedBy;
    private List<AvailabilitySlot> availabilitySlot;

    // Κατασκευαστές (Constructors)
    public Calendar() {
        this.availabilitySlot = new ArrayList<>();
    }

    public Calendar(String calendarId, String updatedBy) {
        this.calendarId = calendarId;
        this.updatedBy = updatedBy;
        this.lastUpdated = new Date();
        this.availabilitySlot = new ArrayList<>();
    }

    // ── Η ΜΕΘΟΔΟΣ ΑΠΟ ΤΟ SEQUENCE DIAGRAM (SD) ───────────────────
    // Σταθερά URL για τη σύνδεση με την SQLite βάση δεδομένων σου
    private static final String URL = "jdbc:sqlite:unipath.db";

    /**
     * [Sequence Diagram]: addEvent()
     * Καταχωρεί το νέο συμβάν/ραντεβού στον πίνακα Appointment της βάσης δεδομένων[cite: 38, 39].
     */
    public static boolean addEvent(int studentId, int slotId, int diplomaticId) {
        String sql = "INSERT INTO Appointment (studentId, slotId, diplomaticId, status) VALUES (?, ?, ?, 'PENDING')";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, slotId);
            pstmt.setInt(3, diplomaticId);

            int rows = pstmt.executeUpdate();
            return rows > 0; // Επιστρέφει true αν η εγγραφή έγινε επιτυχώς

        } catch (SQLException e) {
            System.err.println("❌ [Calendar Model]: Αποτυχία κατά την εκτέλεση της addEvent στη ΒΔ: " + e.getMessage());
            return false;
        }
    }
    // ─────────────────────────────────────────────────────────────

    // Getters
    public String getCalendarId() {
        return calendarId;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public List<AvailabilitySlot> getAvailabilitySlots() {
        return availabilitySlot;
    }

    // Setters
    public void setCalendarId(String id) {
        this.calendarId = id;
    }

    public void setLastUpdated(Date date) {
        this.lastUpdated = date;
    }

    public void setUpdatedBy(String user) {
        this.updatedBy = user;
    }

    public void setAvailabilitySlots(List<AvailabilitySlot> slots) {
        this.availabilitySlot = slots;
    }

    // Βοηθητική μέθοδος για προσθήκη slot
    public void addSlot(AvailabilitySlot slot) {
        this.availabilitySlot.add(slot);
    }
}