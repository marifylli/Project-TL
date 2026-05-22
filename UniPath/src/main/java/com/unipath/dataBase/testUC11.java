package com.unipath.dataBase;

import com.unipath.controller.ManageThesisInterest;
import com.unipath.model.*;
import java.util.List;

public class testUC11 {
    public static void main(String[] args) {
        ManageThesisInterest manager = new ManageThesisInterest();

        System.out.println("=== TEST UC11: Ξεκίνημα ===");

        // Βήμα 1-2: findAllThesis
        List<Thesis> theses = manager.findAllThesis();
        System.out.println("✅ Βρέθηκαν " + theses.size() + " θέματα.");

        // Βήμα 3-4: findThesisDetails
        if (!theses.isEmpty()) {
            Thesis t = manager.findThesisDetails(theses.get(0).getDiplomaticId());
            System.out.println("✅ Λεπτομέρειες θέματος: " + t.getTitle());

            // Βήμα 6: findAvailableSlots
            List<AvailabilitySlot> slots = manager.findAvailableSlots(t.getProfessorId());
            System.out.println("✅ Διαθέσιμα slots: " + slots.size());

            // Βήμα 8-9: selectConfirmAppointment
            if (!slots.isEmpty()) {
                boolean result = manager.selectConfirmAppointment(1, t.getProfessorId(), slots.get(0).getSlotId(), t.getDiplomaticId());
                System.out.println(result ? "✅ Επιτυχής κράτηση!" : "❌ Αποτυχία κράτησης!");
            }
        }

        System.out.println("=== ΤΕΛΟΣ TEST ===");
    }
}