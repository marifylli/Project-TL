package com.unipath.controller;
import com.unipath.model.Course;
import com.unipath.ui.UC1.ConfirmationScreen;
import com.unipath.ui.UC1.CourseSelectionScreen;
import com.unipath.ui.UC1.PlanSummaryScreen;
import com.unipath.model.Scenario;
import com.unipath.ui.UC1.ScenarioSelectionScreen;

import java.util.List;

public class ManageStudyPlan {
    private ScenarioSelectionScreen scenarioSelectionScreen;
    private CourseSelectionScreen courseSelectionScreen;
    private PlanSummaryScreen planSummaryScreen;
    private ConfirmationScreen confirmationScreen;



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

    public boolean validateRules(){

    return false;}//false mexri na graftei to soma

    public int calculateects(){

    return 0;}// mexri na mpei soma

    public void removeCourses(){

    }

}
