package com.unipath.model;

import java.util.List;

public class Scenario {
    private int scenarioId;
    private String title;
    private String description;
    private int groupARequiredECTS;
    private int groupBRequiredECTS;




    public Scenario() {
    }

    public Scenario(int scenarioId, String title, String description, int groupARequiredECTS, int groupBRequiredECTS) {
        this.scenarioId = scenarioId;
        this.title = title;
        this.description = description;
        this.groupARequiredECTS = groupARequiredECTS;
        this.groupBRequiredECTS = groupBRequiredECTS;
    }
    public int getScenarioId() {
        return scenarioId;
    }


    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGroupARequiredECTS() {
        return groupARequiredECTS;
    }

    public void setGroupARequiredECTS(int groupARequiredECTS) {
        this.groupARequiredECTS = groupARequiredECTS;
    }

    public int getGroupBRequiredECTS() {
        return groupBRequiredECTS;
    }

    public void setGroupBRequiredECTS(int groupBRequiredECTS) {
        this.groupBRequiredECTS = groupBRequiredECTS;
    }

    @Override
    public String toString() {
        return this.title != null ? this.title : "Σενάριο " + scenarioId;
    }
}
