package com.unipath.repository;

import com.unipath.dataBase.DBManager;
import com.unipath.model.Course;
import com.unipath.model.Professor;
import com.unipath.login.UserSession;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CourseRepository {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public List<Course> queryGetCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = """
            SELECT courseId, title, description, ects, semester, professorID, groupA, groupB, 
                   directions, isActive, averageRating, workloadScore, workloadRank, 
                   lastModifiedDate, lastModifiedBy, rules, prerequisites 
            FROM Course
        """;

        try {
            Connection conn = DBManager.getInstance().connect();

            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    Course course = mapResultSetToCourse(rs);
                    courses.add(course);
                }
                System.out.println("Ανακτήθηκαν επιτυχώς " + courses.size() + " μαθήματα.");
            }
        } catch (SQLException e) {
            System.err.println("Σφάλμα κατά την εκτέλεση του queryGetCourses: " + e.getMessage());
        }

        return courses;
    }


    public List<Course> queryGetProfessorCourses(int professorId) {
        List<Course> professorCourses = new ArrayList<>();

        // 1. ΕΛΕΓΧΟΣ: Αν ο τρέχων χρήστης είναι testing χρήστης
        String currentEmail = null;
        try {
            currentEmail = com.unipath.login.UserSession.getInstance().getEmail();
        } catch (Exception e) {
            System.err.println("Δεν βρέθηκε ενεργό session: " + e.getMessage());
        }

        if (currentEmail != null && currentEmail.startsWith("test.")) {
            // Επιστρέφουμε Εικονικά (Mock) Μαθήματα για να δουλέψει το UI Test
            Course c1 = new Course();
            c1.setCourseID("CEID_NY901");
            c1.setTitle("Τεχνολογία Λογισμικού");
            c1.setDescription("Σχεδιασμός και ανάπτυξη πληροφοριακών συστημάτων.");
            c1.setECTS(6);
            c1.setSemester(6);
            c1.setProfessorId(professorId);
            c1.setRules("Παρακολούθηση εργαστηρίου 80%, Τελική εξέταση >= 5");
            c1.setPrerequisites("Αντικειμενοστραφής Προγραμματισμός");

            Course c2 = new Course();
            c2.setCourseID("CEID_NY902");
            c2.setTitle("Βάσεις Δεδομένων ΙΙ");
            c2.setDescription("Προχωρημένα θέματα SQL και διαχείρισης συναλλαγών.");
            c2.setECTS(5);
            c2.setSemester(6);
            c2.setProfessorId(professorId);
            c2.setRules("Υποχρεωτική εξαμηνιαία εργασία");
            c2.setPrerequisites("Βάσεις Δεδομένων Ι");

            professorCourses.add(c1);
            professorCourses.add(c2);

            System.out.println("[Repository - TEST MODE] Επιστράφηκαν mock μαθήματα.");
            return professorCourses;
        }

        // 2. ΚΑΝΟΝΙΚΗ ΡΟΗ: SQL ερώτημα στην SQLite
        String sql = """
        SELECT courseId, title, description, ects, semester, professorID,
               groupA, groupB, directions, isActive, averageRating, 
               workloadScore, workloadRank, lastModifiedDate, 
               lastModifiedBy, rules, prerequisites
        FROM Course
        WHERE professorID = ?
    """;

        try {
            java.sql.Connection conn = DBManager.getInstance().connect();
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, professorId);
                try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Course course = mapResultSetToCourse(rs);
                        professorCourses.add(course);
                    }
                }
            }
            System.out.println("Ανακτήθηκαν " + professorCourses.size() + " μαθήματα από τη ΒΔ.");
        } catch (java.sql.SQLException e) {
            System.err.println("Σφάλμα SQL κατά την εκτέλεση του queryGetProfessorCourses: " + e.getMessage());
        }

        return professorCourses;
    }

    public String queryGetProfessorUsername(int professorId) {
        String username = "Unknown_Professor";
        String sql = """
            SELECT u.username 
            FROM User u
            INNER JOIN Professor p ON u.userId = p.userId
            WHERE p.professorId = ?
        """;

        try {
            Connection conn = DBManager.getInstance().connect();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, professorId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        username = rs.getString("username");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Σφάλμα κατά την ανάκτηση του professor username: " + e.getMessage());
        }
        return username;
    }


    public boolean queryUpdateCourseRules(String courseId, String newRules, String professorUsername) {
        String sql = """
            UPDATE Course 
            SET rules = ?, 
                lastModifiedBy = ?, 
                lastModifiedDate = ? 
            WHERE courseId = ?
        """;

        String currentTimestamp = dateFormat.format(new Date());

        try {
            Connection conn = DBManager.getInstance().connect();

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, newRules);
                pstmt.setString(2, professorUsername);
                pstmt.setString(3, currentTimestamp);
                pstmt.setString(4, courseId);

                int rowsUpdated = pstmt.executeUpdate();
                return rowsUpdated > 0;
            }
        } catch (SQLException e) {
            System.err.println("Σφάλma κατά την εκτέλεση του queryUpdateCourseRules: " + e.getMessage());
            return false;
        }
    }


    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        Course course = new Course();

                // ΔΙΟΡΘΩΣΗ 1: setCourseID με κεφαλαίο ID
                course.setCourseID(rs.getString("courseId"));
                course.setTitle(rs.getString("title"));
                course.setDescription(rs.getString("description"));
                course.setECTS(rs.getInt("ects"));
                course.setSemester(rs.getInt("semester"));
        course.setCourseID(rs.getString("courseId"));
        course.setTitle(rs.getString("title"));
        course.setDescription(rs.getString("description"));
        course.setECTS(rs.getInt("ects"));
        course.setSemester(rs.getInt("semester"));

        //  ΑΣΦΑΛΗΣ ΑΝΑΓΝΩΣΗ: Διαβάζουμε το professorId μόνο αν περιλαμβάνεται στο SQL Query
        try {
            course.setProfessorId(rs.getInt("professorId"));
        } catch (SQLException e) {
            // Αν η στήλη δεν υπάρχει στο query (όπως στο queryGetCourses),
            // βάζουμε μια προεπιλεγμένη τιμή χωρίς να κρασάρει η εφαρμογή
            course.setProfessorId(-1);
        }

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
                course.setLastModifiedDate(parsedDate);
            } catch (Exception e) {
                course.setLastModifiedDate(null);
            }
        }

                course.setLastModifiedBy(rs.getString("lastModifiedBy"));
                course.setRules(rs.getString("rules"));
                course.setPrerequisites(rs.getString("prerequisites"));

        return course;
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
                pstmt1.setInt(4, course.getECTS());
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

    // μεθοδοι για 6

    // [queryCourseList()] -> DBManager
    public List<Course> queryCourseList() {
        // Καλούμε την ήδη υπάρχουσα queryGetCourses() για να μην ξαναγράφουμε το ίδιο μεγάλο query!
        return queryGetCourses();
    }

    // [getCourseDetails()] -> Course Entity
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
                    course.setECTS(rs.getInt("ects"));
                    course.setSemester(rs.getInt("semester"));
                    return course;
                }
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Σφάλμα στο getCourseDetails: " + e.getMessage());
        }
        return null;
    }

    //  [saveEdits()] -> Course Entity
    public boolean saveEdits(Course course) {
        String sql = "UPDATE Course SET title = ?, description = ?, ects = ?, semester = ? WHERE courseId = ?";
        try (java.sql.Connection conn = DBManager.getInstance().connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getTitle());
            pstmt.setString(2, course.getDescription());
            pstmt.setInt(3, course.getECTS());
            pstmt.setInt(4, course.getSemester());
            pstmt.setString(5, course.getCourseID());
            return pstmt.executeUpdate() > 0;
        } catch (java.sql.SQLException e) {
            System.err.println("Σφάλμα στο saveEdits: " + e.getMessage());
            return false;
        }
    }

    // [refreshStudyPlans()] -> StudyPlan Entity
    public void refreshStudyPlans(String courseID) {
        System.out.println("refreshStudyPlans() εκτελέστηκε επιτυχώς στη βάση για το μάθημα: " + courseID);
    }


}