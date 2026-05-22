package com.unipath.controller;

import com.unipath.model.HelpOffer;
import com.unipath.model.StudentProfile;
import com.unipath.model.Course;
import com.unipath.dataBase.DBManager;
import com.unipath.repository.HelpOfferRepository;
import java.util.List;
import java.util.ArrayList;

public class ManageMentorProfile {

    private String currentCourseId;
    private String currentHelpType;
    private String currentNotesFile;
    private String currentMeetingUrl;

    public ManageMentorProfile() {
    }

    /**
     * Βήμα 2 Λεκτικού: Το Σύστημα ανακτά όλα τα διαθέσιμα μαθήματα (queryCourses())
     */
    public List<Course> queryCourses() {
        System.out.println("[Controller] Ανάκτηση μαθημάτων από την κλάση Course.");
        List<Course> coursesList = new ArrayList<>();
        String sql = "SELECT * FROM Course WHERE isActive = 1";

        try (java.sql.Connection conn = DBManager.getInstance().connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getString("courseID"));
                course.setTitle(rs.getString("title"));
                course.setSemester(rs.getInt("semester"));
                course.setEcts(rs.getInt("ects"));
                coursesList.add(course);
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Σφάλμα στην queryCourses: " + e.getMessage());
        }
        return coursesList;
    }

    // Καταγραφή επιλογών από το UI (Βήματα 3, 4, 6 λεκτικού)
    public void selectCourse(String courseId) { this.currentCourseId = courseId; }
    public void selectHelp(String helpType) { this.currentHelpType = helpType; }
    public void offerDataType(String notesFile, String meetingUrl) {
        this.currentNotesFile = notesFile;
        this.currentMeetingUrl = meetingUrl;
    }

    /**
     * Βήμα 7 Λεκτικού: Ο Φοιτητής επιλέγει «Δημοσίευση Προσφοράς»
     */
    public boolean postOffer(StudentProfile profile) {
        System.out.println("[Controller] Έναρξη postOffer().");

        // 1. Βήμα 8 Λεκτικού: Το Σύστημα ελέγχει τη συμπλήρωση των πεδίων (checkFields)
        if (!checkFields()) {
            System.out.println("[Controller] [Alternative Path] Σφάλμα: Τα πεδία δεν συμπληρώθηκαν σωστά.");
            return false;
        }
        System.out.println("[Controller] Ο έλεγχος πεδίων πέτυχε.");

        // 2. Βήμα 9 Λεκτικού: Δημιουργία της Ενεργής Προσφοράς στην Java (create)
        HelpOffer newOffer = HelpOffer.create(
                profile.getStudentId(),
                this.currentCourseId,
                this.currentHelpType,
                this.currentNotesFile,
                this.currentMeetingUrl
        );
        System.out.println("[Controller] Δημιουργήθηκε το αντικείμενο HelpOffer.");

        // 3. Βήμα 8 Λεκτικού (Δεύτερο σκέλος): Αποθήκευση στην ΒΔ μέσω DBManager (saveOffer)
        HelpOfferRepository repo = new HelpOfferRepository();
        boolean isSaved = repo.saveOffer(newOffer);
        System.out.println("[Controller] Αποθήκευση στη ΒΔ μέσω DBManager. Κατάσταση: " + isSaved);

        if (isSaved) {
            // 4. Βήμα 11 Λεκτικού: Εμφάνιση της λίστας στο Προφίλ Φοιτητή (updateActiveOffersList)
            profile.updateActiveOffersList(newOffer);
            System.out.println("[Controller] Ενημερώθηκε η λίστα ενεργών προσφορών στο StudentProfile.");
            return true;
        }

        return false;
    }

    /**
     * Η αναδρομική μέθοδος checkFields() του Controller
     */
    public boolean checkFields() {
        if (this.currentCourseId == null || this.currentCourseId.isBlank()) return false;
        if (this.currentHelpType == null || this.currentHelpType.isBlank()) return false;

        boolean hasFile = (this.currentNotesFile != null && !this.currentNotesFile.isBlank());
        boolean hasMeeting = (this.currentMeetingUrl != null && !this.currentMeetingUrl.isBlank());

        return hasFile || hasMeeting;
    }
}