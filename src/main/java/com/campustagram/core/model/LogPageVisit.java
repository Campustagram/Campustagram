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

import com.campustagram.core.common.CommonDate;

@Entity
@Table(name = "logpagevisit")
public class LogPageVisit implements Serializable {

	@Id
	@SequenceGenerator(name = "logpagevisit_seq", sequenceName = "logpagevisit_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "logpagevisit_seq")
	@Column(nullable = false)
	private Long id;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id")
	private User user;

	private String browserInfo;
	@Column(length = 16)
	private String ip;
	@Column(length = 512)
	private String enteredURL;
	@Column(columnDefinition = "TEXT")
	private String logInfo;

	private Date process_date = CommonDate.currentDate();

	/**
	 * 1:Danger 3:Warning 5:Info 11:Update 12:Delete 13:Record 21:Block 22:Unblock
	 */
	private Short logLevel = 5;

	public LogPageVisit() {
		super();
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof LogPageVisit) && (id != null) ? id.equals(((LogPageVisit) other).id) : (other == this);
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

	public String getEnteredURL() {
		return enteredURL;
	}

	public void setEnteredURL(String enteredURL) {
		this.enteredURL = enteredURL;
	}

	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	public Short getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(Short logLevel) {
		this.logLevel = logLevel;
	}

}
