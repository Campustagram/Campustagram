package com.campustagram.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "page")
public class Page implements Serializable {

	@Id
	@SequenceGenerator(name = "page_seq", sequenceName = "page_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "page_seq")
	@Column(nullable = false)
	private Long id;

	private String pageName;
	private boolean isActive;
	private boolean isDeleted;

	public Page() {
		super();
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof Page) && (id != null) ? id.equals(((Page) other).id) : (other == this);
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

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
