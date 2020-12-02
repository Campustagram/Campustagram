package com.campustagram.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.campustagram.core.common.CommonDate;
import com.campustagram.core.enums.TicketProcessType;

@Entity
@Table(name = "ticket")
public class Ticket implements Serializable {

	@Id
	@SequenceGenerator(name = "ticket_seq", sequenceName = "ticket_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_seq")
	@Column(nullable = false)
	private Long id;

	@JoinColumn(name = "user_id")
	private Long userId;

	@Column(columnDefinition = "TEXT")
	private String content;
	@Column(length = 512)
	private String subject;
	/*
	 * Answered-Open
	 */
	private String status = TicketProcessType.OPEN.toString();

	private Date createDate = CommonDate.currentDate();
	private Date updateDate = CommonDate.currentDate();

	/**
	 * Low-Medium-High
	 */
	private String priority = TicketProcessType.LOW.toString();

	private Long replyTo;

	public Ticket() {
		super();
	}

	@Override
	public String toString() {
		return "Machine [id=" + id + ", userId=" + userId + ", ticketContent=" + content + ", ticketSubject=" + subject
				+ ", status=" + status + ", createDate=" + createDate + ", updateDate=" + updateDate + ", priority="
				+ priority + ", replyTo=" + replyTo + "]";
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof Ticket) && (id != null) ? id.equals(((Ticket) other).id) : (other == this);
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Long getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(Long replyTo) {
		this.replyTo = replyTo;
	}

}
