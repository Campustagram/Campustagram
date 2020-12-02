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
@Table(name = "memory_info")
public class MemoryInfo {

	@Id
	@SequenceGenerator(name = "memory_info_seq", sequenceName = "memory_info_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "memory_info_seq")
	@Column(nullable = false)
	private Long id;

	private Date createDate = new Date();

	// freeMemory in JVM
	private Long freeMemory;
	// Used Memory in JVM
	private Long usedMemory;
	// totalMemory in JVM shows current size of java heap
	// JVM totalMemory also equals to initial heap size of JVM
	private Long totalMemory;
	// max in JVM
	// JVM maxMemory also equals to maximum heap size of JVM
	private Long maxMemory;
	private Double usedPercentage;

	private Long committedVirtualMemorySize;
	private Long freePhysicalMemorySize;
	private Long freeSwapSpaceSize;
	private Long totalPhysicalMemorySize;
	private Long totalSwapSpaceSize;

	public MemoryInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "RamInfo [maxMemory=" + maxMemory + ", usedPercentage=" + usedPercentage
				+ ", committedVirtualMemorySize=" + committedVirtualMemorySize + ", freePhysicalMemorySize="
				+ freePhysicalMemorySize + ", freeSwapSpaceSize=" + freeSwapSpaceSize + ", totalPhysicalMemorySize="
				+ totalPhysicalMemorySize + ", totalSwapSpaceSize=" + totalSwapSpaceSize + ", getTotalMemoryMB()="
				+ getTotalMemoryMB() + ", getFreeMemoryMB()=" + getFreeMemoryMB() + ", getUsedMemoryMB()="
				+ getUsedMemoryMB() + "]";
	}

	public Long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(Long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public Long getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(Long usedMemory) {
		this.usedMemory = usedMemory;
	}

	public Long getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(Long totalMemory) {
		this.totalMemory = totalMemory;
	}

	public Long getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(Long maxMemory) {
		this.maxMemory = maxMemory;
	}

	public Double getUsedPercentage() {
		return usedPercentage;
	}

	public void setUsedPercentage(Double usedPercentage) {
		this.usedPercentage = usedPercentage;
	}

	public Long getTotalMemoryMB() {
		return getTotalMemory() / 1024 / 1014;
	}

	public Long getFreeMemoryMB() {
		return getFreeMemory() / 1024 / 1014;
	}

	public Long getUsedMemoryMB() {
		return getUsedMemory() / 1024 / 1014;
	}

	public Long getCommittedVirtualMemorySize() {
		return committedVirtualMemorySize;
	}

	public void setCommittedVirtualMemorySize(Long committedVirtualMemorySize) {
		this.committedVirtualMemorySize = committedVirtualMemorySize;
	}

	public Long getFreePhysicalMemorySize() {
		return freePhysicalMemorySize;
	}

	public void setFreePhysicalMemorySize(Long freePhysicalMemorySize) {
		this.freePhysicalMemorySize = freePhysicalMemorySize;
	}

	public Long getTotalPhysicalMemorySize() {
		return totalPhysicalMemorySize;
	}

	public void setTotalPhysicalMemorySize(Long totalPhysicalMemorySize) {
		this.totalPhysicalMemorySize = totalPhysicalMemorySize;
	}

	public Long getFreeSwapSpaceSize() {
		return freeSwapSpaceSize;
	}

	public void setFreeSwapSpaceSize(Long freeSwapSpaceSize) {
		this.freeSwapSpaceSize = freeSwapSpaceSize;
	}

	public Long getTotalSwapSpaceSize() {
		return totalSwapSpaceSize;
	}

	public void setTotalSwapSpaceSize(Long totalSwapSpaceSize) {
		this.totalSwapSpaceSize = totalSwapSpaceSize;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
