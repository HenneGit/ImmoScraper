package org.oszimt.fa83.email;

import org.oszimt.fa83.pojo.EmailCredentials;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * class for sending emails.
 */
public class EmailHandler {

    private EmailCredentials credentials;

    public EmailHandler(EmailCredentials credentials) {
        this.credentials = credentials;
    }

    /**
     * set properties for email.
     * @return new Properties for email sending.
     */
    private Properties getMailServerProperties() {

        Properties emailProperties = new Properties();
        emailProperties.put("mail.smtp.port", credentials.getPort());
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
        emailProperties.put("mail.transport.protocol", "smtp");
        return emailProperties;

    }

    /**
     * create a new email message.
     * @param email the email to send email to.
     * @param body content of email
     * @param emailSubject subject of email.
     * @throws AddressException
     * @throws MessagingException
     */
    public void createEmailMessage(String email, String body, String emailSubject) throws AddressException,
            MessagingException {
        Session mailSession = Session.getDefaultInstance(getMailServerProperties(), null);
        MimeMessage emailMessage = new MimeMessage(mailSession);
        emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(body, "text/html");//for a html email
        emailMessage.setFrom(credentials.getEmail());
        emailMessage.setText(body);// for a text email
        sendEmail(mailSession, emailMessage);

    }

    /**
     *  send email.
     * @param mailSession properties of email handler.
     * @param emailMessage the message to send.
     * @throws AddressException
     * @throws MessagingException
     */
    private void sendEmail(Session mailSession, MimeMessage emailMessage) throws MessagingException {
        Transport transport = mailSession.getTransport("smtp");
        transport.connect(credentials.getSmtp(), credentials.getEmail(), credentials.getPassword());
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
    }


}
