package com.unipath.dataBase;

public class TestDB {
    public static void main(String[] args) {
        try {
            DBManager db = DBManager.getInstance();
            db.createTables();
            System.out.println("Η βάση δημιουργήθηκε επιτυχώς!");
            db.disconnect();
        } catch (Exception e) {
            System.out.println("Σφάλμα: " + e.getMessage());
        }
    }
}