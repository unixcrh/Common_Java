package com.orange.common.mail;

import javax.mail.*;
import javax.mail.internet.*;

import org.apache.log4j.Logger;

import java.util.*;

public class MailSender {
	
	Logger log = Logger.getLogger(this.getClass().getName());
	
	String host = "smtp.163.com";
	String from = "gckj123@163.com";
	String to = "";
	String username = from;
	String password = "gckj123456";
//	String host = "smtp.sina.com.cn";
//	String from = "gckj_dev@sina.com";
//	String to = "";
//	String username = from;
//	String password = "gckjdev123";

//	For Test
//	public static void main(String[] args) {
//		MailSender sm = new MailSender();
//		sm.send("gckj123@163.com", "www.google.com");
//	}
	public void send(String to, String confirmUrl) {

		try {
			Properties props = new Properties();

			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", "true"); 

			Authenticator auth = new PopupAuthenticator(username, password);
			Session session = Session.getDefaultInstance(props, auth);

			// watch the mail commands go by to the mail server
			session.setDebug(true);

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("柑橙科技确认注册");
			message.setSentDate(new Date());
			
			BodyPart part = new MimeBodyPart();
			String content = "你好：<br> 请点击下面的链接完成注册：<br><a href='"    
							+ confirmUrl + "'>"+ confirmUrl + "</a><br>" 
							+ "如果以上链接无法点击，请将上面的地址复制到你的浏览器。 ";
			part.setContent(content, "text/html;charset=gb2312");
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(part);
			message.setContent(mp);

			message.saveChanges();
			Transport transport = session.getTransport("smtp");
			transport.connect(host, username, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			System.out.println("Send mail successfully");
		} catch (Exception e) {
			System.err.println("Send mail failure:" + e.getMessage());
			e.printStackTrace(System.err);
		}

	}
}

class PopupAuthenticator extends Authenticator {
	private String username;
	private String password;

	public PopupAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.username, this.password);
	}
}
