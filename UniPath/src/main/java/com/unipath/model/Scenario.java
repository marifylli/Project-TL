package com.unipath.model;

import java.util.List;

public class Scenario {
    private int scenarioId;
    private String title;
    private String description;
    private int groupARequiredECTS;
    private int groupBRequiredECTS;
    private StudyPlan studyPlan;

    private List<String> mainDirections;


    public Scenario() {
    }
    public int getScenarioId() {
        return scenarioId;
    }

    public List<String> getMainDirections() {
        return mainDirections;
    }
}
