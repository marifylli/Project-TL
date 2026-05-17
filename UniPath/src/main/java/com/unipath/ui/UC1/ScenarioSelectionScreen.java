package com.unipath.ui.UC1;

import com.unipath.controller.ManageStudyPlan;
import com.unipath.model.Scenario;
public class ScenarioSelectionScreen {

    private ManageStudyPlan manageStudyPlan;




    public ScenarioSelectionScreen(ManageStudyPlan manageStudyPlan) {

        this.manageStudyPlan = manageStudyPlan;
    }

    public void displayScenarios(){
    }

    public void selectScenario(Scenario scenario) {
        manageStudyPlan.onScenarioSelected(scenario);
    }

}
