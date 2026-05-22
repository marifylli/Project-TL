package com.unipath.dataBase;

import com.unipath.controller.ManageThesisClass;
import com.unipath.model.Calendar;
import javafx.application.Platform;

public class testUC10 {
    public static void main(String[] args) {
        ManageThesisClass manager = new ManageThesisClass();

        System.out.println("=== TEST UC10 ===\n");

        // ── Test 1: Αποθήκευση Διαθεσιμότητας (setAvailableSlots) ──
        System.out.println("Test 1: Αποθήκευση Διαθεσιμότητας Καθηγητή");
        boolean slot1 = manager.setAvailableSlots(1, "Δευτέρα", "10:00", "12:00");
        boolean slot2 = manager.setAvailableSlots(1, "Τετάρτη", "14:00", "16:00");

        System.out.println(slot1 ? "✅ Slot Δευτέρας αποθηκεύτηκε!" : "❌ Σφάλμα Slot Δευτέρας!");
        System.out.println(slot2 ? "✅ Slot Τετάρτης αποθηκεύτηκε!" : "❌ Σφάλμα Slot Τετάρτης!");

        // ── Test 2: Ανάκτηση Ημερολογίου (requestCalendar) ──
        System.out.println("\nTest 2: Ανάκτηση Ημερολογίου");
        Calendar cal = manager.requestCalendar(1);
        System.out.println(cal != null ? "✅ Το ημερολόγιο ανακτήθηκε επιτυχώς!" : "❌ Αποτυχία ανάκτησης ημερολογίου!");

        // ── Test 3: Δημοσίευση Διπλωματικής (saveNewThesis) ──
        System.out.println("\nTest 3: Δημοσίευση Διπλωματικής Εργασίας");
        boolean published = manager.saveNewThesis(
                1,
                "Ανάπτυξη Εφαρμογής Διαχείρισης Σπουδών",
                "Η εφαρμογή αφορά τη διαχείριση σπουδών φοιτητών",
                "Αντικειμενοστραφής Προγραμματισμός, Βάσεις Δεδομένων",
                30,
                3,
                "Java, SQL, JavaFX"
        );

        System.out.println(published ? "✅ Η διπλωματική δημοσιεύτηκε επιτυχώς!" : "❌ Σφάλμα δημοσίευσης!");

        System.out.println("\n=== ΤΕΛΟΣ TEST ===");
        System.exit(0);
    }
}