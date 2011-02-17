package org.manentia.kasai.util;

import java.util.ResourceBundle;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.manentia.kasai.Constants;

/**
 *
 * @author  rzuasti
 */
public class MailUtil {
    
    public static void send(String subject, String message, String recipient) 
            throws AddressException, MessagingException{
        ResourceBundle res = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);
        
        com.manentia.commons.mail.MailUtil.send(formatSubject(subject), formatBody(message), recipient, true, res.getString("mail.from"),
            res.getString("mail.replyTo"), res.getString("mail.smtp.host"),
            res.getString("mail.smtp.user"), res.getString("mail.smtp.password"));
    }
    
    public static void send(String subject, String message, String[] recipients) 
            throws AddressException, MessagingException{
        ResourceBundle res = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);
        
        com.manentia.commons.mail.MailUtil.send(formatSubject(subject), formatBody(message), recipients, true, res.getString("mail.from"),
            res.getString("mail.replyTo"), res.getString("mail.smtp.host"),
            res.getString("mail.smtp.user"), res.getString("mail.smtp.password"));
    }
    
    private static String formatSubject(String subject){
        ResourceBundle messages = ResourceBundle.getBundle(Constants.MESSAGES_PROPERTY_FILE);
        
        return messages.getString("mails.subjectPrefix") + subject;
    }
    
    private static String formatBody(String body){
        ResourceBundle messages = ResourceBundle.getBundle(Constants.MESSAGES_PROPERTY_FILE);
        
        return body + messages.getString("mails.signature");
    }
    
}
