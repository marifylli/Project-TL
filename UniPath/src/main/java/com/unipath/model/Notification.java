package com.unipath.model;

public class Notification {

    private int notificationId;
    private String recipientId;
    private String senderId;
    private String message;
    private String dateSent;
    private int isRead;
    private String notificationType;

    // Constructor για τη δημιουργία νέας ειδοποίησης
    public Notification(String recipientId, String senderId, String message, String dateSent, String notificationType) {
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.message = message;
        this.dateSent = dateSent;
        this.isRead = 0; // Default: Μη διαβασμένο
        this.notificationType = notificationType;
    }

    // --- GETTERS ---
    public String getRecipientId() { return recipientId; }
    public String getSenderId() { return senderId; }
    public String getMessage() { return message; }
    public String getDateSent() { return dateSent; }
    public int getIsRead() { return isRead; }
    public String getNotificationType() { return notificationType; }

}
