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
    public boolean checkEvaluationRight(String courseIdOrName) {
        int studentId = UserSession.getInstance().getUserId();

        // Παίρνουμε το καθαρό String ID (π.χ. "CEID_24Y332")
        String courseId = convertCourseNameToId(courseIdOrName);

        // Στέλνουμε το String απευθείας στο repository χωρίς αλχημείες με Integer.parseInt
        return !evaluationRepository.hasAlreadySubmitted(studentId, courseId);
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
    public boolean saveEvaluation(String courseIdOrName, int rating, String comments) {
        CourseEvaluation evaluation = new CourseEvaluation();

        evaluation.setStudentID(UserSession.getInstance().getUserId());
        evaluation.setRating(rating);
        evaluation.setComments(comments);
        evaluation.setSubmissionDate(new Date());
        evaluation.setSubmitted(true);

        // Αν είναι ήδη ID το χρησιμοποιεί, αλλιώς το μετατρέπει
        String courseId = convertCourseNameToId(courseIdOrName);
        evaluation.setCourseId(courseId);

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

        // ΑΝ είναι ήδη ID, επέστρεψέ το απευθείας!
        if (courseName.startsWith("CEID_")) {
            return courseName;
        }

        if (courseName.contains("Λογισμικού")) return "CEID_24Y332";
        if (courseName.contains("Δεδομένων")) return "CEID_24Y334";
        if (courseName.contains("Δίκτυα")) return "CEID_24Y387";
        return "CEID_22Y103"; // Default fallback ID
    }

}