package com.unipath.repository;

import com.unipath.model.AvailabilitySlot;
import com.unipath.model.Calendar;
import com.unipath.model.Thesis;
import com.unipath.dataBase.DBManager;

import java.sql.*;

public class ThesisRepository {

    private Connection getConnection() throws SQLException {
        return DBManager.getInstance().connect();
    }

    // ==========================================
    // ΜΕΘΟΔΟΙ ΓΙΑ ΤΟ UC10 (ΔΙΚΟ ΣΟΥ)
    // ==========================================

    // Βήμα 5: Μήνυμα requestCalendar(professorId)
    public Calendar requestCalendar(int professorId) {
        String sql = "SELECT * FROM AvailabilitySlot WHERE professorId = ?";
        Calendar calendar = new Calendar(String.valueOf(professorId), "professor");
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
                slot.setAvailable(rs.getInt("isAvailable") == 1);
                calendar.addSlot(slot);
            }
        } catch (SQLException e) {
            System.out.println("Σφάλμα requestCalendar: " + e.getMessage());
        }
        return calendar;
    }

    // Βήμα 10: Μήνυμα setAvailabilitySlot(slot)
    public boolean setAvailabilitySlot(AvailabilitySlot slot) {
        String sql = """
            INSERT INTO AvailabilitySlot
            (professorId, date, dayOfWeek, startTime, endTime, isAvailable)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, slot.getProfessorId());
            pstmt.setString(2, slot.getDate() != null ? slot.getDate().toString() : "");
            pstmt.setString(3, slot.getDayOfWeek());
            pstmt.setString(4, slot.getStartTime());
            pstmt.setString(5, slot.getEndTime());
            pstmt.setInt(6, slot.isAvailable() ? 1 : 0);
            pstmt.executeUpdate();
            System.out.println("Slot αποθηκεύτηκε!");
            return true;
        } catch (SQLException e) {
            System.out.println("Σφάλμα setAvailabilitySlot: " + e.getMessage());
            return false;
        }
    }

    // Βήμα 11: Μήνυμα saveThesis(thesis)
    public boolean saveThesis(Thesis thesis) {
        String sql = """
            INSERT INTO Thesis 
            (professorId, title, description, prerequisites, requiredECTS, isAvailable, maxCandidates, requiredSkills)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, thesis.getProfessorId());
            pstmt.setString(2, thesis.getTitle());
            pstmt.setString(3, thesis.getDescription());
            pstmt.setString(4, thesis.getPrerequisites());
            pstmt.setInt(5, thesis.getRequiredECTS());
            pstmt.setInt(6, thesis.isAvailable() ? 1 : 0);
            pstmt.setInt(7, thesis.getMaxCandidates());
            pstmt.setString(8, thesis.getRequiredSkills());
            pstmt.executeUpdate();
            System.out.println("Διπλωματική αποθηκεύτηκε!");
            return true;
        } catch (SQLException e) {
            System.out.println("Σφάλμα saveThesis: " + e.getMessage());
            return false;
        }
    }
}