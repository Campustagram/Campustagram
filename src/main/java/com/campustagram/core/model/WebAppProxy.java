package com.campustagram.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "web_app_proxy", indexes = { @Index(name = "webproxyidx", columnList = "id", unique = true),
		@Index(name = "webproxyhostx", columnList = "host", unique = false),
		@Index(name = "webproxyportx", columnList = "port", unique = false),
		@Index(name = "webproxyisdelx", columnList = "is_deleted", unique = false) })
public class WebAppProxy implements Serializable {

	@Id
	@SequenceGenerator(name = "webProxy_seq", sequenceName = "webProxy_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "webProxy_seq")
	@Column(name = "id", nullable = false)
	private Long id;

	@Transient
	private Long urlAccessTime;

	@Column(name = "host")
	private String host;
	@Column(name = "port")
	private Integer port;
	@Column(name = "is_deleted")
	private boolean isDeleted = false;

	private Date createDate = new Date();
	private Date updateDate = new Date();
	private Date lastUsageDate;
	private Date lastSuccessDate;
	private Date lastErrorDate;

	@Column(name = "usage_count")
	private Long usageCount = 0L;

	@Transient
	private Integer usageCountByApp = 0;

	private Integer activeWorkerOnProxy = 0;

	@Column(name = "usage_count_in_last10min")
	private Integer usageCountInLast10Min = 0;
	private Date lastResetDate = new Date();

	private Integer errorCount = 0;
	private Integer errorCountInLast10Min = 0;

	@ManyToOne(targetEntity = WebProxyGroup.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "web_app_proxy_group_id")
	private WebProxyGroup webProxyGroup;

	public WebAppProxy() {
		super();
	}

	@Override
	public String toString() {
		return "WebAppProxy [id=" + id + ", host=" + host + ", port=" + port + ", isDeleted=" + isDeleted
				+ ", createDate=" + createDate + ", updateDate=" + updateDate + ", lastUsageDate=" + lastUsageDate
				+ ", lastSuccessDate=" + lastSuccessDate + ", lastErrorDate=" + lastErrorDate + ", usageCount="
				+ usageCount + ", usageCountByApp=" + usageCountByApp + ", activeWorkerOnProxy=" + activeWorkerOnProxy
				+ ", usageCountInLast10Min=" + usageCountInLast10Min + ", lastResetDate=" + lastResetDate
				+ ", errorCount=" + errorCount + ", errorCountInLast10Min=" + errorCountInLast10Min + ", webProxyGroup="
				+ webProxyGroup + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getLastUsageDate() {
		return lastUsageDate;
	}

	public void setLastUsageDate(Date lastUsageDate) {
		this.lastUsageDate = lastUsageDate;
	}

	public Date getLastSuccessDate() {
		return lastSuccessDate;
	}

	public void setLastSuccessDate(Date lastSuccessDate) {
		this.lastSuccessDate = lastSuccessDate;
	}

	public Long getUsageCount() {
		return usageCount;
	}

	public void setUsageCount(Long usageCount) {
		this.usageCount = usageCount;
	}

	public Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}

	public Integer getUsageCountInLast10Min() {
		return usageCountInLast10Min;
	}

	public void setUsageCountInLast10Min(Integer usageCountInLast10Min) {
		this.usageCountInLast10Min = usageCountInLast10Min;
	}

	public Date getLastResetDate() {
		return lastResetDate;
	}

	public void setLastResetDate(Date lastResetDate) {
		this.lastResetDate = lastResetDate;
	}

	public Date getLastErrorDate() {
		return lastErrorDate;
	}

	public void setLastErrorDate(Date lastErrorDate) {
		this.lastErrorDate = lastErrorDate;
	}

	public Integer getActiveWorkerOnProxy() {
		return activeWorkerOnProxy;
	}

	public void setActiveWorkerOnProxy(Integer activeWorkerOnProxy) {
		this.activeWorkerOnProxy = activeWorkerOnProxy;
	}

	public Integer getErrorCountInLast10Min() {
		return errorCountInLast10Min;
	}

	public void setErrorCountInLast10Min(Integer errorCountInLast10Min) {
		this.errorCountInLast10Min = errorCountInLast10Min;
	}

	public WebProxyGroup getWebProxyGroup() {
		return webProxyGroup;
	}

	public void setWebProxyGroup(WebProxyGroup webProxyGroup) {
		this.webProxyGroup = webProxyGroup;
	}

	public Integer getUsageCountByApp() {
		return usageCountByApp;
	}

	public void setUsageCountByApp(Integer usageCountByApp) {
		this.usageCountByApp = usageCountByApp;
	}

	public Long getUrlAccessTime() {
		return urlAccessTime;
	}

	public void setUrlAccessTime(Long urlAccessTime) {
		this.urlAccessTime = urlAccessTime;
	}

}
