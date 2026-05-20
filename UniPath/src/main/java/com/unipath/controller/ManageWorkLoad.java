package com.unipath.controller;

import com.unipath.model.Course;
import com.unipath.model.StudyPlan;
import com.unipath.ui.UC3.WorkloadResultScreen;
import java.util.List;

public class ManageWorkLoad {

    private WorkloadResultScreen workloadResultScreen;

    public void create() {
        workloadResultScreen = new WorkloadResultScreen(this);
        workloadResultScreen.display();
    }

    public void onStudyPlanLoaded(StudyPlan plan) {
        // TODO
    }

    public float calculateWorkLoadIndex(List<Course> courses) {
        return 0; // TODO
    }

    public List<Course> sortCoursesByWorkload(List<Course> courses) {
        return null; // TODO
    }

    public void saveWorkLoadIndex(float index) {
        // TODO
    }

    public void onAnalysisConfirmed() {
        // TODO
    }

    public void addWorkloadToProfile(float index) {
        // TODO
    }
}
