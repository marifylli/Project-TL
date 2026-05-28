package com.unipath.repository;

import com.unipath.dataBase.DBManager;
import com.unipath.model.Course;
import com.unipath.model.CourseEvaluation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EvaluationRepository {

    // UC2 βήμα 4: έλεγξε αν έχει ήδη αξιολογήσει
    public boolean hasAlreadySubmitted(int studentId, String courseId) { // <- Αλλαγή από int σε String
        String sql = "SELECT COUNT(*) FROM CourseEvaluation WHERE studentId = ? AND courseId = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setString(2, courseId); // <- Αλλαγή από setInt σε setString

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Σφάλμα κατά τον έλεγχο υποβολής αξιολόγησης: " + e.getMessage());
        }
        return false;
    }

    // UC2 βήμα 3: παίρνεις τα μαθήματα που παρακολούθησε
    public List<Course> getAttendedCourses(int studentId) {
        List<Course> courses = new ArrayList<>();

        // DEBUG
        String debugSql = "SELECT planId, courses, isFinalized FROM StudyPlan WHERE studentId = ?";
        try (Connection conn = DBManager.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(debugSql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("DEBUG: Βρέθηκε πλάνο - planId=" + rs.getInt("planId") +
                        ", isFinalized=" + rs.getInt("isFinalized") +
                        ", courses=" + rs.getString("courses"));
            } else {
                System.out.println("DEBUG: Δεν βρέθηκε κανένα πλάνο για studentId=" + studentId);
            }
        } catch (SQLException e) {
            System.err.println("DEBUG Error: " + e.getMessage());
        }

        // Κύριο query
        String sql = """
        SELECT c.courseId, c.title, c.ects, c.semester
        FROM Course c
        INNER JOIN StudyPlan sp ON (',' || sp.courses || ',') LIKE ('%,' || c.courseId || ',%')
        WHERE sp.studentId = ? AND sp.isFinalized = 1
        ORDER BY sp.planId DESC
    """;

        try (Connection conn = DBManager.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getString("courseId"));
                course.setTitle(rs.getString("title"));
                course.setECTS(rs.getInt("ects"));
                course.setSemester(rs.getInt("semester"));
                courses.add(course);
            }

        } catch (SQLException e) {
            System.err.println("Σφάλμα getAttendedCourses: " + e.getMessage());
        }

        return courses;
    }


    // UC2 βήμα 9: αποθήκευσε αξιολόγηση
    public void saveEvaluation(CourseEvaluation evaluation) {
        String sql = """
            INSERT INTO CourseEvaluation 
            (studentId, courseId, rating, comments, submissionDate, isSubmitted, isAnonymous)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBManager.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, evaluation.getStudentID());
            pstmt.setString(2, evaluation.getCourseId());
            pstmt.setInt(3, evaluation.getRating());
            pstmt.setString(4, evaluation.getComments());
            pstmt.setString(5, evaluation.getSubmissionDate().toString());
            pstmt.setInt(6, evaluation.isSubmitted() ? 1 : 0);
            pstmt.setInt(7, evaluation.isAnonymous() ? 1 : 0);
            pstmt.executeUpdate();

            System.out.println("Η αξιολόγηση αποθηκεύτηκε επιτυχώς!");

        } catch (SQLException e) {
            System.err.println("Σφάλμα saveEvaluation: " + e.getMessage());
        }
    }

    // UC4: ανάκτηση αξιολογήσεων για στατιστικά
    public List<CourseEvaluation> getEvaluationsByCourse(String courseId) {
        List<CourseEvaluation> evaluations = new ArrayList<>();
        String sql = "SELECT * FROM CourseEvaluation WHERE courseId=?";

        try (Connection conn = DBManager.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                CourseEvaluation eval = new CourseEvaluation();
                eval.setEvaluationId(rs.getInt("evaluationId"));
                eval.setStudentID(rs.getInt("studentId"));
                eval.setCourseId(rs.getString("courseId"));
                eval.setRating(rs.getInt("rating"));
                eval.setComments(rs.getString("comments"));
                eval.setSubmitted(rs.getInt("isSubmitted") == 1);
                evaluations.add(eval);
            }

        } catch (SQLException e) {
            System.err.println("Σφάλμα getEvaluationsByCourse: " + e.getMessage());
        }

        return evaluations;
    }
}



