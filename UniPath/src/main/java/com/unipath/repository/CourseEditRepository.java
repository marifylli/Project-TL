package com.unipath.repository;

import com.unipath.dataBase.DBManager;
import com.unipath.model.Course;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseEditRepository {

    // 1. Ανάκτηση πληροφοριών μαθήματος από τη βάση
    public Course getCourseDetails(String courseId) {
        String sql = "SELECT * FROM Course WHERE courseId = ?";
        try {
            Connection conn = DBManager.getInstance().connect();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, courseId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    Course course = new Course();
                    course.setCourseID(rs.getString("courseId"));
                    course.setTitle(rs.getString("title"));
                    course.setDescription(rs.getString("description"));
                    course.setRules(rs.getString("rules"));
                    // Πρόσθεσε εδώ επιπλέον setters αν υπάρχουν κι άλλες στήλες (ects, semester κλπ)
                    return course;
                }
            }
        } catch (SQLException e) {
            System.err.println("[CourseEditRepository] Σφάλμα getCourseDetails: " + e.getMessage());
        }
        return null;
    }

    // 2. Αποθήκευση των αλλαγών (Κανόνων) στη βάση δεδομένων
    public boolean saveCourseChanges(String courseId, String newRulesText, String professorUsername) {
        String sql = "UPDATE Course SET rules = ?, lastModifiedBy = ? WHERE courseId = ?";
        try {
            DBManager.getInstance().createTables();
            Connection conn = DBManager.getInstance().connect();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, newRulesText);
                pstmt.setString(2, professorUsername);
                pstmt.setString(3, courseId);

                int rowsAffected = pstmt.executeUpdate();
                System.out.println("[CourseEditRepository] Επιτυχές saveCourseChanges στη SQLite!");
                return rowsAffected > 0;
            }

        } catch (SQLException e) {
            System.err.println("[CourseEditRepository] Σφάλμα saveCourseChanges: " + e.getMessage());
            return false;
        }
    }
}