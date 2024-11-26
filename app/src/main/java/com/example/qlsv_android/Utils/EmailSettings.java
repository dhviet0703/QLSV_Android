package com.example.qlsv_android.Utils;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSettings {
    private final String senderEmail;
    private final String senderPassword;

    // Constructor
    public EmailSettings(String senderEmail, String senderPassword) {
        this.senderEmail = senderEmail;
        this.senderPassword = senderPassword;
    }

    // Callback interface
    public interface EmailSendCallback {
        void onEmailSent(boolean success);
    }

    // Method to send email with callback
    @SuppressLint("StaticFieldLeak")
    public void sendEmail(final String recipientEmail, final String subject, final String body, final EmailSendCallback callback) {
        new AsyncTask<Void, Void, Boolean>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    Properties props = new Properties();
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.port", "587");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");

                    // Create a session with the email credentials
                    Session session = Session.getInstance(props,
                            new javax.mail.Authenticator() {
                                @Override
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(senderEmail, senderPassword);
                                }
                            });

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(senderEmail));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                    message.setSubject(subject);
                    message.setText(body);

                    Transport.send(message);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                // Call the callback with the result
                callback.onEmailSent(success);
            }
        }.execute();
    }
}
