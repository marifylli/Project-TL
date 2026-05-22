package com.unipath.repository;

import com.unipath.dataBase.DBManager;
import com.unipath.model.StudyPlan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// gia na pairnei h grammateia ta fixed plans apo db kai na ta kanei statistics
public class StudyPlanRepository {

    private final DBManager dbManager;

    public StudyPlanRepository() {
        this.dbManager = DBManager.getInstance();
    }
    // uc4 vima 5 epistrefei ola ta oristikopoihmena plana otan ta zitisei o ypallilos

    public List<StudyPlan> queryFinalizedPlans() throws SQLException {
        List<StudyPlan> plans = new ArrayList<>();
        String sql = "SELECT * FROM StudyPlan WHERE isFinalized = 1";

        Connection conn = dbManager.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            plans.add(mapResultSetToStudyPlan(rs));
        }

        rs.close();
        pstmt.close();
        return plans;
    }
     // gia na metatrapei to Result se study plan adikeimeno
    private StudyPlan mapResultSetToStudyPlan(ResultSet rs) throws SQLException {
        StudyPlan plan = new StudyPlan();
        plan.setPlanId(rs.getInt("planId"));
        plan.setStudentId(rs.getInt("studentId"));
        plan.setScenarioId(rs.getInt("scenarioId"));
        plan.setAcademicYear(rs.getString("academicYear"));
        plan.setDirectionName(rs.getString("directionName"));
        plan.setTotalECTS(rs.getInt("totalECTS"));
        plan.setStatus(rs.getString("status"));
        plan.setFinalized(rs.getInt("isFinalized") == 1);
        plan.setValid(rs.getInt("isValid") == 1);
        plan.setIsDraft(rs.getInt("isDraft"));
        plan.setWorkloadIndex(rs.getFloat("workLoadIndex"));
        return plan;
    }
}

