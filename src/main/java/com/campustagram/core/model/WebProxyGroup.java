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
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "web_app_proxy_group", indexes = { @Index(name = "webproxygroupidx", columnList = "id", unique = true),
		@Index(name = "webproxygroupnamex", columnList = "name", unique = false),
		@Index(name = "webproxygroupisdelx", columnList = "is_deleted", unique = false),
		@Index(name = "webproxygroupisactivex", columnList = "is_active", unique = false) })
public class WebProxyGroup implements Serializable {

	@Id
	@SequenceGenerator(name = "webProxy_seq", sequenceName = "webProxy_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "webProxy_seq")
	@Column(name = "id", nullable = false)
	private Long id;

	@OneToMany(mappedBy = "webProxyGroup", targetEntity = WebAppProxy.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<WebAppProxy> webProxyList = new ArrayList<>();

	@Column(name = "name")
	private String name;
	@Column(name = "is_deleted")
	private boolean isDeleted = false;
	@Column(name = "is_active")
	private boolean isActive = false;
	private Date createDate = new Date();
	private Date updateDate = new Date();

	public WebProxyGroup() {
		super();
	}

	@Override
	public String toString() {
		return "WebProxyGroup [id=" + id + ", name=" + name + ", isDeleted=" + isDeleted + ", isActive=" + isActive
				+ ", createDate=" + createDate + ", updateDate=" + updateDate + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<WebAppProxy> getWebProxyList() {
		return webProxyList;
	}

	public void setWebProxyList(List<WebAppProxy> webProxyList) {
		this.webProxyList = webProxyList;
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

}
