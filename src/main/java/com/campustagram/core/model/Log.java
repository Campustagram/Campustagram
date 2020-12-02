package com.campustagram.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonDate;

@Entity
@Table(name = "log")
public class Log implements Serializable {

	@Id
	@SequenceGenerator(name = "log_seq", sequenceName = "log_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "log_seq")
	@Column(nullable = false)
	private Long id;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_process_id")
	private User processUser;

	@Column(length = 512)
	private String logInfo;
	private String browserInfo;
	@Column(length = 16)
	private String ip;
	@Column(length = 512)
	private String enteredURL;

	private Date process_date = CommonDate.currentDate();

	/**
	 * 1:Danger 3:Warning 5:Info 11:Update 12:Delete 13:Record 21:Block 22:Unblock
	 */
	private Short logLevel = CommonConstants.LOG_INFO;

	public Log() {
		super();
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof Log) && (id != null) ? id.equals(((Log) other).id) : (other == this);
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

	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	public String getBrowserInfo() {
		return browserInfo;
	}

	public void setBrowserInfo(String browserInfo) {
		this.browserInfo = browserInfo;
	}

	public Date getProcess_date() {
		return process_date;
	}

	public void setProcess_date(Date process_date) {
		this.process_date = process_date;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public User getProcessUser() {
		return processUser;
	}

	public void setProcessUser(User processUser) {
		this.processUser = processUser;
	}

	public String getEnteredURL() {
		return enteredURL;
	}

	public void setEnteredURL(String enteredURL) {
		this.enteredURL = enteredURL;
	}

	public Short getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(Short logLevel) {
		this.logLevel = logLevel;
	}

}
