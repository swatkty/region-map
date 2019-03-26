package egovframework.example.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.lang3.StringUtils;

public class MailUtil {
	public static boolean sendHtmlMail(String toMail, String toName, String fromMail, String formName, String subject, String content) {
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", PropertiesUtil.getValue("mail_host"));

			Session session = null;
			String emailId = PropertiesUtil.getValue("mail_id");
			String emailPw = PropertiesUtil.getValue("mail_pw");
			if (StringUtils.isNotEmpty(emailId) && StringUtils.isNotEmpty(emailPw)) {
				session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(PropertiesUtil.getValue("mail_id"), PropertiesUtil.getValue("mail_pw"));
					}
				});
			}
			else {
				session = Session.getDefaultInstance(props, null);
			}

			Multipart mp = new MimeMultipart();
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(fromMail));

			InternetAddress[] toAddress = InternetAddress.parse(toMail);
			msg.setRecipients(Message.RecipientType.TO, toAddress);
			msg.setSubject(subject, "UTF-8");
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setContent(content, "text/html; charset=UTF-8");
			
			mp.addBodyPart(mbp1);
			msg.setContent(mp);
			msg.setSentDate(new Date());

			Transport.send(msg);

			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
