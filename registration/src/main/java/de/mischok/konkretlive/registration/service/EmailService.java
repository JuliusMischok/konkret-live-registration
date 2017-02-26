package de.mischok.konkretlive.registration.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Provides methods for mailing
 */
@Service
public class EmailService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${mail.sendenabled}")
	private boolean sendEnabled;

	public void sendEmail(final String recipient, final String subject, final String htmlBody, final String attachmentName, final InputStreamSource attachmentStream) throws MailException {
		Assert.hasText(recipient);
		Assert.hasText(subject);
		Assert.hasText(htmlBody);
		Assert.hasText(attachmentName);
		Assert.notNull(attachmentStream);
		
		if (this.isSendEnabled()) {
			this.log.debug("Trying to send mail to '" + recipient + "' with subject '" + subject + "' and body '"+ htmlBody + "'");
			
			MimeMessage message = this.mailSender.createMimeMessage();
			
			try {
				MimeMessageHelper helper = new MimeMessageHelper(message, true);
				
				helper.setTo(recipient);
				helper.setFrom(new InternetAddress("office@konkretlive.de", "konkret live Office"));
				helper.setText(htmlBody, true);
				helper.addAttachment(attachmentName, attachmentStream, "application/vnd.ms-excel");
				
			} catch (MessagingException | UnsupportedEncodingException e) {
				throw new MailException("Creating Mime-Message failed", e) {
					private static final long serialVersionUID = 1550365875450075002L;};
			}
			
			this.log.debug("Message created, attempting to send...");
			this.mailSender.send(message);
			this.log.info("Message to '" + recipient + "' successfully sent!");
		} else {
			this.log.info("Message to '" + recipient + "' will not be sent, configuration value mail.sendenabled is false");
		}
	}

	/**
	 * @return the sendEnabled
	 */
	public boolean isSendEnabled() {
		return sendEnabled;
	}
}
