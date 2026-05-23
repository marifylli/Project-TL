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
    public List<String> queryCoursesAttended(int studentId) {
        List<String> courseNames = new ArrayList<>();

        // Κλήση της πραγματικής μεθόδου του Repository της ομάδας!
        List<Course> attendedCourses = evaluationRepository.getAttendedCourses(studentId);

        if (attendedCourses != null) {
            for (Course c : attendedCourses) {
                // Κρατάμε το όνομα (ή συνδυασμό ID και ονόματος) για να εμφανιστεί στο ListView
                courseNames.add(c.getTitle());
            }
        }
        return courseNames;
    }

    // 2. Ελέγχει αν ο φοιτητής έχει δικαίωμα αξιολόγησης (Βήμα 4 - sd2)
    public boolean checkEvaluationRight(String courseName) {
        int studentId = UserSession.getInstance().getUserId();
        int courseId = convertCourseNameToId(courseName);

        // Κλήση της πραγματικής μεθόδου hasAlreadySubmitted της ομάδας!
        // Αν έχει ΉΔΗ υποβάλει (true), τότε ΔΕΝ έχει δικαίωμα (επιστρέφει false)
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
    public boolean saveEvaluation(String courseName, int rating, String comments) {
        // Δημιουργία του αντικειμένου από το Model σας
        CourseEvaluation evaluation = new CourseEvaluation();

        // Γέμισμα των στοιχείων
        evaluation.setStudentID(UserSession.getInstance().getUserId());
        evaluation.setRating(rating);
        evaluation.setComments(comments);
        evaluation.setSubmissionDate(new Date());
        evaluation.setSubmitted(true);

        // Μετατροπή του ονόματος σε ID για τη βάση δεδομένων
        int courseId = convertCourseNameToId(courseName);
        evaluation.setCourseId(courseId);

        // Κλήση της πραγματικής μεθόδου saveEvaluation του Repository της ομάδας!
        evaluationRepository.saveEvaluation(evaluation);

        return true;
    }

    // 5. Ενημέρωση στατιστικών μαθήματος (Βήμα 11 - sd2)
    public void updateCourseStats(String courseName) {
        int courseId = convertCourseNameToId(courseName);
        System.out.println("Ενημέρωση μέσου όρου στη ΒΔ για το μάθημα με ID: " + courseId);
        // Εδώ στο μέλλον η ομάδα μπορεί να καλέσει την getEvaluationsByCourse(String.valueOf(courseId))
        // του Repository για να υπολογίσει τον νέο Μέσο Όρο στατιστικών (UC4).
    }

    // Βοηθητική μέθοδος για την προσομοίωση εύρεσης του ID από το όνομα του μαθήματος
    private int convertCourseNameToId(String courseName) {
        if (courseName == null) return 0;
        if (courseName.contains("Λογισμικού")) return 101;
        if (courseName.contains("Δεδομένων")) return 102;
        return 103; // Default ID για άλλα μαθήματα
    }
}

