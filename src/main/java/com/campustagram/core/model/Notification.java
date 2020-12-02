package com.campustagram.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.campustagram.core.common.CommonDate;

@Entity
@Table(name = "notification")
public class Notification implements Serializable {

	@Id
	@SequenceGenerator(name = "notification_seq", sequenceName = "notification_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq")
	@Column(nullable = false)
	private Long id;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "inform_user_id")
	private User user;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "process_user_id")
	private User processUser;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "processed_user_id")
	private User processedUser;

	@Column(length = 512)
	private String info;

	@Column(length = 512)
	private String content;
	/*
	 * 11:Update 12:Delete 13:Create 21:Block 22:Unblock
	 */
	private Short status;
	private Date process_date = CommonDate.currentDate();
	private boolean isSeen = false;

	public Notification() {
		super();
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof Notification) && (id != null) ? id.equals(((Notification) other).id) : (other == this);
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Date getProcess_date() {
		return process_date;
	}

	public void setProcess_date(Date process_date) {
		this.process_date = process_date;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public boolean isSeen() {
		return isSeen;
	}

	public void setSeen(boolean isSeen) {
		this.isSeen = isSeen;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getProcessUser() {
		return processUser;
	}

	public void setProcessUser(User processUser) {
		this.processUser = processUser;
	}

	public User getProcessedUser() {
		return processedUser;
	}

	public void setProcessedUser(User processedUser) {
		this.processedUser = processedUser;
	}

}
