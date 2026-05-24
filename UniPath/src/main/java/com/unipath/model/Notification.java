package com.unipath.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Notification {

    // 1. Πεδία της κλάσης (Αντιστοιχούν στις στήλες του πίνακα Notification στη ΒΔ)
    private int notificationId;
    private int recipientId;
    private String senderId;
    private String message;
    private String dateSent;
    private int isRead;
    private String notificationType;

    // 2. Default Κατασκευαστής (Constructor)
    public Notification() {
    }

    // 3. Πλήρης Κατασκευαστής
    public Notification(int notificationId, int recipientId, String senderId, String message, String dateSent, int isRead, String notificationType) {
        this.notificationId = notificationId;
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.message = message;
        this.dateSent = dateSent;
        this.isRead = isRead;
        this.notificationType = notificationType;
    }

    // 4. Σταθερά (URL) για τη σύνδεση με την SQLite βάση σου
    private static final String URL = "jdbc:sqlite:unipath.db"; // Προσάρμοσε το path αν η βάση σου είναι σε άλλον φάκελο

    /**
     * Η ΜΕΘΟΔΟΣ ΑΠΟ ΤΟ SEQUENCE DIAGRAM (SD)
     * Αναλαμβάνει να κάνει INSERT την ειδοποίηση απευθείας στον πίνακα Notification της βάσης δεδομένων.
     */
    public static boolean sendNotification(int recipientId, String senderId, String message, String notificationType) {
        String sql = "INSERT INTO Notification (recipientId, senderId, message, dateSent, isRead, notificationType) " +
                "VALUES (?, ?, ?, datetime('now'), 0, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, recipientId);
            pstmt.setString(2, senderId);
            pstmt.setString(3, message);
            pstmt.setString(4, notificationType);

            int rows = pstmt.executeUpdate();
            return rows > 0; // Επιστρέφει true αν η εγγραφή έγινε με επιτυχία

        } catch (SQLException e) {
            System.err.println("❌ [Notification Model Error] Αποτυχία κατά την εκτέλεση του sendNotification: " + e.getMessage());
            return false;
        }
    }

    // 5. Getters και Setters (Απαραίτητα για ένα ολοκληρωμένο Model)
    public int getNotificationId() { return notificationId; }
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }

    public int getRecipientId() { return recipientId; }
    public void setRecipientId(int recipientId) { this.recipientId = recipientId; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getDateSent() { return dateSent; }
    public void setDateSent(String dateSent) { this.dateSent = dateSent; }

    public int getIsRead() { return isRead; }
    public void setIsRead(int isRead) { this.isRead = isRead; }

    public String getNotificationType() { return notificationType; }
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }
}