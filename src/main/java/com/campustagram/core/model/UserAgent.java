package com.campustagram.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "user_agent", indexes = { @Index(name = "useragentidx", columnList = "id", unique = true),
		@Index(name = "useragentnamex", columnList = "name", unique = false),
		@Index(name = "useragentisdelx", columnList = "is_deleted", unique = false),
		@Index(name = "useragentisactivex", columnList = "is_active", unique = false) })
public class UserAgent implements Serializable {

	@Id
	@SequenceGenerator(name = "user_agent_seq", sequenceName = "user_agent_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_agent_seq")
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "name")
	private String name;
	@Column(name = "is_deleted")
	private boolean isDeleted = false;
	@Column(name = "is_active")
	private boolean isActive = true;

	public UserAgent() {
		super();
	}

	@Override
	public String toString() {
		return "UserAgent [id=" + id + ", name=" + name + ", isDeleted=" + isDeleted + ", isActive=" + isActive + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
