package com.unipath.repository;

import com.unipath.model.AvailabilitySlot;
import com.unipath.model.Calendar;
import com.unipath.model.Thesis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThesisRepository {

    private static final String DB_URL = "jdbc:sqlite:UniPath/unipath.db";

    // ── Αποθήκευση Διπλωματικής ─────────────────────────────────────
    public boolean saveThesis(Thesis thesis) {
        String sql = """
            INSERT INTO thesis 
            (professorId, title, description, prerequisites, 
             requiredECTS, isAvailable, maxCandidates, requiredSkills,
             publicationDate, lastModifiedDate)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, datetime('now'), datetime('now'))
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, thesis.getProfessorId());
            pstmt.setString(2, thesis.getTitle());
            pstmt.setString(3, thesis.getDescription());
            pstmt.setString(4, thesis.getPrerequisites());
            pstmt.setInt(5, thesis.getRequiredECTS());
            pstmt.setBoolean(6, thesis.isAvailable());
            pstmt.setInt(7, thesis.getMaxCandidates());
            pstmt.setString(8, thesis.getRequiredSkills());
            pstmt.executeUpdate();

            System.out.println("Διπλωματική αποθηκεύτηκε επιτυχώς!");
            return true;

        } catch (SQLException e) {
            System.out.println("Σφάλμα αποθήκευσης: " + e.getMessage());
            return false;
        }
    }

    // ── Αποθήκευση Slot Διαθεσιμότητας ──────────────────────────────
    public boolean saveAvailabilitySlot(AvailabilitySlot slot) {
        String sql = """
            INSERT INTO availability_slots
            (professorId, date, dayOfWeek, startTime, endTime, isAvailable)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, slot.getProfessorId());
            pstmt.setString(2, slot.getDate().toString());
            pstmt.setString(3, slot.getDayOfWeek());
            pstmt.setString(4, slot.getStartTime());
            pstmt.setString(5, slot.getEndTime());
            pstmt.setBoolean(6, slot.isAvailable());
            pstmt.executeUpdate();

            System.out.println("Slot αποθηκεύτηκε επιτυχώς!");
            return true;

        } catch (SQLException e) {
            System.out.println("Σφάλμα αποθήκευσης slot: " + e.getMessage());
            return false;
        }
    }

    // ── Ανάκτηση Ημερολογίου Καθηγητή ───────────────────────────────
    public Calendar getCalendarByProfessor(int professorId) {
        String sql = "SELECT * FROM availability_slots WHERE professorId = ?";
        Calendar calendar = new Calendar(String.valueOf(professorId), "professor");

        try (Connection conn = DriverManager.getConnection(DB_URL);
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
                slot.setAvailable(rs.getBoolean("isAvailable"));
                calendar.addSlot(slot);
            }

        } catch (SQLException e) {
            System.out.println("Σφάλμα ανάκτησης ημερολογίου: " + e.getMessage());
        }

        return calendar;
    }

    // ── Ανάκτηση όλων των Διπλωματικών ──────────────────────────────
    public List<Thesis> getAllTheses() {
        String sql = "SELECT * FROM thesis WHERE isAvailable = 1";
        List<Thesis> theses = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Thesis thesis = new Thesis();
                thesis.setDiplomaticId(rs.getInt("diplomaticId"));
                thesis.setProfessorId(rs.getInt("professorId"));
                thesis.setTitle(rs.getString("title"));
                thesis.setDescription(rs.getString("description"));
                thesis.setPrerequisites(rs.getString("prerequisites"));
                thesis.setRequiredECTS(rs.getInt("requiredECTS"));
                thesis.setAvailable(rs.getBoolean("isAvailable"));
                thesis.setMaxCandidates(rs.getInt("maxCandidates"));
                thesis.setRequiredSkills(rs.getString("requiredSkills"));
                theses.add(thesis);
            }

        } catch (SQLException e) {
            System.out.println("Σφάλμα ανάκτησης: " + e.getMessage());
        }

        return theses;
    }
}