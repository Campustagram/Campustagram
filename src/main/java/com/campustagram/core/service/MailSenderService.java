package com.campustagram.core.service;

import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;

import com.campustagram.core.model.Email;
import com.campustagram.core.model.EmailTemplate;
import com.campustagram.core.model.MailSenderProperties;
import com.campustagram.core.model.User;

@Transactional
public interface MailSenderService {
	public void sendMail(Email email) throws Exception;

	public void sendMailTemplate(EmailTemplate emailTemplate) throws Exception;

	public List<EmailTemplate> getEmailTemplates() throws Exception;

	public boolean sendPoolTemplates(List<EmailTemplate> listEmailTemplates) throws Exception;

	public EmailTemplate mailGenerator(User user) throws Exception;

	public JavaMailSender createTestMailSender(MailSenderProperties mailSenderProperties) throws Exception;

	public boolean sendTestMail(JavaMailSender testMailSender, EmailTemplate emailTemplate) throws Exception;

}
