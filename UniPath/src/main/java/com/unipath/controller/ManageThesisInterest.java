package com.unipath.controller;

import com.unipath.model.AvailabilitySlot;
import com.unipath.model.Notification;
import com.unipath.repository.InterviewRepository;
import java.util.List;

public class ManageThesisInterest {

    // Ορισμός του repository που διαχειρίζεται τις βάσεις για τα ραντεβού και τις ώρες
    private final InterviewRepository interviewRepository = new InterviewRepository();

    public ManageThesisInterest() {
    }

    /**
     * 1. Έλεγχος προϋποθέσεων (ECTS κλπ) βάσει του διαγράμματος
     */
    public boolean checkAcademicStatus(int studentId, int thesisId) {
        return interviewRepository.checkStudentEligibility(studentId, thesisId);
    }

    /**
     * 3. [Sequence Diagram]: Επιβεβαίωση Ραντεβού & Αποστολή Ειδοποίησης
     */
    public boolean selectConfirmAppointment(int studentId, int professorId, int slotId, int diplomaticId) {

        // 1. [Sequence Diagram]: Κλήση της addEvent ΑΚΡΙΒΩΣ πάνω στην κλάση Calendar
        boolean appointmentCreated = com.unipath.model.Calendar.addEvent(studentId, slotId, diplomaticId);

        if (!appointmentCreated) {
            System.out.println("❌ [ManageThesisInterest]: Αποτυχία δημιουργίας συμβάντος στην κλάση Calendar.");
            return false;
        }

        // 2. [Sequence Diagram]: Κλήση της sendNotification απευθείας στην κλάση μοντέλου Notification
        String studentEmail = "test.student1@ceid.upatras.gr";
        String msgText = "Νέο ραντεβού για τη διπλωματική σας (ID: " + diplomaticId + ") από τον φοιτητή με ID " + studentId + ".";

        boolean notificationSent = com.unipath.model.Notification.sendNotification(
                professorId,
                studentEmail,
                msgText,
                "APPOINTMENT"
        );

        if (notificationSent) {
            System.out.println("[BACKEND] [UC11]: Το Notification στάλθηκε επιτυχώς βάσει του Sequence Diagram!");
        }

        return appointmentCreated;
    }
}