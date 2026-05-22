package com.unipath.repository;

import com.unipath.dataBase.DBManager;
import com.unipath.model.Scenario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScenarioRepository {

    public List<Scenario> queryGetScenarios() {
        List<Scenario> scenarios = new ArrayList<>();
        String sql = "SELECT scenarioId, title, description, groupARequiredECTS, groupBRequiredECTS FROM Scenario";


        try (Connection conn = DBManager.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {

                Scenario scenario = new Scenario();


                scenario.setScenarioId(rs.getInt("scenarioId"));
                scenario.setTitle(rs.getString("title"));
                scenario.setDescription(rs.getString("description"));
                scenario.setGroupARequiredECTS(rs.getInt("groupARequiredECTS"));
                scenario.setGroupBRequiredECTS(rs.getInt("groupBRequiredECTS"));

                scenarios.add(scenario);
            }

            System.out.println("Ανακτήθηκαν επιτυχώς " + scenarios.size() + " σενάρια.");

        } catch (SQLException e) {
            System.err.println("Σφάλμα κατά την εκτέλεση του queryGetScenarios: " + e.getMessage());
        }

        return scenarios;
    }
}
