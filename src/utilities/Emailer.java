package utilities;

import java.security.Security;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import models.User;

public class Emailer {
	// From
	// http://www.velocityreviews.com/forums/t141237-p2-send-smtp-mail-using-javamail-with-gmail-account.html
	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_PORT = "465";

	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private static final String USERNAME = "telehealth.up@gmail.com";
	private static final String PASSWORD = "telehealth";

	/**
	 * @param args
	 */
	public static void main(String args[]) {
//		new Connector(Constants.TEST_CONFIG);
//		User user = null;
//		try {
//			user = UserDAO.getUserById(1);
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		user.setEmail("rayvincent.gomez@gmail.com");
//		user.setPassword("1234");
//		try {
//			new Emailer().sendForgetPasswordEmail(user);
//		} catch (MessagingException e) {
//			e.printStackTrace();
//		}
		
//		System.out.println(RandomStringUtils.randomAlphanumeric(8));

	}

	public void sendSSLMessage(String recipients, String subject,
			String message, String from) throws MessagingException {

		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "false");
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {

					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(USERNAME, PASSWORD);
					}
				});

		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = new InternetAddress[1];
		addressTo[0] = new InternetAddress(recipients);
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		msg.setSubject(subject);
		msg.setContent(message, "text/plain");
		Transport.send(msg);
	}

	public static void sendAcceptedEmail(User user) throws MessagingException {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

		String sendTo = user.getEmail();
		String emailMsgTxt = "Congratulations!\n\n"
				+ "Your application for the Pealibrary has been granted.\n"
				+ "The username is " + user.getUserName() + ".\n\n"
				+ "This email is autogenerated.\n"
				+ "Do not reply to this email.";
		String emailSubjectTxt = "[Pealib] Application to Pealibrary Granted";
		String emailFromAddress = "pealibrary@gmail.com";
		new Emailer().sendSSLMessage(sendTo, emailSubjectTxt, emailMsgTxt,
				emailFromAddress);
	}
	
	public static void sendRejectEmail(User user) throws MessagingException {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

		String sendTo = user.getEmail();
		String emailMsgTxt = "Sorry!\n\n"
				+ "Your application for the Pealibrary has been denied.\n\n"
				+ "This email is autogenerated.\n"
				+ "Do not reply to this email.";
		String emailSubjectTxt = "[Pealib] Application to Pealibrary Granted";
		String emailFromAddress = "pealibrary@gmail.com";
		new Emailer().sendSSLMessage(sendTo, emailSubjectTxt, emailMsgTxt,
				emailFromAddress);
	}

	public static void sendForgetPasswordEmail(User user) throws MessagingException {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

		String sendTo = user.getEmail();
		String emailMsgTxt = "You applied for a change of password.\n"
				+ "The username is " + user.getUserName() + ".\n"
				+ "The new password is " + user.getPassword() + ".\n\n"
				+ "This email is autogenerated.\n"
				+ "Do not reply to this email.";
		String emailSubjectTxt = "[Pealib] New Password";
		String emailFromAddress = "pealibrary@gmail.com";
		new Emailer().sendSSLMessage(sendTo, emailSubjectTxt, emailMsgTxt,
				emailFromAddress);
		
		
	}
}
