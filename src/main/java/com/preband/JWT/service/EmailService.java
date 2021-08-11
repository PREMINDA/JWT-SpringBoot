package com.preband.JWT.service;

import com.preband.JWT.constant.EmailConstant;
import com.sun.mail.smtp.SMTPTransport;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;


import static com.preband.JWT.constant.EmailConstant.*;

@Service
public class EmailService {

    public void sendNewMessage(String firstname,String password,String email) throws MessagingException {
        Message message = createEmail(firstname, password, email);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(GMAIL_SMTP_SERVER,USERNAME,PASSWORD);
        smtpTransport.sendMessage(message,message.getAllRecipients());
        smtpTransport.close();
    }

    private Message createEmail(String firstname,String password,String email) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipient(Message.RecipientType.TO,new InternetAddress(email,false));
        message.setRecipient(Message.RecipientType.CC,new InternetAddress(CC_EMAIL,false));
        message.setText(EMAIL_SUBJECT);
        message.setText("Hello "+firstname+", \n \n Your new account password is " + password + "\n \n Team support Team");
        message.setSentDate(new Date());
        message.saveChanges();
        return  message;
    }

    private Session getEmailSession(){
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST, GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH, true);
        properties.put(SMTP_PORT, DEFAULT_PORT);
        properties.put(SMTP_STARTTLS_ENABLE, true);
        properties.put(SMTP_STARTTLS_REQUIRED, true);

        return  Session.getDefaultInstance(properties,null);
    }
}
