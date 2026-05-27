package com.unipath.controller;

import com.unipath.model.Course;
import com.unipath.model.CourseEvaluation;
import com.unipath.repository.EvaluationRepository;
import com.unipath.login.UserSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManageEvaluation {


    private final EvaluationRepository evaluationRepository = new EvaluationRepository();


    public List<Course> queryCoursesAttended(int studentId) {
        // Κλήση της πραγματικής μεθόδου του Repository της ομάδας
        return evaluationRepository.getAttendedCourses(studentId);
    }


    public boolean checkEvaluationRight(String courseName) {
        int studentId = UserSession.getInstance().getUserId();
        String courseId = convertCourseNameToId(courseName);

        // Μετατροπή του String courseId σε int
        int courseIdInt = 0;
        try {

            courseIdInt = Integer.parseInt(courseId.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            courseIdInt = 101;
        }

        return !evaluationRepository.hasAlreadySubmitted(studentId, courseIdInt);
    }

    //  Ελέγχει την εγκυρότητα των πεδίων της φόρμας
    public boolean checkFields(String rating, String comments) {
        if (rating == null || rating.isBlank() || comments == null || comments.isBlank()) {
            return false;
        }
        try {
            int r = Integer.parseInt(rating);
            return r >= 1 && r <= 5;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public boolean saveEvaluation(String courseName, int rating, String comments) {

        CourseEvaluation evaluation = new CourseEvaluation();


        evaluation.setStudentID(UserSession.getInstance().getUserId());
        evaluation.setRating(rating);
        evaluation.setComments(comments);
        evaluation.setSubmissionDate(new Date());
        evaluation.setSubmitted(true);


        String courseId = convertCourseNameToId(courseName);
        evaluation.setCourseId(courseId);


        evaluationRepository.saveEvaluation(evaluation);

        return true;
    }

    //  Ενημέρωση στατιστικών μαθήματος
    public void updateCourseStats(String courseName) {
        String courseId = convertCourseNameToId(courseName);
        System.out.println("Ενημέρωση μέσου όρου στη ΒΔ για το μάθημα με ID: " + courseId);
    }

    // Η μέθοδος επιστρέφει  String
    private String convertCourseNameToId(String courseName) {
        if (courseName == null) return "UNKNOWN";
        if (courseName.contains("Λογισμικού")) return "CEID_24Y332";
        if (courseName.contains("Δεδομένων")) return "CEID_24Y334";
        if (courseName.contains("Δίκτυα")) return "CEID_24Y387";
        return "CEID_22Y103"; // Default fallback ID
    }


}