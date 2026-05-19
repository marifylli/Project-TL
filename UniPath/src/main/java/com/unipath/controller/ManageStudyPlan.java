package com.unipath.controller;
import com.unipath.model.Course;
import com.unipath.ui.UC1.ConfirmationScreen;
import com.unipath.ui.UC1.CourseSelectionScreen;
import com.unipath.ui.UC1.PlanSummaryScreen;
import com.unipath.model.Scenario;
import com.unipath.ui.UC1.ScenarioSelectionScreen;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManageStudyPlan {
    private ScenarioSelectionScreen scenarioSelectionScreen;
    private CourseSelectionScreen courseSelectionScreen;
    private PlanSummaryScreen planSummaryScreen;
    private ConfirmationScreen confirmationScreen;

    private Scenario selectedScenario;
    private List<Course> selectedCourses;

    public void startCreatePlan() {
        scenarioSelectionScreen = new ScenarioSelectionScreen(this);
        scenarioSelectionScreen.displayScenarios();
    }
    public void onScenarioSelected(Scenario scenario) {
        courseSelectionScreen = new CourseSelectionScreen(this, scenario);
        courseSelectionScreen.displayCourses();
    }
    public void onCoursesSelected(Scenario scenario, List<Course> courses) {
        planSummaryScreen = new PlanSummaryScreen(this, scenario, courses);
        planSummaryScreen.displaySummary();
    }

    public void onConfirmPlan(Scenario scenario, List<Course> courses) {
        confirmationScreen = new ConfirmationScreen(this);
        confirmationScreen.displayConfirmation();
    }





    public int calculateEcts(){
        if (selectedCourses == null) return 0;
        int total = 0;
        for (Course c : selectedCourses) {
            total += c.getECTS();
        }
        return total;
    }

    public void removeCourses(){

    }

    public boolean validateRules() {
        if (selectedScenario == null || selectedCourses == null) return false;

        // total ECTS must be 270
        if (calculateEcts() != 270) return false;

        int scenarioId = selectedScenario.getScenarioId();
        List<String> mainDirs = selectedScenario.getMainDirections();

        if (scenarioId == 1) {
            return validateScenario1(mainDirs);
        } else if (scenarioId == 2) {
            return validateScenario2(mainDirs);
        } else if (scenarioId == 3) {
            return validateScenario3();
        }

        return false;
    }

// --------- Helpers ---------

    private boolean validateScenario1(List<String> mainDirs) {
        if (mainDirs == null || mainDirs.size() != 1) return false;
        String main = mainDirs.get(0);

        int mainA = 0, mainB = 0;
        int otherA = 0;
        Set<String> otherADirections = new HashSet<>();
        int otherCourses = 0;
        Set<String> otherCourseDirs = new HashSet<>();

        for (Course c : selectedCourses) {
            List<String> dirs = c.getDirections();

            boolean inMain = dirs != null && dirs.contains(main);

            if (inMain) {
                if (c.isGroupA()) mainA++;
                if (c.isGroupB()) mainB++;
            } else {
                if (c.isGroupA()) {
                    otherA++;
                    if (dirs != null) otherADirections.addAll(dirs);
                }
                otherCourses++;
                if (dirs != null) otherCourseDirs.addAll(dirs);
            }
        }

        return mainA >= 5
                && mainB >= 5
                && otherA >= 5
                && otherADirections.size() >= 3
                && otherCourses >= 2
                && otherCourseDirs.size() >= 2
                && selectedCourses.size() == 17;
    }

    private boolean validateScenario2(List<String> mainDirs) {
        if (mainDirs == null || mainDirs.size() != 2) return false;
        String main1 = mainDirs.get(0);
        String main2 = mainDirs.get(1);

        int main1A = 0, main1B = 0;
        int main2A = 0, main2B = 0;
        int otherCourses = 0;
        Set<String> otherDirs = new HashSet<>();

        for (Course c : selectedCourses) {
            List<String> dirs = c.getDirections();
            boolean inMain1 = dirs != null && dirs.contains(main1);
            boolean inMain2 = dirs != null && dirs.contains(main2);

            if (inMain1) {
                if (c.isGroupA()) main1A++;
                if (c.isGroupB()) main1B++;
            } else if (inMain2) {
                if (c.isGroupA()) main2A++;
                if (c.isGroupB()) main2B++;
            } else {
                otherCourses++;
                if (dirs != null) otherDirs.addAll(dirs);
            }
        }

        return main1A >= 5 && main1B >= 2
                && main2A >= 5 && main2B >= 2
                && otherCourses >= 3
                && otherDirs.size() >= 2
                && selectedCourses.size() == 17;
    }

    private boolean validateScenario3() {
        int groupAcount = 0;
        int otherCount = 0;
        Set<String> dirs = new HashSet<>();

        for (Course c : selectedCourses) {
            if (c.isGroupA()) groupAcount++;
            else otherCount++;

            if (c.getDirections() != null) dirs.addAll(c.getDirections());
        }

        return groupAcount >= 10
                && otherCount >= 7
                && dirs.size() >= 4
                && selectedCourses.size() == 17;
    }

}
