package com.unipath.model;

import com.unipath.dataBase.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Notification {


    public static void sendNotification(int studentId, int professorId, int diplomaticId) {
        String messageText = "Ο φοιτητής με ID " + studentId + " έκλεισε ραντεβού για τη διπλωματική σας (ID: " + diplomaticId + ").";
        String sql = "INSERT INTO Notification (recipientId, senderId, message, notificationType, dateSent) " +
                "VALUES ('prof" + professorId + "', 'student" + studentId + "', ?, 'APPOINTMENT', datetime('now'))";

        try (Connection conn = DBManager.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, messageText);
            pstmt.executeUpdate();
            System.out.println("[BACKEND] [UC11]: Το Notification καταγράφηκε επιτυχώς μέσω του Notification Model!");
        } catch (SQLException e) {
            System.err.println("Σφάλμα SQL στο Notification Model: " + e.getMessage());
        }
    }
}