package org.oszimt.fa83.emailhandler;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailHandler {



    Session mailSession;
    MimeMessage emailMessage;

    public static void main(String args[]) throws AddressException,
            MessagingException {

        Properties properties = setMailServerProperties();
        createEmailMessage(properties);
    }

    public static Properties setMailServerProperties() {

        String emailPort = "587";//gmail's smtp port

        Properties emailProperties = new Properties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
        return emailProperties;

    }

    public static void createEmailMessage(Properties emailProperties) throws AddressException,
            MessagingException {
        String[] toEmails = { "he.ahrens@gmail.com" };
        String emailSubject = "Java Email";
        String emailBody = "This is an email sent by JavaMail api.";



        Session mailSession = Session.getDefaultInstance(emailProperties, null);
        MimeMessage emailMessage = new MimeMessage(mailSession);

        for (int i = 0; i < toEmails.length; i++) {
            emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
        }

        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailBody, "text/html");//for a html email
        //emailMessage.setText(emailBody);// for a text email
        sendEmail(mailSession, emailMessage);

    }

    public static void sendEmail(Session mailSession, MimeMessage emailMessage) throws AddressException, MessagingException {

        String emailHost = "smtp.gmail.com";

        String fromUser = "immoscraper24";
        String fromUserEmailPassword = "!mm0Scr4p3r";

        Transport transport = mailSession.getTransport("smtp");

        transport.connect(emailHost, fromUser, fromUserEmailPassword);
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        System.out.println("Email sent successfully.");
    }


//    public static void sendMail() {
//
//        String username = "immoscraper24@gmail.com";
//        String password = "!mm0Scr4p3r";
//
//        Properties props = new Properties();
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        Session session = Session.getInstance(props, null);
//
//        try {
//            MimeMessage msg = new MimeMessage(session);
//            msg.setFrom("immoscraper24@gmail.com");
//            msg.setRecipients(Message.RecipientType.TO,
//                    "he.ahrens@gmail.com");
//            msg.setSubject("JavaMail hello world example");
//            msg.setSentDate(new Date());
//            msg.setText("Hello, world!\n");
//            Transport.send(msg, username, password);
//        } catch (
//                MessagingException mex) {
//            System.out.println("send failed, exception: " + mex);
//        }
//
//    }

}
