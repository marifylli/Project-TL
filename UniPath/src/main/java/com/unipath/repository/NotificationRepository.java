package com.unipath.repository;

import com.unipath.dataBase.DBManager;
import com.unipath.model.Notification;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationRepository {

    public void notifyAllStudentsAboutCourse(String messageText) {
        // 1. Φέρνουμε ΜΟΝΟ τα userId των φοιτητών που υπάρχουν πραγματικά στον πίνακα User (INNER JOIN)
        String selectQuery = """
        SELECT Student.userId 
        FROM Student 
        INNER JOIN User ON Student.userId = User.userId
    """;

        // 2. Το INSERT χρησιμοποιεί το 'sec1' ως senderId, το οποίο υπάρχει 100% στη βάση σου
        String insertQuery = """
        INSERT INTO Notification (recipientId, senderId, message, dateSent, isRead, notificationType) 
        VALUES (?, 'sec1', ?, datetime('now'), 0, 'REMINDER')
    """;

        try (Connection conn = com.unipath.dataBase.DBManager.getInstance().connect();
             java.sql.PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
             java.sql.ResultSet rs = selectStmt.executeQuery();
             java.sql.PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            // Απενεργοποιούμε το auto-commit για να γίνουν όλα τα inserts μαζί (Batch) για ταχύτητα
            conn.setAutoCommit(false);

            while (rs.next()) {
                String studentUserId = rs.getString("userId");

                insertStmt.setString(1, studentUserId); // recipientId
                insertStmt.setString(2, messageText);     // message
                insertStmt.addBatch();
            }

            // Εκτέλεση όλων των ειδοποιήσεων
            insertStmt.executeBatch();
            conn.commit();

            System.out.println("📢 Η ειδοποίηση στάλθηκε επιτυχώς σε όλους τους έγκυρους φοιτητές!");

        } catch (SQLException e) {
            System.err.println("❌ NotificationRepository Error: [SQLITE_CONSTRAINT_FOREIGNKEY] αποφεύχθηκε αλλά υπήρξε άλλο σφάλμα:");
            e.printStackTrace();
        }
    }
}