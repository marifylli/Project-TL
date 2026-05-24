package com.unipath.repository;

import com.unipath.dataBase.DBManager;
import com.unipath.model.Notification;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationRepository {

    public void notifyAllStudentsAboutCourse(String message) {
        String fetchStudentsSql = "SELECT userId FROM Student";
        String insertNotificationSql = "INSERT INTO Notification (recipientId, senderId, message, dateSent, isRead, notificationType) VALUES (?, ?, ?, ?, ?, ?)";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = dateFormat.format(new Date());

        try (Connection conn = DBManager.getInstance().connect();
             PreparedStatement pstmtFetch = conn.prepareStatement(fetchStudentsSql);
             ResultSet rs = pstmtFetch.executeQuery()) {

            try (PreparedStatement pstmtInsert = conn.prepareStatement(insertNotificationSql)) {
                while (rs.next()) {
                    // Δημιουργία Notification object με 6 παραμέτρους
                    Notification notification = new Notification(
                            rs.getString("userId"),
                            "Secretary",
                            message,
                            currentDate,
                            0, // isRead = 0 (false)
                            "NEW_COURSE"
                    );

                    pstmtInsert.setString(1, notification.getRecipientId());
                    pstmtInsert.setString(2, notification.getSenderId());
                    pstmtInsert.setString(3, notification.getMessage());
                    pstmtInsert.setString(4, notification.getDateSent());
                    pstmtInsert.setInt(5, notification.getIsRead());
                    pstmtInsert.setString(6, notification.getNotificationType());

                    pstmtInsert.addBatch();
                }
                pstmtInsert.executeBatch();
                System.out.println("Οι ειδοποιήσεις στάλθηκαν επιτυχώς σε όλους τους φοιτητές.");
            }
        } catch (SQLException e) {
            System.err.println("Σφάλμα στο NotificationRepository: " + e.getMessage());
        }
    }
}