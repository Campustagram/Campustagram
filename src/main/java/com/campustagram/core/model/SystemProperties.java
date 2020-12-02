package com.campustagram.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.campustagram.core.common.CommonDate;

@Entity
@Table(name = "systemproperties")
public class SystemProperties implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1069726912073762741L;

	@Id
	@SequenceGenerator(name = "systemproperties_seq", sequenceName = "systemproperties_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "systemproperties_seq")
	@Column(nullable = false)
	private Long id;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "update_user_id")
	private User updateUser;
	private Date updateDate;

	private boolean isRegisterEnabled = true;
	// ====================================================================================================================
	// MAINTENANCE :
	// ====================================================================================================================
	private Date maintenanceStartDate;
	private Date maintenanceEndDate;
	private boolean isOnMaintenance = false;

	// ====================================================================================================================
	// EMAIL :
	// ====================================================================================================================
	@Embedded
	private MailSenderProperties mailSenderProperties = new MailSenderProperties();
	// ====================================================================================================================
	// THEME :
	// ====================================================================================================================
	private String theme_layout = "moody";
	private String theme = "bluegrey";

	// ====================================================================================================================
	// PREDEFINED COLOR :
	// ====================================================================================================================
	@Embedded
	private SystemColor systemColor = new SystemColor();
	
	/*
	 * This role is default role that is displayed when creating new user
	 * 
	 */
	@OneToOne(targetEntity = Role.class  , fetch = FetchType.EAGER)
	@JoinColumn(name = "default_role_id")
	private Role defaultNewUserRole;

	public SystemProperties() {
		super();
	}

	@Transient
	public boolean isEndDateActive() {
		if (null != this.maintenanceStartDate && null != this.maintenanceEndDate && this.isOnMaintenance()) {
			if (this.maintenanceStartDate.before(CommonDate.currentDate())) {
				return true;
			}
		}
		return false;
	}

	@Transient
	public Date getMaintenanceDate() {
		if (isEndDateActive()) {
			return this.maintenanceEndDate;
		}
		return this.maintenanceStartDate;
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof SystemProperties) && (id != null) ? id.equals(((SystemProperties) other).id)
				: (other == this);
	}

	@Override
	public int hashCode() {
		return (id != null) ? (this.getClass().hashCode() + id.hashCode()) : super.hashCode();
	}

	@Override
	public String toString() {
		return "SystemProperties [id=" + id + ", updateUser=" + updateUser + ", updateDate=" + updateDate
				+ ", maintenanceStartDate=" + maintenanceStartDate + ", maintenanceEndDate=" + maintenanceEndDate
				+ ", isOnMaintenance=" + isOnMaintenance + ", mailSenderProperties=" + mailSenderProperties
				+ ", theme_layout=" + theme_layout + ", theme=" + theme + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getTheme_layout() {
		return theme_layout;
	}

	public void setTheme_layout(String theme_layout) {
		this.theme_layout = theme_layout;
	}

	public User getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(User updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getMaintenanceEndDate() {
		return maintenanceEndDate;
	}

	public void setMaintenanceEndDate(Date maintenanceEndDate) {
		this.maintenanceEndDate = maintenanceEndDate;
	}

	public boolean isOnMaintenance() {
		return isOnMaintenance;
	}

	public void setOnMaintenance(boolean isOnMaintenance) {

		this.isOnMaintenance = isOnMaintenance;
	}

	public Date getMaintenanceStartDate() {
		return maintenanceStartDate;
	}

	public void setMaintenanceStartDate(Date maintenanceStartDate) {
		this.maintenanceStartDate = maintenanceStartDate;
	}

	public MailSenderProperties getMailSenderProperties() {
		return mailSenderProperties;
	}

	public void setMailSenderProperties(MailSenderProperties mailSenderProperties) {
		this.mailSenderProperties = mailSenderProperties;
	}

	public SystemColor getSystemColor() {
		return systemColor;
	}

	public void setSystemColor(SystemColor systemColor) {
		this.systemColor = systemColor;
	}

	public boolean isRegisterEnabled() {
		return isRegisterEnabled;
	}

	public void setRegisterEnabled(boolean isRegisterEnabled) {
		this.isRegisterEnabled = isRegisterEnabled;
	}

	public Role getDefaultNewUserRole() {
		return defaultNewUserRole;
	}

	public void setDefaultNewUserRole(Role defaultNewUserRole) {
		this.defaultNewUserRole = defaultNewUserRole;
	}
}