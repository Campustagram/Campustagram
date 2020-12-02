package com.campustagram.core.model;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "rememberme")
public class RememberMe implements Serializable {

	@Id
	@SequenceGenerator(name = "rememberme_seq", sequenceName = "rememberme_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rememberme_seq")
	private Long id;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id")
	private User user;

	private String hash;
	private String hashOfUserId;
	private String browserInfo;

	private Date createDate = CommonDate.currentDate();

	public RememberMe() {
		super();
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof RememberMe) && (id != null) ? id.equals(((RememberMe) other).id) : (other == this);
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

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getBrowserInfo() {
		return browserInfo;
	}

	public void setBrowserInfo(String browserInfo) {
		this.browserInfo = browserInfo;
	}

	public String getHashOfUserId() {
		return hashOfUserId;
	}

	public void setHashOfUserId(String hashOfUserId) {
		this.hashOfUserId = hashOfUserId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
