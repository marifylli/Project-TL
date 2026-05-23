package com.unipath.repository;

import com.unipath.dataBase.DBManager;
import com.unipath.model.AvailabilitySlot;
import com.unipath.model.InterviewMeeting;
import com.unipath.model.Thesis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterviewRepository {

    private Connection getConnection() throws SQLException {
        return DBManager.getInstance().connect();
    }

    // ── Έλεγχος Προϋποθέσεων Φοιτητή ───────────────────────────────
    public boolean checkStudentEligibility(int studentId, int thesisId) {
        String sql = """
            SELECT t.requiredECTS, s.completedECTS
            FROM Thesis t, Student s
            WHERE t.thesisId = ? AND s.studentId = ?
        """;
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, thesisId);
            pstmt.setInt(2, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int requiredECTS = rs.getInt("requiredECTS");
                int completedECTS = rs.getInt("completedECTS");
                return completedECTS >= requiredECTS;
            }
        } catch (SQLException e) {
            System.out.println("Σφάλμα ελέγχου: " + e.getMessage());
        }
        return false;
    }

    // ── Ανάκτηση Διαθέσιμων Slots ───────────────────────────────────
    public List<AvailabilitySlot> getAvailableSlots(int professorId) {
        String sql = """
            SELECT * FROM AvailabilitySlot 
            WHERE professorId = ? AND isAvailable = 1
        """;
        List<AvailabilitySlot> slots = new ArrayList<>();
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, professorId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                AvailabilitySlot slot = new AvailabilitySlot();
                slot.setSlotId(rs.getInt("slotId"));
                slot.setProfessorId(rs.getInt("professorId"));
                slot.setDayOfWeek(rs.getString("dayOfWeek"));
                slot.setStartTime(rs.getString("startTime"));
                slot.setEndTime(rs.getString("endTime"));
                slot.setAvailable(rs.getBoolean("isAvailable"));
                slots.add(slot);
            }
        } catch (SQLException e) {
            System.out.println("Σφάλμα ανάκτησης slots: " + e.getMessage());
        }
        return slots;
    }

    // ── Αποθήκευση Ραντεβού ─────────────────────────────────────────
    public boolean saveInterviewMeeting(int studentId, int professorId,
                                        int slotId, int thesisId) {
        String sql = """
            INSERT INTO InterviewMeeting
            (studentId, professorId, slotId, thesisId,
             date, startTime, status, notificationSent)
            VALUES (?, ?, ?, ?, datetime('now'), 
                   (SELECT startTime FROM AvailabilitySlot WHERE slotId = ?),
                   'PENDING', 0)
        """;
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, professorId);
            pstmt.setInt(3, slotId);
            pstmt.setInt(4, thesisId);
            pstmt.setInt(5, slotId);
            pstmt.executeUpdate();

            // Ενημέρωσε το slot ως μη διαθέσιμο
            updateSlotAvailability(slotId);
            System.out.println("Ραντεβού καταχωρήθηκε!");
            return true;
        } catch (SQLException e) {
            System.out.println("Σφάλμα καταχώρησης: " + e.getMessage());
            return false;
        }
    }

    // ── Ενημέρωση Διαθεσιμότητας Slot ───────────────────────────────
    private void updateSlotAvailability(int slotId) {
        String sql = "UPDATE AvailabilitySlot SET isAvailable = 0 WHERE slotId = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, slotId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Σφάλμα ενημέρωσης slot: " + e.getMessage());
        }
    }

    // ── Ανάκτηση Ραντεβού Φοιτητή ───────────────────────────────────
    public List<InterviewMeeting> getStudentMeetings(int studentId) {
        String sql = "SELECT * FROM InterviewMeeting WHERE studentId = ?";
        List<InterviewMeeting> meetings = new ArrayList<>();
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                InterviewMeeting meeting = new InterviewMeeting();
                meeting.setMeetingId(rs.getInt("meetingId"));
                meeting.setStudentId(rs.getInt("studentId"));
                meeting.setProfessorId(rs.getInt("professorId"));
                meeting.setSlotId(rs.getInt("slotId"));
                meeting.setDiplomaticId(rs.getInt("thesisId"));
                meeting.setStatus(rs.getString("status"));
                meetings.add(meeting);
            }
        } catch (SQLException e) {
            System.out.println("Σφάλμα ανάκτησης: " + e.getMessage());
        }
        return meetings;
    }
}