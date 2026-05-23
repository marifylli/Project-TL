package com.unipath.repository;

import com.unipath.dataBase.DBManager;
import com.unipath.model.Notification;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationRepository {
    // Βήμα 10: Ανάκτηση φοιτητών και μαζική αποστολή (Batch Insert) ειδοποιήσεων
    public void notifyAllStudentsAboutCourse(String message) {
        String fetchStudentsSql = "SELECT userId FROM Student";
        String insertNotificationSql = """
            INSERT INTO Notification (recipientId, senderId, message, dateSent, isRead, notificationType) 
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = dateFormat.format(new Date());

        try (Connection conn = DBManager.getInstance().connect();
             PreparedStatement pstmtFetch = conn.prepareStatement(fetchStudentsSql);
             ResultSet rs = pstmtFetch.executeQuery()) {

            try (PreparedStatement pstmtInsert = conn.prepareStatement(insertNotificationSql)) {
                while (rs.next()) {
                    // Δημιουργία του Model Object
                    Notification notification = new Notification(
                            rs.getString("userId"),
                            "Secretary",
                            message,
                            currentDate,
                            "NEW_COURSE"
                    );

                    // Αντιστοίχιση των πεδίων του Model στην SQL
                    pstmtInsert.setString(1, notification.getRecipientId());
                    pstmtInsert.setString(2, notification.getSenderId());
                    pstmtInsert.setString(3, notification.getMessage());
                    pstmtInsert.setString(4, notification.getDateSent());
                    pstmtInsert.setInt(5, notification.getIsRead());
                    pstmtInsert.setString(6, notification.getNotificationType());

                    pstmtInsert.addBatch();
                }
                pstmtInsert.executeBatch(); // Εκτέλεση όλων μαζί για μέγιστη ταχύτητα
                System.out.println("Οι ειδοποιήσεις στάλθηκαν επιτυχώς σε όλους τους φοιτητές.");
            }
        } catch (SQLException e) {
            System.err.println("Σφάλμα κατά τη δημιουργία ειδοποιήσεων στο NotificationRepository: " + e.getMessage());
        }
    }
}
