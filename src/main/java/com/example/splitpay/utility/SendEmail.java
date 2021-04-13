package com.example.splitpay.utility;
// Java program to send email

import javax.activation.*;
import javax.mail.*;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.Properties;


public class SendEmail
{
    public static void sendEmail(String sender , String password, String subject , String textMessage, String recipient){
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();

        // Setting properties for creating session
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.starttls.enable", "true");
        // creating session object
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sender,password);
                    }
                });
        System.out.println("session created");

        try
        {
            // MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setText(textMessage);
            // Send email.
            Transport.send(message);
            System.out.println("Email successfully sent");
        }
        catch (MessagingException mex)
        {
            mex.printStackTrace();
        }
    }

}


