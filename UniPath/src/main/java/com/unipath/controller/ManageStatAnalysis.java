package com.unipath.controller;

import com.unipath.dataBase.DBManager;
import com.unipath.model.FilterCriteria;
import com.unipath.model.Report;
import com.unipath.model.StudyPlan;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// controller gia uc4 pou dimiorgeitai apo secretary main screen

public class ManageStatAnalysis {
    private DBManager dbManager;
    private FilterCriteria currentFilters;
    private List<StudyPlan> filteredPlans;
    private Map<String, Object> computedStatistics;
    private Report currentReport;

    public ManageStatAnalysis() {
        this.dbManager = DBManager.getInstance();
    }

    // vima 3 : o ypallilos rithmizei filtra to systima lamvanei kai apothikeuei tis allages poy ekane
    public void setFiltersCriteria(FilterCriteria filters) {
        this.currentFilters = filters;
    }

    // vima 4,5,6,7
    public boolean selectViewStatistics() {
        try {
            // vima 5: queryFinalizedPlans apo DB
            List<StudyPlan> allPlans = dbManager.queryFinalizedPlans();

            // Vima 6: compareWithFilters for every plan
            filteredPlans = allPlans.stream()
                    .filter(plan -> plan.compareWithFilters(currentFilters))
                    .collect(Collectors.toList());

            // enallaktiki [not enough data] — epistrefei false and ErrorScreen
            if (filteredPlans.isEmpty()) {
                return false;
            }

            // vima 7: computeStatistics
            computedStatistics = computeStatistics(filteredPlans);
            return true;

        } catch (SQLException e) {
            System.out.println("Σφάλμα κατά την ανάκτηση πλάνων: " + e.getMessage());
            return false;
        }
    }

    // vima 7 ypologismos statistikon gia filtered plans

    private Map<String, Object> computeStatistics(List<StudyPlan> plans) {
        Map<String, Object> stats = new HashMap<>();

        // total plans number
        stats.put("totalPlans", plans.size());

        // Katanomi ana kateuthinsi
        Map<String, Long> perDirection = plans.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getDirectionName() != null ? p.getDirectionName() : "Άγνωστη",
                        Collectors.counting()
                ));
        stats.put("plansPerDirection", perDirection);

        // Katanomi ana akadimaiko etos
        Map<String, Long> perYear = plans.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getAcademicYear() != null ? p.getAcademicYear() : "Άγνωστο",
                        Collectors.counting()
                ));
        stats.put("plansPerYear", perYear);

        // MO ECTS
        double avgEcts = plans.stream()
                .mapToInt(StudyPlan::getTotalECTS)
                .average()
                .orElse(0.0);
        stats.put("averageECTS", avgEcts);

        return stats;
    }

    // vima 9-11 o ypallilos pataei eksagogi eggraofu to systima dimiourgei kai apothikeuei tin episimi anafora

    public Report generateReport(String format, int secretaryId) {
        try {
            // vima 10: create Report
            currentReport = new Report(
                    "Αναφορά Στατιστικής Ανάλυσης",
                    currentFilters,
                    computedStatistics,
                    format,
                    String.valueOf(secretaryId)
            );

            // vima 11: saveReport in DB
            dbManager.saveReport(currentReport, secretaryId);

            return currentReport;

        } catch (SQLException e) {
            System.out.println("Σφάλμα κατά την αποθήκευση αναφοράς: " + e.getMessage());
            return null;
        }
    }

    // screens get
    public FilterCriteria getCurrentFilters()
    { return currentFilters; }
    public List<StudyPlan> getFilteredPlans()
    { return filteredPlans; }
    public Map<String, Object> getComputedStatistics()
    { return computedStatistics; }
    public Report getCurrentReport()
    { return currentReport; }






}
