package com.campustagram.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.campustagram.core.common.CommonDate;

@Entity
@Table(name = "language")
public class Language implements Serializable {

	@Id
	@SequenceGenerator(name = "language_seq", sequenceName = "language_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "language_seq")
	@Column(nullable = false)
	private Long id;
	private String code;

	@OneToMany(mappedBy = "language", targetEntity = User.class, fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private List<User> userList = new ArrayList<>();

	private boolean isDeleted = false;

	private Date createDate = CommonDate.currentDate();

	public Language() {
		super();
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof Language) && (id != null) ? id.equals(((Language) other).id) : (other == this);
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

}
