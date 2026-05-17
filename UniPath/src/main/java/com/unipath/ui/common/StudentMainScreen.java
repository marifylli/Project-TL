package com.unipath.ui.common;

import com.unipath.controller.ManageStudyPlan;

public class StudentMainScreen {


    public void clickCreatePlan() {
        ManageStudyPlan manage = new ManageStudyPlan();
        manage.startCreatePlan();
    }
}
