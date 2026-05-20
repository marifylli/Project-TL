package com.unipath.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

    private static final String DB_URL = "jdbc:sqlite:unipath.db";
    private static DBManager instance;
    private Connection connection;

    private DBManager() {}

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
            Statement stmt = connection.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.close();
            System.out.println("SQLite Successfully connected");
        }
        return connection;
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("SQLite Disconnected!");
            }
        } catch (SQLException e) {
            System.out.println("Σφάλμα: " + e.getMessage());
        }
    }

    public void createTables() throws SQLException {
        Connection conn = connect();
        Statement stmt = conn.createStatement();

        // Πίνακας User
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS User (
                userId TEXT PRIMARY KEY,
                username TEXT NOT NULL,
                passwordHash TEXT,
                email TEXT NOT NULL UNIQUE,
                firstName TEXT,
                lastName TEXT,
                createdAt TEXT
            )
        """);



        // Πίνακας Student
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS Student (
                studentId INTEGER PRIMARY KEY,
                userId TEXT NOT NULL UNIQUE,
                registrationYear INTEGER,
                currentSemester INTEGER,
                completedECTS INTEGER DEFAULT 0,
                notificationsEnabled INTEGER DEFAULT 1,
                academicHistory TEXT,
                FOREIGN KEY (userId) REFERENCES User(userId)
            )
        """);
     //Πινακασ Proffesor
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS Professor (
                professorId INTEGER PRIMARY KEY,
                userId TEXT NOT NULL UNIQUE,
                office TEXT,
                maxTeachingLoad INTEGER DEFAULT 6,
                currentTeachingLoad INTEGER DEFAULT 0
            )
        """);

        // Πίνακας Secretary
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS Secretary (
                secretaryId INTEGER PRIMARY KEY,
                userId TEXT NOT NULL UNIQUE,
                department TEXT,
                FOREIGN KEY (userId) REFERENCES User(userId)
            )
        """);

        // Πίνακας Course
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS Course (
                courseId TEXT PRIMARY KEY,
                title TEXT NOT NULL,
                description TEXT,
                ects INTEGER,
                semester INTEGER,
                groupA INTEGER DEFAULT 0,
                groupB INTEGER DEFAULT 0,
                directions TEXT,
                isActive INTEGER DEFAULT 1,
                averageRating REAL DEFAULT 0.0,
                workloadScore REAL DEFAULT 0.0,
                workloadRank INTEGER,
                lastModifiedDate TEXT,
                lastModifiedBy TEXT,
                rules TEXT,
                prerequisites TEXΤ            
            )
        """);

// Ενδιάμεσος πίνακας Student-Course
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS StudentCourse (
                studentId INTEGER NOT NULL,
                courseId TEXT NOT NULL,
                PRIMARY KEY (studentId, courseId),
                FOREIGN KEY (studentId) REFERENCES Student(studentId),
                FOREIGN KEY (courseId) REFERENCES Course(courseId)
            )
        """);

        // Ενδιάμεσος πίνακας Professor-Course
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS ProfessorCourse (
                professorId INTEGER NOT NULL,
                courseId TEXT NOT NULL,
                role TEXT DEFAULT 'teaches',
                PRIMARY KEY (professorId, courseId),
                FOREIGN KEY (professorId) REFERENCES Professor(professorId),
                FOREIGN KEY (courseId) REFERENCES Course(courseId)
            )
        """);


        // Πίνακας Scenario
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS Scenario (
                scenarioId INTEGER PRIMARY KEY,
                title TEXT NOT NULL,
                description TEXT,
                groupARequiredECTS INTEGER,
                groupBRequiredECTS INTEGER
            )
        """);

        stmt.execute("""
    CREATE TABLE IF NOT EXISTS StudyPlan (
        planId INTEGER PRIMARY KEY,
        studentId INTEGER NOT NULL,
        scenarioId INTEGER NOT NULL,
        courses TEXT,
        academicYear TEXT,
        directionName TEXT,
        mainDirection TEXT,
        secondDirection TEXT,
        totalECTS INTEGER DEFAULT 0,
        status TEXT,
        isValid INTEGER DEFAULT 0,
        isDraft INTEGER DEFAULT 1,
        isFinalized INTEGER DEFAULT 0,
        workLoadIndex REAL DEFAULT 0.0,
        workloadCalculationDate TEXT,
        createdDate TEXT,
        lastUpdatedDate TEXT,
        FOREIGN KEY (studentId) REFERENCES Student(studentId),
        FOREIGN KEY (scenarioId) REFERENCES Scenario(scenarioId)
    )
""");
        // Πίνακας StudentProfile
        stmt.execute("""
    CREATE TABLE IF NOT EXISTS StudentProfile (
        profileId INTEGER PRIMARY KEY,
        studentId INTEGER NOT NULL UNIQUE,
        activePlanId INTEGER,
        workloadIndex REAL,
        transcriptId TEXT,
        lastUpdated TEXT,
        FOREIGN KEY (studentId) REFERENCES Student(studentId),
        FOREIGN KEY (activePlanId) REFERENCES StudyPlan(planId)
    )
""");

// Πίνακας CourseEvaluation
        stmt.execute("""
    CREATE TABLE IF NOT EXISTS CourseEvaluation (
        evaluationId INTEGER PRIMARY KEY,
        studentId INTEGER NOT NULL,
        courseId TEXT NOT NULL,
        submissionDate TEXT,
        isSubmitted INTEGER DEFAULT 0,
        rating INTEGER,
        comments TEXT,
        isAnonymous INTEGER DEFAULT 0,
        FOREIGN KEY (studentId) REFERENCES Student(studentId),
        FOREIGN KEY (courseId) REFERENCES Course(courseId)
    )
""");

// Πίνακας Report
        stmt.execute("""
    CREATE TABLE IF NOT EXISTS Report (
        reportId INTEGER PRIMARY KEY,
        generatedBy INTEGER NOT NULL,
        academicYear TEXT,
        appliedFilters TEXT,
        creationDate TEXT,
        exportDate TEXT,
        filterCriteria TEXT,
        status TEXT,
        format TEXT,
        FOREIGN KEY (generatedBy) REFERENCES Secretary(secretaryId)
    )
""");

// Πίνακας Thesis
        stmt.execute("""
    CREATE TABLE IF NOT EXISTS Thesis (
        thesisId INTEGER PRIMARY KEY,
        professorId INTEGER NOT NULL,
        title TEXT NOT NULL,
        description TEXT,
        prerequisites TEXT,
        requiredECTS INTEGER,
        isAvailable INTEGER DEFAULT 1,
        maxCandidates INTEGER,
        interestedStudents INTEGER DEFAULT 0,
        publicationDate TEXT,
        requiredSkills TEXT,
        lastModifiedDate TEXT,
        FOREIGN KEY (professorId) REFERENCES Professor(professorId)
    )
""");

        // Πίνακας AvailabilitySlot
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS AvailabilitySlot (
                slotId INTEGER PRIMARY KEY,
                professorId INTEGER NOT NULL,
                date TEXT,
                dayOfWeek TEXT,
                startTime TEXT,
                endTime TEXT,
                isAvailable INTEGER DEFAULT 1,
                FOREIGN KEY (professorId) REFERENCES Professor(professorId)
            )
        """);

        // Ενδιάμεσος πίνακας Thesis-AvailabilitySlot
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS ThesisSlot (
                thesisId INTEGER NOT NULL,
                slotId INTEGER NOT NULL,
                PRIMARY KEY (thesisId, slotId),
                FOREIGN KEY (thesisId) REFERENCES Thesis(thesisId),
                FOREIGN KEY (slotId) REFERENCES AvailabilitySlot(slotId)
            )
        """);


        // Πίνακας Calendar
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS Calendar (
                calendarId TEXT PRIMARY KEY,
                lastUpdated TEXT,
                updatedBy TEXT,
                availabilitySlots TEXT
            )
        """);

        // Πίνακας InterviewMeeting
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS InterviewMeeting (
                meetingId INTEGER PRIMARY KEY,
                studentId INTEGER NOT NULL,
                professorId INTEGER NOT NULL,
                slotId INTEGER NOT NULL,
                thesisId INTEGER NOT NULL,
                date TEXT,
                startTime TEXT,
                status TEXT,
                confirmedAt TEXT,
                notificationSent INTEGER DEFAULT 0,
                FOREIGN KEY (studentId) REFERENCES Student(studentId),
                FOREIGN KEY (professorId) REFERENCES Professor(professorId),
                FOREIGN KEY (slotId) REFERENCES AvailabilitySlot(slotId),
                FOREIGN KEY (thesisId) REFERENCES Thesis(thesisId)
            )
        """);

        // Πίνακας HelpOffer
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS HelpOffer (
                offerId INTEGER PRIMARY KEY,
                mentorId INTEGER NOT NULL,
                courseId TEXT NOT NULL,
                helpType TEXT,
                fileUrl TEXT,
                meetingUrl TEXT,
                isActive INTEGER DEFAULT 1,
                publicationDate TEXT,
                downloadCount INTEGER DEFAULT 0,
                meetingAccessCount INTEGER DEFAULT 0,
                FOREIGN KEY (mentorId) REFERENCES Student(studentId),
                FOREIGN KEY (courseId) REFERENCES Course(courseId)
            )
        """);

        // Πίνακας Notification
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS Notification (
                notificationId INTEGER PRIMARY KEY,
                recipientId TEXT NOT NULL,
                senderId TEXT,
                message TEXT,
                dateSent TEXT,
                isRead INTEGER DEFAULT 0,
                notificationType TEXT,
                FOREIGN KEY (recipientId) REFERENCES User(userId),
                FOREIGN KEY (senderId) REFERENCES User(userId)
            )
        """);

        System.out.println("Οι πίνακες δημιουργήθηκαν επιτυχώς!");
        stmt.close();
    }
}