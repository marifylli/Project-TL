package com.unipath.ui.UC5;

import com.unipath.controller.ManageCourseAddition;
import com.unipath.model.Professor;
import java.util.List;

public class AvailableProfessorsScreen {

    private ManageCourseAddition controller;
    private List<Professor> professors;
    private String courseId, title, description;
    private int ects, semester;

    public AvailableProfessorsScreen(ManageCourseAddition controller, List<Professor> professors,
                                     String courseId, String title, String description, int ects, int semester) {
        this.controller = controller;
        this.professors = professors;
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.ects = ects;
        this.semester = semester;
    }

    public void displayProfessorList() {
        System.out.println("Εμφάνιση Λίστας Διαθέσιμων Καθηγητών...");
    }
}
