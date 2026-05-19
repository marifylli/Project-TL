package com.unipath.ui.UC1;

import com.unipath.controller.ManageStudyPlan;
import com.unipath.model.Scenario;
import com.unipath.model.Course;
import java.util.List;

public class PlanSummaryScreen {
    private ManageStudyPlan manageStudyPlan;
    private Scenario scenario;
    private List<Course> courses;

    public PlanSummaryScreen(ManageStudyPlan manageStudyPlan, Scenario scenario, List<Course> courses) {
        this.manageStudyPlan = manageStudyPlan;
        this.scenario = scenario;
        this.courses = courses;
    }

    public void displaySummary() {
        // show summary
    }

    public void confirmSave() {
        manageStudyPlan.onConfirmPlan(scenario, courses);
    }


}
