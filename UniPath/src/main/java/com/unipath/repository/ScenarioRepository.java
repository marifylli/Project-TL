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
        System.out.println("DEBUG REPO: 🏁 Ξεκίνησε η queryGetScenarios().");
        List<Scenario> scenarios = new ArrayList<>();
        String sql = "SELECT scenarioId, title, description, groupARequiredECTS, groupBRequiredECTS FROM Scenario";


        try (Connection conn = DBManager.getInstance().connect()) {
            System.out.println("DEBUG REPO: Η σύνδεση με τη βάση πέτυχε. Εκτέλεση Query...");

            try (PreparedStatement pstmt = conn.prepareStatement(sql);
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
            }
            System.out.println("DEBUG REPO: Ανακτήθηκαν επιτυχώς " + scenarios.size() + " σενάρια από τη βάση.");

        } catch (SQLException e) {
            System.err.println(" ΣΦΑΛΜΑ SQL ΣΤΟ REPOSITORY:");
            e.printStackTrace(); // Θα τυπώσει το ακριβές σφάλμα αν π.χ. δεν υπάρχει ο πίνακας
        } catch (Exception e) {
            System.err.println(" ΓΕΝΙΚΟ ΣΦΑΛΜΑ ΣΤΟ REPOSITORY:");
            e.printStackTrace();
        }

        return scenarios;
    }
}
