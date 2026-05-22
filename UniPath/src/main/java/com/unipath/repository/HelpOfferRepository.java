package com.unipath.repository;

import com.unipath.dataBase.DBManager;
import com.unipath.model.HelpOffer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HelpOfferRepository {

    // Αποθήκευση προσφοράς στη βάση δεδομένων (UC7 - SD7)
    public boolean saveOffer(HelpOffer offer) {
        String sql = """
            INSERT INTO HelpOffer (mentorId, courseId, helpType, fileUrl, meetingUrl, isActive)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBManager.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, offer.getMentorId());
            pstmt.setString(2, offer.getCourseId());
            pstmt.setString(3, offer.getHelpType());
            pstmt.setString(4, offer.getNotesFile());
            pstmt.setString(5, offer.getMeetingUrl());
            pstmt.setInt(6, offer.isActive() ? 1 : 0); // Μετατροπή boolean σε 0/1 για SQLite

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Σφάλμα κατά την εκτέλεση του saveOffer: " + e.getMessage());
            return false;
        }
    }

    // Ανάκτηση ενεργών προσφορών για ένα συγκεκριμένο μάθημα (UC8 - SD8)
    public List<HelpOffer> getActiveOffers(String courseId) {
        List<HelpOffer> offers = new ArrayList<>();
        String sql = """
            SELECT offerId, mentorId, courseId, helpType, fileUrl, meetingUrl, isActive 
            FROM HelpOffer 
            WHERE courseId = ? AND isActive = 1
        """;

        try (Connection conn = DBManager.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    HelpOffer offer = new HelpOffer();
                    offer.setOfferId(rs.getInt("offerId"));
                    offer.setMentorId(rs.getInt("mentorId"));
                    offer.setCourseId(rs.getString("courseId"));
                    offer.setHelpType(rs.getString("helpType"));
                    offer.setNotesFile(rs.getString("fileUrl"));
                    offer.setMeetingUrl(rs.getString("meetingUrl"));
                    offer.setActive(rs.getInt("isActive") == 1);

                    offers.add(offer);
                }
            }

            System.out.println("Ανακτήθηκαν επιτυχώς " + offers.size() + " ενεργές προσφορές για το μάθημα " + courseId);

        } catch (SQLException e) {
            System.err.println("Σφάλμα κατά την εκτέλεση του getActiveOffers: " + e.getMessage());
        }

        return offers;
    }
}
