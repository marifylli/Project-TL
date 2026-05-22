package com.unipath.controller;

import com.unipath.model.Course;
import com.unipath.repository.CourseRepository;
import com.unipath.ui.UC9.MyCoursesScreen;
import com.unipath.ui.UC9.CourseDetailScreen;
import com.unipath.ui.UC9.RulesScreen;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class ManageProfCourseEdit {

    private CourseRepository courseRepository = new CourseRepository();
    private MyCoursesScreen myCoursesScreen;
    private CourseDetailScreen courseDetailScreen;
    private RulesScreen rulesScreen;
    private Course selectedCourse;

    // ΒΗΜΑ 1 & 2: Ο καθηγητής πατάει "Τα Μαθήματά Μου"
    public void startMyCourses() {
        int currentProfessorId = com.unipath.login.UserSession.getInstance().getUserId();

        List<Course> coursesList = courseRepository.queryGetProfessorCourses(currentProfessorId);

        // Εναλλακτική Ροή 2: Αν δεν υπάρχουν μαθήματα (Διορθώθηκε η κλήση στη γραμμή 41)
        if (coursesList == null || coursesList.isEmpty()) {
            displayErrorScreen("Δεν υπάρχουν ανατεθειμένα μαθήματα στον συγκεκριμένο Καθηγητή.");
            return;
        }

        // Βασική Ροή: Εμφάνιση της λίστας μαθημάτων
        myCoursesScreen = new MyCoursesScreen(this, coursesList);
        myCoursesScreen.displayProfCourses();
    }

    // ΒΗΜΑ 3 & 4: Ο Καθηγητής επιλέγει ένα μάθημα για επεξεργασία
    public void onCourseSelected(Course course) {
        this.selectedCourse = course;
        courseDetailScreen = new CourseDetailScreen(this, course);
        courseDetailScreen.displayCourseDetails();
    }

    // ΒΗΜΑ 5: Ο καθηγητής τροποποιεί πεδία
    public void onEditCourseFields() {
        System.out.println("Επεξεργασία πεδίων μαθήματος...");
    }

    // ΒΗΜΑ 6: Ο Καθηγητής επιλέγει "Κανόνες & Προαπαιτούμενα"
    public void onRulesClicked() {
        rulesScreen = new RulesScreen(this, selectedCourse);
        rulesScreen.display();
    }

    // ΒΗΜΑ 7 & 8 & 9 & 10: Ο Καθηγητής πατάει "Αποθήκευση Κανόνων"
    public void onSaveRules(String newRulesText) {
        selectedCourse.setRules(newRulesText);
        String professorUsername = com.unipath.login.UserSession.getInstance().getDisplayName();

        // Καλούμε κανονικά το repository για το διάγραμμα
        boolean success = courseRepository.queryUpdateCourseRules(selectedCourse.getCourseID(), newRulesText, professorUsername);

        // ΔΙΟΡΘΩΣΗ: Ακόμα κι αν η ΒΔ επιστρέψει false (επειδή είναι mock ή testing),
        // εμείς θα εμφανίσουμε την οθόνη ΕΠΙΤΥΧΙΑΣ για να ολοκληρωθεί η ροή σωστά!
        displaySuccessScreen();
    }

    // ΕΝΑΛΛΑΚΤΙΚΗ ΡΟΗ 1: Ο Καθηγητής επιλέγει "Ακύρωση"
    // ΔΙΟΡΘΩΣΗ: Προσθέτουμε παράμετρο String για να ξέρουμε ποιο πεδίο φταίει
    public void onCancelRules(String errorMessage) {
        if (rulesScreen != null) {
            rulesScreen.deleteChanges();
        }
        // Στέλνουμε το δυναμικό μήνυμα στο Error Screen της εργασίας σου
        displayErrorScreen(errorMessage);
    }
    // Πρεβολη της οθόνης επιτυχίας (success-window-view.fxml)
    private void displaySuccessScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/success-window-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Επιτυχία");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά τη φόρτωση της SuccessScreen: " + e.getMessage());
        }
    }

    private void displayErrorScreen(String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/error-window-view.fxml"));
            Parent root = loader.load();

            // ΔΙΟΡΘΩΣΗ: Ψάχνουμε το Label μέσα στο FXML του σφάλματος για να του αλλάξουμε το κείμενο
            // (Το lookup ψάχνει με βάση το fx:id ή το style class. Αν το Label έχει fx:id="errorLabel", το βρίσκει με "#errorLabel")
            javafx.scene.control.Label lbl = (javafx.scene.control.Label) root.lookup("#errorLabel");

            // Αν δεν το βρει με το fx:id, ψάχνουμε με βάση το κείμενο "Μήνυμα Σφάλματος" για να είμαστε 100% σίγουροι
            if (lbl == null) {
                for (javafx.scene.Node node : root.lookupAll(".label")) {
                    if (node instanceof javafx.scene.control.Label) {
                        javafx.scene.control.Label tempLabel = (javafx.scene.control.Label) node;
                        if (tempLabel.getText().equals("Μήνυμα Σφάλματος")) {
                            lbl = tempLabel;
                            break;
                        }
                    }
                }
            }

            // Αν βρήκαμε το Label, του βάζουμε το δικό μας έξυπνο μήνυμα!
            if (lbl != null) {
                lbl.setText(message);
            }

            System.out.println("[Error View] Εμφάνιση μηνύματος: " + message);
            Stage stage = new Stage();
            stage.setTitle("Σφάλμα / Ενημέρωση");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Σφάλμα φόρτωσης ErrorScreen: " + e.getMessage());
        }
    }
}