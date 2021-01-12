package org.oszimt.fa83.emailhandler;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailHandler {

    public EmailHandler() {
    }

    public Properties getMailServerProperties() {

        String emailPort = "587";//gmail's smtp port

        Properties emailProperties = new Properties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
        return emailProperties;

    }

    public void createEmailMessage(Properties emailProperties, String email, String body, String emailSubject) throws AddressException,
            MessagingException {
        Session mailSession = Session.getDefaultInstance(emailProperties, null);
        MimeMessage emailMessage = new MimeMessage(mailSession);
        emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(body, "text/html");//for a html email
        emailMessage.setText(body);// for a text email
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
    }
}
