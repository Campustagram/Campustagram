package com.campustagram.core.service;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.controller.log.ILogger;
import com.campustagram.core.model.Email;
import com.campustagram.core.model.EmailTemplate;
import com.campustagram.core.model.MailSenderProperties;
import com.campustagram.core.model.SystemProperties;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.EmailTemplateRepository;
import com.campustagram.core.persistence.SystemPropertiesRepository;

@Service
public class MailSenderServiceImpl implements MailSenderService {
	@Autowired
	private SystemPropertiesRepository systemPropertiesRepository;
	@Autowired
	private EmailTemplateRepository emailTemplateRepository;

	private JavaMailSender javamailSender;

	private ILogger logger = new ILogger();
	private static final String ACTIVE_CLASS_NAME = "MailSenderServiceImpl";

	@PostConstruct
	public void init() {
		final String ACTIVE_METHOD_NAME = "init";

		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		try {
			javamailSender = createJavaMailSender();
		} catch (Exception e) {
			logger.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
		}
		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public JavaMailSender createJavaMailSender() throws Exception {
		final String ACTIVE_METHOD_NAME = "createJavaMailSender";

		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		SystemProperties systemProperties = systemPropertiesRepository.getSystemProperties();
		if ((null != systemProperties) && (null != systemProperties.getId())) {
			mailSender.setUsername(systemProperties.getMailSenderProperties().getUsername());
			mailSender.setPassword(systemProperties.getMailSenderProperties().getPassword());
			mailSender.setHost(systemProperties.getMailSenderProperties().getHost());
			mailSender.setPort(Integer.parseInt(systemProperties.getMailSenderProperties().getPort()));

			Properties props = mailSender.getJavaMailProperties();
			props.put("mail.transport.protocol", systemProperties.getMailSenderProperties().getTransportProtocol());
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.debug", "false");
			logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
			return mailSender;
		}
		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return null;
	}

	@Override
	public EmailTemplate mailGenerator(User user) throws Exception {
		logger.writeInfo(ACTIVE_CLASS_NAME, "mailGenerator", null, CommonConstants.START);
		EmailTemplate emailTemplate = new EmailTemplate();

		emailTemplate.setTo(user.getEmail());
		emailTemplate.setFrom("Campustagram");
		logger.writeInfo(ACTIVE_CLASS_NAME, "mailGenerator", null, CommonConstants.END);
		return emailTemplate;
	}

	@Override
	public JavaMailSender createTestMailSender(MailSenderProperties mailSenderProperties) throws Exception {
		final String ACTIVE_METHOD_NAME = "createTestMailSender";

		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		if (null == mailSenderProperties) {
			logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, "mailSenderProperties null", CommonConstants.END);
			return null;
		}
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setUsername(mailSenderProperties.getUsername());
		
		mailSender.setPassword(mailSenderProperties.getPassword());
		mailSender.setHost(mailSenderProperties.getHost());
		mailSender.setPort(Integer.parseInt(mailSenderProperties.getPort()));

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", mailSenderProperties.getTransportProtocol());
		props.put("mail.smtp.ssl.trust", "*");
		props.put("mail.smtp.auth", mailSenderProperties.isSmtpAuth());
		props.put("mail.smtp.starttls.enable", mailSenderProperties.isStarttlsEnable());
		props.put("mail.debug", mailSenderProperties.isDebug());
		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return mailSender;
	}

	@Override
	public boolean sendTestMail(JavaMailSender testMailSender, EmailTemplate emailTemplate) throws Exception {
		logger.writeInfo(ACTIVE_CLASS_NAME, "sendTestMail", null, CommonConstants.START);
		MimeMessage message = testMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
		helper.setTo(emailTemplate.getTo());
		helper.setFrom(emailTemplate.getFrom());
		helper.setSubject(emailTemplate.getSubject());
		message.setContent(emailTemplate.getContent(), "text/html; charset=utf-8");
		testMailSender.send(message);
		logger.writeInfo(ACTIVE_CLASS_NAME, "sendTestMail", null, CommonConstants.END);
		return false;
	}

	@Override
	public List<EmailTemplate> getEmailTemplates() throws Exception {
		return emailTemplateRepository.get10NotSendedEmailTemplate();
	}

	@Override
	public boolean sendPoolTemplates(List<EmailTemplate> listEmailTemplates) throws Exception {
		final String ACTIVE_METHOD_NAME = "sendPoolTemplates";

		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);

		for (EmailTemplate emailTemplate : listEmailTemplates) {
			sendMailTemplate(emailTemplate);
		}
		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);

		return false;
	}

	@Override
	public void sendMail(Email email) throws Exception {
		final String ACTIVE_METHOD_NAME = "sendMail";

		if (null == javamailSender) {
			javamailSender = createJavaMailSender();
		}

		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		MimeMessage message = javamailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
		helper.setTo(email.getTo());
		helper.setFrom(email.getFrom());
		helper.setSubject(email.getSubject());
		message.setContent(email.getContent(), "text/html; charset=utf-8");
		javamailSender.send(message);
		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	@Override
	public void sendMailTemplate(EmailTemplate emailTemplate) throws Exception {
		final String ACTIVE_METHOD_NAME = "sendMail";

		if (null == javamailSender) {
			javamailSender = createJavaMailSender();
		}

		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		MimeMessage message = javamailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
		helper.setTo(emailTemplate.getTo());
		helper.setFrom(emailTemplate.getFrom());
		helper.setSubject(emailTemplate.getSubject());
		message.setContent(emailTemplate.getContent(), "text/html; charset=utf-8");
		javamailSender.send(message);

		emailTemplate.setSendDate(new Date());
		emailTemplateRepository.save(emailTemplate);
		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

}
