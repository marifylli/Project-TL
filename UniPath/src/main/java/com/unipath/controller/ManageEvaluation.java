package com.unipath.controller;

import com.unipath.model.Course;
import com.unipath.model.CourseEvaluation;
import com.unipath.repository.EvaluationRepository;
import com.unipath.login.UserSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManageEvaluation {

    // Αρχικοποίηση του έτοιμου Repository της ομάδας σας
    private final EvaluationRepository evaluationRepository = new EvaluationRepository();

    // 1. Επιστρέφει τη λίστα με τα ονόματα των μαθημάτων για το UI
    // Επιστρέφει List<Course> για να συμβαδίζει με τις αλλαγές που κάναμε στο UI
    public List<Course> queryCoursesAttended(int studentId) {
        // Κλήση της πραγματικής μεθόδου του Repository της ομάδας
        return evaluationRepository.getAttendedCourses(studentId);
    }

    // 2. Ελέγχει αν ο φοιτητής έχει δικαίωμα αξιολόγησης (Βήμα 4 - sd2)
    public boolean checkEvaluationRight(String courseName) {
        int studentId = UserSession.getInstance().getUserId();
        String courseId = convertCourseNameToId(courseName);

        // Μετατροπή του String courseId σε int προσωρινά
        int courseIdInt = 0;
        try {
            // Αν το ID περιέχει μόνο αριθμούς, το μετατρέπουμε. Αν είναι σαν το CEID_24Y332, παίρνουμε ένα fallback νούμερο.
            courseIdInt = Integer.parseInt(courseId.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            courseIdInt = 101;
        }

        return !evaluationRepository.hasAlreadySubmitted(studentId, courseIdInt);
    }

    // 3. Ελέγχει την εγκυρότητα των πεδίων της φόρμας
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

    // 4. Δημιουργεί και αποθηκεύει το αντικείμενο CourseEvaluation μέσω του Repository της ομάδας
    public boolean saveEvaluation(String courseName, int rating, String comments) {
        // Δημιουργία του αντικειμένου από το Model σας
        CourseEvaluation evaluation = new CourseEvaluation();

        // Γέμισμα των στοιχείων
        evaluation.setStudentID(UserSession.getInstance().getUserId());
        evaluation.setRating(rating);
        evaluation.setComments(comments);
        evaluation.setSubmissionDate(new Date());
        evaluation.setSubmitted(true);

        // Τώρα περνάμε String ID ("CEID_...") όπως ακριβώς το περιμένει το Model!
        String courseId = convertCourseNameToId(courseName);
        evaluation.setCourseId(courseId);

        // Κλήση της πραγματικής μεθόδου saveEvaluation του Repository της ομάδας!
        evaluationRepository.saveEvaluation(evaluation);

        return true;
    }

    // 5. Ενημέρωση στατιστικών μαθήματος (Βήμα 11 - sd2)
    public void updateCourseStats(String courseName) {
        String courseId = convertCourseNameToId(courseName);
        System.out.println("Ενημέρωση μέσου όρου στη ΒΔ για το μάθημα με ID: " + courseId);
    }

    // Η μέθοδος επιστρέφει  String και χρησιμοποιεί τα πραγματικά IDs της βάσης
    private String convertCourseNameToId(String courseName) {
        if (courseName == null) return "UNKNOWN";
        if (courseName.contains("Λογισμικού")) return "CEID_24Y332";
        if (courseName.contains("Δεδομένων")) return "CEID_24Y334";
        if (courseName.contains("Δίκτυα")) return "CEID_24Y387";
        return "CEID_22Y103"; // Default fallback ID
    }


}