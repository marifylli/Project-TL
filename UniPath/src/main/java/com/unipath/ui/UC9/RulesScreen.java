package com.unipath.ui.UC9;

import com.unipath.controller.ManageProfCourseEdit;
import com.unipath.model.Course;

public class RulesScreen {

    private ManageProfCourseEdit manageProfCourseEdit;
    private Course course;

    public RulesScreen(ManageProfCourseEdit manageProfCourseEdit, Course course) {
        this.manageProfCourseEdit = manageProfCourseEdit;
        this.course = course;
    }

    public void display() {
        // render UI με τους κανόνες του μαθήματος
    }

    // Από το βέλος editsRules() του χρήστη στην οθόνη
    public void editsRules(String text) {
        // Προσωρινή αποθήκευση των αλλαγών στο UI text field
    }

    // Από το βέλος clickSaveRules() του χρήστη
    public void clickSaveRules(String newRulesText) {
        manageProfCourseEdit.onSaveRules(newRulesText);
    }

    // Από το βέλος clickCancel() του χρήστη
    public void clickCancel() {
        manageProfCourseEdit.onCancelRules();
    }

    // Από το εσωτερικό βέλος deleteChanges() που στέλνει ο Controller στην οθόνη
    public void deleteChanges() {
        // Καθαρισμός των πεδίων επεξεργασίας χωρίς αποθήκευση
        System.out.println("Οι αλλαγές απορρίφθηκαν.");
    }


}
