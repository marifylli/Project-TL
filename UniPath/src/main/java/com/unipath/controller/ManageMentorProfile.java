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

    private static String currentCourseId;
    private static String currentHelpType;
    private String currentNotesFile;
    private String currentMeetingUrl;
    private Stage mainStage;

    public ManageMentorProfile() {
    }


    public void startMentorFlow(Stage stage) {
        this.mainStage = stage;
        navigateToNewOfferForm();
    }


    public void navigateToNewOfferForm() {
        try {
            java.net.URL fxmlLocation = getClass().getResource("/fxml/Student/new-offer-form-view.fxml");
            if (fxmlLocation == null) {
                fxmlLocation = getClass().getClassLoader().getResource("fxml/Student/new-offer-form-view.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();


            if (this.mainStage == null) {
                this.mainStage = (Stage) Stage.getWindows().filtered(window -> window.isShowing()).get(0);
            }

            mainStage.setScene(new Scene(root, 1000, 650));
            mainStage.setTitle("UniPath - Επιλογή Μαθήματος & Τύπου Βοήθειας");
            mainStage.show();
        } catch (Exception e) {
            System.err.println(" Σφάλμα κατά τη φόρτωση του new-offer-form-view.fxml:");
            e.printStackTrace();
            showErrorPopup("Αδυναμία φόρτωσης της οθόνης φόρμας.");
        }
    }


    public void navigateToOfferSubmission(String selectedCourse, String assistanceType) {
        try {

            selectCourse(selectedCourse);
            selectHelp(assistanceType);


            java.net.URL fxmlLocation = getClass().getResource("/fxml/Student/offer-submission-view.fxml");
            if (fxmlLocation == null) {
                fxmlLocation = getClass().getClassLoader().getResource("fxml/Student/offer-submission-view.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();


            if (this.mainStage == null) {
                this.mainStage = (Stage) Stage.getWindows().filtered(window -> window.isShowing()).get(0);
            }

            mainStage.setScene(new Scene(root, 1000, 650));
            mainStage.setTitle("UniPath - Συμπλήρωση Στοιχείων Προσφοράς");

        } catch (Exception e) {
            System.err.println(" Σφάλμα κατά το φόρτωμα του offer-submission-view.fxml:");
            e.printStackTrace();
            showErrorPopup("Αδυναμία φόρτωσης της οθόνης υποβολής.");
        }
    }


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
            System.err.println(" Σφάλμα κατά την εμφάνιση του SuccessScreen: " + e.getMessage());
        }
    }


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
            System.err.println("Σφάλμα κατά την εμφάνιση του ErrorScreen: " + e.getMessage());
        }
    }


    public void returnToMainMenu() {
        try {

            java.net.URL fxmlLocation = getClass().getResource("/fxml/Student/student-main-view.fxml");

            if (fxmlLocation == null) {
                fxmlLocation = getClass().getClassLoader().getResource("fxml/Student/student-main-view.fxml");
            }
            if (fxmlLocation == null) {
                fxmlLocation = getClass().getResource("/fxml/Student/student-main-screen.fxml");
            }
            if (fxmlLocation == null) {
                fxmlLocation = getClass().getClassLoader().getResource("fxml/Student/student-main-screen.fxml");
            }


            if (fxmlLocation == null) {
                System.err.println("  ΣΦΑΛΜΑ: Δεν βρέθηκε πουθενά το αρχείο FXML της Κεντρικής Οθόνης!");
                showErrorPopup("Αδυναμία επιστροφής στο κεντρικό μενού. Ελέγξτε τα FXML Paths.");
                return;
            }

            System.out.println("✓ [UI Flow] Φόρτωση κεντρικής οθόνης από: " + fxmlLocation.getPath());
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            com.unipath.ui.common.StudentMainScreen mainController = loader.getController();
            if (mainController != null) {

                mainController.loadSubmittedStudyPlans();

                mainController.loadActiveHelpOffers();
            }

            if (this.mainStage == null) {
                this.mainStage = (Stage) Stage.getWindows().filtered(window -> window.isShowing()).get(0);
            }

            mainStage.setScene(new Scene(root, 1000, 650));
            mainStage.setTitle("UniPath - Κεντρικό Μενού");
            mainStage.show();

        } catch (Exception e) {
            System.err.println(" Σφάλμα κατά την επιστροφή στο κεντρικό μενού: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public List<Course> queryCourses() {
        System.out.println("[Controller] Ανάκτηση μαθημάτων από την κλάση Course.");
        List<Course> coursesList = new ArrayList<>();
        String sql = "SELECT * FROM Course WHERE isActive = 1";

        try (java.sql.Connection conn = DBManager.getInstance().connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getString("courseId"));
                course.setTitle(rs.getString("title"));
                course.setSemester(rs.getInt("semester"));
                course.setECTS(rs.getInt("ects"));
                coursesList.add(course);
            }
        } catch (java.sql.SQLException e) {
            System.err.println(" Σφάλμα στην queryCourses: " + e.getMessage());
        }
        return coursesList;
    }


    public void selectCourse(String courseId) { this.currentCourseId = courseId; }
    public void selectHelp(String helpType) { this.currentHelpType = helpType; }
    public void offerDataType(String notesFile, String meetingUrl) {
        this.currentNotesFile = notesFile;
        this.currentMeetingUrl = meetingUrl;
    }


    public boolean postOffer(StudentProfile profile) {
        System.out.println("[Controller] Έναρξη postOffer().");


        if (!checkFields()) {
            System.out.println("[Controller] [Alternative Path] Σφάλμα: Τα πεδία δεν συμπληρώθηκαν σωστά.");
            showErrorPopup("Παρακαλώ συμπληρώστε όλα τα απαιτούμενα πεδία (Μάθημα, Τύπος Βοήθειας και τουλάχιστον ένα Αρχείο ή Σύνδεσμο).");
            return false;
        }
        System.out.println("[Controller] Ο έλεγχος πεδίων πέτυχε.");


        HelpOffer newOffer = HelpOffer.create(
                profile.getStudentId(),
                this.currentCourseId,
                this.currentHelpType,
                this.currentNotesFile,
                this.currentMeetingUrl
        );
        System.out.println("[Controller] Δημιουργήθηκε το αντικείμενο HelpOffer.");


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


    public boolean checkFields() {

        System.out.println("DEBUG [CheckFields] CourseID: " + this.currentCourseId);
        System.out.println("DEBUG [CheckFields] HelpType: " + this.currentHelpType);
        System.out.println("DEBUG [CheckFields] NotesFile: '" + this.currentNotesFile + "'");
        System.out.println("DEBUG [CheckFields] MeetingUrl: '" + this.currentMeetingUrl + "'");


        if (this.currentCourseId == null || this.currentCourseId.trim().isEmpty()) return false;
        if (this.currentHelpType == null || this.currentHelpType.trim().isEmpty()) return false;


        boolean hasFile = false;
        if (this.currentNotesFile != null) {
            String cleanFile = this.currentNotesFile.trim();
            if (!cleanFile.isEmpty() && !cleanFile.equalsIgnoreCase("null") && cleanFile.length() > 3) {
                hasFile = true;
            }
        }


        boolean hasMeeting = false;
        if (this.currentMeetingUrl != null) {
            String cleanUrl = this.currentMeetingUrl.trim();
            if (!cleanUrl.isEmpty() && !cleanUrl.equalsIgnoreCase("null") && cleanUrl.length() > 3) {
                hasMeeting = true;
            }
        }

        System.out.println("DEBUG [CheckFields] Τελικό αποτέλεσμα -> Έχει Αρχείο: " + hasFile + " | Έχει Σύνδεσμο: " + hasMeeting);


        return hasFile || hasMeeting;
    }
}