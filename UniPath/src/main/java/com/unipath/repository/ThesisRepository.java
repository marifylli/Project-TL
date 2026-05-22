package com.unipath.repository;

import com.unipath.model.AvailabilitySlot;
import com.unipath.model.Calendar;
import com.unipath.model.Thesis;
import com.unipath.dataBase.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThesisRepository {

    private Connection getConnection() throws SQLException {
        return DBManager.getInstance().connect();
    }

    public boolean saveThesis(Thesis thesis) {
        String sql = """
            INSERT INTO Thesis 
            (professorId, title, description, prerequisites, 
             requiredECTS, isAvailable, maxCandidates, requiredSkills,
             publicationDate, lastModifiedDate)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, datetime('now'), datetime('now'))
        """;
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, thesis.getProfessorId());
            pstmt.setString(2, thesis.getTitle());
            pstmt.setString(3, thesis.getDescription());
            pstmt.setString(4, thesis.getPrerequisites());
            pstmt.setInt(5, thesis.getRequiredECTS());
            pstmt.setBoolean(6, thesis.isAvailable());
            pstmt.setInt(7, thesis.getMaxCandidates());
            pstmt.setString(8, thesis.getRequiredSkills());
            pstmt.executeUpdate();
            System.out.println("Διπλωματική αποθηκεύτηκε!");
            return true;
        } catch (SQLException e) {
            System.out.println("Σφάλμα: " + e.getMessage());
            return false;
        }
    }

    public boolean saveAvailabilitySlot(AvailabilitySlot slot) {
        String sql = """
            INSERT INTO AvailabilitySlot
            (professorId, date, dayOfWeek, startTime, endTime, isAvailable)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, slot.getProfessorId());
            pstmt.setString(2, slot.getDate().toString());
            pstmt.setString(3, slot.getDayOfWeek());
            pstmt.setString(4, slot.getStartTime());
            pstmt.setString(5, slot.getEndTime());
            pstmt.setBoolean(6, slot.isAvailable());
            pstmt.executeUpdate();
            System.out.println("Slot αποθηκεύτηκε!");
            return true;
        } catch (SQLException e) {
            System.out.println("Σφάλμα: " + e.getMessage());
            return false;
        }
    }

    public Calendar getCalendarByProfessor(int professorId) {
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
                slot.setAvailable(rs.getBoolean("isAvailable"));
                calendar.addSlot(slot);
            }
        } catch (SQLException e) {
            System.out.println("Σφάλμα: " + e.getMessage());
        }
        return calendar;
    }

    public List<Thesis> getAllTheses() {
        String sql = "SELECT * FROM Thesis WHERE isAvailable = 1";
        List<Thesis> theses = new ArrayList<>();
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Thesis thesis = new Thesis();
                thesis.setDiplomaticId(rs.getInt("thesisId"));
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
            System.out.println("Σφάλμα: " + e.getMessage());
        }
        return theses;
    }

    // ── Ανάκτηση Διπλωματικής βάσει ID ─────────────────────────────
    public Thesis getThesisById(int thesisId) {
        String sql = "SELECT * FROM Thesis WHERE thesisId = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, thesisId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Thesis thesis = new Thesis();
                thesis.setDiplomaticId(rs.getInt("thesisId"));
                thesis.setProfessorId(rs.getInt("professorId"));
                thesis.setTitle(rs.getString("title"));
                thesis.setDescription(rs.getString("description"));
                thesis.setPrerequisites(rs.getString("prerequisites"));
                thesis.setRequiredECTS(rs.getInt("requiredECTS"));
                thesis.setAvailable(rs.getBoolean("isAvailable"));
                thesis.setMaxCandidates(rs.getInt("maxCandidates"));
                thesis.setRequiredSkills(rs.getString("requiredSkills"));
                return thesis;
            }
        } catch (SQLException e) {
            System.out.println("Σφάλμα ανάκτησης: " + e.getMessage());
        }
        return null;
    }
}