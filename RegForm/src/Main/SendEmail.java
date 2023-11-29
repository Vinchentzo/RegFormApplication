package Main;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class SendEmail {
    final String senderEmail = "vinchentzotestemail@gmail.com"; //change email address
    final String senderPassword = "t1t2t3t4t5"; //change password
    final String emailSMTPserver = "smtp.gmail.com";
    final String emailServerPort = "465";
    String receiverEmail = null;
    public SendEmail(String receiverEmail, String name) {
        this.receiverEmail = receiverEmail;
        String subject = "New registration validation!";
        String body = "Congratulations! " + name + ", you have successfully registered to our application";

        Properties props = new Properties();
        props.put("mail.smtp.user",senderEmail);
        props.put("mail.smtp.host", emailSMTPserver);
        props.put("mail.smtp.port", emailServerPort);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", emailServerPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        try {
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);
            msg.setText(body);
            msg.setSubject(subject);
            msg.setFrom(new InternetAddress(senderEmail));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));
            Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(senderEmail, senderPassword);
        }
    }
}