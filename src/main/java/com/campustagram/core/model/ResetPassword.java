package com.campustagram.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.campustagram.core.common.CommonDate;

@Entity
@Table(name = "resetpassword")
public class ResetPassword implements Serializable {

	@Id
	@SequenceGenerator(name = "resetpassword_seq", sequenceName = "resetpassword_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resetpassword_seq")
	private Long id;

	@OneToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id")
	private User user;

	private String hash;
	private Date createDate = CommonDate.currentDate();
	private Integer counter = 0;

	public ResetPassword() {
		super();
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof ResetPassword) && (id != null) ? id.equals(((ResetPassword) other).id)
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

	public Integer getCounter() {
		return counter;
	}

	public void setCounter(Integer counter) {
		this.counter = counter;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
