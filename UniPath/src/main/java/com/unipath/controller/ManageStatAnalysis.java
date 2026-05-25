package com.unipath.controller;

import com.unipath.model.FilterCriteria;
import com.unipath.model.Report;
import com.unipath.model.StudyPlan;
import com.unipath.repository.ReportRepository;
import com.unipath.repository.StudyPlanRepository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// controller gia uc4 pou dimiorgeitai apo secretary main screen

public class ManageStatAnalysis {
    private final StudyPlanRepository studyPlanRepository;
    private final ReportRepository reportRepository;

    private FilterCriteria currentFilters;
    private List<StudyPlan> filteredPlans;
    private Map<String, Object> computedStatistics;
    private Report currentReport;


    public ManageStatAnalysis() {
        this.studyPlanRepository = new StudyPlanRepository();
        this.reportRepository    = new ReportRepository();
    }

    // ── UC4 vima 3 o ypallilos rithmizei ta filtra

    public void setFiltersCriteria(FilterCriteria filters) {
        this.currentFilters = filters;
    }

    // view statistics


    public boolean selectViewStatistics() {
        try {
            // vima 5: queryFinalizedPlans από db
            List<StudyPlan> allPlans = studyPlanRepository.queryFinalizedPlans();

            //vima 6: compareWithFilters gia kathe plano
            filteredPlans = allPlans.stream()
                    .filter(plan -> plan.compareWithFilters(currentFilters))
                    .collect(Collectors.toList());

            // enallaktiki not enough data
            if (filteredPlans.isEmpty()) {
                return false;
            }

            // Βήμα 7: computeStatistics
            computedStatistics = computeStatistics(filteredPlans);
            return true;

        } catch (SQLException e) {
            System.out.println("Σφάλμα κατά την ανάκτηση πλάνων: " + e.getMessage());
            return false;
        }
    }

    // UC4 vima 7: ypologismos statistikon
    private Map<String, Object> computeStatistics(List<StudyPlan> plans) {
        Map<String, Object> stats = new HashMap<>();

        // total plans
        stats.put("totalPlans", plans.size());

        // Κατανομή ανά κατεύθυνση
        Map<String, Long> perDirection = plans.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getDirectionName() != null ? p.getDirectionName() : "Άγνωστη",
                        Collectors.counting()
                ));
        stats.put("plansPerDirection", perDirection);

        // plans per year
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

    // UC4 Βήμα 9-11: Ο ipallilos epilegei eksagogi eggrafou

    // creates and save report, kaleitai apo ReportExportScreen.

    public Report generateReport(String format, int secretaryId) {
        try {
            // Βήμα 10: create Report
            currentReport = new Report(
                    "Αναφορά Στατιστικής Ανάλυσης",
                    currentFilters,
                    computedStatistics,
                    format,
                    String.valueOf(secretaryId)
            );

            // vima 11: saveReport stin db
            reportRepository.saveReport(currentReport, secretaryId);

            return currentReport;

        } catch (Exception e) {
            System.out.println("Σφάλμα κατά την αποθήκευση αναφοράς: " + e.getMessage());
            return null;
        }
    }

    // Getters gia Screens

    public FilterCriteria getCurrentFilters()         { return currentFilters; }
    public List<StudyPlan> getFilteredPlans()         { return filteredPlans; }
    public Map<String, Object> getComputedStatistics(){ return computedStatistics; }
    public Report getCurrentReport()                  { return currentReport; }




}
