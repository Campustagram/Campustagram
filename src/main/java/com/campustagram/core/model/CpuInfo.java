package com.campustagram.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "cpu_info")
public class CpuInfo {

	@Id
	@SequenceGenerator(name = "cpu_info_seq", sequenceName = "cpu_info_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cpu_info_seq")
	@Column(nullable = false)
	private Long id;
	private Date createDate = new Date();

	private Long processCpuTime;
	private Double systemCpuLoad;
	private Double processCpuLoad;

	public CpuInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "CpuInfo [processCpuTime=" + processCpuTime + ", systemCpuLoad=" + systemCpuLoad + ", processCpuLoad="
				+ processCpuLoad + "]";
	}

	public Long getProcessCpuTime() {
		return processCpuTime;
	}

	public void setProcessCpuTime(Long processCpuTime) {
		this.processCpuTime = processCpuTime;
	}

	public Double getSystemCpuLoad() {
		return systemCpuLoad;
	}

	public void setSystemCpuLoad(Double systemCpuLoad) {
		this.systemCpuLoad = systemCpuLoad;
	}

	public Double getProcessCpuLoad() {
		return processCpuLoad;
	}

	public void setProcessCpuLoad(Double processCpuLoad) {
		this.processCpuLoad = processCpuLoad;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
