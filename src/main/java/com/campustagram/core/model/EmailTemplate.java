package com.campustagram.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "email_template")
public class EmailTemplate implements Serializable {

	@Id
	@SequenceGenerator(name = "email_template_seq", sequenceName = "email_template_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_template_seq")
	@Column(nullable = false)
	private Long id;

	private Date createDate = new Date();
	private Date sendDate;

	@Column(columnDefinition = "TEXT")
	private String content;
	@Column(length = 512)
	private String subject;
	@Column(length = 512)
	private String toStr;
	@Column(length = 512)
	private String fromStr;

	public EmailTemplate() {
		super();
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof EmailTemplate) && (id != null) ? id.equals(((EmailTemplate) other).id)
				: (other == this);
	}

	@Override
	public int hashCode() {
		return (id != null) ? (this.getClass().hashCode() + id.hashCode()) : super.hashCode();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = "<html>" + "<body>" + content + "</body>" + "</html>";
	}

	public String getTo() {
		return toStr;
	}

	public void setTo(String to) {
		this.toStr = to;
	}

	public String getFrom() {
		return fromStr;
	}

	public void setFrom(String from) {
		this.fromStr = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

}
