package com.unipath.login;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import com.unipath.login.User;
import com.unipath.login.EmailOtpService;
import com.unipath.login.FirestoreService;

public class LoginController {

    @FXML
    private Circle dot1, dot2, dot3;
    @FXML private VBox screenEmail, screenOtp, screenSuccess;

    @FXML private TextField emailField;
    @FXML private Label errorLabel;
    @FXML private Button sendOtpButton;

    @FXML private TextField otp1, otp2, otp3, otp4, otp5, otp6;
    @FXML private Label sentToLabel, timerLabel, otpErrorLabel;

    @FXML private Label roleLabel, redirectLabel;

    private String generatedOtp;
    private String currentEmail;
    private final EmailOtpService otpService = new EmailOtpService();
    private final FirestoreService firestoreService = new FirestoreService();
    private Timeline countdownTimer;
    private int secondsLeft = 299;
    private User loggedInUser;

    private void showScreen(int step) {
        screenEmail.setVisible(step == 1);   screenEmail.setManaged(step == 1);
        screenOtp.setVisible(step == 2);     screenOtp.setManaged(step == 2);
        screenSuccess.setVisible(step == 3); screenSuccess.setManaged(step == 3);
        dot1.getStyleClass().setAll(step >= 1 ? "dot-active" : "dot-inactive");
        dot2.getStyleClass().setAll(step >= 2 ? "dot-active" : "dot-inactive");
        dot3.getStyleClass().setAll(step >= 3 ? "dot-active" : "dot-inactive");
    }

    @FXML
    private void onEmailTyped() {
        String email = emailField.getText().trim();
        boolean valid = isValidEmail(email);
        sendOtpButton.setDisable(!valid);
        errorLabel.setText("");
    }

    @FXML
    private void onSendOtp() {
        currentEmail = emailField.getText().trim();
        if (!isValidEmail(currentEmail)) {
            errorLabel.setText("Μη έγκυρο email.");
            return;
        }
        sendOtpButton.setDisable(true);
        sendOtpButton.setText("Αποστολή...");
        new Thread(() -> {
            try {
                generatedOtp = otpService.sendOtp(currentEmail);
                Platform.runLater(() -> {
                    sentToLabel.setText("Στάλθηκε στο: " + currentEmail);
                    clearOtpFields();
                    startCountdown();
                    showScreen(2);
                    otp1.requestFocus();
                    sendOtpButton.setText("Αποστολή κωδικού →");
                    sendOtpButton.setDisable(false);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    errorLabel.setText("Σφάλμα: " + e.getMessage());
                    sendOtpButton.setText("Αποστολή κωδικού →");
                    sendOtpButton.setDisable(false);
                });
            }
        }).start();
    }

    @FXML
    private void onOtpKey(javafx.scene.input.KeyEvent event) {
        TextField src = (TextField) event.getSource();
        if (src.getText().length() > 1) src.setText(String.valueOf(src.getText().charAt(0)));
        TextField[] boxes = {otp1, otp2, otp3, otp4, otp5, otp6};
        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i] == src) {
                if (!src.getText().isEmpty() && i < boxes.length - 1) boxes[i+1].requestFocus();
                else if (src.getText().isEmpty() && i > 0 &&
                        event.getCode() == javafx.scene.input.KeyCode.BACK_SPACE) boxes[i-1].requestFocus();
                break;
            }
        }
        if (getEnteredOtp().length() == 6) onVerifyOtp();
    }

    @FXML
    private void onVerifyOtp() {
        String entered = getEnteredOtp();

        // --- LOGIC ΓΙΑ TESTING ---
        if (currentEmail.startsWith("test.") && entered.equals("000000")) {
            generatedOtp = "000000"; // Παράκαμψη για τα test emails
        }
        // -------------------------

        if (entered.length() < 6) { otpErrorLabel.setText("Συμπληρώστε 6 ψηφία."); return; }
        if (!entered.equals(generatedOtp)) {
            otpErrorLabel.setText("Λάθος κωδικός.");
            return;
        }

        stopCountdown();
        new Thread(() -> {
            try {
                // --- ΕΛΕΓΧΟΣ ΓΙΑ MOCK USER ---
                if (currentEmail.startsWith("test.")) {
                    loggedInUser = getMockUser(currentEmail);
                } else {
                    loggedInUser = firestoreService.getUserByEmail(currentEmail);
                }
                // ----------------------------

                // --- ΕΝΣΩΜΑΤΩΣΗ ΔΥΝΑΜΙΚΟΥ USER SESSION ---
                int intUserId;
                try {
                    // Μετατροπή UID σε int
                    intUserId = Integer.parseInt(loggedInUser.getUid().replaceAll("[^0-9]", ""));
                } catch (NumberFormatException e) {
                    intUserId = Math.abs(loggedInUser.getUid().hashCode() % 100000);
                }

                // ΔΙΟΡΘΩΣΗ: Χρησιμοποιούμε τη σωστή μέθοδο getDisplayName() από την κλάση User
                UserSession.getInstance().startSession(
                        intUserId,
                        loggedInUser.getEmail(),
                        loggedInUser.getDisplayName(), // ΕΔΩ ΗΤΑΝ ΤΟ ΛΑΘΟΣ
                        loggedInUser.getRole().name()
                );
                // -----------------------------------------

                Platform.runLater(() -> {
                    roleLabel.setText("Ρόλος: " + loggedInUser.getRoleDisplayName());
                    redirectLabel.setText("Ανακατεύθυνση στο dashboard...");
                    showScreen(3);
                });
            } catch (Exception e) {
                Platform.runLater(() -> otpErrorLabel.setText("Σφάλμα: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void onResend() {
        stopCountdown();
        clearOtpFields();
        new Thread(() -> {
            try {
                generatedOtp = otpService.sendOtp(currentEmail);
                Platform.runLater(() -> {
                    secondsLeft = 299;
                    startCountdown();
                    otpErrorLabel.setText("Νέος κωδικός εστάλη.");
                });
            } catch (Exception e) {
                Platform.runLater(() -> otpErrorLabel.setText("Σφάλμα: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void onBack() { stopCountdown(); showScreen(1); }

    @FXML
    private void onEnterDashboard() {
        // ΔΙΟΡΘΩΣΗ: Μεταφέρουμε όλη τη φόρτωση και αλλαγή οθονών στο JavaFX Application Thread
        Platform.runLater(() -> {
            try {
                javafx.fxml.FXMLLoader loader;

                switch (loggedInUser.getRole()) {
                    case STUDENT:
                        java.net.URL studentFxml = getClass().getResource("/fxml/Student/student-main-screen.fxml");
                        if (studentFxml == null) {
                            studentFxml = getClass().getClassLoader().getResource("fxml/Student/student-main-screen.fxml");
                        }
                        if (studentFxml == null) {
                            throw new java.io.IOException("Το αρχείο student-main-screen.fxml δεν βρέθηκε στον φάκελο fxml/Student/");
                        }
                        loader = new javafx.fxml.FXMLLoader(studentFxml);
                        break;

                    case PROFESSOR:
                        loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/Professor/professor-main-view.fxml"));
                        break;

                    case SECRETARY:
                        loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/Secretary/secretary-main-screen.fxml"));
                        break;


                    default:
                        loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/Professor/professor-main-view.fxml"));
                        break;
                }

                // Φόρτωση του FXML που επιλέχθηκε
                javafx.scene.Parent root = loader.load();

                // Αλλαγή της σκηνής στο παράθυρο με ασφάλεια
                javafx.stage.Stage stage = (javafx.stage.Stage) emailField.getScene().getWindow();
                stage.setScene(new javafx.scene.Scene(root, 1000, 650));
                stage.setTitle("UniPath - Dashboard");
                stage.show();

            } catch (Exception e) {
                System.out.println("Σφάλμα φόρτωσης οθόνης: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }


    private User getMockUser(String email) {
        if (email.startsWith("test.secretary")) {
            return new User("1001", email, Role.SECRETARY, "Γραμματεία (Test)");
        } else if (email.startsWith("test.student")) {
            return new User("2002", email, Role.STUDENT, "Φοιτητής (Test)");
        } else {
            // Δυναμικό Test για Καθηγητές: Αν γράψεις π.χ. test.georgiou@ceid.upatras.gr,
            // θα βγάλει το όνομα "Prof. test.georgiou" αντί για καρφωμένο κείμενο
            String namePart = email.substring(5, email.indexOf("@"));
            String capitalizedName = namePart.substring(0, 1).toUpperCase() + namePart.substring(1);
            return new User("1", email, Role.PROFESSOR, capitalizedName + " (Test)");
        }
    }

    private boolean isValidEmail(String email) {
        if (email.startsWith("test.")) return true; // Επιτρέπουμε άμεσα όλα τα test emails
        if (email.equals("secretary@ceid.upatras.gr")) return true;
        if (email.matches("st\\d+@ceid\\.upatras\\.gr")) return true;
        if (email.matches(".+@ceid\\.upatras\\.gr")) return true;
        return false;
    }

    private String getEnteredOtp() {
        return otp1.getText() + otp2.getText() + otp3.getText()
                + otp4.getText() + otp5.getText() + otp6.getText();
    }

    private void clearOtpFields() {
        for (TextField f : new TextField[]{otp1, otp2, otp3, otp4, otp5, otp6}) f.clear();
    }

    private void startCountdown() {
        secondsLeft = 299;
        countdownTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsLeft--;
            int m = secondsLeft / 60, s = secondsLeft % 60;
            timerLabel.setText(String.format("(0%d:%02d)", m, s));
            if (secondsLeft <= 0) { stopCountdown(); timerLabel.setText("(λήξε)"); }
        }));
        countdownTimer.setCycleCount(299);
        countdownTimer.play();
    }

    private void stopCountdown() { if (countdownTimer != null) countdownTimer.stop(); }
}