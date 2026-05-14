package com.unipath.login;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Properties;



public class EmailOtpService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private final String fromEmail;
    private final String fromPassword;

    public EmailOtpService() {
        try (InputStream in = getClass()
                .getResourceAsStream("/config/config.properties")) {
            Properties config = new Properties();
            config.load(in);
            this.fromEmail    = config.getProperty("email.from");
            this.fromPassword = config.getProperty("email.password");
        } catch (Exception e) {
            throw new RuntimeException("Αδυναμία φόρτωσης config: " + e.getMessage());
        }
    }

    public String sendOtp(String toEmail) throws Exception {
        String otp = String.format("%06d", RANDOM.nextInt(1_000_000));

        Properties props = new Properties();
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host",            "smtp.gmail.com");
        props.put("mail.smtp.port",            "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, fromPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail, "CEID Study Plan App"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Κωδικός Επαλήθευσης — Σύστημα Ακαδημαϊκής Πορείας CEID");
        message.setText(
                "Ο κωδικός επαλήθευσής σας είναι:\n\n" +
                        "     " + otp + "\n\n" +
                        "Ισχύει για 5 λεπτά.\n" +
                        "Αν δεν το ζητήσατε εσείς, αγνοήστε αυτό το email."
        );

        Transport.send(message);
        return otp;
    }
}