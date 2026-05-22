package com.unipath.repository;

import com.unipath.dataBase.DBManager;
import com.unipath.model.Report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

// methodoi gia apothikeusi reports stin db- vima 11 toi uc4
public class ReportRepository {

    private final DBManager dbManager;

    public ReportRepository() {
        this.dbManager = DBManager.getInstance();
    }

    public void saveReport(Report report, int secretaryId) throws SQLException {
        String sql = """
            INSERT INTO Report
                (generatedBy, academicYear, appliedFilters, creationDate, format, status)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        Connection conn = dbManager.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, secretaryId);
        pstmt.setString(2, report.getAppliedFilters() != null
                ? report.getAppliedFilters().getAcademicYear() : null);
        pstmt.setString(3, report.getAppliedFilters() != null
                ? report.getAppliedFilters().toString() : null);
        pstmt.setString(4, LocalDateTime.now().toString());
        pstmt.setString(5, report.getFormat());
        pstmt.setString(6, "SAVED");

        pstmt.executeUpdate();
        pstmt.close();
        System.out.println("Report αποθηκεύτηκε επιτυχώς!");
    }
}
