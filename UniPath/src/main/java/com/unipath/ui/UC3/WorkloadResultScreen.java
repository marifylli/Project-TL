package com.unipath.ui.UC3;

import com.unipath.controller.ManageWorkLoad;
import com.unipath.model.Course;
import java.util.List;

public class WorkloadResultScreen {

    private ManageWorkLoad manageWorkLoad;

    public WorkloadResultScreen(ManageWorkLoad manageWorkLoad) {
        this.manageWorkLoad = manageWorkLoad;
    }

    public void display() {
        // show workload result screen
    }

    public void displayWorkloadIndex(float index) {
        // TODO
    }

    public void displaySortedCourses(List<Course> courses) {
        // TODO
    }

    public void confirmAnalysis() {
        manageWorkLoad.onAnalysisConfirmed();
    }
}

