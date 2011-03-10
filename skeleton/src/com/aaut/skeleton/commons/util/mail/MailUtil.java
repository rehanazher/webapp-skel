package com.aaut.skeleton.commons.util.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.aaut.skeleton.commons.util.Validators;

public class MailUtil {

	protected static Logger log = Logger.getLogger(MailUtil.class);

	public MailUtil() {
		// TODO Auto-generated constructor stub
	}

	public void sendMail(String smtpHost, final String smtpUser,
			final String smtpPassword, String smtpPort, String subject,
			String mailFrom, String message, String recipient, String _cc,
			boolean htmlMode) {

		Session session = null;
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", smtpHost);
		props.setProperty("mail.smtp.port", smtpPort);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");

		if (Validators.isEmpty(smtpUser)) {
			session = Session.getInstance(props, null);
		} else {
			props.put("mail.smtp.auth", "true");
			Authenticator auth = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(smtpUser, smtpPassword);
				}
			};
			session = Session.getInstance(props, auth);
		}
		Message msg = new MimeMessage(session);
		try {
			msg.setSubject(subject);

			msg.setText(message);
			msg.setFrom(new InternetAddress(mailFrom));
			msg.setSentDate(new Date());
			if (htmlMode) {
				msg.setContent(message, "text/html");
			} else {
				msg.setText(message);
			}

			// recipient
			String[] toAdds = recipient.split(";");
			InternetAddress[] toAddress = new InternetAddress[toAdds.length];
			for (int i = 0; i < toAdds.length; i++) {

				toAddress[i] = new InternetAddress(toAdds[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, toAddress);

			// CC
			String[] ccAdds = _cc.split(";");
			InternetAddress[] ccAddress = new InternetAddress[ccAdds.length];
			for (int i = 0; i < ccAdds.length; i++) {

				ccAddress[i] = new InternetAddress(ccAdds[i]);
			}
			msg.setRecipients(Message.RecipientType.CC, ccAddress);

			Transport.send(msg);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		MailUtil m = new MailUtil();
		String mailFrom = "test@test.com";

		String recipient = "goffeeko@gmail.com;coffeegao@qq.com";
		String _cc = "goffee.ko@transact24.com";
		String subject = "test";

		String textContent = " ";

		String message = "<td align='left' >" + "<div><pre>content</pre>"

		+ "<img src=\"cid:IMG1\" width=\"595\" height=\"765\" border=\"0\">"
				+ "</div></td>";

		final String smtpUser = "james.cheung@transact24.com";
		final String smtpPassword = "zzq8207";
		String smtpHost = "mail.transact24.com";
		String smtpPort = "25";
		// String ReplyTo ="";
		boolean htmlMode = true;
		m.sendMail(smtpHost, smtpUser, smtpPassword, smtpPort, subject,
				mailFrom, message, recipient, _cc, htmlMode);

	}

}
