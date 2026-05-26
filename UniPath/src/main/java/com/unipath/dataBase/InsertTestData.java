package com.unipath.dataBase;

import java.sql.Connection;
import java.sql.SQLException;

public class InsertTestData {
    public static void main(String[] args) {
        try {
            DBManager db = DBManager.getInstance();
            db.createTables();
            Connection conn = db.connect();

            System.out.println("=== ΕΙΣΑΓΩΓΗ ΔΕΔΟΜΕΝΩΝ CEID 2025-2026 ===\n");

            // ── USERS - PROFESSORS ───────────────────────────────────
            conn.createStatement().execute("""
                INSERT OR IGNORE INTO User 
                (userId, username, passwordHash, email, firstName, lastName, createdAt)
                VALUES 
                ('p1',  'kaklamanis',    'hash1',  'kakl@ceid.upatras.gr',   'Christos',    'Kaklamanis',       datetime('now')),
                ('p2',  'megalooik',     'hash2',  'mega@ceid.upatras.gr',   'Vasileios',   'Megalooikonomou',  datetime('now')),
                ('p3',  'zaroliagkis',   'hash3',  'zaro@ceid.upatras.gr',   'Christos',    'Zaroliagkis',      datetime('now')),
                ('p4',  'xenos',         'hash4',  'xeno@ceid.upatras.gr',   'Michail',     'Xenos',            datetime('now')),
                ('p5',  'makris',        'hash5',  'makr@ceid.upatras.gr',   'Christos',    'Makris',           datetime('now')),
                ('p6',  'garofalakis',   'hash6',  'garo@ceid.upatras.gr',   'Ioannis',     'Garofalakis',      datetime('now')),
                ('p7',  'vlachos',       'hash7',  'vlac@ceid.upatras.gr',   'Konstantinos','Vlachos',          datetime('now')),
                ('p8',  'berberi',       'hash8',  'berb@ceid.upatras.gr',   'Konstantinos','Barberidis',       datetime('now')),
                ('p9',  'sklavos',       'hash9',  'skla@ceid.upatras.gr',   'Nikolaos',    'Sklavos',          datetime('now')),
                ('p10', 'psarakis',      'hash10', 'psar@ceid.upatras.gr',   'Emmanouel',   'Psarakis',         datetime('now')),
                ('p11', 'kosmopoulos',   'hash11', 'kosm@ceid.upatras.gr',   'Dimitrios',   'Kosmopoulos',      datetime('now')),
                ('p12', 'papadimitriou', 'hash12', 'papa@ceid.upatras.gr',   'Georgios',    'Papadimitriou',    datetime('now')),
                ('p13', 'nikoletseas',   'hash13', 'niko@ceid.upatras.gr',   'Stavros',     'Nikoletseas',      datetime('now')),
                ('p14', 'kosmadakis',    'hash14', 'kos2@ceid.upatras.gr',   'Stathis',     'Kosmadakis',       datetime('now')),
                ('p15', 'andrikopoulos', 'hash15', 'andr@ceid.upatras.gr',   'Apostolos',   'Andrikopoulos',    datetime('now')),
                ('p16', 'xatzidoukas',   'hash16', 'xatz@ceid.upatras.gr',   'Panagiotis',  'Xatzidoukas',      datetime('now')),
                ('p17', 'gallopoulos',   'hash17', 'gall@ceid.upatras.gr',   'Efstratios',  'Gallopoulos',      datetime('now')),
                ('p18', 'vergos',        'hash18', 'verg@ceid.upatras.gr',   'Charalampos', 'Vergos',           datetime('now')),
                ('p19', 'xristidis',     'hash19', 'xris@ceid.upatras.gr',   'Christos',    'Xristidis',        datetime('now')),
                ('p20', 'kontogiannhs',  'hash20', 'kont@ceid.upatras.gr',   'Spyridon',    'Kontogiannhs',     datetime('now')),
                ('p21', 'dermatas',      'hash21', 'derm@ceid.upatras.gr',   'Eleftherios', 'Dermatas',         datetime('now')),
                ('p22', 'sioutas',       'hash22', 'siou@ceid.upatras.gr',   'Spyridon',    'Sioutas',          datetime('now')),
                ('p23', 'sermpanos',     'hash23', 'serm@ceid.upatras.gr',   'Dimitrios',   'Sermpanos',        datetime('now')),
                ('p24', 'tsixlas',       'hash24', 'tsix@ceid.upatras.gr',   'Konstantinos','Tsixlas',          datetime('now')),
                ('p25', 'verykouкis',    'hash25', 'very@ceid.upatras.gr',   'Christos',    'Verykouкis',       datetime('now'))
            """);
            System.out.println("✅ Users-Professors (25) προστέθηκαν!");

            // ── USERS - SECRETARY ────────────────────────────────────
            conn.createStatement().execute("""
                INSERT OR IGNORE INTO User 
                (userId, username, passwordHash, email, firstName, lastName, createdAt)
                VALUES 
                ('sec1','secretary1','hash1','secretary@ceid.upatras.gr','Ioanna',   'Giannacopoulou',datetime('now')),
                ('sec2','secretary2','hash2','secr2@ceid.upatras.gr',   'Georgia',  'Papadopoulou',  datetime('now')),
                ('sec3','secretary3','hash3','secr3@ceid.upatras.gr',   'Nikolaos', 'Alexandrou',    datetime('now')),
                ('sec4','secretary4','hash4','secr4@ceid.upatras.gr',   'Maria',    'Petropoulou',   datetime('now')),
                ('sec5','secretary5','hash5','secr5@ceid.upatras.gr',   'Dimitrios','Stavropoulos',  datetime('now'))
            """);
            System.out.println("✅ Users-Secretary (5) προστέθηκαν!");

            // ── USERS - STUDENTS (42) ────────────────────────────────
            conn.createStatement().execute("""
                INSERT OR IGNORE INTO User 
                (userId, username, passwordHash, email, firstName, lastName, createdAt)
                VALUES 
                ('st1', 'st1','hash','st1020001@ceid.upatras.gr','Alexios',   'Papadopoulos',   datetime('now')),
                ('st2', 'st2','hash','st1020002@ceid.upatras.gr','Maria',     'Skondra',        datetime('now')),
                ('st3', 'st3','hash','st1020003@ceid.upatras.gr','Nikos',     'Antonopoulos',   datetime('now')),
                ('st4', 'st4','hash','st1020004@ceid.upatras.gr','Eleni',     'Kostopoulou',    datetime('now')),
                ('st5', 'st5','hash','st1020005@ceid.upatras.gr','Giorgos',   'Dimitriou',      datetime('now')),
                ('st6', 'st6','hash','st1020006@ceid.upatras.gr','Sofia',     'Nikolaou',       datetime('now')),
                ('st7', 'st7','hash','st1020007@ceid.upatras.gr','Kostas',    'Vasileiadis',    datetime('now')),
                ('st8', 'st8','hash','st1020008@ceid.upatras.gr','Anna',      'Panagiotopoulou',datetime('now')),
                ('st9', 'st9','hash','st1020009@ceid.upatras.gr','Petros',    'Alexiou',        datetime('now')),
                ('st10','st10','hash','st1020010@ceid.upatras.gr','Katerina', 'Georgiou',       datetime('now')),
                ('st11','st11','hash','st1030011@ceid.upatras.gr','Stavros',  'Papadimitriou',  datetime('now')),
                ('st12','st12','hash','st1030012@ceid.upatras.gr','Christina','Lampropoulou',   datetime('now')),
                ('st13','st13','hash','st1030013@ceid.upatras.gr','Andreas',  'Makris',         datetime('now')),
                ('st14','st14','hash','st1030014@ceid.upatras.gr','Ioanna',   'Theodorou',      datetime('now')),
                ('st15','st15','hash','st1030015@ceid.upatras.gr','Michalis', 'Konstantinou',   datetime('now')),
                ('st16','st16','hash','st1030016@ceid.upatras.gr','Despoina', 'Athanasiou',     datetime('now')),
                ('st17','st17','hash','st1030017@ceid.upatras.gr','Panagiotis','Oikonomou',     datetime('now')),
                ('st18','st18','hash','st1030018@ceid.upatras.gr','Vasiliki', 'Papanikolaou',   datetime('now')),
                ('st19','st19','hash','st1030019@ceid.upatras.gr','Thanos',   'Ioannidis',      datetime('now')),
                ('st20','st20','hash','st1030020@ceid.upatras.gr','Rania',    'Karagianni',     datetime('now')),
                ('st21','st21','hash','st1040021@ceid.upatras.gr','Leonidas', 'Mavropoulos',    datetime('now')),
                ('st22','st22','hash','st1040022@ceid.upatras.gr','Fotini',   'Christodoulou',  datetime('now')),
                ('st23','st23','hash','st1040023@ceid.upatras.gr','Spyros',   'Triantafyllou',  datetime('now')),
                ('st24','st24','hash','st1040024@ceid.upatras.gr','Angeliki', 'Sotiriou',       datetime('now')),
                ('st25','st25','hash','st1040025@ceid.upatras.gr','Dimitris', 'Papageorgiou',   datetime('now')),
                ('st26','st26','hash','st1040026@ceid.upatras.gr','Margarita','Kyriakidou',     datetime('now')),
                ('st27','st27','hash','st1040027@ceid.upatras.gr','Thomas',   'Anagnostopoulos',datetime('now')),
                ('st28','st28','hash','st1040028@ceid.upatras.gr','Zoe',      'Economou',       datetime('now')),
                ('st29','st29','hash','st1040029@ceid.upatras.gr','Alexandros','Theodorakis',   datetime('now')),
                ('st30','st30','hash','st1040030@ceid.upatras.gr','Irini',    'Papadaki',       datetime('now')),
                ('st31','st31','hash','st1050031@ceid.upatras.gr','Nektarios','Katsaros',       datetime('now')),
                ('st32','st32','hash','st1050032@ceid.upatras.gr','Panagiota','Stefanidou',     datetime('now')),
                ('st33','st33','hash','st1050033@ceid.upatras.gr','Gianni',   'Roussos',        datetime('now')),
                ('st34','st34','hash','st1050034@ceid.upatras.gr','Eleftheria','Karamanlidou',  datetime('now')),
                ('st35','st35','hash','st1050035@ceid.upatras.gr','Vaggelis', 'Simonidis',      datetime('now')),
                ('st36','st36','hash','st1050036@ceid.upatras.gr','Kyriaki',  'Deligianni',     datetime('now')),
                ('st37','st37','hash','st1050037@ceid.upatras.gr','Apostolos','Karagiannidis',  datetime('now')),
                ('st38','st38','hash','st1050038@ceid.upatras.gr','Melina',   'Fragkopoulou',   datetime('now')),
                ('st39','st39','hash','st1050039@ceid.upatras.gr','Ioannis',  'Tzimas',         datetime('now')),
                ('st40','st40','hash','st1050040@ceid.upatras.gr','Stavroula','Bekiari',        datetime('now')),
                ('test.student1@ceid.upatras.gr', 'test_st1', 'hash', 'test.student1@ceid.upatras.gr', 'Testing', 'Student_Passed', datetime('now')),
                ('test.student2@ceid.upatras.gr', 'test_st2', 'hash', 'test.student2@ceid.upatras.gr', 'Testing', 'Student_Failed', datetime('now'))
            """);
            System.out.println("✅ Users-Students (42) προστέθηκαν!");

            // ── PROFESSORS ───────────────────────────────────────────
            conn.createStatement().execute("""
                INSERT OR IGNORE INTO Professor 
                (professorId, userId, office, maxTeachingLoad, currentTeachingLoad)
                VALUES 
                (1, 'p1','Κτίριο Β-101',6,3),(2,'p2','Κτίριο Β-102',6,4),
                (3,'p3','Κτίριο Α-201',6,2),(4,'p4','Κτίριο Α-205',6,3),
                (5,'p5','Κτίριο Β-305',6,3),(6,'p6','Κτίριο Α-110',6,2),
                (7,'p7','Κτίριο Β-210',6,3),(8,'p8','Κτίριο Α-301',6,2),
                (9,'p9','Κτίριο Β-401',6,3),(10,'p10','Κτίριο Α-105',6,2),
                (11,'p11','Κτίριο Β-205',6,3),(12,'p12','Κτίριο Α-310',6,2),
                (13,'p13','Κτίριο Β-115',6,3),(14,'p14','Κτίριο Α-215',6,2),
                (15,'p15','Κτίριο Β-315',6,3),(16,'p16','Κτίριο Α-415',6,2),
                (17,'p17','Κτίριο Β-120',6,3),(18,'p18','Κτίριο Α-220',6,2),
                (19,'p19','Κτίριο Β-320',6,3),(20,'p20','Κτίριο Α-420',6,2),
                (21,'p21','Κτίριο Β-125',6,3),(22,'p22','Κτίριο Α-225',6,2),
                (23,'p23','Κτίριο Β-325',6,3),(24,'p24','Κτίριο Α-425',6,2),
                (25,'p25','Κτίριο Β-130',6,3)
            """);
            System.out.println("✅ Professors (25) προστέθηκαν!");

            // ── SECRETARY ────────────────────────────────────────────
            conn.createStatement().execute("""
                INSERT OR IGNORE INTO Secretary (secretaryId, userId, department)
                VALUES 
                (1,'sec1','Γραμματεία ΤΜΗΥΠ'),(2,'sec2','Γραμματεία ΤΜΗΥΠ'),
                (3,'sec3','Γραμματεία ΤΜΗΥΠ'),(4,'sec4','Γραμματεία ΤΜΗΥΠ'),
                (5,'sec5','Γραμματεία ΤΜΗΥΠ')
            """);
            System.out.println("✅ Secretary (5) προστέθηκαν!");

            // ── STUDENTS ─────────────────────────────────────────────
            conn.createStatement().execute("""
                INSERT OR IGNORE INTO Student 
                (studentId, userId, registrationYear, currentSemester, 
                 completedECTS, notificationsEnabled, academicHistory)
                VALUES 
                (1,'st1',2020,9,240,1,'GPA:8.9'),(2,'st2',2020,9,235,1,'GPA:8.5'),
                (3,'st3',2020,9,230,1,'GPA:8.1'),(4,'st4',2020,9,220,1,'GPA:7.8'),
                (5,'st5',2020,9,210,1,'GPA:7.5'),(6,'st6',2020,9,200,1,'GPA:7.2'),
                (7,'st7',2020,9,195,0,'GPA:6.9'),(8,'st8',2020,9,190,1,'GPA:7.0'),
                (9,'st9',2020,9,185,1,'GPA:7.3'),(10,'st10',2020,9,180,1,'GPA:7.6'),
                (11,'st11',2021,7,180,1,'GPA:8.7'),(12,'st12',2021,7,175,1,'GPA:8.3'),
                (13,'st13',2021,7,170,1,'GPA:8.0'),(14,'st14',2021,7,165,1,'GPA:7.7'),
                (15,'st15',2021,7,160,1,'GPA:7.4'),(16,'st16',2021,7,155,0,'GPA:7.1'),
                (17,'st17',2021,7,150,1,'GPA:6.8'),(18,'st18',2021,7,145,1,'GPA:7.2'),
                (19,'st19',2021,7,140,1,'GPA:7.5'),(20,'st20',2021,7,135,1,'GPA:7.8'),
                (21,'st21',2022,5,120,1,'GPA:8.5'),(22,'st22',2022,5,115,1,'GPA:8.1'),
                (23,'st23',2022,5,110,1,'GPA:7.8'),(24,'st24',2022,5,105,1,'GPA:7.5'),
                (25,'st25',2022,5,100,1,'GPA:7.2'),(26,'st26',2022,5,95,0,'GPA:6.9'),
                (27,'st27',2022,5,90,1,'GPA:7.0'),(28,'st28',2022,5,85,1,'GPA:7.3'),
                (29,'st29',2022,5,80,1,'GPA:7.6'),(30,'st30',2022,5,75,1,'GPA:7.9'),
                (31,'st31',2023,3,60,1,'GPA:8.2'),(32,'st32',2023,3,55,1,'GPA:7.9'),
                (33,'st33',2023,3,50,1,'GPA:7.6'),(34,'st34',2023,3,45,1,'GPA:7.3'),
                (35,'st35',2023,3,40,0,'GPA:7.0'),(36,'st36',2023,3,35,1,'GPA:6.7'),
                (37,'st37',2023,3,30,1,'GPA:7.1'),(38,'st38',2023,3,25,1,'GPA:7.4'),
                (39,'st39',2023,3,20,1,'GPA:7.7'),(40,'st40',2023,3,15,1,'GPA:8.0'),
                (41, 'test.student1@ceid.upatras.gr', 2020, 9, 240, 1, 'GPA:8.5'), 
                (42, 'test.student2@ceid.upatras.gr', 2023, 3, 45,  1, 'GPA:6.0') 
            """);
            System.out.println("✅ Students (42) προστέθηκαν!");

            // ── COURSES ──────────────────────────────────────────────
            conn.createStatement().execute("""
                INSERT OR IGNORE INTO Course 
                (courseId,title,description,ects,semester,groupA,groupB,directions,isActive,averageRating,workloadScore)
                VALUES 
                ('CEID_22Y101','Διακριτά Μαθηματικά','Λογική, σύνολα, γράφοι',7,1,0,0,'',1,7.2,6.5),
                ('CEID_22Y102','Γραμμική Άλγεβρα','Πίνακες, διανύσματα',7,1,0,0,'',1,7.0,6.2),
                ('CEID_22Y103','Εισαγωγή στον Προγραμματισμό','Βασικός προγραμματισμός',9,1,0,0,'',1,8.1,7.5),
                ('CEID_22Y104','Βασικές Αρχές Οργάνωσης Υπολογιστικών Συστημάτων','Οργάνωση Η/Υ',7,1,0,0,'',1,7.5,6.0),
                ('CEID_22Y105','Γενικά Μαθηματικά Ι','Απειροστικός λογισμός',7,2,0,0,'',1,6.8,7.0),
                ('CEID_23Y106','Αντικειμενοστρεφής Προγραμματισμός','OOP με Java',9,2,0,0,'',1,8.5,7.8),
                ('CEID_23Y107','Λογική Σχεδίαση','Ψηφιακά κυκλώματα',7,2,0,0,'',1,7.3,6.5),
                ('CEID_22Y108','Ηλεκτρικά Κυκλώματα','Θεωρία κυκλωμάτων',7,2,0,0,'',1,6.9,6.8),
                ('CEID_23Y201','Γενικά Μαθηματικά ΙΙ','Διαφορικές εξισώσεις',6,3,0,0,'',1,6.5,6.8),
                ('CEID_23Y202','Θεωρία Γραφημάτων και Εφαρμογές','Γράφοι και αλγόριθμοι',6,3,0,0,'',1,7.8,6.5),
                ('CEID_23Y203','Αρχιτεκτονική Υπολογιστών','CPU, μνήμη, I/O',4,3,0,0,'',1,7.1,6.3),
                ('CEID_23Y204','Πιθανότητες και Αρχές Στατιστικής','Στατιστική ανάλυση',6,3,0,0,'',1,7.1,6.8),
                ('CEID_23Y205','Εισαγωγή στους Αλγόριθμους','Αλγόριθμοι, πολυπλοκότητα',6,3,0,0,'',1,8.0,7.2),
                ('CEID_23Y211','Εργαστήριο Λογικού Σχεδιασμού','Lab ψηφιακών κυκλωμάτων',2,3,0,0,'',1,7.5,5.0),
                ('CEID_23Y206','Αρχές Γλωσσών Προγραμματισμού','Μεταγλωττιστές, γλώσσες',6,4,0,0,'',1,7.5,7.0),
                ('CEID_23Y207','Θεωρία Σημάτων και Συστημάτων','Σήματα, μετασχηματισμοί',6,4,0,0,'',1,7.0,7.2),
                ('CEID_23Y208','Αναλογικά και Ψηφιακά Ηλεκτρονικά','Ηλεκτρονικά κυκλώματα',4,4,0,0,'',1,6.8,6.5),
                ('CEID_23Y209','Αριθμητική Ανάλυση','Αριθμητικές μέθοδοι',6,4,0,0,'',1,7.2,6.8),
                ('CEID_23Y210','Δομές Δεδομένων','Λίστες, δέντρα, hashing',6,4,0,0,'',1,7.9,7.0),
                ('CEID_23Y212','Εργαστήριο Αρχιτεκτονικής','Lab αρχιτεκτονικής',2,4,0,0,'',1,7.3,5.0),
                ('CEID_24Y303','Εργαστήριο Ηλεκτρονικών','Lab ηλεκτρονικών',2,5,0,0,'',1,7.0,5.0),
                ('CEID_24Y330','Λειτουργικά Συστήματα','Linux, processes, memory',6,5,0,0,'',1,7.6,7.0),
                ('CEID_24Y334','Βάσεις Δεδομένων','SQL, κανονικοποίηση',6,5,0,0,'',1,8.2,6.8),
                ('CEID_24Y351','Τεχνητή Νοημοσύνη','ML, νευρωνικά δίκτυα',6,5,0,0,'',1,8.4,7.3),
                ('CEID_24Y361','Συστήματα Μικροϋπολογιστών','Embedded systems',5,5,0,0,'',1,7.2,6.5),
                ('CEID_24Y381','Ψηφιακή Επεξεργασία Σημάτων','DSP αλγόριθμοι',5,5,0,0,'',1,7.4,7.0),
                ('CEID_24Y387','Δίκτυα Υπολογιστών','TCP/IP, πρωτόκολλα',6,6,0,0,'',1,7.8,7.0),
                ('CEID_24Y302','Βασικές Έννοιες Συστημάτων Επικοινωνίας','Επικοινωνίες',6,6,0,0,'',1,7.1,6.8),
                ('CEID_24Y332','Τεχνολογία Λογισμικού','UML, agile, patterns',6,6,0,0,'',1,7.5,6.5),
                ('CEID_24Y338','Προγραμματισμός στον Παγκόσμιο Ιστό','HTML, JS, React',6,6,0,0,'',1,8.7,7.8),
                ('CEID_24Y301','Θεωρία Υπολογισμού','Αυτόματα, Turing, NP',6,6,0,0,'',1,7.4,7.5),
                ('CEID_25Y401','Προγραμματισμός Συστημάτων','System programming',5,7,0,0,'',1,7.8,7.2),
                ('CEID_NE5057','Αλγόριθμοι και Συνδυαστική Βελτιστοποίηση','LP, ILP, heuristics',5,7,1,0,'K1:A',1,7.9,8.0),
                ('CEID_NE4117','Κατανεμημένα Συστήματα','Fault tolerance, consensus',5,7,1,1,'K1:A,K2:B,K4:B,K6:B',1,8.1,7.5),
                ('CEID_NE5017','Πιθανοτικές Τεχνικές','Τυχαίοι αλγόριθμοι',5,7,1,0,'K1:A',1,7.5,7.8),
                ('CEID_24EE594','Αλγοριθμικές Τεχνικές Επιστήμης Δεδομένων','Data science αλγόριθμοι',5,8,1,0,'K1:A',1,7.8,7.5),
                ('CEID_NE4168','Κρυπτογραφία','Κρυπτογραφικά πρωτόκολλα',5,8,1,1,'K1:A,K2:B,K4:B,K6:B',1,8.0,7.8),
                ('CEID_NE4128','Παράλληλοι Αλγόριθμοι','OpenMP, MPI',5,7,1,1,'K1:B,K2:B,K6:B',1,7.7,7.8),
                ('CEID_NE5038','Σημασιολογία στην Επιστήμη Υπολογιστών','Σημασιολογικά μοντέλα',5,7,0,1,'K1:B',1,7.2,7.0),
                ('CEID_NE5237','Στατιστικές Μέθοδοι Μηχανικής Μάθησης','Στατιστικές μέθοδοι ML',5,7,1,1,'K1:B,K3:A,K6:B',1,8.0,7.5),
                ('CEID_NE4338','Πολυδιάστατες Δομές Δεδομένων','Χωρικές δομές',5,7,0,1,'K1:B,K5:B,K6:A',1,7.5,7.2),
                ('CEID_NE565','Ανάπτυξη Βιντεοπαιγνιδιών','Game development',5,7,0,1,'K1:B,K2:B,K5:B',1,8.5,7.0),
                ('CEID_NE590','Κυβερνοασφάλεια','Network security, hacking',5,7,1,1,'K1:B,K2:B,K3:A,K4:B,K6:B',1,8.9,8.0),
                ('CEID_NE509','Αλγοριθμική Θεωρία Παιγνίων','Game theory',5,8,0,1,'K1:B',1,7.8,7.5),
                ('CEID_NE5288','Ειδικά Θέματα Υπολογιστικής Λογικής','Computational logic',5,8,0,1,'K1:B',1,7.3,7.5),
                ('CEID_25EE606','Τοπολογική Ανάλυση Δεδομένων','Topological data analysis',5,8,1,1,'K1:B,K6:A',1,7.5,7.5),
                ('CEID_NE592','Βασικές Αρχές Δικτύων Κινητών Επικοινωνιών','5G, κινητές επικοινωνίες',5,7,1,0,'K2:A',1,7.5,7.0),
                ('CEID_NE489','Ευφυείς Τεχνολογίες Ασύρματων Επικοινωνιών','Ασύρματες επικοινωνίες',5,7,1,0,'K2:A',1,7.3,7.0),
                ('CEID_NE575','Υλοποιήσεις Ασφάλειας Δικτύων','Network security',5,7,1,0,'K2:A',1,7.8,7.5),
                ('CEID_NE577','Αρχιτεκτονικές Δικτύων Επόμενης Γενιάς','Next-gen networks',5,8,1,0,'K2:A',1,7.4,7.2),
                ('CEID_NE5168','Ευρυζωνικές Τεχνολογίες','Broadband technologies',5,8,1,0,'K2:A',1,7.2,7.0),
                ('CEID_NE4157','Δίκτυα Δημόσιας Χρήσης','WAN, Internet',5,7,0,1,'K2:B',1,7.4,7.0),
                ('CEID_NE574','Οπτικά Δίκτυα Επικοινωνιών','Fiber, WDM',5,7,0,1,'K2:B',1,7.2,7.0),
                ('CEID_NE4547','Τεχνικές Εκτίμησης Συστημάτων','Performance evaluation',5,7,0,1,'K2:B,K5:B,K6:B',1,7.0,6.8),
                ('CEID_NE576','Διάχυτος Υπολογισμός','Pervasive computing, IoT',5,8,1,1,'K2:B,K5:A,K6:B',1,7.4,7.0),
                -- Κ3 Ομάδα Α
                ('CEID_NE5597','Ανάκτηση Πληροφορίας','Search engines, IR',5,7,1,1,'K3:A,K5:B,K6:A',1,7.8,7.2),
                ('CEID_NE471','Όραση Υπολογιστών και Γραφικά','Computer vision',5,7,1,0,'K3:A',1,7.9,7.5),
                ('CEID_NE4847','Στατιστική Επεξεργασία Σήματος','Signal processing ML',5,7,1,0,'K3:A',1,7.6,7.3),
                ('CEID_NE4828','Επεξεργασία και Ανάλυση Εικόνας','Image processing',5,8,1,1,'K3:A,K4:B',1,7.8,7.5),
                ('CEID_NE593','Επεξεργασία Σημάτων και Μάθηση','Signals meets ML',5,8,0,1,'K3:B',1,7.6,7.5),
                ('CEID_NE579','Εφαρμογές Ψηφιακής Επεξεργασίας Σημάτων','DSP applications',5,8,0,1,'K3:B,K4:B',1,7.4,7.2),
                ('CEID_25EE609','Επεξεργασία και Ανάλυση Video','Video deep learning',5,8,1,0,'K3:A,K6:A',1,8.0,7.5),
                -- Κ4 Ομάδα Α
                ('CEID_NE4648','Εισαγωγή σε VLSI','VLSI design, CMOS',5,7,1,0,'K4:A',1,7.2,7.5),
                ('CEID_NE5678','Σχεδιασμός Συστημάτων Ειδικού Σκοπού','Embedded design',5,7,1,0,'K4:A',1,7.0,7.0),
                ('CEID_NE591','Ασφάλεια σε Υλικό','Hardware security',5,8,1,0,'K4:A',1,7.5,7.5),
                ('CEID_NE588','Ενσωματωμένα Συστήματα','RTOS, embedded',5,8,1,1,'K4:A,K3:B',1,7.6,7.2),
                ('CEID_NE4617','Προχωρημένα Θέματα Αρχιτεκτονικής','Advanced architecture',5,8,1,0,'K4:A',1,7.4,7.5),
                ('CEID_NE4658','Σχεδιασμός με Χρήση Υπολογιστών (CAD)','CAD, EDA tools',5,8,1,0,'K4:A',1,7.1,7.2),
                ('CEID_NE5668','Ειδικά Θέματα Ψηφιακών Συστημάτων','Digital systems',5,8,1,0,'K4:A',1,7.3,7.0),
                -- Κ5 Ομάδα Α
                ('CEID_25EX598','Αλληλεπίδραση Ανθρώπου-Υπολογιστή','HCI, UX design',5,7,1,0,'K5:A',1,8.1,6.5),
                ('CEID_NE5407','Λογισμικό Συστημάτων Υψηλής Επίδοσης','HPC programming',5,7,1,1,'K5:A,K6:A',1,7.8,8.0),
                ('CEID_NE5577','Ποιότητα Λογισμικού','Software quality, testing',5,7,1,0,'K5:A',1,7.5,6.5),
                ('CEID_NE5367','Προηγμένα Πληροφοριακά Συστήματα','Advanced IS',5,7,1,1,'K5:A,K6:B',1,7.6,7.0),
                ('CEID_24EE595','Διαχείριση Έργων Λογισμικού','Scrum, Kanban',5,8,1,0,'K5:A',1,8.3,6.0),
                ('CEID_25EE607','Παράλληλη Επεξεργασία','Parallel computing, GPU',5,8,1,1,'K5:A,K6:A',1,7.9,8.0),
                -- Κ5 Ομάδα Β
                ('CEID_NE1411','Αλγόριθμοι Αποκεντρωμένων Δεδομένων','Blockchain, P2P',5,7,0,1,'K5:B,K6:B',1,7.8,7.5),
                ('CEID_NE584','e-Επιχειρείν','E-business, digital economy',5,7,0,1,'K5:B',1,7.5,5.8),
                ('CEID_NE5908','Κοινωνικές Πλευρές Τεχνολογίας','Tech law, society',5,8,0,1,'K5:B',1,7.2,5.5),
                -- Κ6 Ομάδα Α
                ('CEID_NE562','Εξόρυξη Δεδομένων και Μηχανική Μάθηση','Data mining, ML',5,8,1,1,'K6:A,K3:B,K5:B',1,8.6,7.5),
                ('CEID_NE4348','Συστήματα Διαχείρισης Μεγάλων Δεδομένων','Hadoop, Spark',5,8,1,1,'K6:A,K3:B,K5:B',1,8.2,7.8),
                -- Κοινά
                ('CEID_NE548','Εισαγωγή στη Βιοπληροφορική','Bioinformatics',5,8,0,1,'K1:B,K3:B,K5:B,K6:B',1,7.6,7.2),
                ('CEID_NE520','Αλγόριθμοι ΤΝ για IoT','AI for IoT',5,8,0,1,'K1:B,K2:A,K3:B,K4:B,K5:B',1,7.9,7.5),
                ('CEID_NE5078','Τεχνολογίες Υλοποίησης Αλγορίθμων','Algorithm implementation',5,8,0,1,'K1:B,K4:B',1,7.5,7.8),
                ('CEID_NE5357','Εισαγωγή στα Πληροφοριακά Συστήματα','IS fundamentals',5,8,0,1,'K5:B,K6:B',1,7.4,6.5),
                ('CEID_24EE596','Μέθοδοι Μητρώων Επιστήμης Δεδομένων','Matrix methods',5,8,0,1,'K1:B,K6:B',1,7.3,7.5)
            """);
            System.out.println("✅ Courses (85+) προστέθηκαν!");

            // ── SCENARIOS ────────────────────────────────────────────
            conn.createStatement().execute("""
                INSERT OR IGNORE INTO Scenario 
                (scenarioId, title, description, groupARequiredECTS, groupBRequiredECTS)
                VALUES 
                (1,'Πρώτο Σενάριο',
                 'Μία κύρια Κατεύθυνση: 5A+5B+5A από 3 άλλες+2 ελεύθερα',
                 60, 30),
                (2,'Δεύτερο Σενάριο',
                 'Δύο κύριες Κατευθύνσεις: (5A+2B)+(5A+2B)+3 ελεύθερα',
                 45, 45),
                (3,'Τρίτο Σενάριο',
                 'Γενικής Κατεύθυνσης: 10 Α από όλες+7 ελεύθερα',
                 30, 30)
            """);
            System.out.println("✅ Scenarios (3) προστέθηκαν!");

            // ── STUDY PLANS ──────────────────────────────────────────
            conn.createStatement().execute("""
                INSERT OR IGNORE INTO StudyPlan 
                (planId,studentId,scenarioId,academicYear,directionName,
                 mainDirection,secondDirection,
                 totalECTS,status,isValid,isDraft,isFinalized,
                 workLoadIndex,createdDate,lastUpdatedDate)
                VALUES 
                (1,1,1,'2024-2025','K5-Τεχνολογία Λογισμικού','K5',null,240,'FINALIZED',1,0,1,7.2,datetime('now'),datetime('now')),
                (2,2,1,'2024-2025','K1-Αλγοριθμικές Θεμελιώσεις','K1',null,235,'FINALIZED',1,0,1,7.8,datetime('now'),datetime('now')),
                (3,3,2,'2024-2025','K5+K6','K5','K6',230,'FINALIZED',1,0,1,7.5,datetime('now'),datetime('now')),
                (4,4,1,'2024-2025','K6-Ευφυή Συστήματα','K6',null,220,'FINALIZED',1,0,1,7.0,datetime('now'),datetime('now')),
                (5,5,3,'2024-2025','Γενική Κατεύθυνση',null,null,210,'ACTIVE',1,0,0,6.8,datetime('now'),datetime('now')),
                (6,6,1,'2024-2025','K2-Δίκτυα','K2',null,200,'ACTIVE',1,0,0,6.5,datetime('now'),datetime('now')),
                (7,7,2,'2024-2025','K1+K2','K1','K2',195,'ACTIVE',1,0,0,6.2,datetime('now'),datetime('now')),
                (8,8,1,'2024-2025','K4-Τεχνολογία Υλικού','K4',null,190,'ACTIVE',1,0,0,6.8,datetime('now'),datetime('now')),
                (9,9,3,'2024-2025','Γενική Κατεύθυνση',null,null,185,'ACTIVE',1,0,0,7.0,datetime('now'),datetime('now')),
                (10,10,1,'2024-2025','K3-Τεχνολογία Πληροφορίας','K3',null,180,'ACTIVE',1,0,0,7.3,datetime('now'),datetime('now')),
                (11,11,1,'2024-2025','K5-Τεχνολογία Λογισμικού','K5',null,180,'ACTIVE',1,0,0,8.0,datetime('now'),datetime('now')),
                (12,12,2,'2024-2025','K3+K6','K3','K6',175,'ACTIVE',1,0,0,7.5,datetime('now'),datetime('now')),
                (13,13,1,'2024-2025','K1-Αλγοριθμικές','K1',null,170,'ACTIVE',1,0,0,7.2,datetime('now'),datetime('now')),
                (14,14,3,'2024-2025','Γενική Κατεύθυνση',null,null,165,'ACTIVE',1,0,0,6.9,datetime('now'),datetime('now')),
                (15,15,1,'2024-2025','K6-Ευφυή Συστήματα','K6',null,160,'ACTIVE',1,0,0,6.6,datetime('now'),datetime('now')),
                (16,16,2,'2024-2025','K4+K2','K4','K2',155,'DRAFT',0,1,0,6.3,datetime('now'),datetime('now')),
                (17,17,1,'2024-2025','K5-Λογισμικό','K5',null,150,'DRAFT',0,1,0,6.0,datetime('now'),datetime('now')),
                (18,18,3,'2024-2025','Γενική',null,null,145,'DRAFT',0,1,0,6.5,datetime('now'),datetime('now')),
                (19,19,1,'2024-2025','K2-Δίκτυα','K2',null,140,'DRAFT',0,1,0,7.0,datetime('now'),datetime('now')),
                (20,20,2,'2024-2025','K1+K5','K1','K5',135,'DRAFT',0,1,0,7.3,datetime('now'),datetime('now')),
                (21,21,1,'2024-2025','K5-Λογισμικό','K5',null,120,'ACTIVE',1,0,0,8.2,datetime('now'),datetime('now')),
                (22,22,1,'2024-2025','K6-Ευφυή','K6',null,115,'ACTIVE',1,0,0,7.8,datetime('now'),datetime('now')),
                (23,23,2,'2024-2025','K3+K6','K3','K6',110,'ACTIVE',1,0,0,7.5,datetime('now'),datetime('now')),
                (24,24,3,'2024-2025','Γενική',null,null,105,'DRAFT',0,1,0,7.2,datetime('now'),datetime('now')),
                (25,25,1,'2024-2025','K1-Αλγοριθμικές','K1',null,100,'DRAFT',0,1,0,6.9,datetime('now'),datetime('now'))
            """);
            System.out.println("✅ StudyPlans (25) προστέθηκαν!");

            // ── THESIS ───────────────────────────────────────────────
            conn.createStatement().execute("""
                INSERT OR IGNORE INTO Thesis 
                (thesisId,professorId,title,description,prerequisites,
                 requiredECTS,isAvailable,maxCandidates,interestedStudents,
                 requiredSkills,publicationDate,lastModifiedDate)
                VALUES 
                (1,1,'Παράλληλοι Αλγόριθμοι σε Κατανεμημένα Συστήματα',
                 'Υλοποίηση παράλληλων αλγορίθμων βελτιστοποίησης',
                 'Αλγόριθμοι, Κατανεμημένα Συστήματα',180,1,2,1,'Python,MPI',datetime('now'),datetime('now')),
                (2,2,'Σύστημα Διαχείρισης Μεγάλων Δεδομένων',
                 'Σχεδίαση Big Data συστήματος',
                 'Βάσεις Δεδομένων, Κατανεμημένα',160,1,3,2,'MongoDB,Spark',datetime('now'),datetime('now')),
                (3,3,'Αλγόριθμοι ML για Ανάλυση Γράφων',
                 'Machine learning σε δίκτυα',
                 'ΤΝ, Θεωρία Γραφημάτων',200,1,2,0,'Python,PyTorch',datetime('now'),datetime('now')),
                (4,4,'Πλατφόρμα Διαχείρισης Ακαδημαϊκής Πορείας',
                 'Εφαρμογή για φοιτητές CEID',
                 'Τεχνολογία Λογισμικού, Βάσεις',150,1,4,3,'Java,JavaFX,SQLite',datetime('now'),datetime('now')),
                (5,1,'Κρυπτογραφικά Πρωτόκολλα για IoT',
                 'Ελαφριά κρυπτογραφία για IoT',
                 'Κυβερνοασφάλεια, Δίκτυα',180,1,2,1,'C/C++,Cryptography',datetime('now'),datetime('now')),
                (6,5,'Σύστημα Εξόρυξης Δεδομένων από Ιστοσελίδες',
                 'Web scraping και NLP',
                 'Προγραμματισμός Ιστού, ΤΝ',150,1,3,2,'Python,NLTK',datetime('now'),datetime('now')),
                (7,2,'Βελτιστοποίηση Ερωτημάτων σε Κατανεμημένες Βάσεις',
                 'Query optimization',
                 'Βάσεις, Κατανεμημένα',170,0,2,2,'SQL,Spark',datetime('now'),datetime('now')),
                (8,7,'Ασφάλεια Δικτύων Επόμενης Γενιάς',
                 'Security σε 5G networks',
                 'Δίκτυα, Κυβερνοασφάλεια',180,1,2,1,'Python,Networking',datetime('now'),datetime('now')),
                (9,11,'Βαθιά Μάθηση για Αναγνώριση Εικόνας',
                 'Deep learning για image recognition',
                 'ΤΝ, Επεξεργασία Εικόνας',190,1,3,2,'Python,TensorFlow',datetime('now'),datetime('now')),
                (10,13,'Αλγόριθμοι για Δίκτυα Αισθητήρων IoT',
                 'Wireless sensor networks',
                 'Κατανεμημένα, Αλγόριθμοι',175,1,2,0,'C,Python,ns-3',datetime('now'),datetime('now'))
            """);
            System.out.println("✅ Theses (10) προστέθηκαν!");

            // ── AVAILABILITY SLOTS ───────────────────────────────────
            conn.createStatement().execute("""
                INSERT OR IGNORE INTO AvailabilitySlot 
                (slotId,professorId,date,dayOfWeek,startTime,endTime,isAvailable)
                VALUES 
                (1,1,'2025-10-06','Δευτέρα','10:00','11:00',1),
                (2,1,'2025-10-08','Τετάρτη','14:00','15:00',1),
                (3,1,'2025-10-10','Παρασκευή','11:00','12:00',1),
                (4,2,'2025-10-07','Τρίτη','09:00','10:00',1),
                (5,2,'2025-10-09','Πέμπτη','15:00','16:00',1),
                (6,2,'2025-10-14','Τρίτη','10:00','11:00',1),
                (7,3,'2025-10-06','Δευτέρα','13:00','14:00',1),
                (8,3,'2025-10-08','Τετάρτη','10:00','11:00',1),
                (9,4,'2025-10-07','Τρίτη','11:00','12:00',1),
                (10,4,'2025-10-09','Πέμπτη','14:00','15:00',1),
                (11,4,'2025-10-10','Παρασκευή','09:00','10:00',1),
                (12,5,'2025-10-06','Δευτέρα','09:00','10:00',1),
                (13,5,'2025-10-08','Τετάρτη','15:00','16:00',1),
                (14,6,'2025-10-07','Τρίτη','10:00','11:00',1),
                (15,7,'2025-10-09','Πέμπτη','11:00','12:00',1),
                (16,8,'2025-10-06','Δευτέρα','14:00','15:00',1),
                (17,9,'2025-10-08','Τετάρτη','09:00','10:00',1),
                (18,10,'2025-10-10','Παρασκευή','13:00','14:00',1)
            """);
            System.out.println("✅ AvailabilitySlots (18) προστέθηκαν!");

            // ── COURSE EVALUATIONS ───────────────────────────────────
            conn.createStatement().execute("""
                INSERT OR IGNORE INTO CourseEvaluation 
                (evaluationId,studentId,courseId,submissionDate,isSubmitted,rating,comments,isAnonymous)
                VALUES 
                (1,1,'CEID_24Y334',datetime('now'),1,9,'Εξαιρετικό SQL!',0),
                (2,1,'CEID_24Y332',datetime('now'),1,8,'Πολύ χρήσιμο',1),
                (3,1,'CEID_24Y338',datetime('now'),1,10,'Το καλύτερο μάθημα!',0),
                (4,2,'CEID_24Y351',datetime('now'),1,9,'Ενδιαφέρον ΤΝ',0),
                (5,2,'CEID_24Y387',datetime('now'),1,7,'Καλό αλλά πολύ ύλη',1),
                (6,2,'CEID_NE4117',datetime('now'),1,8,'Δύσκολο αλλά χρήσιμο',0),
                (7,3,'CEID_23Y106',datetime('now'),1,8,'Βοηθητικό OOP',0),
                (8,3,'CEID_23Y210',datetime('now'),1,7,'Καλές δομές',1),
                (9,4,'CEID_24Y338',datetime('now'),1,10,'Άριστο React!',0),
                (10,4,'CEID_24Y332',datetime('now'),1,8,'Χρήσιμο',1),
                (11,5,'CEID_NE590',datetime('now'),1,9,'Σημαντικό!',1),
                (12,5,'CEID_NE562',datetime('now'),1,9,'Εξαιρετικό ML',0),
                (13,6,'CEID_24Y334',datetime('now'),1,8,'Καλά labs SQL',0),
                (14,6,'CEID_24Y351',datetime('now'),1,9,'Η ΤΝ είναι το μέλλον!',1),
                (15,7,'CEID_22Y101',datetime('now'),1,6,'Δύσκολο',0),
                (16,8,'CEID_NE4128',datetime('now'),1,8,'Ενδιαφέρον παράλληλος',0),
                (17,9,'CEID_NE5407',datetime('now'),1,7,'Χρήσιμο HPC',1),
                (18,10,'CEID_24Y330',datetime('now'),1,8,'Καλά Linux labs',0),
                (19,11,'CEID_NE562',datetime('now'),1,9,'Άριστο ML!',0),
                (20,12,'CEID_NE471',datetime('now'),1,8,'Computer vision rocks',1)
            """);
            System.out.println("✅ CourseEvaluations (20) προστέθηκαν!");

            // ── REPORTS ──────────────────────────────────────────────
            conn.createStatement().execute("""
                INSERT OR IGNORE INTO Report 
                (reportId,generatedBy,academicYear,appliedFilters,creationDate,status,format)
                VALUES 
                (1,1,'2024-2025','Κατεύθυνση K5-Λογισμικό',datetime('now'),'COMPLETED','PDF'),
                (2,1,'2024-2025','Όλοι-Στατιστικά ECTS',datetime('now'),'COMPLETED','PDF'),
                (3,1,'2023-2024','Εξάμηνο 8-Αξιολογήσεις',datetime('now'),'COMPLETED','EXCEL'),
                (4,1,'2024-2025','K1-Ζήτηση μαθημάτων',datetime('now'),'COMPLETED','PDF'),
                (5,1,'2024-2025','9ο εξάμηνο-Πλάνα σπουδών',datetime('now'),'PENDING','PDF'),
                (6,2,'2024-2025','K6-Ευφυή Συστήματα',datetime('now'),'COMPLETED','PDF'),
                (7,2,'2023-2024','Σύγκριση K1 vs K5',datetime('now'),'COMPLETED','EXCEL')
            """);
            System.out.println("✅ Reports (7) προστέθηκαν!");

            // ── NOTIFICATIONS ────────────────────────────────────────
            conn.createStatement().execute("""
                INSERT OR IGNORE INTO Notification 
                (notificationId,recipientId,senderId,message,dateSent,isRead,notificationType)
                VALUES 
                (1,'st1','p1','Νέα διπλωματική: Παράλληλοι Αλγόριθμοι!',datetime('now'),0,'THESIS'),
                (2,'st2','p2','Νέα διπλωματική: Big Data',datetime('now'),0,'THESIS'),
                (3,'st3','p3','Νέα διπλωματική: ML Γράφοι',datetime('now'),1,'THESIS'),
                (4,'st4','p4','Ραντεβού Τρίτη 11:00',datetime('now'),1,'APPOINTMENT'),
                (5,'st5','sec1','Δήλωση μαθημάτων μέχρι 30/10',datetime('now'),0,'REMINDER'),
                (6,'st6','p1','Αίτηση ενδιαφέροντος ελήφθη',datetime('now'),1,'THESIS'),
                (7,'st7','sec2','Ολοκλήρωση πλάνου σπουδών',datetime('now'),0,'REMINDER'),
                (8,'st8','p9','Νέα διπλωματική: Deep Learning',datetime('now'),0,'THESIS'),
                (9,'st9','sec1','Αξιολόγηση μαθημάτων!',datetime('now'),0,'EVALUATION'),
                (10,'st10','p5','Νέα διπλωματική: Web Scraping',datetime('now'),0,'THESIS')
            """);
            System.out.println("✅ Notifications (10) προστέθηκαν!");

            // ── PROFESSOR-COURSE ─────────────────────────────────────
            conn.createStatement().execute("""
                INSERT OR IGNORE INTO ProfessorCourse (professorId,courseId,role)
                VALUES 
                (1,'CEID_22Y101','teaches'),(1,'CEID_24Y301','teaches'),(1,'CEID_NE4128','teaches'),
                (2,'CEID_24Y334','teaches'),(2,'CEID_24Y351','teaches'),(2,'CEID_NE4348','teaches'),
                (3,'CEID_23Y205','teaches'),(3,'CEID_NE5057','teaches'),
                (4,'CEID_24Y332','teaches'),(4,'CEID_24Y338','manages'),(4,'CEID_25EX598','teaches'),
                (5,'CEID_24Y330','teaches'),(5,'CEID_23Y210','teaches'),
                (6,'CEID_24Y338','teaches'),(6,'CEID_NE584','teaches'),
                (7,'CEID_24Y387','teaches'),(7,'CEID_NE574','teaches'),
                (8,'CEID_24Y302','teaches'),(8,'CEID_NE489','teaches'),
                (9,'CEID_24Y361','teaches'),(9,'CEID_NE590','teaches'),
                (10,'CEID_24Y381','teaches'),(10,'CEID_NE579','teaches'),
                (11,'CEID_24Y351','teaches'),(11,'CEID_NE562','teaches'),
                (12,'CEID_23Y203','teaches'),(12,'CEID_NE4648','teaches'),
                (13,'CEID_23Y204','teaches'),(13,'CEID_NE520','teaches'),
                (14,'CEID_23Y202','teaches'),
                (15,'CEID_22Y105','teaches'),(15,'CEID_23Y201','teaches'),
                (16,'CEID_23Y206','teaches'),(16,'CEID_NE5407','teaches'),
                (17,'CEID_22Y102','teaches'),
                (18,'CEID_22Y104','teaches'),(18,'CEID_NE5678','teaches'),
                (19,'CEID_22Y108','teaches'),
                (20,'CEID_NE4117','teaches'),
                (21,'CEID_23Y208','teaches'),
                (22,'CEID_22Y103','teaches'),(22,'CEID_25Y401','teaches'),
                (23,'CEID_NE575','teaches'),
                (24,'CEID_22Y101','teaches'),(24,'CEID_NE565','teaches'),
                (25,'CEID_NE592','teaches')
            """);
            System.out.println("✅ ProfessorCourse προστέθηκαν!");

            System.out.println("\n✅ Όλα τα δεδομένα εισήχθησαν επιτυχώς!");

             //  ΣΥΝΔΕΣΗ ΦΟΙΤΗΤΗ ΜΕ ΜΑΘΗΜΑΤΑ (UC2 TESTING) ──────
            // Χρησιμοποιούμε το ID 'test.student1@ceid.upatras.gr'
            // και το συνδέουμε με 3 υπαρκτά μαθήματα από τη λίστα των COURSES (π.χ. Βάσεις, Λογισμικό, Δίκτυα)
            conn.createStatement().execute("""
                INSERT OR IGNORE INTO StudentCourse (studentId, courseId)
                VALUES 
                (41, 'CEID_24Y334'), -- Βάσεις Δεδομένων (ID: 41 για τον test.student1)
                (41, 'CEID_24Y332'), -- Τεχνολογία Λογισμικού
                (41, 'CEID_24Y387')  -- Δίκτυα Υπολογιστών
            """);
            System.out.println("✅ Δοκιμαστικές εγγραφές StudentCourse (UC2) προστέθηκαν!");

            db.disconnect();

        } catch (SQLException e) {
            System.out.println("❌ Σφάλμα: " + e.getMessage());
            e.printStackTrace();
        }
    }
}