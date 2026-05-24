package com.unipath.controller;

import com.unipath.model.HelpOffer;
import com.unipath.model.StudentProfile;
import com.unipath.model.Course;
import com.unipath.dataBase.DBManager;
import com.unipath.repository.HelpOfferRepository;
import com.unipath.ui.common.SuccessScreen;
import com.unipath.ui.common.ErrorScreen;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.ArrayList;

public class ManageMentorProfile {

    private String currentCourseId;
    private String currentHelpType;
    private String currentNotesFile;
    private String currentMeetingUrl;
    private Stage mainStage;

    public ManageMentorProfile() {
    }

    /**
     * ΕΚΚΙΝΗΣΗ ΡΟΗΣ UC7: Καλείται όταν ο χρήστης πατάει "Add New Offer" στη StudentMainScreen
     */
    public void startMentorFlow(Stage stage) {
        this.mainStage = stage;
        navigateToNewOfferForm();
    }

    /**
     * Μετάβαση στην πρώτη φόρμα επιλογής μαθήματος (NewOfferFormScreen)
     */
    public void navigateToNewOfferForm() {
        try {
            java.net.URL fxmlLocation = getClass().getResource("/fxml/Student/new-offer-form-view.fxml");
            if (fxmlLocation == null) {
                fxmlLocation = getClass().getClassLoader().getResource("fxml/Student/new-offer-form-view.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            // ✨ ΔΙΑΣΩΣΗ: Αν το mainStage είναι null, "κλέβουμε" το τρέχον ανοιχτό παράθυρο
            if (this.mainStage == null) {
                this.mainStage = (Stage) Stage.getWindows().filtered(window -> window.isShowing()).get(0);
            }

            mainStage.setScene(new Scene(root, 1000, 650));
            mainStage.setTitle("UniPath - Επιλογή Μαθήματος & Τύπου Βοήθειας");
            mainStage.show();
        } catch (Exception e) {
            System.err.println("❌ Σφάλμα κατά τη φόρτωση του new-offer-form-view.fxml:");
            e.printStackTrace();
            showErrorPopup("Αδυναμία φόρτωσης της οθόνης φόρμας.");
        }
    }

    /**
     * Μετάβαση στη δεύτερη φόρμα υποβολής αρχείων (OfferSubmissionFormScreen)
     */
    public void navigateToOfferSubmission(String selectedCourse, String assistanceType) {
        try {
            // Καταγράφουμε τις επιλογές από το 1ο βήμα για να μην χαθούν
            selectCourse(selectedCourse);
            selectHelp(assistanceType);

            // Κρατάμε το Path με το σωστό, κεφαλαίο Student
            java.net.URL fxmlLocation = getClass().getResource("/fxml/Student/offer-submission-view.fxml");
            if (fxmlLocation == null) {
                fxmlLocation = getClass().getClassLoader().getResource("fxml/Student/offer-submission-view.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            // ✨ ΔΙΑΣΩΣΗ: Αν το mainStage είναι null, "κλέβουμε" το τρέχον ανοιχτό παράθυρο
            if (this.mainStage == null) {
                this.mainStage = (Stage) Stage.getWindows().filtered(window -> window.isShowing()).get(0);
            }

            mainStage.setScene(new Scene(root, 1000, 650));
            mainStage.setTitle("UniPath - Συμπλήρωση Στοιχείων Προσφοράς");

        } catch (Exception e) {
            System.err.println("❌ Σφάλμα κατά το φόρτωμα του offer-submission-view.fxml:");
            e.printStackTrace();
            showErrorPopup("Αδυναμία φόρτωσης της οθόνης υποβολής.");
        }
    }

    /**
     * Εμφάνιση του Κοινού SuccessScreen ως Αναδυόμενο Παράθυρο (Popup)
     */
    public void showSuccessPopup(String message) {
        try {
            java.net.URL fxmlLocation = getClass().getResource("/fxml/common/success-window-view.fxml");
            if (fxmlLocation == null) {
                fxmlLocation = getClass().getClassLoader().getResource("fxml/common/success-window-view.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            SuccessScreen controller = loader.getController();
            controller.setSuccessMessage(message);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Επιτυχία");
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();

            returnToMainMenu();
        } catch (Exception e) {
            System.err.println("❌ Σφάλμα κατά την εμφάνιση του SuccessScreen: " + e.getMessage());
        }
    }

    /**
     * Εμφάνιση του Κοινού ErrorScreen ως Αναδυόμενο Παράθυρο (Popup)
     */
    public void showErrorPopup(String message) {
        try {
            java.net.URL fxmlLocation = getClass().getResource("/fxml/common/error-window-view.fxml");
            if (fxmlLocation == null) {
                fxmlLocation = getClass().getClassLoader().getResource("fxml/common/error-window-view.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            ErrorScreen controller = loader.getController();
            controller.setErrorMessage(message);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Σφάλμα");
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();
        } catch (Exception e) {
            System.err.println("❌ Σφάλμα κατά την εμφάνιση του ErrorScreen: " + e.getMessage());
        }
    }

    /**
     * Επιστροφή στην Κεντρική Οθόνη Φοιτητή μετά από επιτυχία ή ακύρωση
     */
    public void returnToMainMenu() {
        try {
            // Διορθώθηκε σε Κεφαλαίο Student και προστέθηκε ασφαλής έλεγχος τοποθεσίας
            java.net.URL fxmlLocation = getClass().getResource("/fxml/Student/student-main-screen.fxml");
            if (fxmlLocation == null) {
                fxmlLocation = getClass().getClassLoader().getResource("fxml/Student/student-main-screen.fxml");
            }

            Parent root = FXMLLoader.load(fxmlLocation);

            if (this.mainStage == null) {
                this.mainStage = (Stage) Stage.getWindows().filtered(window -> window.isShowing()).get(0);
            }

            mainStage.setScene(new Scene(root, 1000, 650));
            mainStage.setTitle("UniPath - Κεντρικό Μενού");
        } catch (Exception e) {
            System.err.println("❌ Σφάλμα κατά την επιστροφή στο κεντρικό μενού: " + e.getMessage());
        }
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
                course.setCourseID(rs.getString("courseId")); // Προσοχή στο Case της SQLite
                course.setTitle(rs.getString("title"));
                course.setSemester(rs.getInt("semester"));
                course.setECTS(rs.getInt("ects"));
                coursesList.add(course);
            }
        } catch (java.sql.SQLException e) {
            System.err.println("❌ Σφάλμα στην queryCourses: " + e.getMessage());
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
            showErrorPopup("Παρακαλώ συμπληρώστε όλα τα απαιτούμενα πεδία (Μάθημα, Τύπος Βοήθειας και τουλάχιστον ένα Αρχείο ή Σύνδεσμο).");
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
            profile.updateActiveOffersList(newOffer);
            System.out.println("[Controller] Ενημερώθηκε η λίστα ενεργών προσφορών στο StudentProfile.");

            showSuccessPopup("Η προσφορά ακαδημαϊκής βοήθειας δημοσιεύτηκε με επιτυχία!");
            return true;
        }

        showErrorPopup("Παρουσιάστηκε σφάλμα κατά την αποθήκευση της προσφοράς στη βάση δεδομένων.");
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