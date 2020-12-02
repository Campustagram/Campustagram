package com.campustagram.core.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.campustagram.core.model.EmailTemplate;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {

	@Query(value = "SELECT * FROM email_template t WHERE 1=1 ORDER BY id", nativeQuery = true)
	public List<EmailTemplate> findAllNotDeleted();

	@Query(value = "SELECT * FROM email_template t WHERE send_date IS NULL LIMIT 10", nativeQuery = true)
	public List<EmailTemplate> get10NotSendedEmailTemplate();

}
