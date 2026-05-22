package com.unipath.repository;

import com.unipath.dataBase.DBManager;
import com.unipath.model.Course;
import com.unipath.model.Professor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CourseRepository {

    public List<Course> queryGetCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = """
            SELECT courseId, title, description, ects, semester, groupA, groupB, 
                   directions, isActive, averageRating, workloadScore, workloadRank, 
                   lastModifiedDate, lastModifiedBy, rules, prerequisites 
            FROM Course
        """;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try (Connection conn = DBManager.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Course course = new Course();

                // ΔΙΟΡΘΩΣΗ 1: setCourseID με κεφαλαίο ID
                course.setCourseID(rs.getString("courseId"));
                course.setTitle(rs.getString("title"));
                course.setDescription(rs.getString("description"));
                course.setEcts(rs.getInt("ects"));
                course.setSemester(rs.getInt("semester"));

                course.setGroupA(rs.getInt("groupA") == 1);
                course.setGroupB(rs.getInt("groupB") == 1);
                course.setActive(rs.getInt("isActive") == 1);

                course.setAverageRating(rs.getFloat("averageRating"));
                course.setWorkloadScore(rs.getFloat("workloadScore"));
                course.setWorkloadRank(rs.getInt("workloadRank"));

                String dateStr = rs.getString("lastModifiedDate");
                if (dateStr != null && !dateStr.isEmpty()) {
                    try {
                        Date parsedDate = dateFormat.parse(dateStr);
                        // ΔΙΟΡΘΩΣΗ 2: setLastModifiedDate
                        course.setLastModifiedDate(parsedDate);
                    } catch (Exception e) {
                        // ΔΙΟΡΘΩΣΗ 3: setLastModifiedDate
                        course.setLastModifiedDate(null);
                    }
                }

                course.setLastModifiedBy(rs.getString("lastModifiedBy"));
                course.setRules(rs.getString("rules"));
                course.setPrerequisites(rs.getString("prerequisites"));

                courses.add(course);
            }

            System.out.println("Ανακτήθηκαν επιτυχώς " + courses.size() + " μαθήματα.");

        } catch (SQLException e) {
            System.err.println("Σφάλμα κατά την εκτέλεση του queryGetCourses: " + e.getMessage());
        }

        return courses;
    }

    // μέθοδοι gia uc5

    // εναλλακτική [queryCheckDuplicates()] - Ελέγχει αν το courseId υπάρχει ήδη στη ΒΔ
    public boolean queryCheckDuplicates(String courseID) {
        String sql = "SELECT COUNT(*) FROM Course WHERE courseId = ?";
        try (java.sql.Connection conn = DBManager.getInstance().connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseID);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Σφάλμα στο queryCheckDuplicates: " + e.getMessage());
        }
        return false;
    }

    // Βήμα 5: [queryManageProfessors()] - Επιστρέφει τους καθηγητές από τη ΒΔ
    public List<Professor> queryManageProfessors() {
        List<Professor> professors = new ArrayList<>();
        String sql = """
            SELECT p.professorId, (u.firstName || ' ' || u.lastName) AS fullName, u.email, p.office 
            FROM Professor p
            JOIN User u ON p.userId = u.userId
        """;

        try (java.sql.Connection conn = DBManager.getInstance().connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Professor prof = new Professor(
                        rs.getInt("professorId"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("office")
                );
                professors.add(prof);
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Σφάλμα στο queryManageProfessors: " + e.getMessage());
        }
        return professors;
    }

    // εναλλακτική & Βασική Ροή: [saveCourse()] - Αποθηκεύει το μάθημα και την ανάθεση (Πίνακας ProfessorCourse)
    public boolean saveCourse(Course course, int professorID) {
        String insertCourseSql = """
            INSERT INTO Course (courseId, title, description, ects, semester, isActive, lastModifiedDate, lastModifiedBy) 
            VALUES (?, ?, ?, ?, ?, 1, ?, 'Secretary')
        """;
        String insertRelationSql = "INSERT INTO ProfessorCourse (professorId, courseId, role) VALUES (?, ?, 'teaches')";
        String updateLoadSql = "UPDATE Professor SET currentTeachingLoad = currentTeachingLoad + 1 WHERE professorId = ?";

        java.sql.Connection conn = null;
        try {
            conn = DBManager.getInstance().connect();
            conn.setAutoCommit(false);

            try (java.sql.PreparedStatement pstmt1 = conn.prepareStatement(insertCourseSql)) {
                pstmt1.setString(1, course.getCourseID());
                pstmt1.setString(2, course.getTitle());
                pstmt1.setString(3, course.getDescription());
                pstmt1.setInt(4, course.getEcts());
                pstmt1.setInt(5, course.getSemester());
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                pstmt1.setString(6, dateFormat.format(new Date()));
                pstmt1.executeUpdate();
            }

            try (java.sql.PreparedStatement pstmt2 = conn.prepareStatement(insertRelationSql)) {
                pstmt2.setInt(1, professorID);
                pstmt2.setString(2, course.getCourseID());
                pstmt2.executeUpdate();
            }

            try (java.sql.PreparedStatement pstmt3 = conn.prepareStatement(updateLoadSql)) {
                pstmt3.setInt(1, professorID);
                pstmt3.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (java.sql.SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (java.sql.SQLException ex) {} }
            return false;
        }
    }

    // μεθοδοι για uc6
    public List<Course> queryCourseList() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM Course";
        try (java.sql.Connection conn = DBManager.getInstance().connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getString("courseId"));
                course.setTitle(rs.getString("title"));
                course.setDescription(rs.getString("description"));
                course.setEcts(rs.getInt("ects"));
                course.setSemester(rs.getInt("semester"));
                courses.add(course);
            }
        } catch (java.sql.SQLException e) {
            System.err.println(e.getMessage());
        }
        return courses;
    }

    public Course getCourseDetails(String courseID) {
        String sql = "SELECT * FROM Course WHERE courseId = ?";
        try (java.sql.Connection conn = DBManager.getInstance().connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseID);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Course course = new Course();
                    course.setCourseID(rs.getString("courseId"));
                    course.setTitle(rs.getString("title"));
                    course.setDescription(rs.getString("description"));
                    course.setEcts(rs.getInt("ects"));
                    course.setSemester(rs.getInt("semester"));
                    return course;
                }
            }
        } catch (java.sql.SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public boolean saveEdits(Course course) {
        String sql = "UPDATE Course SET title = ?, description = ?, ects = ?, semester = ? WHERE courseId = ?";
        try (java.sql.Connection conn = DBManager.getInstance().connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getTitle());
            pstmt.setString(2, course.getDescription());
            pstmt.setInt(3, course.getEcts());
            pstmt.setInt(4, course.getSemester());
            pstmt.setString(5, course.getCourseID());
            return pstmt.executeUpdate() > 0;
        } catch (java.sql.SQLException e) {
            return false;
        }
    }

    public void refreshStudyPlans(String courseID) {
        System.out.println("refreshStudyPlans() εκτελέστηκε για το μάθημα: " + courseID);
    }



}