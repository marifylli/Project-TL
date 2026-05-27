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


    public boolean hasAlreadySubmitted(int studentId, int courseId) {
        String sql = "SELECT * FROM CourseEvaluation WHERE studentId=? AND courseId=?";

        try (Connection conn = DBManager.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Σφάλμα hasAlreadySubmitted: " + e.getMessage());
            return false;
        }
    }


    public List<Course> getAttendedCourses(int studentId) {
        List<Course> courses = new ArrayList<>();
        String sql = """
            SELECT c.courseId, c.title, c.ects, c.semester
            FROM Course c
            JOIN StudentCourse sc ON c.courseId = sc.courseId
            WHERE sc.studentId = ?
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



