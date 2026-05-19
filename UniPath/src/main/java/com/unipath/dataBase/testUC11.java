package com.unipath.dataBase;

import com.unipath.controller.ManageThesisInterest;
import com.unipath.model.AvailabilitySlot;
import com.unipath.model.Thesis;

import java.util.List;

public class testUC11 {
    public static void main(String[] args) {
        ManageThesisInterest manager = new ManageThesisInterest();

        System.out.println("=== TEST UC11 ===\n");

        // ── Test 1: Ανάκτηση διαθέσιμων διπλωματικών ────────────────
        System.out.println("Test 1: Ανάκτηση διαθέσιμων διπλωματικών");
        List<Thesis> theses = manager.getAvailableTheses();
        if (theses.isEmpty()) {
            System.out.println("⚠️ Δεν υπάρχουν διπλωματικές - προσθέτουμε test δεδομένα!");
        } else {
            System.out.println("✅ Βρέθηκαν " + theses.size() + " διπλωματικές:");
            for (Thesis t : theses) {
                System.out.println("  → " + t.getTitle());
            }
        }

        // ── Test 2: Λεπτομέρειες θέματος ────────────────────────────
        System.out.println("\nTest 2: Λεπτομέρειες θέματος με ID=1");
        Thesis thesis = manager.getThesisDetails(1);
        if (thesis != null) {
            System.out.println("✅ Τίτλος: " + thesis.getTitle());
            System.out.println("   ECTS: " + thesis.getRequiredECTS());
            System.out.println("   Προϋποθέσεις: " + thesis.getPrerequisites());
        } else {
            System.out.println("⚠️ Δεν βρέθηκε διπλωματική με ID=1");
        }

        // ── Test 3: Έλεγχος προϋποθέσεων (βασική ροή) ───────────────
        System.out.println("\nTest 3: Έλεγχος προϋποθέσεων φοιτητή");
        boolean eligible = manager.checkEligibility(1, 1);
        System.out.println(eligible ?
                "✅ Ο φοιτητής πληροί τις προϋποθέσεις!" :
                "⚠️ Ο φοιτητής ΔΕΝ πληροί τις προϋποθέσεις (εναλλακτική ροή 1)");

        // ── Test 4: Ανάκτηση διαθέσιμων slots ───────────────────────
        System.out.println("\nTest 4: Ανάκτηση διαθέσιμων slots καθηγητή");
        List<AvailabilitySlot> slots = manager.getAvailableSlots(1);
        if (slots.isEmpty()) {
            System.out.println("⚠️ Δεν υπάρχουν διαθέσιμα slots (εναλλακτική ροή 2)");
        } else {
            System.out.println("✅ Βρέθηκαν " + slots.size() + " slots:");
            for (AvailabilitySlot slot : slots) {
                System.out.println("  → " + slot.getDayOfWeek() +
                        " " + slot.getStartTime() +
                        " - " + slot.getEndTime());
            }
        }

        // ── Test 5: Κράτηση ραντεβού (βασική ροή) ───────────────────
        System.out.println("\nTest 5: Κράτηση ραντεβού");
        if (!slots.isEmpty()) {
            int slotId = slots.get(0).getSlotId();
            boolean booked = manager.bookAppointment(1, 1, slotId, 1);
            System.out.println(booked ?
                    "✅ Ραντεβού κλείστηκε επιτυχώς!" :
                    "❌ Σφάλμα κράτησης ραντεβού!");

            // ── Test 6: Έλεγχος ότι το slot έγινε μη διαθέσιμο ─────
            System.out.println("\nTest 6: Έλεγχος slot μετά την κράτηση");
            List<AvailabilitySlot> updatedSlots = manager.getAvailableSlots(1);
            boolean slotStillAvailable = updatedSlots.stream()
                    .anyMatch(s -> s.getSlotId() == slotId);
            System.out.println(!slotStillAvailable ?
                    "✅ Slot έγινε μη διαθέσιμο σωστά!" :
                    "❌ Slot παραμένει διαθέσιμο - πρόβλημα!");
        } else {
            System.out.println("⚠️ Παράλειψη test - δεν υπάρχουν slots!");
        }

        System.out.println("\n=== ΤΕΛΟΣ TEST ===");
        DBManager.getInstance().disconnect();
    }
}