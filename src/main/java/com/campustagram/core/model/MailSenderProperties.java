package com.campustagram.core.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class MailSenderProperties implements Serializable {
	private String username = "dodgehellcat3478@gmail.com";
	private String password = "123qweASD!3478";
	private String host = "smtp.gmail.com";
	private String port = "587";
	private String transportProtocol = "smtp";
	private boolean smtpAuth = true;
	private boolean starttlsEnable = true;
	private boolean debug = false;

	@Override
	public String toString() {
		return "MailSenderProperties [username=" + username + ", password=" + password + ", host=" + host + ", port="
				+ port + ", transportProtocol=" + transportProtocol + ", smtpAuth=" + smtpAuth + ", starttlsEnable="
				+ starttlsEnable + ", debug=" + debug + "]";
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof User) && (username != null) ? username.equals(((MailSenderProperties) other).username)
				: (other == this);
	}

	@Override
	public int hashCode() {
		return (username != null) ? (this.getClass().hashCode() + username.hashCode()) : super.hashCode();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getTransportProtocol() {
		return transportProtocol;
	}

	public void setTransportProtocol(String transportProtocol) {
		this.transportProtocol = transportProtocol;
	}

	public boolean isSmtpAuth() {
		return smtpAuth;
	}

	public void setSmtpAuth(boolean smtpAuth) {
		this.smtpAuth = smtpAuth;
	}

	public boolean isStarttlsEnable() {
		return starttlsEnable;
	}

	public void setStarttlsEnable(boolean starttlsEnable) {
		this.starttlsEnable = starttlsEnable;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
