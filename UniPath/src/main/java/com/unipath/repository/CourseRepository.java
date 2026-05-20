package com.unipath.repository;

import com.unipath.dataBase.DBManager;
import com.unipath.model.Course;

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
                        course.setLastModifiedDate(parsedDate);
                    } catch (Exception e) {

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

}
