package com.campustagram.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "wstb_error_log", indexes = { @Index(name = "wstberrorlogidx", columnList = "id", unique = true),
		@Index(name = "wstberrorlogscrapetourx", columnList = "scrape_tour", unique = false),
		@Index(name = "wstberrorlogworkerpartx", columnList = "worker_part", unique = false),
		@Index(name = "wstberrorlogscrapetourworkerpartx", columnList = "scrape_tour,worker_part") })
public class ErrorLog implements Serializable {

	@Id
	// @SequenceGenerator(name = "wstb_wstb_error_log", sequenceName =
	// "wstb_wstb_error_log")
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
	// "wstb_wstb_error_log")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	private String url;

	private Date createDate = new Date();

	@Column(columnDefinition = "TEXT")
	private String errorMessage;

	@Column(name = "scrape_tour")
	private Integer scrapeTour;
	@Column(name = "worker_part")
	private String workerPart;

	@Column(length = 750)
	private String threadInfo;
	@Column(length = 1000)
	private String proxyInfo;

	public ErrorLog() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWorkerPart() {
		return workerPart;
	}

	public void setWorkerPart(String workerPart) {
		this.workerPart = workerPart;
	}

	public Integer getScrapeTour() {
		return scrapeTour;
	}

	public void setScrapeTour(Integer scrapeTour) {
		this.scrapeTour = scrapeTour;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getThreadInfo() {
		return threadInfo;
	}

	public void setThreadInfo(String threadInfo) {
		this.threadInfo = threadInfo;
	}

	public String getProxyInfo() {
		return proxyInfo;
	}

	public void setProxyInfo(String proxyInfo) {
		this.proxyInfo = proxyInfo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
