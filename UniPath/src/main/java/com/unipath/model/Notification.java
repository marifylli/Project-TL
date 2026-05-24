package com.unipath.model;

import com.unipath.dataBase.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Notification {
    // Πεδία δεδομένων
    private String recipientId;
    private String senderId;
    private String message;
    private String dateSent;
    private int isRead;
    private String notificationType;

    // Constructor για το Repository (για το Batch Insert)
    public Notification(String recipientId, String senderId, String message, String dateSent, int isRead, String notificationType) {
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.message = message;
        this.dateSent = dateSent;
        this.isRead = isRead;
        this.notificationType = notificationType;
    }

    // Getters για να μπορεί το Repository να διαβάσει τα δεδομένα
    public String getRecipientId() { return recipientId; }
    public String getSenderId() { return senderId; }
    public String getMessage() { return message; }
    public String getDateSent() { return dateSent; }
    public int getIsRead() { return isRead; }
    public String getNotificationType() { return notificationType; }

    // Στατική μέθοδος για μεμονωμένη αποστολή (UC11)
    public static void sendNotification(int studentId, int professorId, int diplomaticId) {
        String messageText = "Ο φοιτητής με ID " + studentId + " έκλεισε ραντεβού για τη διπλωματική σας (ID: " + diplomaticId + ").";
        String sql = "INSERT INTO Notification (recipientId, senderId, message, notificationType, dateSent) " +
                "VALUES (?, ?, ?, 'APPOINTMENT', datetime('now'))";

        try (Connection conn = DBManager.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "prof" + professorId);
            pstmt.setString(2, "student" + studentId);
            pstmt.setString(3, messageText);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Σφάλμα SQL στο Notification Model: " + e.getMessage());
        }
    }
}