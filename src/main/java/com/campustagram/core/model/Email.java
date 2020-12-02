package com.campustagram.core.model;

public class Email {
	private String content;
	private String subject;
	private String to;
	private String from;

	private User user;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = "<html>" + "<body>" + content + "</body>" + "</html>";
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}
