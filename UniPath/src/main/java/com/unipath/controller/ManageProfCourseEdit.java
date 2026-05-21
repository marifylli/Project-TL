package com.unipath.controller;

import com.unipath.model.Course;
import com.unipath.repository.CourseRepository;
import com.unipath.ui.UC9.CourseDetailScreen;
import com.unipath.ui.UC9.MyCoursesScreen;
import com.unipath.ui.UC9.RulesScreen;
import com.unipath.ui.common.SuccessScreen;

import java.util.List;


public class ManageProfCourseEdit {

    private MyCoursesScreen myCoursesScreen;
    private CourseDetailScreen courseDetailScreen;
    private RulesScreen rulesScreen;
    private CourseRepository courseRepository;

    private Course selectedCourse;
    private int currentProfessorId = 1;

    public ManageProfCourseEdit() {
        this.courseRepository = new CourseRepository();
    }

    public void startMyCourses(){
        List<Course> coursesList = courseRepository.queryGetProfessorCourses(currentProfessorId);

        // alt2: [Has Courses]
        if (coursesList != null && !coursesList.isEmpty()) {
            myCoursesScreen = new MyCoursesScreen(this, coursesList);
            myCoursesScreen.displayProfCourses();
        } else {
            System.out.println("Ο καθηγητής δεν έχει ανατεθειμένα μαθήματα.");
        }
    }

    public void onCourseSelected(Course course) {
        this.selectedCourse = course;
        courseDetailScreen = new CourseDetailScreen(this, selectedCourse);
        courseDetailScreen.displayCourseDetails();
    }

    // Όταν πατηθεί το κουμπί επεξεργασίας πεδίων
    public void onEditCourseFields() {

    }

    // Όταν πατηθεί το "Rules" (clicksRules)
    public void onRulesClicked() {
        rulesScreen = new RulesScreen(this, selectedCourse);
        rulesScreen.display();
    }


    public void onSaveRules(String updatedRules) {

        String professorUsername = "Nikos_Michail";

        // ΔΙΟΡΘΩΣΗ: Προσθέτουμε το professorUsername ως 3η παράμετρο
        boolean success = courseRepository.queryUpdateCourseRules(
                selectedCourse.getCourseID(),
                updatedRules,
                professorUsername
        );

        if (success) {
            selectedCourse.setRules(updatedRules);

            // "create" SuccessScreen -> display() όπως δείχνει το sequence diagram σου
            SuccessScreen successScreen = new SuccessScreen(this);
            successScreen.display();
        }
    }

    public void onCancelRules() {

        if (rulesScreen != null) {
            rulesScreen.deleteChanges();
        }

    }



}
