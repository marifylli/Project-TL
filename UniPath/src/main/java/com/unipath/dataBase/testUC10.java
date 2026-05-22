package com.unipath.dataBase;

import com.unipath.controller.ManageThesisClass;
import com.unipath.model.Calendar;
import com.unipath.model.AvailabilitySlot;

public class testUC10 {
    public static void main(String[] args) {
        ManageThesisClass manager = new ManageThesisClass();

        System.out.println("=== TEST UC10 ===\n");

        // ── Test 1: Επικύρωση πεδίων (εναλλακτική ροή) ──────────────
        System.out.println("Test 1: Ελλιπή πεδία");
        boolean result = manager.publishThesis(1, "", "", "", 0, 0, "");
        System.out.println(result ? "❌ Λάθος - έπρεπε να αποτύχει!"
                : "✅ Σωστό - απέτρεψε δημοσίευση!");

        // ── Test 2: Δημοσίευση Διπλωματικής (βασική ροή) ────────────
        System.out.println("\nTest 2: Δημοσίευση διπλωματικής");
        boolean published = manager.publishThesis(
                1,
                "Ανάπτυξη Εφαρμογής Διαχείρισης Σπουδών",
                "Η εφαρμογή αφορά τη διαχείριση σπουδών φοιτητών",
                "Αντικειμενοστραφής Προγραμματισμός, Βάσεις Δεδομένων",
                30,
                3,
                "Java, SQL, JavaFX"
        );
        System.out.println(published ? "✅ Διπλωματική δημοσιεύτηκε!"
                : "❌ Σφάλμα δημοσίευσης!");

        // ── Test 3: Αποθήκευση Διαθεσιμότητας ───────────────────────
        System.out.println("\nTest 3: Αποθήκευση slots");
        boolean slot1 = manager.setAvailability(1, "Δευτέρα", "10:00", "12:00");
        boolean slot2 = manager.setAvailability(1, "Τετάρτη", "14:00", "16:00");
        System.out.println(slot1 ? "✅ Slot Δευτέρας αποθηκεύτηκε!"
                : "❌ Σφάλμα slot Δευτέρας!");
        System.out.println(slot2 ? "✅ Slot Τετάρτης αποθηκεύτηκε!"
                : "❌ Σφάλμα slot Τετάρτης!");

        // ── Test 4: Ανάκτηση Ημερολογίου ────────────────────────────
        System.out.println("\nTest 4: Ανάκτηση ημερολογίου");
        Calendar calendar = manager.requestCalendar(1);
        System.out.println("Αριθμός slots: " + calendar.getAvailabilitySlots().size());
        for (AvailabilitySlot slot : calendar.getAvailabilitySlots()) {
            System.out.println("  → " + slot.getDayOfWeek() +
                    " " + slot.getStartTime() +
                    " - " + slot.getEndTime());
        }

        System.out.println("\n=== ΤΕΛΟΣ TEST ===");
        DBManager.getInstance().disconnect();
    }
}