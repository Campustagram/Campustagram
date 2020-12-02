package com.campustagram.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "systeminformation")
public class SystemInformation implements Serializable {

	@Id
	@SequenceGenerator(name = "systeminformation_seq", sequenceName = "systeminformation_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "systeminformation_seq")
	@Column(nullable = false)
	private Long id;

	private Date processDate = new Date();
	@Column(columnDefinition = "TEXT")
	private String info;
	private String processType;
	private String code;

	public SystemInformation() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "SystemInformation [id=" + id + ", processDate=" + processDate + ", info=" + info + ", processType="
				+ processType + ", code=" + code + "]";
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof SystemInformation) && (id != null) ? id.equals(((SystemInformation) other).id)
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

	public Date getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}